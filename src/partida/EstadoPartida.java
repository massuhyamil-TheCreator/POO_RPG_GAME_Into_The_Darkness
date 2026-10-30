package partida;

public enum EstadoPartida {
    EN_MAPA,             // eligiendo a qué nodo ir
    PREPARANDO_BATALLA,  // eligiendo arma, armadura y pociones antes de pelear
    EN_BATALLA,
    EN_FOGATA,
    EN_HERRERO,
    GANADA,
    PERDIDA
}
