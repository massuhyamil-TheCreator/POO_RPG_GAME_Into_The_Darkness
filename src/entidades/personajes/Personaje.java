public class Personaje extends Entidad{
    private int experiencia;
    private int inventario;
    private int habilidades;
    private String atributoDistintivo;

    public Personaje(String nombre,int ataque,int puntosDeSalud,int experiencia, int inventario, int habilidades, String atributoDistintivo,int velocidad) {
        super(nombre, ataque, puntosDeSalud,velocidad);
        this.experiencia = experiencia;
        this.habilidades = habilidades;
        this.inventario = inventario;
        this.atributoDistintivo = atributoDistintivo;
    }
}
