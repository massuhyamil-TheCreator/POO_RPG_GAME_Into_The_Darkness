package dto;
import java.util.List;

// La pantalla final: Victoria o Game Over.
public class ResultadoFinal {

    private final boolean ganada;
    private final String mensajes;
    private final int piso;
    private final List<String> party;
    private final int oro;
    private final boolean hayGuardado;

    public ResultadoFinal(boolean ganada, String mensajes, int piso, List<String> party, int oro, boolean hayGuardado) {
        this.ganada = ganada;
        this.mensajes = mensajes;
        this.piso = piso;
        this.party = party;
        this.oro = oro;
        this.hayGuardado = hayGuardado;
    }

    public boolean isGanada() { return ganada; }
    public String getMensajes() { return mensajes; }
    public int getPiso() { return piso; }
    // "Mago  ·  nivel 3"
    public List<String> getParty() { return party; }
    public int getOro() { return oro; }
    public boolean hayGuardado() { return hayGuardado; }
}
