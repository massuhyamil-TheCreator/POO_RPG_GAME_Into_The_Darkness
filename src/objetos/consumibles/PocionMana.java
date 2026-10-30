package objetos.consumibles;
import entidades.Entidad;

public class PocionMana extends Consumibles {

    private int mana;

    public PocionMana(int id, String nombre, String descripcion, double precio, int espacios, int mana) {
        super(id, nombre, descripcion, precio, espacios);
        this.mana = mana;
    }

    @Override
    public String usar(Entidad objetivo) {
        objetivo.recuperarRecurso(mana);
        return objetivo.getNombre() + " recupera " + mana + " de maná.";
    }

    public int getMana() {
        return mana;
    }
}
