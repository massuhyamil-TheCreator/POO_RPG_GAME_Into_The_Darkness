package objetos;
import objetos.consumibles.Consumibles;
import objetos.equipables.Armas;
import java.util.ArrayList;
import java.util.List;

// Guarda los objetos de la party en una lista por tipo.
// No pregunta "¿qué tipo sos?": cada Item le dice en qué lista va (ver Item.guardarEn).
public class Inventario {

    private final List<Armas> armas = new ArrayList<>();
    private final List<Consumibles> consumibles = new ArrayList<>();

    public void agregar(Item item) {
        if (item != null) item.guardarEn(this);
    }

    public void quitar(Item item) {
        if (item != null) item.sacarDe(this);
    }

    public boolean contiene(Item item) {
        return item != null && item.estaEn(this);
    }

    public void vaciar() {
        armas.clear();
        consumibles.clear();
    }

    // ---- los llaman los propios objetos ----

    public void guardarArma(Armas arma) { armas.add(arma); }
    public void sacarArma(Armas arma) { armas.remove(arma); }
    public boolean tieneArma(Armas arma) { return armas.contains(arma); }

    public void guardarConsumible(Consumibles consumible) { consumibles.add(consumible); }
    public void sacarConsumible(Consumibles consumible) { consumibles.remove(consumible); }
    public boolean tieneConsumible(Consumibles consumible) { return consumibles.contains(consumible); }

    // ---- consultas (devuelven copias: para modificar el inventario se usa agregar/quitar) ----

    public List<Armas> getArmas() {
        return new ArrayList<>(armas);
    }

    public List<Consumibles> getConsumibles() {
        return new ArrayList<>(consumibles);
    }

    public List<Item> getItems() {
        List<Item> todos = new ArrayList<>(armas);
        todos.addAll(consumibles);
        return todos;
    }
}
