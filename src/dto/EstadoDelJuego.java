package dto;

// En qué punto está la partida (copia, para la vista, de los estados del modelo).
public enum EstadoDelJuego {
    EN_MAPA,
    PREPARANDO_BATALLA,
    EN_BATALLA,
    EN_FOGATA,
    GANADA,
    PERDIDA
}
