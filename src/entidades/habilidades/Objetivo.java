package entidades.habilidades;
import entidades.Entidad;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// A quién le pega una habilidad. Cada tipo de objetivo sabe elegir a sus objetivos (polimorfismo):
// agregar un tipo nuevo es agregar una constante acá, sin tocar Batalla.
public enum Objetivo {

    UN_ENEMIGO {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            List<Entidad> objetivos = new ArrayList<>();
            if (enemigos.contains(elegido)) objetivos.add(elegido);
            return objetivos;
        }
    },
    VARIOS_ENEMIGOS {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            Collections.shuffle(enemigos, azar);
            int rango = habilidad.getMaximoObjetivos() - habilidad.getMinimoObjetivos() + 1;
            int cantidad = Math.min(enemigos.size(), habilidad.getMinimoObjetivos() + azar.nextInt(rango));
            return new ArrayList<>(enemigos.subList(0, cantidad));
        }
    },
    TODOS_LOS_ENEMIGOS {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            return new ArrayList<>(enemigos);
        }
    },
    UN_ALIADO {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            List<Entidad> objetivos = new ArrayList<>();
            if (aliados.contains(elegido)) objetivos.add(elegido);
            return objetivos;
        }
    },
    OTRO_ALIADO {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            List<Entidad> objetivos = new ArrayList<>();
            if (aliados.contains(elegido) && elegido != usuario) objetivos.add(elegido);
            return objetivos;
        }
    },
    SI_MISMO {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            List<Entidad> objetivos = new ArrayList<>();
            objetivos.add(usuario);
            return objetivos;
        }
    },
    TODOS_LOS_ALIADOS {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            return new ArrayList<>(aliados);
        }
    },
    ALIADOS_CON_MENOS_VIDA {
        @Override
        public List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar) {
            aliados.sort((a, b) -> a.getPuntosDeSalud() - b.getPuntosDeSalud());
            return new ArrayList<>(aliados.subList(0, Math.min(aliados.size(), habilidad.getMaximoObjetivos())));
        }
    };

    // Devuelve los objetivos de la habilidad. "aliados" y "enemigos" ya vienen desde el punto de vista
    // del usuario, vivos y (en el caso de los enemigos) atacables.
    public abstract List<Entidad> resolver(Entidad usuario, Entidad elegido, List<Entidad> aliados, List<Entidad> enemigos, Habilidad habilidad, Random azar);

    public boolean hayQueElegirEnemigo() {
        return this == UN_ENEMIGO;
    }

    public boolean esSobreAliados() {
        return this == UN_ALIADO || this == OTRO_ALIADO || this == SI_MISMO || this == TODOS_LOS_ALIADOS || this == ALIADOS_CON_MENOS_VIDA;
    }

    public boolean hayQueElegirAliado() {
        return this == UN_ALIADO || this == OTRO_ALIADO;
    }
}
