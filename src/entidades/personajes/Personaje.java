package entidades.personajes;
import combate.Batalla;
import entidades.Categoria;
import entidades.Entidad;
import entidades.Estadistica;
import objetos.consumibles.Consumibles;
import objetos.equipables.Armas;
import java.util.ArrayList;
import java.util.List;

public abstract class Personaje extends Entidad {

    public static final int MANA_MAXIMO = 100;
    public static final int NIVEL_MAXIMO = 10;
    public static final int EXPERIENCIA_POR_NIVEL = 30;

    private String atributoDistintivo;
    private Armas arma;
    private List<Consumibles> mochila = new ArrayList<>();

    public Personaje(String nombre, String atributoDistintivo, int vida, int velocidad, int precision, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica) {
        super(nombre, Categoria.HUMANO, vida, vida, MANA_MAXIMO, MANA_MAXIMO, velocidad, precision, 1, 0, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica);
        this.atributoDistintivo = atributoDistintivo;
    }

    public abstract int getCapacidadInventario();

    public int getEspaciosUsados() {
        int usados = 0;
        for (Consumibles consumible : mochila) {
            usados += consumible.getEspacios();
        }
        return usados;
    }

    public boolean puedeLlevar(Consumibles consumible) {
        return getEspaciosUsados() + consumible.getEspacios() <= getCapacidadInventario();
    }

    // la UI lo usa para pasar sola al siguiente personaje cuando ya no entra nada
    public boolean mochilaLlena() {
        return getEspaciosUsados() >= getCapacidadInventario();
    }

    public void guardarEnMochila(Consumibles consumible) {
        if (puedeLlevar(consumible)) mochila.add(consumible);
    }

    public void sacarDeMochila(Consumibles consumible) {
        mochila.remove(consumible);
    }

    public List<Consumibles> getMochila() { return mochila; }

    // ---- Experiencia ----

    public List<String> ganarExperiencia(int cantidad) {
        List<String> mensajes = new ArrayList<>();
        if (cantidad <= 0 || getNivel() >= NIVEL_MAXIMO) return mensajes;
        mensajes.add(getNombre() + " gana " + cantidad + " de experiencia.");
        setExperiencia(getExperiencia() + cantidad);
        while (getNivel() < NIVEL_MAXIMO && getExperiencia() >= getExperienciaParaSubir()) {
            setExperiencia(getExperiencia() - getExperienciaParaSubir());
            subirNivel();
            mensajes.add("¡" + getNombre() + " sube al nivel " + getNivel() + "!");
        }
        if (getNivel() >= NIVEL_MAXIMO) setExperiencia(0);
        return mensajes;
    }

    // cada nivel pide un poco más que el anterior: 30, 60, 90...
    public int getExperienciaParaSubir() {
        return EXPERIENCIA_POR_NIVEL * getNivel();
    }

    // cada clase lo sobrescribe con lo que mejora al subir de nivel
    protected void subirNivel() {
        subirNivel(5, 1, 1, 1, 1, 0);
    }

    // para cargar una partida guardada: sube los mismos niveles y deja la vida y el maná como estaban
    public void restaurar(int nivel, int experiencia, int vida, int mana) {
        while (getNivel() < nivel) subirNivel();
        setExperiencia(experiencia);
        setPuntosDeSalud(vida);
        setRecurso(mana);
    }

    public Armas equiparArma(Armas nueva) {
        Armas anterior = this.arma;
        this.arma = nueva;
        return anterior;
    }

    @Override
    protected int getEstadisticaBase(Estadistica estadistica) {
        int valor = super.getEstadisticaBase(estadistica);
        switch (estadistica) {
            case ATAQUE_FISICO: return valor + (arma == null ? 0 : arma.getDanioFisico());
            case ATAQUE_MAGICO: return valor + (arma == null ? 0 : arma.getDanioMagico());
            default: return valor;
        }
    }

    public Armas getArma() { return arma; }
    public String getAtributoDistintivo() { return atributoDistintivo; }


    // ---- Lo propio de cada clase de personaje (las subclases lo completan) ----

    // nombre estable de la clase: sirve para crear el personaje y para guardarlo
    public abstract String getClase();

    @Override
    public String getNombreDelTipo() {
        return getClase();
    }

    @Override
    public boolean esAliado() {
        return true;
    }

    @Override
    public void unirseA(Batalla batalla) {
        batalla.getPartyAliada().getMiembrosJugador().add(this);
    }

    // lo que pasa con este personaje cuando la party avanza a otro nodo o descansa;
    // por defecto nada, y el que tenga algo para hacer lo sobrescribe
    public void alAvanzarNodo() {
    }

    public void alDescansarEnFogata() {
    }

    // estado propio de la clase que hay que guardar (null = no tiene)
    public String guardarEstadoPropio() {
        return null;
    }

    public void restaurarEstadoPropio(String valor) {
    }
}
