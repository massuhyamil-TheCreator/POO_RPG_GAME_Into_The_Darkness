package entidades.personajes;

public class Asesino extends Personaje {

    private int letalidad;

    public Asesino(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int inventario, int habilidades, String atributoDistintivo, int letalidad) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, inventario, habilidades, atributoDistintivo);
        this.letalidad = letalidad;
    }

    public int getLetalidad() {
        return letalidad;
    }

    public void setLetalidad(int letalidad) {
        this.letalidad = letalidad;
    }
}
