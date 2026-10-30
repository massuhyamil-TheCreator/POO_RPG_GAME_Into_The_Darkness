package objetos.equipables;
import objetos.Item;
// Datos compartidos por armas y armaduras.
// (merge) Lo de clase permitida y bonos viene del catálogo por clase (CatalogoArmas/CatalogoArmaduras):
// por ahora solo se guarda, la partida todavía no lo aplica al personaje.
public abstract class Equipables extends Item {

    private String tipo;
    private String clasePermitida;
    private int bonoVida, bonoAtaqueFisico, bonoAtaqueMagico;
    private int bonoDefensaFisica, bonoDefensaMagica, bonoVelocidad;
    private int bonoCuracionPorcentaje, bonoCriticoPorcentaje;

    public Equipables(int id, String nombre, String descripcion, double precio, String tipo) {
        super(id, nombre, descripcion, precio);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getClasePermitida() { return clasePermitida; }
    public boolean permiteClase(String clase) { return clasePermitida != null && clasePermitida.equals(clase); }
    public void setClasePermitida(String clase) { this.clasePermitida = clase; }

    public int getBonoVida() { return bonoVida; }
    public int getBonoAtaqueFisico() { return bonoAtaqueFisico; }
    public int getBonoAtaqueMagico() { return bonoAtaqueMagico; }
    public int getBonoDefensaFisica() { return bonoDefensaFisica; }
    public int getBonoDefensaMagica() { return bonoDefensaMagica; }
    public int getBonoVelocidad() { return bonoVelocidad; }
    public int getBonoCuracionPorcentaje() { return bonoCuracionPorcentaje; }
    public int getBonoCriticoPorcentaje() { return bonoCriticoPorcentaje; }

    // Únicamente guarda las bonificaciones: NO las aplica al personaje.
    public void definirBonos(int vida, int fisico, int magico, int defFisica,
                             int defMagica, int velocidad, int curacion, int critico) {
        this.bonoVida = vida; this.bonoAtaqueFisico = fisico; this.bonoAtaqueMagico = magico;
        this.bonoDefensaFisica = defFisica; this.bonoDefensaMagica = defMagica;
        this.bonoVelocidad = velocidad; this.bonoCuracionPorcentaje = curacion;
        this.bonoCriticoPorcentaje = critico;
    }
}
