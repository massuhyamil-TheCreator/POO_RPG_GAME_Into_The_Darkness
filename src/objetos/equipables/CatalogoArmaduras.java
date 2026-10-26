package objetos.equipables;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/** Lista de las 7 armaduras, sin gestionar su aparición ni equipamiento. */
public class CatalogoArmaduras {
    private static List<Armaduras> armaduras = new ArrayList<>();
    static {
        armaduras.add(new Armaduras(301,"Armadura del Coloso","GUERRERO",6,0,0,1,1,0));
        armaduras.add(new Armaduras(302,"Túnica Arcana","MAGO",5,0,1,0,0,0));
        armaduras.add(new Armaduras(303,"Vestiduras de la Luz","CURADOR",6,0,0,0,0,10));
        armaduras.add(new Armaduras(304,"Atuendo del Explorador","ARQUERO",4,1,0,0,0,0));
        armaduras.add(new Armaduras(305,"Manto de las Sombras","ASESINO",0,2,0,0,0,0));
        armaduras.add(new Armaduras(306,"Armadura del Guardián","CABALLERO_SAGRADO",0,0,0,2,2,0));
        armaduras.add(new Armaduras(307,"Abrigo del Acechador","CAZADOR",3,1,0,1,1,0));
    }
    public static List<Armaduras> todas() {return new ArrayList<>(armaduras);}
    public static Armaduras paraClase(String clase) {
        for (Armaduras a: armaduras) if (a.permiteClase(clase)) return a;
        return null;
    }
    public static List<Armaduras> paraParty(Set<String> clases) {
        List<Armaduras> lista = new ArrayList<>();
        for (Armaduras a: armaduras) if (clases.contains(a.getClasePermitida())) lista.add(a);
        return lista;
    }
}
