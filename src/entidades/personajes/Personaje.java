package entidades.personajes;
import entidades.Entidad;

public class Personaje extends Entidad{
    private int inventario;
    private int habilidades;
    private String atributoDistintivo;

    public Personaje(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int inventario, int habilidades, String atributoDistintivo) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica);
        this.inventario = inventario;
        this.habilidades = habilidades;
        this.atributoDistintivo = atributoDistintivo;
    }
}
