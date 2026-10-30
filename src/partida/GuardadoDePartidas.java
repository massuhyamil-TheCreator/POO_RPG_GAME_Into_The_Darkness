package partida;
import combate.PartyAliada;
import combate.PartyEnemiga;
import entidades.enemigos.Enemigo;
import entidades.personajes.Curador;
import entidades.personajes.Personaje;
import mapa.Nodo;
import objetos.Catalogo;
import objetos.Item;
import objetos.consumibles.Consumibles;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// Hay 3 ranuras de guardado (1, 2 y 3). Cada una es un archivo de texto (clave=valor)
// dentro de la carpeta "partidas". Los objetos se guardan por su id del Catalogo;
// armas y armaduras también con sus valores, porque el Herrero los puede haber mejorado.
public class GuardadoDePartidas {

    public static final int RANURAS = 3;

    private static final Path CARPETA = Paths.get("partidas");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // si se guarda en medio de una pelea, se guarda cómo estaba todo justo antes de empezarla
    public static void guardar(Partida partida, int ranura) throws IOException {
        validar(ranura);
        if (!partida.puedeGuardar()) throw new IllegalStateException("La partida ya terminó.");
        Properties datos = partida.estaEnBatalla() ? partida.getAntesDeLaBatalla() : capturar(partida);
        datos.setProperty("fecha", LocalDateTime.now().format(FORMATO_FECHA));
        Files.createDirectories(CARPETA);
        try (Writer archivo = Files.newBufferedWriter(archivoDe(ranura), StandardCharsets.UTF_8)) {
            datos.store(archivo, "Into The Darkness - partida guardada");
        }
    }

    public static Partida cargar(int ranura) throws IOException {
        Properties datos = leer(ranura);
        int cantidad = entero(datos, "personajes");
        List<Personaje> personajes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            String clave = "personaje." + i + ".";
            Personaje p = CreadorDePersonajes.crear(datos.getProperty(clave + "clase"), datos.getProperty(clave + "nombre"));
            if (datos.containsKey(clave + "arma")) p.equiparArma((Armas) decodificar(datos.getProperty(clave + "arma")));
            if (datos.containsKey(clave + "armadura")) p.equiparArmadura((Armaduras) decodificar(datos.getProperty(clave + "armadura")));
            for (Item item : decodificarLista(datos.getProperty(clave + "mochila"))) {
                p.guardarEnMochila((Consumibles) item);
            }
            p.restaurar(entero(datos, clave + "nivel"), entero(datos, clave + "experiencia"),
                    entero(datos, clave + "vida"), entero(datos, clave + "mana"));
            if (p instanceof Curador && datos.containsKey(clave + "salvador")) {
                ((Curador) p).getSalvador().setProbabilidad(entero(datos, clave + "salvador"));
            }
            personajes.add(p);
        }

        Partida partida = new Partida(personajes, Long.parseLong(datos.getProperty("semilla")));
        PartyAliada party = partida.getParty();
        party.getInventario().getItems().clear();
        for (Item item : decodificarLista(datos.getProperty("inventario"))) {
            party.getInventario().agregar(item);
        }
        party.agregarOro(entero(datos, "oro"));

        for (String id : separar(datos.getProperty("visitados"), ",")) {
            partida.getMapa().buscarNodo(Integer.parseInt(id)).setVisitado(true);
        }
        for (String id : separar(datos.getProperty("consumidos"), ",")) {
            partida.getMapa().buscarNodo(Integer.parseInt(id)).consumir();
        }
        partida.getMapa().ubicarEn(partida.getMapa().buscarNodo(entero(datos, "nodoActual")));

