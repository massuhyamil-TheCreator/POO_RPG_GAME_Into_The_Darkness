package objetos;
import java.util.List;

public class Inventario {

    private List<Item> items;

    public Inventario(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
