package objetos;
import objetos.consumibles.Antidoto;
import objetos.consumibles.PocionCuracion;
import objetos.consumibles.PocionMana;
import objetos.equipables.Armaduras;
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
    public static final int COTA_DE_MALLA = 9;
    public static final int TUNICA_ENCANTADA = 10;
    public static final int ARMADURA_DE_PLACAS = 11;

    // los que se repiten salen más seguido
    private static final int[] BOTIN = {
        POCION_VIDA_CHICA, POCION_VIDA_CHICA, POCION_VIDA_CHICA,
        POCION_MANA_CHICA, POCION_MANA_CHICA,
        ANTIDOTO, ANTIDOTO,
        POCION_VIDA_GRANDE, POCION_MANA_GRANDE,
        ESPADA_LARGA, BACULO_OSCURO, ARCO_DE_CAZA,
        COTA_DE_MALLA, TUNICA_ENCANTADA, ARMADURA_DE_PLACAS
    };

    public static Item crear(int id) {
        switch (id) {
            case POCION_VIDA_CHICA: return new PocionCuracion(id, "Poción de vida chica", "Cura 15 de vida", 20, 1, 15);
            case POCION_VIDA_GRANDE: return new PocionCuracion(id, "Poción de vida grande", "Cura 35 de vida", 50, 2, 35);
            case POCION_MANA_CHICA: return new PocionMana(id, "Poción de maná chica", "Da 20 de maná", 20, 1, 20);
            case POCION_MANA_GRANDE: return new PocionMana(id, "Poción de maná grande", "Da 50 de maná", 50, 2, 50);
            case ANTIDOTO: return new Antidoto(id, "Antídoto", "Cura el veneno y da inmunidad por 2 turnos", 15);
            case ESPADA_LARGA: return new Armas(id, "Espada larga", "Una espada pesada", 100, "Espada", 2, 0);
            case BACULO_OSCURO: return new Armas(id, "Báculo oscuro", "Canaliza magia", 120, "Baculo", 0, 3);
            case ARCO_DE_CAZA: return new Armas(id, "Arco de caza", "Liviano y preciso", 90, "Arco", 2, 1);
            case COTA_DE_MALLA: return new Armaduras(id, "Cota de malla", "Protege del daño físico", 80, "Pecho", 2, 0);
            case TUNICA_ENCANTADA: return new Armaduras(id, "Túnica encantada", "Protege de la magia", 80, "Pecho", 0, 2);
            case ARMADURA_DE_PLACAS: return new Armaduras(id, "Armadura de placas", "Pesada pero resistente", 150, "Pecho", 3, 1);
            default: throw new IllegalArgumentException("No existe el objeto " + id);
        }
    }

    public static Item botinAlAzar() {
        return crear(BOTIN[AZAR.nextInt(BOTIN.length)]);
    }
}
