package entidades.enemigos;
import combate.Batalla;
import entidades.Categoria;
import entidades.Entidad;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import objetos.Catalogo;
import objetos.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Enemigo extends Entidad {

    private static final Random AZAR = new Random();
    public static final int PROBABILIDAD_BOTIN = 30;

    private int experienciaOtorgada;
    private int oroOtorgado;
    private Item objetoOtorgado;

    public Enemigo(String nombre, Categoria categoria, int vida, int velocidad, int precision, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int experienciaOtorgada, int oroOtorgado) {
        super(nombre, categoria, vida, vida, 0, 0, velocidad, precision, 1, 0, ataqueFisico, ataqueMagico, defensaFisica, defensaMagica);
        this.experienciaOtorgada = experienciaOtorgada;
        this.oroOtorgado = oroOtorgado;
        if (AZAR.nextInt(100) < PROBABILIDAD_BOTIN) this.objetoOtorgado = Catalogo.botinAlAzar();
    }

    public Habilidad elegirHabilidad(Batalla batalla) {
        List<Habilidad> opciones = new ArrayList<>();
        opciones.add(getBasico());
        for (Habilidad habilidad : getHabilidades()) {
            if (habilidad.estaDisponible() && habilidad.convieneUsar(this, batalla)) {
                opciones.add(habilidad);
            }
        }
        return opciones.get(AZAR.nextInt(opciones.size()));
    }

    public Entidad elegirObjetivo(Habilidad habilidad, Batalla batalla) {
        if (habilidad.getObjetivo().hayQueElegirEnemigo()) {
            Entidad forzado = getObjetivoForzado();
            if (forzado != null) return forzado;
            List<Entidad> enemigos = batalla.enemigosDe(this);
            return enemigos.get(AZAR.nextInt(enemigos.size()));
        }
        if (habilidad.getObjetivo().hayQueElegirAliado()) {
            Entidad masHerido = null;
            for (Entidad aliado : batalla.aliadosDe(this)) {
                if (aliado == this && habilidad.getObjetivo() == Objetivo.OTRO_ALIADO) continue;
                if (masHerido == null || aliado.getPuntosDeSalud() < masHerido.getPuntosDeSalud()) masHerido = aliado;
            }
            return masHerido;
        }
        return null;
    }

    public int robarOro(int maximo) {
        int robado = Math.min(maximo, oroOtorgado);
        oroOtorgado -= robado;
        return robado;
    }

    public Item robarObjeto() {
        Item robado = objetoOtorgado;
        objetoOtorgado = null;
        return robado;
    }

    public void setObjetoOtorgado(Item objetoOtorgado) { this.objetoOtorgado = objetoOtorgado; }
    public int getExperienciaOtorgada() { return experienciaOtorgada; }
    public int getOroOtorgado() { return oroOtorgado; }
    public Item getObjetoOtorgado() { return objetoOtorgado; }
}
