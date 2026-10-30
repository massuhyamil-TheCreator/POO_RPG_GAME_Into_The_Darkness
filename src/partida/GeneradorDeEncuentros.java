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
import java.util.List;
import java.util.Random;

public class GeneradorDeEncuentros {

    private static final Random AZAR = new Random();
    private static final int FILA_DIFICIL = 4;

    private static final String[] DEBILES = {"Zombie", "Esqueleto", "Lobo Salvaje", "Vampiro"};
    private static final String[] TODOS = {"Zombie", "Esqueleto", "Lobo Salvaje", "Vampiro", "Bruja", "Hombre Lobo"};
    private static final String[] FUERTES = {"Vampiro", "Bruja", "Hombre Lobo"};

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
            enemigos.add(crearEnemigo(tipo, nombre));
        }
        return new PartyEnemiga(enemigos);
    }

    // para cargar una partida guardada justo antes de una pelea: recibe el nombre de la clase Java
    public static Enemigo recrear(String clase, String nombre) {
        switch (clase) {
            case "Dragon": return new Dragon();
            case "Golem": return new Golem();
            case "ReyVampiro": return new ReyVampiro();
            case "LoboSalvaje": return new LoboSalvaje(nombre);
            case "HombreLobo": return new HombreLobo(nombre);
            default: return crearEnemigo(clase, nombre);
        }
    }

    private static Enemigo crearEnemigo(String tipo, String nombre) {
        switch (tipo) {
            case "Zombie": return new Zombie(nombre);
            case "Esqueleto": return new Esqueleto(nombre);
            case "Lobo Salvaje": return new LoboSalvaje(nombre);
            case "Vampiro": return new Vampiro(nombre);
            case "Bruja": return new Bruja(nombre);
            case "Hombre Lobo": return new HombreLobo(nombre);
            default: throw new IllegalArgumentException("Enemigo desconocido: " + tipo);
        }
    }
}
