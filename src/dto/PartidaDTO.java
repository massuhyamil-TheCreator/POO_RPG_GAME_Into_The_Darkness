package dto;
import java.util.ArrayList;
import java.util.List;

// Lo que muestran el mapa y la preparación: la party, el oro y el inventario.
public class PartidaDTO {

    private final List<EntidadDTO> party;
    private final int oro;
    private final int piso;
    private final int ultimoPiso;
    private final List<ItemDTO> armas;
    private final List<ItemDTO> consumibles;

    public PartidaDTO(List<EntidadDTO> party, int oro, int piso, int ultimoPiso, List<ItemDTO> armas, List<ItemDTO> consumibles) {
        this.party = party;
        this.oro = oro;
        this.piso = piso;
        this.ultimoPiso = ultimoPiso;
        this.armas = armas;
        this.consumibles = consumibles;
    }

    public List<EntidadDTO> getParty() { return party; }
    public int getOro() { return oro; }
    public int getPiso() { return piso; }
    public int getUltimoPiso() { return ultimoPiso; }
    public List<ItemDTO> getArmas() { return armas; }
    public List<ItemDTO> getConsumibles() { return consumibles; }

    // todo el inventario junto: primero las armas y después los consumibles
    public List<ItemDTO> getItems() {
        List<ItemDTO> todos = new ArrayList<>(armas);
        todos.addAll(consumibles);
        return todos;
    }
}
