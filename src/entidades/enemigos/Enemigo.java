package entidades.enemigos;
import entidades.Entidad;

public class Enemigo extends Entidad {
    private int experienciaOtorgada;
    private int oroOtorgado;
    private String objetoOtorgado;
    private String tipoEnemigo;
    /** XP, oro y drop pendientes de diseñar: 0 y null no son recompensas definitivas. */
    protected Enemigo(String tipo) {
        super(nombreDelEnemigo(tipo), categoriaDelEnemigo(tipo), entidades.CatalogoEstadisticas.enemigo(tipo));
        this.tipoEnemigo=tipo;
        definirHabilidades(entidades.CatalogoHabilidades.enemigo(tipo));
    }
    protected void asignarTipo(String tipo) {
        this.tipoEnemigo=tipo;
        definirHabilidades(entidades.CatalogoHabilidades.enemigo(tipo));
    }
    private static String nombreDelEnemigo(String tipo) {
        switch (tipo) {
            case "ZOMBIE": return "Zombie";
            case "ESQUELETO": return "Esqueleto";
            case "LOBO_SALVAJE": return "Lobo Salvaje";
            case "HOMBRE_LOBO": return "Hombre Lobo";
            case "VAMPIRO": return "Vampiro";
            case "BRUJA": return "Bruja";
            case "DRAGON": return "Dragón";
            case "GOLEM": return "Golem";
            case "REY_VAMPIRO": return "Rey Vampiro";
            default: return tipo;
        }
    }
    private static String categoriaDelEnemigo(String tipo) {
        if (tipo.equals("ZOMBIE") || tipo.equals("ESQUELETO")) return "No Muerto";
        if (tipo.equals("LOBO_SALVAJE") || tipo.equals("HOMBRE_LOBO")) return "Bestia";
        if (tipo.equals("VAMPIRO") || tipo.equals("BRUJA") || tipo.equals("REY_VAMPIRO")) return "Criatura Nocturna";
        return "Jefe";
    }
    public String getTipoEnemigo(){return tipoEnemigo;}
    public int getExperienciaOtorgada(){return experienciaOtorgada;}
    public int getOroOtorgado(){return oroOtorgado;}
    public String getObjetoOtorgado(){return objetoOtorgado;}

    public Enemigo(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int experienciaOtorgada, int oroOtorgado, String objetoOtorgado) {
        super(nombre, categoria, puntosDeSalud, puntosDeSaludMaximos, recurso, recursoMaximo, velocidad, precision, nivel, experiencia, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica);
        this.experienciaOtorgada = experienciaOtorgada;
        this.oroOtorgado = oroOtorgado;
        this.objetoOtorgado = objetoOtorgado;
    }
}