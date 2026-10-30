package entidades.pasivas;
import entidades.Entidad;
import entidades.Estadistica;
import entidades.efectos.Efecto;

public class ProteccionDelPacto extends Efecto {

    private static final int DEFENSA_EXTRA = 40;

    private PactoDelCaballero pacto;

    public ProteccionDelPacto(PactoDelCaballero pacto) {
        super("Protegido por el Pacto", PERMANENTE);
        this.pacto = pacto;
    }

    @Override
    public int modificar(Estadistica estadistica, int valor) {
        boolean esDefensa = estadistica == Estadistica.DEFENSA_FISICA || estadistica == Estadistica.DEFENSA_MAGICA;
        if (esDefensa && pacto.getPortador().estaVivo() && estaMalHerido()) {
            return valor * (100 + DEFENSA_EXTRA) / 100;
        }
        return valor;
    }

    private boolean estaMalHerido() {
        Entidad portador = getPortador();
        return portador.getPuntosDeSalud() * 2 < portador.getPuntosDeSaludMaximos();
    }
}
