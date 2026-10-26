package entidades;
import java.util.Map;
import java.util.HashMap;

/** Estadísticas base acordadas. Ataque físico/mágico adicional = 0: el daño propio de
 * Espadazo, Bola de Fuego, etc. está definido en CatalogoHabilidades, no se duplica. */
public class CatalogoEstadisticas {
    private static final Map<String, EstadisticasBase> PERSONAJES = new HashMap<>();
    private static final Map<String, EstadisticasBase> ENEMIGOS = new HashMap<>();
    private static final Map<String, EstadisticasBase> INVOCACIONES = new HashMap<>();
    static {
        pj("GUERRERO", 48,22,3,2,90,9,5,0,2);
        pj("MAGO", 30,30,1,3,90,10,5,0,1);
        pj("CURADOR",36,26,2,3,90,9,5,0,2);
        pj("ARQUERO",36,18,2,1,95,15,10,20,2);
        pj("ASESINO",32,18,1,1,90,17,15,0,2);
        pj("CABALLERO_SAGRADO",55,24,4,3,90,7,5,0,2);
        pj("CAZADOR",40,18,2,2,90,11,5,0,2);
        en("ZOMBIE",42,8,3,1,80,5,0);
        en("ESQUELETO",39,10,2,1,85,12,0);
        en("LOBO_SALVAJE",38,8,1,1,90,16,0);
        en("HOMBRE_LOBO",55,15,2,2,85,13,0);
        en("VAMPIRO",51,16,2,3,90,14,0);
        en("BRUJA",46,20,1,3,85,10,0);
        en("DRAGON",120,30,4,4,90,11,0);
        en("GOLEM",78,20,6,2,85,4,0);
        en("REY_VAMPIRO",146,28,3,4,95,15,5);
        INVOCACIONES.put("GUARDIA_REAL", new EstadisticasBase(14,0,2,1,85,7,0,0,0,0,0));
        INVOCACIONES.put("COPIA_GOLEM", new EstadisticasBase(1,0,0,0,85,4,0,0,0,0,0));
        INVOCACIONES.put("MURCIELAGO", new EstadisticasBase(1,0,0,0,80,18,0,0,0,60,0));
    }
    private CatalogoEstadisticas() {}
    private static void pj(String tipo,int hp,int rec,int df,int dm,int precision,int velocidad,int critico,int evasion,int regen) {
        PERSONAJES.put(tipo,new EstadisticasBase(hp,rec,df,dm,precision,velocidad,0,0,critico,evasion,regen));
    }
    private static void en(String tipo,int hp,int rec,int df,int dm,int precision,int velocidad,int critico) {
        ENEMIGOS.put(tipo,new EstadisticasBase(hp,rec,df,dm,precision,velocidad,0,0,critico,0,2));
    }
    public static EstadisticasBase personaje(String tipo) {return PERSONAJES.get(tipo);}
    public static EstadisticasBase enemigo(String tipo) {return ENEMIGOS.get(tipo);}
    public static EstadisticasBase invocacion(String tipo) {return INVOCACIONES.get(tipo);}
    public static Map<String,EstadisticasBase> personajes(){return new HashMap<>(PERSONAJES);}
    public static Map<String,EstadisticasBase> enemigos(){return new HashMap<>(ENEMIGOS);}
    public static Map<String,EstadisticasBase> invocaciones(){return new HashMap<>(INVOCACIONES);}
}
