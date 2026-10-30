package entidades.personajes;
import entidades.Categoria;
import entidades.Estadistica;
import entidades.efectos.ModificadorEstadistica;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Invocacion;
import entidades.habilidades.Objetivo;
import entidades.habilidades.Provocacion;
import entidades.pasivas.PactoDelCaballero;

public class Caballero extends Personaje {

    private int constitucion;

    public Caballero(String nombre) {
        super(nombre, "Constitución", 65, 6, 85, 3, 1, 5, 3);
        this.constitucion = 10;
        aplicarEfecto(new PactoDelCaballero());
        setBasico(new Habilidad("Mandoble Divino", 0, Objetivo.UN_ENEMIGO, 4, 0)
                .conDanioExtraContra(Categoria.NO_MUERTO, 1));
        agregarHabilidad(new Provocacion());
        agregarHabilidad(new Habilidad("Discurso Heroico", 20, Objetivo.TODOS_LOS_ALIADOS, 0, 0)
                .quitandoNegativos()
                .conEfecto(() -> new ModificadorEstadistica("Discurso Heroico", 2)
                        .porcentaje(Estadistica.ATAQUE_FISICO, 20)
                        .porcentaje(Estadistica.ATAQUE_MAGICO, 20), 100));
        agregarHabilidad(new Invocacion("Guardia Real", 20, () -> new GuardiaReal(), 1)
                .conEnfriamiento(4));
    }

    public int getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(int constitucion) {
        this.constitucion = constitucion;
    }

    @Override
    protected void subirNivel() {
        subirNivel(8, 1, 0, 1, 1, 0);
        constitucion++;
    }

    @Override
    public int getCapacidadInventario() {
        return 6;
    }
}

