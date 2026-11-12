package combate;
import entidades.enemigos.Enemigo;
import entidades.personajes.Personaje;
import objetos.Item;
import java.util.ArrayList;
import java.util.List;

// Lo que gana la party al ganar una batalla: experiencia, oro y objetos.
// Batalla decide cuándo se gana; cuánto y cómo se reparte lo resuelve esta clase.
public class Recompensas {

    private int experiencia;
    private int oro;
    private final List<Item> objetos = new ArrayList<>();

    // reparte el botín de los enemigos y devuelve los mensajes para el registro
    public List<String> otorgar(PartyAliada aliados, PartyEnemiga enemigos, boolean conExperiencia) {
        List<String> mensajes = new ArrayList<>();
        for (Enemigo enemigo : enemigos.getMiembrosEnemigos()) {
            experiencia += enemigo.getExperienciaOtorgada();
            oro += enemigo.getOroOtorgado();
            Item objeto = enemigo.robarObjeto();
            if (objeto != null) {
                objetos.add(objeto);
                aliados.getInventario().agregar(objeto);
                mensajes.add("La party encuentra " + objeto.getNombre() + ".");
            }
        }
        aliados.agregarOro(oro);
        if (oro > 0) mensajes.add("La party gana " + oro + " de oro.");
        if (!conExperiencia) {
            // un nodo ya consumido: se pelea igual, pero no da experiencia
            experiencia = 0;
            return mensajes;
        }
        // solo los que quedaron en pie
        for (Personaje p : aliados.getMiembrosJugador()) {
            if (p.estaVivo()) mensajes.addAll(p.ganarExperiencia(experiencia));
        }
        return mensajes;
    }

    public int getExperiencia() { return experiencia; }
    public int getOro() { return oro; }
    public List<Item> getObjetos() { return objetos; }
}
