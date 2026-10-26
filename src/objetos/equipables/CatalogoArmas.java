package objetos.equipables;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/** Lista de 21 armas. Los efectos son datos para el futuro motor. */
public class CatalogoArmas {
    private static List<Armas> armas = new ArrayList<>();
    static {
        agregar(101,"Hacha del Vigor","GUERRERO",2,0,6,0,0,"+6 puntos de vida máxima","Siempre que esté equipada",6,0,100,0);
        agregar(102,"Martillo del Ímpetu","GUERRERO",2,0,0,0,0,"El siguiente ataque físico recibe +1 de daño; no se acumula","Después de recibir daño",1,0,100,0);
        agregar(103,"Espadón del Verdugo","GUERRERO",2,0,0,0,0,"Robo de vida del 20% del daño físico efectivo","Al causar daño físico",20,0,100,0);
        agregar(104,"Vara de Roble Encantado","MAGO",0,2,0,0,0,"+20% de daño mágico contra objetivos con debuff","Objetivo con estado perjudicial",20,0,100,0);
        agregar(105,"Báculo de la Resonancia","MAGO",0,2,0,0,0,"Recupera 2 maná (máximo una vez por ronda)","Proyectil Etéreo acierta",2,0,100,0);
        agregar(106,"Cetro del Abismo","MAGO",0,2,0,0,0,"+10% de daño mágico total","Siempre equipado",10,0,100,0);
        agregar(107,"Vara del Aliento","CURADOR",1,1,0,0,0,"Segundo Aliento también cura 3 HP","Al usar Segundo Aliento",3,0,100,0);
        agregar(108,"Cetro de la Fe","CURADOR",1,1,0,0,0,"+20% a las curaciones realizadas","Al curar",20,0,100,0);
        agregar(109,"Báculo de la Aurora","CURADOR",1,1,0,0,0,"Cura al aliado más herido el 40% del daño efectivo (mínimo 1 HP)","Al causar daño",40,1,100,0);
        agregar(110,"Arco del Halcón","ARQUERO",2,0,0,0,0,"Flechazo básico: +1 físico contra enemigo con menor porcentaje de HP; NO afecta el automático","Flechazo contra enemigo más herido",1,0,100,0);
        agregar(111,"Arco de Escarcha","ARQUERO",2,0,0,0,0,"35% de Ralentización de -3 velocidad durante 2 rondas","Al acertar Flechazo básico",3,0,35,2);
        agregar(112,"Arco de la Tormenta","ARQUERO",2,0,0,0,0,"Lluvia de Flechas inflige +1 físico a cada objetivo","Al usar Lluvia de Flechas",1,0,100,0);
        agregar(113,"Daga del Acechador","ASESINO",2,0,0,0,0,"Primer ataque desde invisibilidad: +2 físico (una vez por activación)","Primer ataque tras Desvanecimiento",2,0,100,0);
        agregar(114,"Colmillos Sangrientos","ASESINO",2,0,0,0,0,"+2 físico contra objetivos que ya están sangrando; no aplica Sangrado","Objetivo con Sangrado",2,0,100,0);
        agregar(115,"Hoja del Eclipse","ASESINO",2,0,0,0,10,"+10 puntos porcentuales de probabilidad crítica","Siempre equipado",10,0,100,0);
        agregar(116,"Espada del Bastión","CABALLERO_SAGRADO",2,0,8,0,0,"+8 vida máxima","Siempre equipado",8,0,100,0);
        agregar(117,"Mandoble del Juramento","CABALLERO_SAGRADO",2,0,0,0,0,"15% de Aturdimiento por 1 acción","Al acertar Mandoble Divino",1,0,15,1);
        agregar(118,"Espada del Vigía","CABALLERO_SAGRADO",2,0,0,4,0,"+4 velocidad","Siempre equipado",4,0,100,0);
        agregar(119,"Escopeta de la Peste","CAZADOR",2,0,0,0,0,"50% de Veneno: 1 HP por ronda durante 2 rondas","Al acertar Escopetazo básico",1,0,50,2);
        agregar(120,"Escopeta de la Penumbra","CAZADOR",2,0,0,0,0,"Bomba de Humo: -40% precisión de comunes / -20% de jefes durante 2 rondas","Al usar Bomba de Humo",40,20,100,2);
        agregar(121,"Escopeta del Vigilante","CAZADOR",2,0,0,0,0,"+1 físico antes del x1.5 de la pasiva","Contra Criaturas Nocturnas",1,0,100,0);
    }
    private static void agregar(int id,String nombre,String clase,int fisico,int magico,
             int vida,int velocidad,int critico,String efecto,String condicion,
             int valor,int otroValor,int probabilidad,int duracion) {
        Armas arma = new Armas(id,nombre,clase,fisico,magico);
        arma.definirBonos(vida,fisico,magico,0,0,velocidad,0,critico);
        arma.definirEfecto(efecto,condicion,valor,otroValor,probabilidad,duracion);
        armas.add(arma);
    }
    public static List<Armas> todas() {return new ArrayList<>(armas);}
    public static List<Armas> paraClase(String clase) {
        List<Armas> lista = new ArrayList<>();
        for(Armas arma: armas) if(arma.permiteClase(clase)) lista.add(arma);
        return lista;
    }
    public static List<Armas> paraParty(Set<String> clases) {
        List<Armas> lista = new ArrayList<>();
        for(Armas arma: armas) if(clases.contains(arma.getClasePermitida())) lista.add(arma);
        return lista;
    }
}
