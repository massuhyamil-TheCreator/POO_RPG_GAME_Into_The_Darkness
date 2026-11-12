package dto;
import java.util.ArrayList;
import java.util.List;

// Los datos de un personaje o enemigo listos para mostrar. La pantalla trabaja con esto y no con
// las clases del modelo. Tiene los mismos getters que Entidad para que el código de la vista sea el mismo.
public class EntidadDTO {

    private final int id;
    private final String nombre;
    private final String tipo;
    private final int nivel;
    private final int puntosDeSalud;
    private final int puntosDeSaludMaximos;
    private final int recurso;
    private final int recursoMaximo;
    private final int ataqueFisico;
    private final int ataqueMagico;
    private final int defensaFisica;
    private final int defensaMagica;
    private final int velocidad;
    private final int precision;
    private final boolean vivo;
    private final boolean jefe;
    private final String efectos;
    private final int experiencia;
    private final int experienciaParaSubir;
    private final boolean nivelMaximo;
    private final int espaciosUsados;
    private final int capacidadInventario;
    private final boolean mochilaLlena;
    private final String arma;
    private final List<String> mochila;

    private EntidadDTO(Builder b) {
        this.id = b.id;
        this.nombre = b.nombre;
        this.tipo = b.tipo;
        this.nivel = b.nivel;
        this.puntosDeSalud = b.puntosDeSalud;
        this.puntosDeSaludMaximos = b.puntosDeSaludMaximos;
        this.recurso = b.recurso;
        this.recursoMaximo = b.recursoMaximo;
        this.ataqueFisico = b.ataqueFisico;
        this.ataqueMagico = b.ataqueMagico;
        this.defensaFisica = b.defensaFisica;
        this.defensaMagica = b.defensaMagica;
        this.velocidad = b.velocidad;
        this.precision = b.precision;
        this.vivo = b.vivo;
        this.jefe = b.jefe;
        this.efectos = b.efectos;
        this.experiencia = b.experiencia;
        this.experienciaParaSubir = b.experienciaParaSubir;
        this.nivelMaximo = b.nivelMaximo;
        this.espaciosUsados = b.espaciosUsados;
        this.capacidadInventario = b.capacidadInventario;
        this.mochilaLlena = b.mochilaLlena;
        this.arma = b.arma;
        this.mochila = new ArrayList<>(b.mochila);
    }

    // identifica a la entidad en las acciones que se le piden al controlador
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    // "Mago", "Zombie"...: sirve para elegir el arte
    public String getTipo() { return tipo; }
    public int getNivel() { return nivel; }
    public int getPuntosDeSalud() { return puntosDeSalud; }
    public int getPuntosDeSaludMaximos() { return puntosDeSaludMaximos; }
    public int getRecurso() { return recurso; }
    public int getRecursoMaximo() { return recursoMaximo; }
    public int getAtaqueFisico() { return ataqueFisico; }
    public int getAtaqueMagico() { return ataqueMagico; }
    public int getDefensaFisica() { return defensaFisica; }
    public int getDefensaMagica() { return defensaMagica; }
    public int getVelocidad() { return velocidad; }
    public int getPrecision() { return precision; }
    public boolean estaVivo() { return vivo; }
    public boolean esJefe() { return jefe; }
    // los estados que tiene encima, separados por coma (sin las pasivas)
    public String getEfectos() { return efectos; }
    public int getExperiencia() { return experiencia; }
    public int getExperienciaParaSubir() { return experienciaParaSubir; }
    public boolean esNivelMaximo() { return nivelMaximo; }
    public int getEspaciosUsados() { return espaciosUsados; }
    public int getCapacidadInventario() { return capacidadInventario; }
    public boolean isMochilaLlena() { return mochilaLlena; }
    // el arma equipada ya descripta ("Espada larga (+2 fís, +0 mág)") o "ninguna"
    public String getArma() { return arma; }
    // nombres de lo que lleva en la mochila
    public List<String> getMochila() { return mochila; }

    // Lo arma el controlador. Los campos son públicos porque es solo un contenedor de datos.
    public static class Builder {
        public int id;
        public String nombre = "";
        public String tipo = "";
        public int nivel;
        public int puntosDeSalud;
        public int puntosDeSaludMaximos;
        public int recurso;
        public int recursoMaximo;
        public int ataqueFisico;
        public int ataqueMagico;
        public int defensaFisica;
        public int defensaMagica;
        public int velocidad;
        public int precision;
        public boolean vivo;
        public boolean jefe;
        public String efectos = "";
        public int experiencia;
        public int experienciaParaSubir;
        public boolean nivelMaximo;
        public int espaciosUsados;
        public int capacidadInventario;
        public boolean mochilaLlena;
        public String arma = "ninguna";
        public List<String> mochila = new ArrayList<>();

        public EntidadDTO build() {
            return new EntidadDTO(this);
        }
    }
}
