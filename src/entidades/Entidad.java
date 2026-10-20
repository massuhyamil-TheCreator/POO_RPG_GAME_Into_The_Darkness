package entidades;
import java.util.List;

public class Entidad {
    private String nombre;
    private String categoria;
    private int puntosDeSaludMaximos;
    private int recurso;
    private int recursoMaximo;
    private int precision;
    private int nivel;
    private int experiencia;
    private int puntosDeSalud;
    private int velocidad;
    private int ataqueFisico;
    private int ataqueMagico;
    private int defensaFisica;
    private int defensaMagica;
    private List<Habilidad> habilidades;

    public Entidad(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.puntosDeSalud = puntosDeSalud;
        this.puntosDeSaludMaximos = puntosDeSaludMaximos;
        this.recurso = recurso;
        this.recursoMaximo = recursoMaximo;
        this.velocidad = velocidad;
        this.precision = precision;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.ataqueFisico = ataqueFisico;
        this.ataqueMagico = ataqueMagico;
        this.defensaFisica = defensaFisica;
        this.defensaMagica = defensaMagica;
    }

   public int atacar(Entidad entidadObjetivo) {
        entidadObjetivo.puntosDeSalud = entidadObjetivo.puntosDeSalud - this.ataqueFisico;
        return this.ataqueFisico;
    }

   public boolean estaVivo() {
        return this.puntosDeSalud > 0;
    }

   public boolean esMasVeloz(Entidad otraEntidad) {
        return this.velocidad > otraEntidad.velocidad;
    }

   public void mostrarEstado() {
        System.out.println("El " + this.nombre + " tiene " + this.puntosDeSalud + " puntos de salud.");
    }

    public String toString() {
        return this.nombre;
    }

}

