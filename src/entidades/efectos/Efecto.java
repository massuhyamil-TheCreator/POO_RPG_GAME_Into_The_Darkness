package entidades.efectos;
import combate.Batalla;
import entidades.Entidad;
import entidades.Estadistica;

public abstract class Efecto {

    public static final int PERMANENTE = -1;

    private String nombre;
    private int turnosRestantes;
    private Entidad portador;
    private boolean recienAplicado;

    public Efecto(String nombre, int duracion) {
        this.nombre = nombre;
        this.turnosRestantes = duracion;
    }

    public String alInicioDelTurno(Entidad portador) {
        return null;
    }

    public void alComenzarBatalla(Batalla batalla) {
    }

    public int modificarDanioRecibido(int danio) {
        return danio;
    }

    public int modificarCuracionRecibida(int curacion) {
        return curacion;
    }

    public int modificarDanioInfligido(int danio, Entidad objetivo) {
        return danio;
    }

    public boolean permiteAtacarA(Entidad objetivo) {
        return true;
    }

    public boolean anulaCritico() {
        return false;
    }

    public int modificar(Estadistica estadistica, int valor) {
        return valor;
    }

    public boolean puedeActuar() {
        return true;
    }

    public boolean puedeUsarItems() {
        return true;
    }

    public boolean bloquea(Efecto nuevo) {
        return false;
    }

    public boolean descontrola() {
        return false;
    }

    public Entidad objetivoForzado() {
        return null;
    }

    public boolean ahorraCosto() {
        return false;
    }

    public boolean esPasiva() {
        return false;
    }

    public boolean esNegativo() {
        return false;
    }

    public void marcarComoRecienAplicado() {
        recienAplicado = true;
    }

    public void pasarTurno() {
        if (recienAplicado) {
            recienAplicado = false;
            return;
        }
        if (turnosRestantes > 0) turnosRestantes--;
    }

    public void terminar() {
        turnosRestantes = 0;
    }

    public boolean termino() {
        return turnosRestantes == 0;
    }

    public String getNombre() { return nombre; }
    public int getTurnosRestantes() { return turnosRestantes; }
    public Entidad getPortador() { return portador; }
    public void setPortador(Entidad portador) { this.portador = portador; }

    @Override
    public String toString() {
        return turnosRestantes == PERMANENTE ? nombre : nombre + " (" + turnosRestantes + ")";
    }
}
