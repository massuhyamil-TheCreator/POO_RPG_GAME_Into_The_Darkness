package ui;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.event.ActionEvent;
import controlador.ControladorJuego;
import excepciones.PartidaException;

// La pausa (botón Pausa o tecla Esc) del mapa, la preparación y el combate.
// Todo se guarda en la ranura de la partida.
public class MenuDePausa {

    // devuelve true si se sigue jugando, false si se volvió al menú
    public static boolean abrir(Navegador navegador, Component pantalla, ControladorJuego juego) {
        String aviso = juego.estaEnBatalla()
                ? "Si guardás en medio de una pelea, al cargar volvés al principio de esa pelea."
                : "La partida se guarda en la ranura " + juego.getRanura() + ".";
        while (true) {
            int opcion = Dialogo.elegir(pantalla, "Pausa", aviso, "Reanudar", "Guardar", "Guardar y salir", "Salir sin guardar");
            if (opcion <= 0) return true;
            if (opcion == 1) {
                if (guardar(pantalla, juego)) Dialogo.avisar(pantalla, "Partida guardada", "Se guardó en la ranura " + juego.getRanura() + ".");
            } else if (opcion == 2) {
                if (guardar(pantalla, juego)) {
                    navegador.cambiarPantallaConFundido("menu");
                    return false;
                }
            } else {
                int seguro = Dialogo.elegir(pantalla, "Salir sin guardar",
                        "Se pierde lo que jugaste desde la última vez que guardaste.", "Salir", "Cancelar");
                if (seguro == 0) {
                    navegador.cambiarPantallaConFundido("menu");
                    return false;
                }
            }
        }
    }

    public static boolean guardar(Component pantalla, ControladorJuego juego) {
        try {
            juego.guardar();
            return true;
        } catch (PartidaException e) {
            Dialogo.avisar(pantalla, "No se pudo guardar", e.getMessage());
            return false;
        }
    }

    // la tecla Esc ejecuta la acción mientras esa pantalla sea la que se ve
    public static void conEscape(JComponent pantalla, Runnable accion) {
        pantalla.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "pausa");
        pantalla.getActionMap().put("pausa", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pantalla.isShowing()) accion.run();
            }
        });
    }
}
