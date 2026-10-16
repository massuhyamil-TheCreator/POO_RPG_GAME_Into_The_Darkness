
void main() {
    Personaje guerrero = new Personaje("Guerrero",6,10, 0, 7,3,"Fuerza",4);
    Personaje mago = new Personaje("Mago",9,5, 0, 2,3,"Inteligencia", 7);
    Enemigo zombie = new Enemigo("Zombie", 5, 7,3);
    Enemigo loboSalvaje  = new Enemigo ("LoboSalvaje",4,7,8);
    guerrero.mostrarEstado();
    mago.mostrarEstado();
    zombie.mostrarEstado();
    combate(guerrero,zombie);
    combate(guerrero,loboSalvaje);
}
void combate(Entidad atacante1, Entidad atacante2) {
    if (atacante1.esMasVeloz(atacante2)) {
        while (atacante1.estaVivo() && atacante2.estaVivo()) {
            System.out.println("El " + atacante1 + " ataca a " + atacante2);
            int danio = atacante1.atacar(atacante2);
            System.out.println("El " + atacante2 + " recibe " + danio + " de daño.");
            atacante1.mostrarEstado();
            atacante2.mostrarEstado();
            System.out.println();
            if (atacante2.estaVivo()) {
                System.out.println("El " + atacante2 + " contraataca a " + atacante1);
                int danioContraataque = atacante2.atacar(atacante1);
                System.out.println("El " + atacante1 + " recibe " + danioContraataque + " de daño.");
                atacante1.mostrarEstado();
                atacante2.mostrarEstado();
                System.out.println();
            } else {
                System.out.println("El " + atacante2 + " fue derrotado.");
            }
        }
    } else {
        while (atacante1.estaVivo() && atacante2.estaVivo()) {
            System.out.println("El " + atacante2 + " ataca a " + atacante1);
            int danio = atacante2.atacar(atacante1);
            System.out.println("El " + atacante1 + " recibe " + danio + " de daño.");
            atacante1.mostrarEstado();
            atacante2.mostrarEstado();
            System.out.println();
            if (atacante1.estaVivo()) {
                System.out.println("El " + atacante1 + " contraataca a " + atacante2);
                int danioContraataque = atacante1.atacar(atacante2);
                System.out.println("El " + atacante2 + " recibe " + danioContraataque + " de daño.");
                atacante1.mostrarEstado();
                atacante2.mostrarEstado();
                System.out.println();
            } else {
                System.out.println("El " + atacante1 + " fue derrotado.");
            }
        }
    }
}