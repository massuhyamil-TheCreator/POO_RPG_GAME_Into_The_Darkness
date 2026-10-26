package entidades.personajes;
import entidades.Entidad;

public class Personaje extends Entidad{
    private int inventario;
    private int habilidades;
    private String atributoDistintivo;
    private String claseJugable;
    /** Inicializa una clase con sus estadísticas y kit, sin equipar ni resolver combate. */
    protected Personaje(String clase) {
        super(nombreDeClase(clase), nombreDeClase(clase), entidades.CatalogoEstadisticas.personaje(clase));
        this.claseJugable=clase;
        this.atributoDistintivo=atributoDeClase(clase);
        definirHabilidades(entidades.CatalogoHabilidades.personaje(clase));
    }
    protected void asignarClase(String clase) {
        this.claseJugable=clase;
        definirHabilidades(entidades.CatalogoHabilidades.personaje(clase));
    }
    public String getClaseJugable(){return claseJugable;}
    private static String nombreDeClase(String clase) {
        if (clase.equals("CABALLERO_SAGRADO")) return "Caballero Sagrado";
        String nombre = clase.substring(0, 1) + clase.substring(1).toLowerCase();
        return nombre;
    }
    private static String atributoDeClase(String clase) {
        switch (clase) {
            case "GUERRERO": return "Fuerza";
            case "MAGO": return "Inteligencia";
            case "CURADOR": return "Curación";
            case "ARQUERO": return "Destreza";
            case "ASESINO": return "Letalidad";
            case "CABALLERO_SAGRADO": return "Constitución";
            case "CAZADOR": return "Astucia";
            default: return "";
        }
    }
    public String getAtributoDistintivo(){return atributoDistintivo;}

    public Personaje(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int inventario, int habilidades, String atributoDistintivo) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica);
        this.inventario = inventario;
        this.habilidades = habilidades;
        this.atributoDistintivo = atributoDistintivo;
    }
}
