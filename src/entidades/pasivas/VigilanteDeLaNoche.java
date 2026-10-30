package entidades.pasivas;
import entidades.Categoria;
import entidades.Entidad;
import entidades.efectos.Pasiva;

public class VigilanteDeLaNoche extends Pasiva {

    public VigilanteDeLaNoche() {
        super("Vigilante de la Noche");
    }

    @Override
    public int modificarDanioInfligido(int danio, Entidad objetivo) {
        if (objetivo.getCategoria() == Categoria.CRIATURA_NOCTURNA) {
            return danio * 3 / 2;
        }
        return danio;
    }
}
