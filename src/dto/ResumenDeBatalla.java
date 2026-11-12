package dto;
import java.util.List;

// Lo que se ganó (o perdió) en una pelea que terminó.
public class ResumenDeBatalla {

    private final boolean ganada;
    private final int experiencia;
    private final int oro;
    private final List<String> objetos;

    public ResumenDeBatalla(boolean ganada, int experiencia, int oro, List<String> objetos) {
        this.ganada = ganada;
        this.experiencia = experiencia;
        this.oro = oro;
        this.objetos = objetos;
    }

    public boolean isGanada() { return ganada; }
    public int getExperiencia() { return experiencia; }
    public int getOro() { return oro; }
    public List<String> getObjetos() { return objetos; }

    // el texto que se muestra al terminar la pelea
    public String texto() {
        if (!ganada) return "Toda la party cayó en combate.";
        return "Cada personaje en pie gana " + experiencia + " de experiencia.\n"
                + "La party gana " + oro + " de oro."
                + (objetos.isEmpty() ? "" : "\nEncuentran: " + String.join(", ", objetos) + ".");
    }
}
