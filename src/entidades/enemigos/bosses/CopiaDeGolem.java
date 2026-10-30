package entidades.enemigos.bosses;
import entidades.Categoria;
import entidades.enemigos.Enemigo;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class CopiaDeGolem extends Enemigo {

    public CopiaDeGolem() {
        super("Copia de Golem", Categoria.JEFE, 1, 4, 85, 3, 1, 0, 0, 0, 0);
        setBasico(new Habilidad("Puñetazo de Piedra", 0, Objetivo.UN_ENEMIGO, 4, 0));
    }
}
