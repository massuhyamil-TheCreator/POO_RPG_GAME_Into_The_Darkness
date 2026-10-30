package objetos;
import objetos.consumibles.Consumibles;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private List<Item> items = new ArrayList<>();

    public Inventario() {
    }

    public void agregar(Item item) {
        if (item != null) items.add(item);
    }

    public void quitar(Item item) {
        items.remove(item);
    }

    public boolean contiene(Item item) {
        return items.contains(item);
    }

    public List<Armas> getArmas() {
        List<Armas> armas = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Armas) armas.add((Armas) item);
        }
        return armas;
    }

    public List<Armaduras> getArmaduras() {
        List<Armaduras> armaduras = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Armaduras) armaduras.add((Armaduras) item);
        }
        return armaduras;
    }

    public List<Consumibles> getConsumibles() {
        List<Consumibles> consumibles = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Consumibles) consumibles.add((Consumibles) item);
        }
        return consumibles;
    }

    public List<Item> getItems() {
        return items;
    }
}
