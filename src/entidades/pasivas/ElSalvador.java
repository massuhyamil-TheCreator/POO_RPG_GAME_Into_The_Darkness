package entidades.pasivas;
import combate.Batalla;
import entidades.Entidad;
import entidades.efectos.Pasiva;
import java.util.Random;

public class ElSalvador extends Pasiva {

    private static final Random AZAR = new Random();
    public static final int PROBABILIDAD_INICIAL = 100;
    public static final int DECAIMIENTO_POR_NODO = 10;

    private int probabilidad = PROBABILIDAD_INICIAL;
    private boolean usadoEnEstaBatalla;

    public ElSalvador() {
        super("El Salvador");
    }

    @Override
    public void alComenzarBatalla(Batalla batalla) {
        usadoEnEstaBatalla = false;
        for (Entidad aliado : batalla.aliadosDe(getPortador())) {
            aliado.aplicarEfecto(new ProteccionDelSalvador(this));
        }
    }

    public boolean intentarSalvar() {
        if (usadoEnEstaBatalla || !getPortador().estaVivo()) return false;
        if (AZAR.nextInt(100) >= probabilidad) return false;
        usadoEnEstaBatalla = true;
        return true;
    }

    public void avanzarNodo() {
        probabilidad = Math.max(0, probabilidad - DECAIMIENTO_POR_NODO);
    }

    public void descansarEnFogata() {
        probabilidad = PROBABILIDAD_INICIAL;
    }

    public int getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(int probabilidad) {
        this.probabilidad = probabilidad;
    }
}
