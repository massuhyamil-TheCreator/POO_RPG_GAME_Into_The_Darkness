package entidades.enemigos;
import entidades.Entidad;

public class Enemigo extends Entidad {
    private int experienciaOtorgada;
    private int oroOtorgado;
    private String objetoOtorgado;

    public Enemigo(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int experienciaOtorgada, int oroOtorgado, String objetoOtorgado) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica);
        this.experienciaOtorgada = experienciaOtorgada;
        this.oroOtorgado = oroOtorgado;
        this.objetoOtorgado = objetoOtorgado;
    }
}