        EstadoPartida estado = EstadoPartida.valueOf(datos.getProperty("estado"));
        PartyEnemiga enemigos = null;
        if (estado == EstadoPartida.PREPARANDO_BATALLA) {
            List<Enemigo> lista = new ArrayList<>();
            for (String enemigo : separar(datos.getProperty("enemigos"), ";")) {
                String[] partes = enemigo.split(":");
                lista.add(GeneradorDeEncuentros.recrear(partes[0], partes[1]));
            }
            enemigos = new PartyEnemiga(lista);
        }
        partida.retomar(estado, enemigos);
        return partida;
    }

    public static boolean hayPartida(int ranura) {
        validar(ranura);
        return Files.exists(archivoDe(ranura));
    }

    // texto para mostrar en la pantalla de ranuras, o "Vacía"
    public static String describir(int ranura) {
        if (!hayPartida(ranura)) return "Vacía";
        try {
            Properties datos = leer(ranura);
            return datos.getProperty("resumen") + " · " + datos.getProperty("fecha");
        } catch (IOException e) {
            return "Archivo dañado";
        }
    }

    public static boolean borrar(int ranura) throws IOException {
        validar(ranura);
        return Files.deleteIfExists(archivoDe(ranura));
    }

    // ---- Pasar la partida a texto ----

    static Properties capturar(Partida partida) {
        Properties datos = new Properties();
        PartyAliada party = partida.getParty();
        datos.setProperty("estado", partida.getEstado().name());
        datos.setProperty("semilla", String.valueOf(partida.getSemilla()));
        datos.setProperty("nodoActual", String.valueOf(partida.getNodoActual().getId()));
        datos.setProperty("oro", String.valueOf(party.getOro()));

        List<String> visitados = new ArrayList<>();
        List<String> consumidos = new ArrayList<>();
        for (List<Nodo> fila : partida.getMapa().getFilas()) {
            for (Nodo nodo : fila) {
                if (nodo.getVisitado()) visitados.add(String.valueOf(nodo.getId()));
                if (nodo.isConsumido()) consumidos.add(String.valueOf(nodo.getId()));
            }
        }
        datos.setProperty("visitados", String.join(",", visitados));
        datos.setProperty("consumidos", String.join(",", consumidos));

        if (partida.getEnemigosPendientes() != null) {
            List<String> enemigos = new ArrayList<>();
            for (Enemigo enemigo : partida.getEnemigosPendientes().getMiembrosEnemigos()) {
                enemigos.add(enemigo.getClass().getSimpleName() + ":" + enemigo.getNombre());
            }
            datos.setProperty("enemigos", String.join(";", enemigos));
        }

        List<Personaje> personajes = party.getMiembrosJugador();
        List<String> resumen = new ArrayList<>();
        datos.setProperty("personajes", String.valueOf(personajes.size()));
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            String clave = "personaje." + i + ".";
            datos.setProperty(clave + "clase", p.getClass().getSimpleName());
            datos.setProperty(clave + "nombre", p.getNombre());
            datos.setProperty(clave + "nivel", String.valueOf(p.getNivel()));
            datos.setProperty(clave + "experiencia", String.valueOf(p.getExperiencia()));
            datos.setProperty(clave + "vida", String.valueOf(p.getPuntosDeSalud()));
            datos.setProperty(clave + "mana", String.valueOf(p.getRecurso()));
            if (p.getArma() != null) datos.setProperty(clave + "arma", codificar(p.getArma()));
            if (p.getArmadura() != null) datos.setProperty(clave + "armadura", codificar(p.getArmadura()));
            datos.setProperty(clave + "mochila", codificar(new ArrayList<Item>(p.getMochila())));
            if (p instanceof Curador) {
                datos.setProperty(clave + "salvador", String.valueOf(((Curador) p).getSalvador().getProbabilidad()));
            }
            resumen.add(p.getNombre() + " nv " + p.getNivel());
        }
        datos.setProperty("inventario", codificar(party.getInventario().getItems()));
        datos.setProperty("resumen", String.join(", ", resumen) + " · piso " + partida.getNodoActual().getFila());
        return datos;
    }

    // ---- Auxiliares ----

    private static void validar(int ranura) {
        if (ranura < 1 || ranura > RANURAS) throw new IllegalArgumentException("Las ranuras van de 1 a " + RANURAS);
    }

    private static Path archivoDe(int ranura) {
        return CARPETA.resolve("ranura" + ranura + ".properties");
    }

    private static Properties leer(int ranura) throws IOException {
        validar(ranura);
        Properties datos = new Properties();
        try (Reader archivo = Files.newBufferedReader(archivoDe(ranura), StandardCharsets.UTF_8)) {
            datos.load(archivo);
        }
        return datos;
    }

    // un objeto queda como "id", o "id:fisico:magico" si es arma o armadura
    private static String codificar(Item item) {
        if (item instanceof Armas) {
            Armas arma = (Armas) item;
            return item.getId() + ":" + arma.getDanioFisico() + ":" + arma.getDanioMagico();
        }
        if (item instanceof Armaduras) {
            Armaduras armadura = (Armaduras) item;
            return item.getId() + ":" + armadura.getDefensaFisica() + ":" + armadura.getDefensaMagica();
        }
        return String.valueOf(item.getId());
    }

    private static String codificar(List<Item> items) {
        List<String> codigos = new ArrayList<>();
        for (Item item : items) codigos.add(codificar(item));
        return String.join(",", codigos);
    }

    private static Item decodificar(String codigo) {
        String[] partes = codigo.split(":");
        Item item = Catalogo.crear(Integer.parseInt(partes[0]));
        if (item instanceof Armas) {
            ((Armas) item).setDanioFisico(Integer.parseInt(partes[1]));
            ((Armas) item).setDanioMagico(Integer.parseInt(partes[2]));
        } else if (item instanceof Armaduras) {
            ((Armaduras) item).setDefensaFisica(Integer.parseInt(partes[1]));
            ((Armaduras) item).setDefensaMagica(Integer.parseInt(partes[2]));
        }
        return item;
    }

    private static List<Item> decodificarLista(String codigos) {
        List<Item> items = new ArrayList<>();
        for (String codigo : separar(codigos, ",")) items.add(decodificar(codigo));
        return items;
    }

    private static List<String> separar(String lista, String separador) {
        List<String> partes = new ArrayList<>();
        if (lista == null || lista.isEmpty()) return partes;
        for (String parte : lista.split(separador)) partes.add(parte);
        return partes;
    }

    private static int entero(Properties datos, String clave) {
        return Integer.parseInt(datos.getProperty(clave));
    }
}
