package partida;
import combate.PartyEnemiga;
import entidades.enemigos.Bruja;
import entidades.enemigos.Enemigo;
import entidades.enemigos.Esqueleto;
import entidades.enemigos.HombreLobo;
import entidades.enemigos.LoboSalvaje;
import entidades.enemigos.Vampiro;
import entidades.enemigos.Zombie;
import entidades.enemigos.bosses.Dragon;
import entidades.enemigos.bosses.Golem;
import entidades.enemigos.bosses.Jefe;
import entidades.enemigos.bosses.ReyVampiro;
import objetos.Catalogo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public class GeneradorDeEncuentros {

    private static final Random AZAR = new Random();
    private static final int FILA_DIFICIL = 4;

    private static final String[] DEBILES = {"Zombie", "Esqueleto", "Lobo Salvaje", "Vampiro"};
    private static final String[] TODOS = {"Zombie", "Esqueleto", "Lobo Salvaje", "Vampiro", "Bruja", "Hombre Lobo"};
    private static final String[] FUERTES = {"Vampiro", "Bruja", "Hombre Lobo"};

    // tipo de enemigo -> cómo crearlo (el nombre de Enemigo.getTipo()). Sin switch: un tipo nuevo es una línea más.
    private static final Map<String, Function<String, Enemigo>> FABRICAS = new LinkedHashMap<>();
    static {
        FABRICAS.put("Zombie", Zombie::new);
        FABRICAS.put("Esqueleto", Esqueleto::new);
        FABRICAS.put("Lobo Salvaje", LoboSalvaje::new);
        FABRICAS.put("Vampiro", Vampiro::new);
        FABRICAS.put("Bruja", Bruja::new);
        FABRICAS.put("Hombre Lobo", HombreLobo::new);
        FABRICAS.put("Dragon", nombre -> new Dragon());
        FABRICAS.put("Golem", nombre -> new Golem());
        FABRICAS.put("Rey Vampiro", nombre -> new ReyVampiro());
    }

    public static PartyEnemiga batalla(int fila) {
        if (fila < FILA_DIFICIL) return crear(DEBILES, 2);
        return crear(TODOS, 2 + AZAR.nextInt(2));
    }

    // tres enemigos fuertes y uno lleva un objeto seguro
    public static PartyEnemiga elite() {
        PartyEnemiga enemigos = crear(FUERTES, 3);
        enemigos.getMiembrosEnemigos().get(0).setObjetoOtorgado(Catalogo.botinAlAzar());
        return enemigos;
    }

    public static PartyEnemiga jefe() {
        return new PartyEnemiga(List.of(Jefe.crearAlAzar()));
    }

    private static PartyEnemiga crear(String[] posibles, int cantidad) {
        List<Enemigo> enemigos = new ArrayList<>();
        List<String> elegidos = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            String tipo = posibles[AZAR.nextInt(posibles.length)];
            elegidos.add(tipo);
            int repetidos = 0;
            for (String elegido : elegidos) {
                if (elegido.equals(tipo)) repetidos++;
            }
            // si hay dos iguales se llaman "Zombie" y "Zombie 2"
            String nombre = repetidos == 1 ? tipo : tipo + " " + repetidos;
            enemigos.add(recrear(tipo, nombre));
        }
        return new PartyEnemiga(enemigos);
    }

    // crea un enemigo por su tipo (también para cargar una partida guardada justo antes de una pelea)
    public static Enemigo recrear(String tipo, String nombre) {
        Function<String, Enemigo> fabrica = FABRICAS.get(tipo);
        if (fabrica == null) throw new IllegalArgumentException("Enemigo desconocido: " + tipo);
        return fabrica.apply(nombre);
    }
}
