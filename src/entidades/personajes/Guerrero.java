package entidades.personajes;
public class Guerrero extends Personaje {
    private int fuerza; // Sin escalado numérico hasta acordar el papel del atributo distintivo.
    public Guerrero() { super("GUERRERO"); }
    public int getFuerza() {return fuerza;}
    public void setFuerza(int v) {this.fuerza=v;}
}
