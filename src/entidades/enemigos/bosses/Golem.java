package entidades.enemigos.bosses;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.Aturdimiento;
import entidades.efectos.Invulnerabilidad;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.habilidades.PiedraViviente;
import entidades.pasivas.PielDePiedra;

public class Golem extends Jefe {

    public Golem() {
        super("Golem", Categoria.JEFE, 260, 4, 85, 10, 4, 5, 4, 100, 100);
        aplicarEfecto(new PielDePiedra());
        setBasico(new Habilidad("Puñetazo de Piedra", 0, Objetivo.UN_ENEMIGO, 8, 0));
        agregarHabilidad(new PiedraViviente(() -> new CopiaDeGolem()));
        agregarHabilidad(new Habilidad("Lanzamiento de Piedra", 0, Objetivo.UN_ENEMIGO, 5, 2)
                .conEfecto(() -> new Aturdimiento("Aturdido", 1), 100));
        agregarHabilidad(new Habilidad("Endurecimiento", 0, Objetivo.SI_MISMO, 0, 0)
                .conEfecto(() -> new Invulnerabilidad(1), 100));
        agregarHabilidad(new Habilidad("Lanzamiento de Lodo", 0, Objetivo.VARIOS_ENEMIGOS, 0, 2)
                .conCantidadDeObjetivos(2, 2)
                .conEfecto(() -> new ModificadorEstadistica("Embarrado", 2).porcentaje(Estadistica.VELOCIDAD, -30), 100));
    }
}
