package entidades.pasivas;
import combate.Batalla;
import entidades.Entidad;
import entidades.efectos.Pasiva;

public class PactoDelCaballero extends Pasiva {

    public PactoDelCaballero() {
        super("Pacto del Caballero");
    }

    @Override
    public void alComenzarBatalla(Batalla batalla) {
        for (Entidad aliado : batalla.aliadosDe(getPortador())) {
            aliado.aplicarEfecto(new ProteccionDelPacto(this));
        }
    }
}
