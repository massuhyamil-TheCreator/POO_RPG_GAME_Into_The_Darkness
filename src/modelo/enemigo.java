package modelo;
//package modelo;: misma carpeta que entidad y personaje.

public class enemigo extends entidad {
//enemigo también hereda de entidad, igual que personaje.
//Por eso un enemigo puede atacar a un personaje y un personaje a un enemigo con los mismos métodos atacarFisico() y atacarMagico().

    public enemigo(String nombre, int vidaMaxima, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int velocidad) {
        super(nombre, vidaMaxima, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, velocidad);
    }
//Los constructores de las hijas tienen que recibir los mismos 7 datos y pasárselos al padre con super(...) en el mismo orden.

}