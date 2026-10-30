package entidades.pasivas;
import entidades.efectos.Efecto;

public class ProteccionDelSalvador extends Efecto {

    private ElSalvador salvador;

    public ProteccionDelSalvador(ElSalvador salvador) {
        super("Protegido por El Salvador", PERMANENTE);
        this.salvador = salvador;
    }

    @Override
    public int modificarDanioRecibido(int danio) {
        int vida = getPortador().getPuntosDeSalud();
        if (danio >= vida && salvador.intentarSalvar()) {
            return vida - 1;
        }
        return danio;
    }
}
