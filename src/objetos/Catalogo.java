package objetos;
import objetos.consumibles.Antidoto;
import objetos.consumibles.Consumibles;
import objetos.consumibles.PocionCuracion;
import objetos.consumibles.PocionMana;
import objetos.equipables.Armas;
import java.util.Random;

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
        return id == ESPADA_LARGA || id == BACULO_OSCURO || id == ARCO_DE_CAZA;
    }

    // con tipo exacto: así quien necesita un arma no tiene que castear
    public static Armas crearArma(int id) {
        switch (id) {
            case ESPADA_LARGA: return new Armas(id, "Espada larga", "Una espada pesada", 100, "Espada", 2, 0);
            case BACULO_OSCURO: return new Armas(id, "Báculo oscuro", "Canaliza magia", 120, "Baculo", 0, 3);
            case ARCO_DE_CAZA: return new Armas(id, "Arco de caza", "Liviano y preciso", 90, "Arco", 2, 1);
            default: throw new IllegalArgumentException("No existe el arma " + id);
        }
    }

    public static Consumibles crearConsumible(int id) {
        switch (id) {
            case POCION_VIDA_CHICA: return new PocionCuracion(id, "Poción de vida chica", "Cura 15 de vida", 20, 1, 15);
            case POCION_VIDA_GRANDE: return new PocionCuracion(id, "Poción de vida grande", "Cura 35 de vida", 50, 2, 35);
            case POCION_MANA_CHICA: return new PocionMana(id, "Poción de maná chica", "Da 20 de maná", 20, 1, 20);
            case POCION_MANA_GRANDE: return new PocionMana(id, "Poción de maná grande", "Da 50 de maná", 50, 2, 50);
            case ANTIDOTO: return new Antidoto(id, "Antídoto", "Cura el veneno y da inmunidad por 2 turnos", 15);
            default: throw new IllegalArgumentException("No existe el consumible " + id);
        }
    }

    public static Item botinAlAzar() {
        return crear(BOTIN[AZAR.nextInt(BOTIN.length)]);
    }
}
