package entidades.personajes;
public class Arquero extends Personaje {
    private int destreza; // Sin escalado numérico hasta acordar el papel del atributo distintivo.
    public Arquero() { super("ARQUERO"); }
    public int getDestreza() {return destreza;}
    public void setDestreza(int v) {this.destreza=v;}
}
