package modelo;
//package modelo;: dice en qué carpeta vive. Tiene que coincidir con la carpeta modelo.

public class personaje extends entidad {
//public class personaje: a diferencia de entidad, acá NO hay abstract, así que sí se puede hacer new personaje(...).
//extends entidad: personaje HEREDA todo lo de entidad (atributos, atacarFisico, atacarMagico, recibirDanio, estaViva y los getters).
//Por eso no hace falta volver a escribirlos.

    public personaje(String nombre, int vidaMaxima, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int velocidad) {
        super(nombre, vidaMaxima, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica, velocidad);
    }
//Los constructores de las hijas tienen que recibir los mismos 7 datos y pasárselos al padre con super(...) en el mismo orden.

}
