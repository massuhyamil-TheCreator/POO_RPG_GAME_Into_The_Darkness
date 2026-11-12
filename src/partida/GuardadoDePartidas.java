package partida;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

// Hay 3 ranuras de guardado (1, 2 y 3). Cada una es un archivo de texto (clave=valor)
// dentro de la carpeta "partidas". Esta clase se ocupa solo de los archivos y las ranuras;
// pasar la partida a texto (y volver) lo hace SerializadorDePartida.
public class GuardadoDePartidas {

    public static final int RANURAS = 3;

    private static final Path CARPETA = Paths.get("partidas");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // si se guarda en medio de una pelea, se guarda cómo estaba todo justo antes de empezarla
    public static void guardar(Partida partida, int ranura) throws IOException {
        validar(ranura);
        if (!partida.puedeGuardar()) throw new IllegalStateException("La partida ya terminó.");
        Properties datos = partida.estaEnBatalla() ? partida.getAntesDeLaBatalla() : SerializadorDePartida.capturar(partida);
        datos.setProperty("fecha", LocalDateTime.now().format(FORMATO_FECHA));
        Files.createDirectories(CARPETA);
        try (Writer archivo = Files.newBufferedWriter(archivoDe(ranura), StandardCharsets.UTF_8)) {
            datos.store(archivo, "Into The Darkness - partida guardada");
        }
    }

    public static Partida cargar(int ranura) throws IOException {
        return SerializadorDePartida.reconstruir(leer(ranura));
    }

    public static boolean hayPartida(int ranura) {
        validar(ranura);
        return Files.exists(archivoDe(ranura));
    }

    // texto para mostrar en la pantalla de ranuras, o "Vacía"
    public static String describir(int ranura) {
        if (!hayPartida(ranura)) return "Vacía";
        try {
            Properties datos = leer(ranura);
            return datos.getProperty("resumen") + " · " + datos.getProperty("fecha");
        } catch (IOException e) {
            return "Archivo dañado";
        }
    }

    public static boolean borrar(int ranura) throws IOException {
        validar(ranura);
        return Files.deleteIfExists(archivoDe(ranura));
    }

    private static void validar(int ranura) {
        if (ranura < 1 || ranura > RANURAS) throw new IllegalArgumentException("Las ranuras van de 1 a " + RANURAS);
    }

    private static Path archivoDe(int ranura) {
        return CARPETA.resolve("ranura" + ranura + ".properties");
    }

    private static Properties leer(int ranura) throws IOException {
        validar(ranura);
        Properties datos = new Properties();
        try (Reader archivo = Files.newBufferedReader(archivoDe(ranura), StandardCharsets.UTF_8)) {
            datos.load(archivo);
        }
        return datos;
    }
}
