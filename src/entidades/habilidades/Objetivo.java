package entidades.habilidades;

public enum Objetivo {
    UN_ENEMIGO,
    VARIOS_ENEMIGOS,
    TODOS_LOS_ENEMIGOS,
    UN_ALIADO,
    OTRO_ALIADO,
    SI_MISMO,
    TODOS_LOS_ALIADOS,
    ALIADOS_CON_MENOS_VIDA;

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
