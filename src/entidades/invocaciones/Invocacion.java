package entidades.invocaciones;
import entidades.Entidad;
import entidades.CatalogoEstadisticas;
import entidades.CatalogoHabilidades;
/** Plantilla de invocación. Su aparición/turnos corresponden al futuro motor. */
public final class Invocacion extends Entidad {
    private final String tipo;
    public Invocacion(String tipo){
        super(nombreDeInvocacion(tipo), "Invocación", CatalogoEstadisticas.invocacion(tipo));
        this.tipo=tipo;
        definirHabilidades(CatalogoHabilidades.invocacion(tipo));
    }
    private static String nombreDeInvocacion(String tipo) {
        if (tipo.equals("GUARDIA_REAL")) return "Guardia Real";
        if (tipo.equals("COPIA_GOLEM")) return "Copia de Golem";
        return "Murciélago";
    }
    public String getTipoInvocacion(){return tipo;}
    public int getDuracionRondas(){return tipo.equals("COPIA_GOLEM") ? 2 : 3;}
    public int getMaximoSimultaneo(){return tipo.equals("GUARDIA_REAL") ? 1 : 2;}
}
