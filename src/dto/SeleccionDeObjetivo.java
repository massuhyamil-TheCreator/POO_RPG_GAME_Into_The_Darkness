package dto;
import java.util.List;

// Qué hay que hacer antes de usar una habilidad: usarla directo, elegir a quién, o no se puede.
public class SeleccionDeObjetivo {

    public enum Modo { DIRECTO, ELEGIR, NINGUNO }

    private final Modo modo;
    private final List<EntidadDTO> candidatos;
    private final String mensaje;

    private SeleccionDeObjetivo(Modo modo, List<EntidadDTO> candidatos, String mensaje) {
        this.modo = modo;
        this.candidatos = candidatos;
        this.mensaje = mensaje;
    }

    public static SeleccionDeObjetivo directo() { return new SeleccionDeObjetivo(Modo.DIRECTO, List.of(), null); }
    public static SeleccionDeObjetivo elegir(List<EntidadDTO> candidatos) { return new SeleccionDeObjetivo(Modo.ELEGIR, candidatos, null); }
    public static SeleccionDeObjetivo ninguno(String mensaje) { return new SeleccionDeObjetivo(Modo.NINGUNO, List.of(), mensaje); }

    public Modo getModo() { return modo; }
    public List<EntidadDTO> getCandidatos() { return candidatos; }
    // si no se puede: por qué
    public String getMensaje() { return mensaje; }
}
