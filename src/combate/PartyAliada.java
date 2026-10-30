package combate;
import entidades.personajes.Personaje;
import objetos.Inventario;
import objetos.consumibles.Consumibles;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import java.util.ArrayList;
import java.util.List;

public class PartyAliada {

    public static final int TAMANIO_MAXIMO = 3;

    private List<Personaje> miembrosJugador;
    private Inventario inventario;
    private int oro;

    public PartyAliada(List<Personaje> miembrosJugador, Inventario inventario) {
        if (miembrosJugador.size() > TAMANIO_MAXIMO)
            throw new IllegalArgumentException("La party no puede tener más de " + TAMANIO_MAXIMO + " personajes");
        // copia propia: así se pueden sumar invocaciones aunque nos pasen un List.of(...)
        this.miembrosJugador = new ArrayList<>(miembrosJugador);
        this.inventario = inventario;
    }

    public void equiparArma(Personaje personaje, Armas arma) {
        if (!inventario.contiene(arma)) return;
        inventario.quitar(arma);
        inventario.agregar(personaje.equiparArma(arma));
    }

    public void equiparArmadura(Personaje personaje, Armaduras armadura) {
        if (!inventario.contiene(armadura)) return;
        inventario.quitar(armadura);
        inventario.agregar(personaje.equiparArmadura(armadura));
    }

    public boolean darConsumible(Personaje personaje, Consumibles consumible) {
        if (!inventario.contiene(consumible) || !personaje.puedeLlevar(consumible)) return false;
        inventario.quitar(consumible);
        personaje.guardarEnMochila(consumible);
        return true;
    }

    public void vaciarMochila(Personaje personaje) {
        for (Consumibles consumible : new ArrayList<>(personaje.getMochila())) {
            personaje.sacarDeMochila(consumible);
            inventario.agregar(consumible);
        }
    }

    public List<Personaje> getMiembrosJugador() {
        return miembrosJugador;
    }

    public void setMiembrosJugador(List<Personaje> miembrosJugador) {
        this.miembrosJugador = miembrosJugador;
    }

    public void agregarOro(int cantidad) {
        oro += cantidad;
    }

    public int getOro() {
        return oro;
    }

    public Inventario getInventario() {
        return inventario;
    }
}

