package entidades.enemigos;
import entidades.Categoria;
import entidades.efectos.AtaduraEspectral;
import entidades.efectos.ReduccionCuracion;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;

public class Esqueleto extends Enemigo {

    public Esqueleto(String nombre) {
        super(nombre, Categoria.NO_MUERTO, 30, 9, 85, 3, 2, 2, 2, 15, 10);
        setBasico(new Habilidad("Flechazo", 0, Objetivo.UN_ENEMIGO, 6, 0));
        agregarHabilidad(new Habilidad("Flechazo Espectral", 0, Objetivo.UN_ENEMIGO, 3, 2)
                .conEfecto(() -> new AtaduraEspectral(2), 100));
        agregarHabilidad(new Habilidad("Maldición de la Cripta", 0, Objetivo.UN_ENEMIGO, 0, 0)
                .conEfecto(() -> new ReduccionCuracion("Maldito", 50, 3), 100));
    }
}

