package partida;
import excepciones.PartidaDanadaException;
import excepciones.PartidaException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public static void guardar(Partida partida, int ranura) throws PartidaException {
        validar(ranura);
        if (!partida.puedeGuardar()) throw new PartidaException("La partida ya terminó.");
        Properties datos = partida.estaEnBatalla() ? partida.getAntesDeLaBatalla() : SerializadorDePartida.capturar(partida);
        datos.setProperty("fecha", LocalDateTime.now().format(FORMATO_FECHA));
        Path temporal = CARPETA.resolve("ranura" + ranura + ".tmp");
        try {
            Files.createDirectories(CARPETA);
            try (Writer archivo = Files.newBufferedWriter(temporal, StandardCharsets.UTF_8)) {
                datos.store(archivo, "Into The Darkness - partida guardada");
            }
            Files.move(temporal, archivoDe(ranura), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new PartidaException("No se pudo guardar en la ranura " + ranura + ".", e);
        } finally {
            try {
                Files.deleteIfExists(temporal);
            } catch (IOException e) {
                System.out.println("No se pudo borrar el archivo temporal: " + e.getMessage());
            }
        }
    }

    public static Partida cargar(int ranura) throws PartidaException {
        Properties datos = leer(ranura);
        try {
            return SerializadorDePartida.reconstruir(datos);
        } catch (RuntimeException e) {
            throw new PartidaDanadaException(ranura, e);
        }
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
        } catch (PartidaException e) {
            return "Archivo dañado";
        }
    }

    public static boolean borrar(int ranura) throws PartidaException {
        validar(ranura);
        try {
            return Files.deleteIfExists(archivoDe(ranura));
        } catch (IOException e) {
            throw new PartidaException("No se pudo borrar la ranura " + ranura + ".", e);
        }
    }

    private static void validar(int ranura) {
        if (ranura < 1 || ranura > RANURAS) throw new IllegalArgumentException("Las ranuras van de 1 a " + RANURAS);
    }

    private static Path archivoDe(int ranura) {
        return CARPETA.resolve("ranura" + ranura + ".properties");
    }

    private static Properties leer(int ranura) throws PartidaException {
        validar(ranura);
        Properties datos = new Properties();
        try (Reader archivo = Files.newBufferedReader(archivoDe(ranura), StandardCharsets.UTF_8)) {
            datos.load(archivo);
        } catch (IOException e) {
            throw new PartidaException("No se pudo leer la ranura " + ranura + ".", e);
        }
        return datos;
    }
}
