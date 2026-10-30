package entidades.pasivas;
import entidades.Estadistica;
import entidades.efectos.Invisibilidad;
import entidades.efectos.Pasiva;

public class CazadorSigiloso extends Pasiva {

    public CazadorSigiloso() {
        super("Cazador Sigiloso");
    }

    @Override
    public int modificar(Estadistica estadistica, int valor) {
        if (estadistica == Estadistica.CRITICO && getPortador().tieneEfecto(Invisibilidad.NOMBRE)) {
            return valor * 2;
        }
        return valor;
    }
}
