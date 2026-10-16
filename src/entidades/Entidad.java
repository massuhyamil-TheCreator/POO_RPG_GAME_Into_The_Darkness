import java.util.List;

public class Entidad {
    private String nombre;
    private int puntosDeSalud;
    private int velocidad;
    private int ataqueFisico;
    private int ataqueMagico;
    private int defensaFisica;
    private int defensaMagica;
    private List<Habilidad> habilidades;

    public Entidad(String nombre, int ataqueFisico, int puntosDeSalud, int velocidad) {
        this.nombre = nombre;
        this.ataqueFisico = ataqueFisico;
        this.puntosDeSalud = puntosDeSalud;
        this.velocidad = velocidad;

    }

    int atacar(Entidad entidadObjetivo) {
        entidadObjetivo.puntosDeSalud = entidadObjetivo.puntosDeSalud - this.ataqueFisico;
        return this.ataqueFisico;
    }

    boolean estaVivo() {
        return this.puntosDeSalud > 0;
    }

    boolean esMasVeloz(Entidad otraEntidad) {
        return this.velocidad > otraEntidad.velocidad;
    }

    void mostrarEstado() {
        System.out.println("El " + this.nombre + " tiene " + this.puntosDeSalud + " puntos de salud.");
    }

    public String toString() {
        return this.nombre;
    }

}

