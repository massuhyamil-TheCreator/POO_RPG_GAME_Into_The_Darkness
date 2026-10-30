package partida;
import entidades.personajes.Arquero;
import entidades.personajes.Asesino;
import entidades.personajes.Caballero;
import entidades.personajes.Cazador;
import entidades.personajes.Curador;
import entidades.personajes.Guerrero;
import entidades.personajes.Mago;
import entidades.personajes.Personaje;
import java.util.ArrayList;
import java.util.List;

// Arma la party a partir de las clases que elige el jugador.
// Cada personaje se llama como su clase; si se repite una, el segundo es "Mago 2".
public class CreadorDePersonajes {

    public static final List<String> CLASES = List.of(
            "Mago", "Guerrero", "Curador", "Arquero", "Asesino", "Caballero Sagrado", "Cazador");

    public static List<Personaje> crearParty(List<String> clases) {
        List<Personaje> party = new ArrayList<>();
        for (int i = 0; i < clases.size(); i++) {
            String clase = clases.get(i);
            int repetidos = 1;
            for (int j = 0; j < i; j++) {
                if (clases.get(j).equals(clase)) repetidos++;
            }
            party.add(crear(clase, repetidos == 1 ? clase : clase + " " + repetidos));
        }
        return party;
    }

    // acepta el nombre que se muestra ("Caballero Sagrado") o el de la clase Java ("Caballero")
    public static Personaje crear(String clase, String nombre) {
        switch (clase) {
            case "Mago": return new Mago(nombre);
            case "Guerrero": return new Guerrero(nombre);
            case "Curador": return new Curador(nombre);
            case "Arquero": return new Arquero(nombre);
            case "Asesino": return new Asesino(nombre);
            case "Caballero Sagrado":
            case "Caballero": return new Caballero(nombre);
            case "Cazador": return new Cazador(nombre);
            default: throw new IllegalArgumentException("Clase desconocida: " + clase);
        }
    }
}
