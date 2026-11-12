package controlador;
import dto.EntidadDTO;
import dto.HabilidadDTO;
import dto.ItemDTO;
import entidades.Entidad;
import entidades.efectos.Efecto;
import entidades.habilidades.Habilidad;
import entidades.personajes.Personaje;
import objetos.Item;
import objetos.consumibles.Consumibles;
import java.util.ArrayList;
import java.util.List;

// Pasa objetos del modelo a DTOs. Es lo único que mira "hacia adentro" para armar lo que ve la pantalla.
final class Conversor {

    private Conversor() {
    }

    static EntidadDTO entidad(Entidad e, int id) {
        return basico(e, id).build();
    }

    static EntidadDTO personaje(Personaje p, int id) {
        EntidadDTO.Builder b = basico(p, id);
        b.experienciaParaSubir = p.getExperienciaParaSubir();
        b.nivelMaximo = p.getNivel() >= Personaje.NIVEL_MAXIMO;
        b.espaciosUsados = p.getEspaciosUsados();
        b.capacidadInventario = p.getCapacidadInventario();
        b.mochilaLlena = p.mochilaLlena();
        b.arma = p.getArma() == null ? "ninguna" : p.getArma().resumen();
        for (Consumibles c : p.getMochila()) b.mochila.add(c.getNombre());
        return b.build();
    }

    private static EntidadDTO.Builder basico(Entidad e, int id) {
        EntidadDTO.Builder b = new EntidadDTO.Builder();
        b.id = id;
        b.nombre = e.getNombre();
        b.tipo = e.getNombreDelTipo();
        b.nivel = e.getNivel();
        b.puntosDeSalud = e.getPuntosDeSalud();
        b.puntosDeSaludMaximos = e.getPuntosDeSaludMaximos();
        b.recurso = e.getRecurso();
        b.recursoMaximo = e.getRecursoMaximo();
        b.ataqueFisico = e.getAtaqueFisico();
        b.ataqueMagico = e.getAtaqueMagico();
        b.defensaFisica = e.getDefensaFisica();
        b.defensaMagica = e.getDefensaMagica();
        b.velocidad = e.getVelocidad();
        b.precision = e.getPrecision();
        b.vivo = e.estaVivo();
        b.jefe = e.esJefe();
        b.experiencia = e.getExperiencia();
        List<String> nombres = new ArrayList<>();
        for (Efecto efecto : e.getEfectos()) {
            if (!efecto.esPasiva()) nombres.add(efecto.toString());
        }
        b.efectos = String.join(", ", nombres);
        return b;
    }

    static HabilidadDTO habilidad(Habilidad h, Entidad usuario) {
        return new HabilidadDTO(h.getNombre(), h.getCosto(), h.toString(), h.estaDisponible(),
                h.getObjetivo().hayQueElegirEnemigo(), h.getObjetivo().hayQueElegirAliado());
    }

    static ItemDTO item(Item i, int indice) {
        return new ItemDTO(indice, i.getNombre(), i.getDescripcion(), i.resumen(), i.getEspacios());
    }
}
