package entidades.enemigos;
import entidades.Categoria;
import entidades.efectos.Encantado;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class Vampiro extends Enemigo {

    public Vampiro(String nombre) {
        super(nombre, Categoria.CRIATURA_NOCTURNA, 30, 12, 85, 2, 2, 2, 2, 18, 12);
        setBasico(new Habilidad("Sed de Sangre", 0, Objetivo.UN_ENEMIGO, 3, 2)
                .conRoboDeVida(1));
        agregarHabilidad(new Habilidad("Curación Vampírica", 0, Objetivo.SI_MISMO, 0, 0)
                .conCuracion(5));
        agregarHabilidad(new Habilidad("Encanto Vampírico", 0, Objetivo.UN_ENEMIGO, 0, 8)
                .conEfecto(() -> new Encantado(this, 2), 100));
    }
}
