package modelo;
//package modelo: misma carpeta de siempre.

public abstract class entidad {

    private final String nombre;
    private final int vidaMaxima;
    private int vidaActual;
    private final int ataqueFisico;
    private final int ataqueMagico;
    private final int defensaFisica;
    private final int defensaMagica;
    private final int velocidad;
//Atributos: ahora hay DOS ataques y DOS defensas en lugar de uno de cada.
//Un mago va a tener ataqueMagico alto y ataqueFisico bajo, y un guerrero al revés.

    public entidad(String nombre, int vidaMaxima, int ataqueFisico, int ataqueMagico,
                   int defensaFisica, int defensaMagica, int velocidad) {
//El constructor ahora recibe 7 datos. Java ignora los saltos de línea, así que se puede partir en dos renglones para que se lea mejor.
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.ataqueFisico = ataqueFisico;
        this.ataqueMagico = ataqueMagico;
        this.defensaFisica = defensaFisica;
        this.defensaMagica = defensaMagica;
        this.velocidad = velocidad;
    }

    public int atacarFisico(entidad objetivo) {
        int danio = Math.max(1, ataqueFisico - objetivo.getDefensaFisica());
        objetivo.recibirDanio(danio);
        return danio;
    }
//atacarFisico: el daño es mi ataque físico menos la defensa FÍSICA del objetivo (mínimo 1).

    public int atacarMagico(entidad objetivo) {
        int danio = Math.max(1, ataqueMagico - objetivo.getDefensaMagica());
        objetivo.recibirDanio(danio);
        return danio;
    }
//atacarMagico: igual, pero con mi ataque mágico y la defensa MÁGICA del objetivo.
//Los dos métodos terminan igual: le piden al objetivo que reciba el daño. Lo único que cambia es qué ataque y qué defensa se comparan.

    public void recibirDanio(int danio) {
        vidaActual = Math.max(0, vidaActual - danio);
    }
//recibirDanio: no le importa si el daño era físico o mágico, la cuenta ya viene hecha. Resta y la vida nunca queda negativa.

    public boolean estaViva() {
        return vidaActual > 0;
    }

    public String getNombre()       { return nombre; }
    public int getVidaMaxima()      { return vidaMaxima; }
    public int getVidaActual()      { return vidaActual; }
    public int getAtaqueFisico()    { return ataqueFisico; }
    public int getAtaqueMagico()    { return ataqueMagico; }
    public int getDefensaFisica()   { return defensaFisica; }
    public int getDefensaMagica()   { return defensaMagica; }
    public int getVelocidad()       { return velocidad; }
//Getters: uno por cada atributo, con los nombres nuevos.

}