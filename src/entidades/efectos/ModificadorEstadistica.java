package entidades.efectos;
import entidades.Estadistica;
import java.util.HashMap;
import java.util.Map;

public class ModificadorEstadistica extends Efecto {

    private Map<Estadistica, Integer> sumas = new HashMap<>();
    private Map<Estadistica, Integer> porcentajes = new HashMap<>();

    public ModificadorEstadistica(String nombre, int duracion) {
        super(nombre, duracion);
    }

    public ModificadorEstadistica sumar(Estadistica estadistica, int cantidad) {
        sumas.put(estadistica, cantidad);
        return this;
    }

    public ModificadorEstadistica porcentaje(Estadistica estadistica, int porcentaje) {
        porcentajes.put(estadistica, porcentaje);
        return this;
    }

    @Override
    public int modificar(Estadistica estadistica, int valor) {
        valor += sumas.getOrDefault(estadistica, 0);
        valor = valor * (100 + porcentajes.getOrDefault(estadistica, 0)) / 100;
        return valor;
    }

    @Override
    public boolean esNegativo() {
        for (int cantidad : sumas.values()) {
            if (cantidad < 0) return true;
        }
        for (int porcentaje : porcentajes.values()) {
            if (porcentaje < 0) return true;
        }
        return false;
    }
}
