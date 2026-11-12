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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

// Arma la party a partir de las clases que elige el jugador.
// Cada personaje se llama como su clase; si se repite una, el segundo es "Mago 2".
public class CreadorDePersonajes {

    public static final List<String> CLASES = List.of(
            "Mago", "Guerrero", "Curador", "Arquero", "Asesino", "Caballero Sagrado", "Cazador");

    // clase -> cómo crearla. Agregar una clase nueva es agregar una línea acá (sin switch).
    private static final Map<String, Function<String, Personaje>> FABRICAS = new LinkedHashMap<>();
    static {
        FABRICAS.put("Mago", Mago::new);
        FABRICAS.put("Guerrero", Guerrero::new);
        FABRICAS.put("Curador", Curador::new);
        FABRICAS.put("Arquero", Arquero::new);
        FABRICAS.put("Asesino", Asesino::new);
        FABRICAS.put("Caballero Sagrado", Caballero::new);
        FABRICAS.put("Cazador", Cazador::new);
    }

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

    public static Personaje crear(String clase, String nombre) {
        Function<String, Personaje> fabrica = FABRICAS.get(clase);
        if (fabrica == null) throw new IllegalArgumentException("Clase desconocida: " + clase);
        return fabrica.apply(nombre);
    }
}
