package partida;
import combate.PartyAliada;
import combate.PartyEnemiga;
import entidades.enemigos.Enemigo;
import entidades.personajes.Personaje;
import mapa.Nodo;
import objetos.Catalogo;
import objetos.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// Pasa una Partida a texto (clave=valor) y arma una Partida a partir de ese texto.
// No conoce las clases concretas del juego: cada personaje, enemigo y objeto dice cómo se llama
// (getClase / getTipo / getId) y se vuelve a crear con CreadorDePersonajes, GeneradorDeEncuentros y Catalogo.
// El archivo en disco y las ranuras son cosa de GuardadoDePartidas.
public class SerializadorDePartida {

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
                enemigos.add(enemigo.getTipo() + ":" + enemigo.getNombre());
            }
            datos.setProperty("enemigos", String.join(";", enemigos));
        }

        List<Personaje> personajes = party.getMiembrosJugador();
        List<String> resumen = new ArrayList<>();
        datos.setProperty("personajes", String.valueOf(personajes.size()));
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            String clave = "personaje." + i + ".";
            datos.setProperty(clave + "clase", p.getClase());
            datos.setProperty(clave + "nombre", p.getNombre());
            datos.setProperty(clave + "nivel", String.valueOf(p.getNivel()));
            datos.setProperty(clave + "experiencia", String.valueOf(p.getExperiencia()));
            datos.setProperty(clave + "vida", String.valueOf(p.getPuntosDeSalud()));
            datos.setProperty(clave + "mana", String.valueOf(p.getRecurso()));
            if (p.getArma() != null) datos.setProperty(clave + "arma", String.valueOf(p.getArma().getId()));
            datos.setProperty(clave + "mochila", ids(new ArrayList<Item>(p.getMochila())));
            String propio = p.guardarEstadoPropio();   // lo que tenga de especial cada clase (o null)
            if (propio != null) datos.setProperty(clave + "propio", propio);
            resumen.add(p.getNombre() + " nv " + p.getNivel());
        }
        datos.setProperty("inventario", ids(party.getInventario().getItems()));
        datos.setProperty("resumen", String.join(", ", resumen) + " · piso " + partida.getNodoActual().getFila());
        return datos;
    }

    static Partida reconstruir(Properties datos) {
        int cantidad = entero(datos, "personajes");
        List<Personaje> personajes = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            String clave = "personaje." + i + ".";
            Personaje p = CreadorDePersonajes.crear(datos.getProperty(clave + "clase"), datos.getProperty(clave + "nombre"));
            if (datos.containsKey(clave + "arma")) p.equiparArma(Catalogo.crearArma(entero(datos, clave + "arma")));
            for (String id : separar(datos.getProperty(clave + "mochila"), ",")) {
                p.guardarEnMochila(Catalogo.crearConsumible(Integer.parseInt(id)));
            }
            p.restaurar(entero(datos, clave + "nivel"), entero(datos, clave + "experiencia"),
                    entero(datos, clave + "vida"), entero(datos, clave + "mana"));
            if (datos.containsKey(clave + "propio")) p.restaurarEstadoPropio(datos.getProperty(clave + "propio"));
            personajes.add(p);
        }

        Partida partida = new Partida(personajes, Long.parseLong(datos.getProperty("semilla")));
        PartyAliada party = partida.getParty();
        party.getInventario().vaciar();
        for (String id : separar(datos.getProperty("inventario"), ",")) {
            party.getInventario().agregar(Catalogo.crear(Integer.parseInt(id)));
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

    // ---- Auxiliares ----

    // los objetos se guardan solo por su id: el Catalogo sabe volver a crearlos
    private static String ids(List<Item> items) {
        List<String> codigos = new ArrayList<>();
        for (Item item : items) codigos.add(String.valueOf(item.getId()));
        return String.join(",", codigos);
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
