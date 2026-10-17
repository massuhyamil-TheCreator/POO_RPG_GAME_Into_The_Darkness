package modelo;
//package modelo: misma carpeta que las otras clases del modelo.

public class batalla {
//batalla maneja UNA pelea 1 contra 1. No sabe nada de pantallas ni de consola, solo la lógica.

    private final entidad jugador;
    private final entidad oponente;
    private String mensaje;
//Atributos: los dos combatientes y un texto (mensaje) con lo último que pasó, para que otras clases lo puedan mostrar.
//Son de tipo entidad (y no personaje o enemigo) para que acepten a cualquiera de los dos.
//La variable se llama oponente y no enemigo para no confundirla con la clase enemigo (las dos están en minúscula).

    public batalla(entidad jugador, entidad oponente) {
        this.jugador = jugador;
        this.oponente = oponente;
        this.mensaje = "Comienza la batalla.";
    }
//El constructor: recibe los dos combatientes y los guarda. El mensaje arranca con un texto inicial.

    public void atacar() {
        if (termino()) {
            return;
        }
//atacar() juega una ronda. Si la batalla ya terminó, return; corta el método y no hace nada.

        int danioJugador = jugador.atacarMagico(oponente);
//El jugador pega con ataque MÁGICO. Por ahora el tipo está fijo acá solo para probar.
//Más adelante lo va a decidir la acción o habilidad que elija el jugador.

        mensaje = jugador.getNombre() + " atacó a " + oponente.getNombre()
                + " por " + danioJugador + " de daño.";
//Se arma el mensaje uniendo textos y números con +. Acá se pisa lo que había con un mensaje nuevo para la ronda.

        if (!oponente.estaViva()) {
            mensaje = mensaje + "\n" + oponente.getNombre() + " fue derrotado.";
            return;
        }
//Si el oponente murió, se agrega un renglón y se corta la ronda: un muerto no contraataca.
//! significa NO. \n es un salto de línea.
//mensaje = mensaje + "..." significa: el mensaje nuevo es el que ya había MÁS este texto al final.

        int danioOponente = oponente.atacarFisico(jugador);
//El oponente contraataca con ataque FÍSICO.

        mensaje = mensaje + "\n" + oponente.getNombre() + " respondió por "
                + danioOponente + " de daño.";
//Si el oponente sigue vivo, contraataca y se suma al mensaje.

        if (!jugador.estaViva()) {
            mensaje = mensaje + "\n" + jugador.getNombre() + " fue derrotado.";
        }
//Por último, se revisa si el contraataque mató al jugador.
    }

    public boolean termino() {
        return !jugador.estaViva() || !oponente.estaViva();
    }
//termino() devuelve true si alguno de los dos ya no está vivo. || significa O.

    public entidad getJugador()  { return jugador; }
    public entidad getOponente() { return oponente; }
    public String getMensaje()   { return mensaje; }
//Getters: para que otras clases (el controlador, más adelante) lean los datos de la batalla.

}