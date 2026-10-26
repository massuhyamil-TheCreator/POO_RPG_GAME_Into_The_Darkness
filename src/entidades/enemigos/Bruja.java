package entidades.enemigos;

public class Bruja extends Enemigo {
    public Bruja() { super("BRUJA"); }

    private int vitalidad;

    public Bruja(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int experienciaOtorgada, int oroOtorgado, String objetoOtorgado, int vitalidad) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, experienciaOtorgada, oroOtorgado, objetoOtorgado);
        this.vitalidad = vitalidad;
        asignarTipo("BRUJA");
    }

    public int getVitalidad() {
        return vitalidad;
    }

    public void setVitalidad(int vitalidad) {
        this.vitalidad = vitalidad;
    }
}
