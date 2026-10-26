package entidades.personajes;
public class Mago extends Personaje {
    private int inteligencia; // Sin escalado numérico hasta acordar el papel del atributo distintivo.
    public Mago() { super("MAGO"); }
    public int getInteligencia() {return inteligencia;}
    public void setInteligencia(int v) {this.inteligencia=v;}
}
