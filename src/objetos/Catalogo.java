package objetos;
import objetos.consumibles.Antidoto;
import objetos.consumibles.Consumibles;
import objetos.consumibles.PocionCuracion;
import objetos.consumibles.PocionMana;
import objetos.equipables.Armas;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class Catalogo {

    private static final Random AZAR = new Random();

    public static final int POCION_VIDA_CHICA = 1;
    public static final int POCION_VIDA_GRANDE = 2;
    public static final int POCION_MANA_CHICA = 3;
    public static final int POCION_MANA_GRANDE = 4;
    public static final int ANTIDOTO = 5;
    public static final int ESPADA_LARGA = 6;
    public static final int BACULO_OSCURO = 7;
    public static final int ARCO_DE_CAZA = 8;

    private static final Map<Integer, Supplier<Armas>> ARMAS = new HashMap<>();
    private static final Map<Integer, Supplier<Consumibles>> CONSUMIBLES = new HashMap<>();

    static {
        ARMAS.put(ESPADA_LARGA, () -> new Armas(ESPADA_LARGA, "Espada larga", "Una espada pesada", 100, "Espada", 2, 0));
        ARMAS.put(BACULO_OSCURO, () -> new Armas(BACULO_OSCURO, "Báculo oscuro", "Canaliza magia", 120, "Baculo", 0, 3));
        ARMAS.put(ARCO_DE_CAZA, () -> new Armas(ARCO_DE_CAZA, "Arco de caza", "Liviano y preciso", 90, "Arco", 2, 1));

        CONSUMIBLES.put(POCION_VIDA_CHICA, () -> new PocionCuracion(POCION_VIDA_CHICA, "Poción de vida chica", "Cura 15 de vida", 20, 1, 15));
        CONSUMIBLES.put(POCION_VIDA_GRANDE, () -> new PocionCuracion(POCION_VIDA_GRANDE, "Poción de vida grande", "Cura 35 de vida", 50, 2, 35));
        CONSUMIBLES.put(POCION_MANA_CHICA, () -> new PocionMana(POCION_MANA_CHICA, "Poción de maná chica", "Da 20 de maná", 20, 1, 20));
        CONSUMIBLES.put(POCION_MANA_GRANDE, () -> new PocionMana(POCION_MANA_GRANDE, "Poción de maná grande", "Da 50 de maná", 50, 2, 50));
        CONSUMIBLES.put(ANTIDOTO, () -> new Antidoto(ANTIDOTO, "Antídoto", "Cura el veneno y da inmunidad por 2 turnos", 15));
    }

    // los que se repiten salen más seguido
    private static final int[] BOTIN = {
        POCION_VIDA_CHICA, POCION_VIDA_CHICA, POCION_VIDA_CHICA,
        POCION_MANA_CHICA, POCION_MANA_CHICA,
        ANTIDOTO, ANTIDOTO,
        POCION_VIDA_GRANDE, POCION_MANA_GRANDE,
        ESPADA_LARGA, BACULO_OSCURO, ARCO_DE_CAZA
    };

    // cualquier objeto por su id (para el botín y para cargar partidas)
    public static Item crear(int id) {
        if (esArma(id)) return crearArma(id);
        return crearConsumible(id);
    }

    private static boolean esArma(int id) {
        return ARMAS.containsKey(id);
    }

    // con tipo exacto: así quien necesita un arma no tiene que castear
    public static Armas crearArma(int id) {
        Supplier<Armas> arma = ARMAS.get(id);
        if (arma == null) throw new IllegalArgumentException("No existe el arma " + id);
        return arma.get();
    }

    public static Consumibles crearConsumible(int id) {
        Supplier<Consumibles> consumible = CONSUMIBLES.get(id);
        if (consumible == null) throw new IllegalArgumentException("No existe el consumible " + id);
        return consumible.get();
    }

    public static Item botinAlAzar() {
        return crear(BOTIN[AZAR.nextInt(BOTIN.length)]);
    }
}
