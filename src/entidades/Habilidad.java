package entidades;
import java.util.List;
/** Datos de una habilidad; no ejecuta ataques. */
public class Habilidad {
    private String id, nombre, descripcion;
    private String tipo; private String objetivo;
    private int costo, cooldown, fisMin, fisMax, magMin, magMax, objetivosMin, objetivosMax;
    private List<Efecto> efectos;
    public Habilidad(String id, String nombre, String tipo, String objetivo,
       int costo, int cooldown, int fisMin, int fisMax, int magMin, int magMax,
       int objetivosMin, int objetivosMax, List<Efecto> efectos, String descripcion) {
       this.id=id; this.nombre=nombre;
       this.tipo=tipo; this.objetivo=objetivo;
       this.costo=costo;this.cooldown=cooldown;this.fisMin=fisMin;this.fisMax=fisMax;
       this.magMin=magMin;this.magMax=magMax;this.objetivosMin=objetivosMin;this.objetivosMax=objetivosMax;
       this.efectos=efectos;this.descripcion=descripcion;
    }
    public String getId(){return id;} public String getNombre(){return nombre;}
    public String getTipo(){return tipo;} public String getObjetivo(){return objetivo;}
    public int getCostoRecurso(){return costo;} public int getCooldown(){return cooldown;}
    public int getDanioFisicoMinimo(){return fisMin;} public int getDanioFisicoMaximo(){return fisMax;}
    public int getDanioMagicoMinimo(){return magMin;} public int getDanioMagicoMaximo(){return magMax;}
    public int getObjetivosMinimos(){return objetivosMin;} public int getObjetivosMaximos(){return objetivosMax;}
    public List<Efecto> getEfectos(){return efectos;} public String getDescripcion(){return descripcion;}
    // Los ataques automáticos no consumen turno; el resto se resuelve en la futura Batalla.
    @Override public String toString(){return nombre;}
}
