package entidades.pasivas;
import combate.Batalla;
import entidades.Entidad;
import entidades.efectos.DanioPorTurno;
import entidades.efectos.Pasiva;

public class RejuvenecimientoGeneracional extends Pasiva {

    private static final int CADA_TURNOS = 3;
    private static final int CURACION = 5;
    private static final int DANIO_EXTRA = 15;

    private Batalla batalla;
    private int turnosJugados;

    public RejuvenecimientoGeneracional() {
        super("Rejuvenecimiento Generacional");
    }

    @Override
    public void alComenzarBatalla(Batalla batalla) {
        this.batalla = batalla;
        this.turnosJugados = 0;
    }

    @Override
    public String alInicioDelTurno(Entidad portador) {
        turnosJugados++;
        if (turnosJugados % CADA_TURNOS != 0) return null;
        int curado = portador.curar(CURACION);
        if (curado == 0) return null;
        return portador.getNombre() + " se rejuvenece y recupera " + curado + " de vida.";
    }

    @Override
    public int modificarDanioInfligido(int danio, Entidad objetivo) {
        if (batalla != null && hayAlguienSangrando()) {
            return danio * (100 + DANIO_EXTRA) / 100;
        }
        return danio;
    }

    private boolean hayAlguienSangrando() {
        for (Entidad enemigo : batalla.enemigosDe(getPortador())) {
            if (enemigo.tieneEfecto(DanioPorTurno.SANGRADO)) return true;
        }
        return false;
    }
}
