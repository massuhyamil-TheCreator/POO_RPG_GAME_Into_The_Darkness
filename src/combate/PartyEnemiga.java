package combate;
import entidades.enemigos.Enemigo;
import java.util.ArrayList;
import java.util.List;

public class PartyEnemiga {

    private List<Enemigo> miembrosEnemigos;

    public PartyEnemiga(List<Enemigo> miembrosEnemigos) {
        // copia propia: así se pueden sumar invocaciones aunque nos pasen un List.of(...)
        this.miembrosEnemigos = new ArrayList<>(miembrosEnemigos);
    }

    public List<Enemigo> getMiembrosEnemigos() {
        return miembrosEnemigos;
    }

    public void setMiembrosEnemigos(List<Enemigo> miembrosEnemigos) {
        this.miembrosEnemigos = miembrosEnemigos;
    }
}
