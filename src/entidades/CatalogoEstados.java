package entidades;
import java.util.Map;
import java.util.HashMap;
/** Efectos de estado y reglas numéricas por defecto, no estados activos de la batalla. */
public class CatalogoEstados {
    private static final Map<String, Efecto> ESTADOS=new HashMap<>();
    static {
       s("SANGRADO",2,2,"Daño efectivo al inicio del turno del afectado");
       s("VENENO",1,2,"Daño efectivo al inicio del turno; no disminuye defensas");
       s("QUEMADURA",2,2,"Daño efectivo al inicio del turno");
       s("PUTREFACCION",1,2,"Daño efectivo al inicio del turno");
       s("MALDITO",25,2,"Porcentaje de reducción de curación recibida");
       s("ANTICURACION",40,2,"Porcentaje de reducción de curación recibida");
       s("ATURDIMIENTO",1,1,"Anula una acción completa");
       s("CONGELACION",1,1,"Bloquea habilidades especiales, permite ataque básico");
       s("MIEDO",1,1,"Impide ataques ofensivos, permite apoyo; Guerrero inmune");
       s("RALENTIZACION",3,2,"Reduce velocidad; la Trampa para Bobos usa 30% en vez de 3 puntos");
       s("RABIA",25,2,"Probabilidad de atacar a un aliado");
       s("ATADURA_ESPECTRAL",1,2,"Impide utilizar objetos");
       s("ENCANTO",1,2,"No puede atacar al Vampiro que aplica el encanto");
       s("RETROCESO",1,1,"Retrasa próxima acción al último lugar del orden");
       s("INVISIBILIDAD",80,2,"Reduce relativamente 80% probabilidad de ser golpeado; atacar no la cancela");
       s("INVULNERABILIDAD",100,1,"Impide todo daño mientras está activa");
       s("PROVOCACION",1,2,"Redirige ataques de un solo objetivo hacia el Caballero");
       s("DEBILITADO",20,2,"Rey Vampiro: -20% defensas y -30% ataque; Crucifijo con Ajo: -15% ambos");
       s("PRECISION",30,2,"Bomba de Humo: reducción relativa de precisión -30% comunes / -15% jefes; Penumbra -40% / -20%");
       s("EVASION",75,2,"Capa del Bosque fija 75% de evasión total");
    }
    private CatalogoEstados(){}
    private static void s(String t,double valor,int duracion,String detalle){
       ESTADOS.put(t,new Efecto(t,valor,duracion,100,detalle));
    }
    public static Efecto obtener(String t){return ESTADOS.get(t);}
    public static Map<String,Efecto> todos(){return new HashMap<>(ESTADOS);}
}
