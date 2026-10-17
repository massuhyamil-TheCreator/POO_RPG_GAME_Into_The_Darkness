package app;
//package app: va en otra carpeta (src/app) porque no es parte del modelo, es el programa que lo pone a funcionar.

import modelo.batalla;
import modelo.enemigo;
import modelo.entidad;
import modelo.personaje;
//import: para usar clases de otra carpeta (otro paquete) hay que traerlas. Cada línea trae una clase de modelo.

public class main {
//La clase se llama main porque el archivo se llama main.java. Tienen que ser iguales.

    public static void main(String[] args) {
//main: es el punto de entrada. Cuando ejecutás el programa, Java busca este método y empieza por acá.
//Se escribe siempre igual. Ojo con String con S mayúscula: es una clase propia de Java.

        entidad mago = new personaje("Mago", 100, 6, 24, 4, 8, 12);
        entidad esqueleto = new enemigo("Esqueleto", 80, 17, 0, 5, 2, 8);
//new crea los objetos. El orden de los números es: vida 100, ataque físico 6, ataque mágico 24, defensa física 4, defensa mágica 8, velocidad 12.
//La variable es de tipo entidad pero el objeto es un personaje: se puede porque un personaje ES una entidad.

        batalla pelea = new batalla(mago, esqueleto);
        System.out.println(pelea.getMensaje());
//Se crea la batalla con los dos combatientes. System.out.println escribe una línea en la consola.
//La variable se llama pelea (y no batalla) para no confundirla con la clase.

        int ronda = 1;
        while (!pelea.termino()) {
//while: repite el bloque mientras la condición sea verdadera. Acá: mientras la batalla NO haya terminado.

            System.out.println("\n--- Ronda " + ronda + " ---");
            pelea.atacar();
            System.out.println(pelea.getMensaje());
            System.out.println(mago.getNombre() + " vida: " + mago.getVidaActual() + "/" + mago.getVidaMaxima());
            System.out.println(esqueleto.getNombre() + " vida: " + esqueleto.getVidaActual() + "/" + esqueleto.getVidaMaxima());
            ronda++;
//En cada vuelta: se juega una ronda, se muestra lo que pasó y la vida de cada uno. ronda++ suma 1 al contador.
        }

        System.out.println("\nLa batalla terminó.");
    }
}