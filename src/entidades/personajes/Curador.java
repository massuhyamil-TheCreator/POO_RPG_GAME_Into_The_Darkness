package entidades.personajes;
import entidades.efectos.Invulnerabilidad;
import entidades.efectos.ReduccionCuracion;
import entidades.habilidades.Bastonazo;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.pasivas.ElSalvador;

public class Curador extends Personaje {

    private int curacion;
    private ElSalvador salvador = new ElSalvador();

    public Curador(String nombre) {
        super(nombre, "Curación", 45, 10, 90, 2, 4, 2, 4);
        this.curacion = 10;
        aplicarEfecto(salvador);
        setBasico(new Bastonazo());
        agregarHabilidad(new Habilidad("Curación Divina", 20, Objetivo.UN_ALIADO, 0, 0)
                .conCuracion(10)
                .conEfecto(() -> new Invulnerabilidad(1), 100));
        agregarHabilidad(new Habilidad("Azote del Curandero", 15, Objetivo.UN_ENEMIGO, 0, 4)
                .conEfecto(() -> new ReduccionCuracion("Azotado", 40, 3), 100));
        agregarHabilidad(new Habilidad("Segundo Aliento", 5, Objetivo.OTRO_ALIADO, 0, 0)
                .conMana(10));
    }

    public ElSalvador getSalvador() {
        return salvador;
    }

    public int getCuracion() {
        return curacion;
    }

    public void setCuracion(int curacion) {
        this.curacion = curacion;
    }

    @Override
    protected void subirNivel() {
        subirNivel(5, 0, 1, 1, 1, 0);
        curacion++;
    }

    @Override
    public int getCapacidadInventario() {
        return 4;
    }
}
