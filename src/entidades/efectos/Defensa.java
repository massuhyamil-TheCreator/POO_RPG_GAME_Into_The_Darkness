package entidades.efectos;
import entidades.Entidad;

// La acción Defender: recibe la mitad del daño hasta que vuelve a tocarle el turno.
public class Defensa extends Efecto {

    public static final String NOMBRE = "Defendiendo";
    public static final int PORCENTAJE_MITIGADO = 50;

    public Defensa() {
        super(NOMBRE, 1);
    }

    @Override
    public String alInicioDelTurno(Entidad portador) {
        terminar();
        return null;
    }

    @Override
    public int modificarDanioRecibido(int danio) {
        if (termino()) return danio;
        return danio * (100 - PORCENTAJE_MITIGADO) / 100;
    }
}
