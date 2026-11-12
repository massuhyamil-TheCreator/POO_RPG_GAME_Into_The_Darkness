package ui;

// Lo único que las pantallas le piden a la ventana principal: cambiar de pantalla.
// (La partida y las acciones del juego no pasan por acá: van por ControladorJuego.)
public interface Navegador {
    void mostrarPantalla(String nombre);
    void cambiarPantallaConFundido(String nombrePantalla);
    void elegirPersonajes(int ranura);
    void jugar();
    void preparar();
    void combatir();
    void volverAlMapa(String resumen);
    void finalizar();
}
