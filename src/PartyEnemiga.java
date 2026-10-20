import entidades.enemigos.Enemigo;
import java.util.List;

public class PartyEnemiga {

    private List<Enemigo> miembrosEnemigos;

    public PartyEnemiga(List<Enemigo> miembrosEnemigos) {
        this.miembrosEnemigos = miembrosEnemigos;
    }

    public List<Enemigo> getMiembrosEnemigos() {
        return miembrosEnemigos;
    }

    public void setMiembrosEnemigos(List<Enemigo> miembrosEnemigos) {
        this.miembrosEnemigos = miembrosEnemigos;
    }
}
