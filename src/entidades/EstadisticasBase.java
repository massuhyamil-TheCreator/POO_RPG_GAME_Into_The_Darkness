package entidades;
/** Estadísticas iniciales de una entidad, sin efectos de batalla. */
public class EstadisticasBase {
    private int vidaMaxima;
    private int recursoMaximo;
    private int defensaFisica;
    private int defensaMagica;
    private int precision;
    private int velocidad;
    private int ataqueFisico;
    private int ataqueMagico;
    private int criticoPorcentaje;
    private int evasionPorcentaje;
    private int regeneracionRecurso;

    public EstadisticasBase(int vidaMaxima, int recursoMaximo, int defensaFisica, int defensaMagica, int precision, int velocidad, int ataqueFisico, int ataqueMagico, int criticoPorcentaje, int evasionPorcentaje, int regeneracionRecurso) {
        this.vidaMaxima = vidaMaxima;
        this.recursoMaximo = recursoMaximo;
        this.defensaFisica = defensaFisica;
        this.defensaMagica = defensaMagica;
        this.precision = precision;
        this.velocidad = velocidad;
        this.ataqueFisico = ataqueFisico;
        this.ataqueMagico = ataqueMagico;
        this.criticoPorcentaje = criticoPorcentaje;
        this.evasionPorcentaje = evasionPorcentaje;
        this.regeneracionRecurso = regeneracionRecurso;
    }

    public int getVidaMaxima() { return vidaMaxima; }
    public int getRecursoMaximo() { return recursoMaximo; }
    public int getDefensaFisica() { return defensaFisica; }
    public int getDefensaMagica() { return defensaMagica; }
    public int getPrecision() { return precision; }
    public int getVelocidad() { return velocidad; }
    public int getAtaqueFisico() { return ataqueFisico; }
    public int getAtaqueMagico() { return ataqueMagico; }
    public int getCriticoPorcentaje() { return criticoPorcentaje; }
    public int getEvasionPorcentaje() { return evasionPorcentaje; }
    public int getRegeneracionRecurso() { return regeneracionRecurso; }
}
