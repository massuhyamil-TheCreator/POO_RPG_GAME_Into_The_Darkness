package entidades;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
/** Fuente única de pasivas, ataques básicos, automáticos y habilidades.
 * SIN ejecución: no descuenta recursos, no calcula daño, no crea batalla. */
public class CatalogoHabilidades {
    private static final Map<String,List<Habilidad>> PERSONAJES=new HashMap<>();
    private static final Map<String,List<Habilidad>> ENEMIGOS=new HashMap<>();
    private static final Map<String,List<Habilidad>> INVOCACIONES=new HashMap<>();
    static {
       PERSONAJES.put("GUERRERO",List.of(
          new Habilidad("valiente","Valiente","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("INMUNIDAD_MIEDO",100,0,100,"")),"Inmune a Miedo."),
          new Habilidad("espadazo","Espadazo","BASICA","UNO_ENEMIGO",0,0,6,6,0,0,1,1,List.of(),""),
          new Habilidad("grito_de_guerra","Grito de Guerra","HABILIDAD","TODOS_ALIADOS",7,4,0,0,0,0,0,0,List.of(new Efecto("BONO_ATAQUE",3,2,100,"")),"+3 ataque físico y mágico al grupo, sin acumulación consigo mismo."),
          new Habilidad("escudazo","Escudazo","HABILIDAD","UNO_ENEMIGO",5,3,4,4,0,0,1,1,List.of(new Efecto("ATURDIMIENTO",1,1,100,"")),"Aturde por 1 acción."),
          new Habilidad("protector","Protector","HABILIDAD","UNO_ALIADO",4,3,0,0,0,0,1,1,List.of(new Efecto("REDUCIR_DANIO_POR_GOLPE",80,1,100,"")),"Protege de un ataque a sí mismo o a un aliado; expira al próximo turno del Guerrero.")
       ));
       PERSONAJES.put("MAGO",List.of(
          new Habilidad("conocimientos_arcanos","Conocimientos Arcanos","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("AHORRAR_MANA",100,0,20,"")),"20% de evitar costo de maná. El TP permite probar la pasiva aun si no alcanza el maná."),
          new Habilidad("proyectil_etereo","Proyectil Etéreo","BASICA","UNO_ENEMIGO",0,0,0,0,7,7,1,1,List.of(),""),
          new Habilidad("bola_de_fuego","Bola de Fuego","HABILIDAD","VARIOS_ENEMIGOS",9,3,0,0,9,9,2,3,List.of(new Efecto("QUEMADURA",2,2,100,"")),"Cada impacto aplica Quemadura, 2 HP efectivos por turno durante 2 turnos."),
          new Habilidad("lluvia_de_escarcha","Lluvia de Escarcha","HABILIDAD","TODOS_ENEMIGOS",14,4,0,0,10,10,0,0,List.of(new Efecto("RALENTIZACION",3,2,100,""),new Efecto("CONGELACION",1,1,20,"")),"Ralentiza a todos los alcanzados y congela con 20% independiente por objetivo."),
          new Habilidad("escudo_magico","Escudo Mágico","HABILIDAD","USUARIO",7,4,0,0,0,0,0,0,List.of(new Efecto("INVULNERABILIDAD",100,1,100,"")),"Invulnerabilidad temporal del usuario.")
       ));
       PERSONAJES.put("CURADOR",List.of(
          new Habilidad("el_salvador","El Salvador","PASIVA","TODOS_ALIADOS",0,0,0,0,0,0,0,0,List.of(new Efecto("EVITAR_MUERTE",25,0,100,"Probabilidad inicial 25%; pierde 5 puntos por nodo, mínimo 5%; 1 salvación por combate; deja 1 HP. Fogata reinicia.")),"Pasiva que puede evitar daño letal a un aliado."),
          new Habilidad("bastonazo","Bastonazo","BASICA","UNO_ENEMIGO",0,0,3,3,0,0,1,1,List.of(new Efecto("CURACION_AL_GOLPE",3,0,20,"Cura 3 HP a un aliado aleatorio.")),""),
          new Habilidad("curacion_divina","Curación Divina","HABILIDAD","UNO_ALIADO",9,4,0,0,0,0,1,1,List.of(new Efecto("CURACION",10,0,100,""),new Efecto("INVULNERABILIDAD",100,1,100,"")),"Cura 10 HP al objetivo y concede una ronda de invulnerabilidad."),
          new Habilidad("azote_del_curandero","Azote del Curandero","HABILIDAD","UNO_ENEMIGO",6,3,0,0,4,4,1,1,List.of(new Efecto("ANTICURACION",40,2,100,"")),""),
          new Habilidad("segundo_aliento","Segundo Aliento","HABILIDAD","UNO_ALIADO_DISTINTO_DEL_USUARIO",5,4,0,0,0,0,1,1,List.of(new Efecto("RECUPERAR_RECURSO",10,0,100,"")),"Restaura 10 recurso al aliado; el Curador no puede elegirse.")
       ));
       PERSONAJES.put("ARQUERO",List.of(
          new Habilidad("ligero_como_pluma","Ligero como Pluma","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("EVASION",20,0,100,"")),"Evasión base 20%."),
          new Habilidad("flechazo","Flechazo","BASICA","UNO_ENEMIGO",0,0,5,5,0,0,1,1,List.of(),"Reemplaza el básico original Flechazo Veloz de 6 físico."),
          new Habilidad("flechazo_veloz","Flechazo Veloz","AUTOMATICA","UNO_ENEMIGO",0,0,1,1,0,0,1,1,List.of(),"Disparo automático de 1 daño efectivo al inicio de la ronda contra el enemigo vivo con menor porcentaje de HP. No consume acción, sin críticos ni bonos de equipamiento. Puede fallar o esquivarse. No se ejecuta si está incapacitado para atacar."),
          new Habilidad("lluvia_de_flechas","Lluvia de Flechas","HABILIDAD","VARIOS_ENEMIGOS",7,3,7,10,0,0,2,3,List.of(new Efecto("SANGRADO",2,2,20,"")),"Daño variable y tirada de Sangrado independiente por enemigo alcanzado."),
          new Habilidad("flechazo_divino","Flechazo Divino","HABILIDAD","UNO_ENEMIGO",5,2,5,5,2,2,1,1,List.of(new Efecto("BONO_DANIO_CONTRA_NO_MUERTOS",150,0,100,"")),"Daño físico y mágico; daño total x1,5 contra No Muertos."),
          new Habilidad("capa_del_bosque","Capa del Bosque","HABILIDAD","USUARIO",6,4,0,0,0,0,0,0,List.of(new Efecto("EVASION",75,2,100,"")),"Establece evasión TOTAL en 75%, no 20%+75%.")
       ));
       PERSONAJES.put("ASESINO",List.of(
          new Habilidad("cazador_sigiloso","Cazador Sigiloso","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("CRITICO_DUPLICADO",200,0,100,"")),"Duplica probabilidad crítica mientras está invisible."),
          new Habilidad("apunalar","Apuñalar","BASICA","UNO_ENEMIGO",0,0,7,7,0,0,1,1,List.of(),""),
          new Habilidad("desvanecimiento","Desvanecimiento","HABILIDAD","USUARIO",6,4,0,0,0,0,0,0,List.of(new Efecto("INVISIBILIDAD",80,2,100,"")),"Reduce 80% relativo probabilidad de impacto durante 2 turnos; atacar no cancela."),
          new Habilidad("golpe_del_ladron","Golpe del Ladrón","HABILIDAD","UNO_ENEMIGO",4,3,6,6,0,0,1,1,List.of(new Efecto("ROBO_ORO",6,0,95,""),new Efecto("ROBO_ITEM",1,0,5,"")),"Robo de 6 unidades de oro propuesto; el 5% de robo de objeto depende del botín futuro."),
          new Habilidad("daga_serruchada","Daga Serruchada","HABILIDAD","UNO_ENEMIGO",6,3,9,9,0,0,1,1,List.of(new Efecto("SANGRADO",2,2,100,"")),"")
       ));
       PERSONAJES.put("CABALLERO_SAGRADO",List.of(
          new Habilidad("pacto_del_caballero","Pacto del Caballero","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("BONO_AMBAS_DEFENSAS_PORCENTUAL",40,0,100,"Activo solo con menos de 50% HP.")),""),
          new Habilidad("mandoble_divino","Mandoble Divino","BASICA","UNO_ENEMIGO",0,0,4,4,0,0,1,1,List.of(new Efecto("BONO_DANIO_CONTRA_NO_MUERTOS",1,0,100,"")),"+1 daño fijo contra No Muertos."),
          new Habilidad("provocacion","Provocación","HABILIDAD","UNO_ENEMIGO",4,3,0,0,0,0,1,1,List.of(new Efecto("PROVOCACION",1,2,100,"")),"Obliga a atacar al Caballero cuando el objetivo use ataque ofensivo individual."),
          new Habilidad("discurso_heroico","Discurso Heroico","HABILIDAD","TODOS_ALIADOS",9,5,0,0,0,0,0,0,List.of(new Efecto("BONO_DANIO_PORCENTUAL",20,2,100,""),new Efecto("DISIPAR_NEGATIVOS",100,0,100,"")),"Potencia al grupo durante 2 rondas y disipa estados negativos."),
          new Habilidad("guardia_real","Guardia Real","HABILIDAD","NINGUNO",10,5,0,0,0,0,0,0,List.of(new Efecto("INVOCAR_GUARDIA",1,3,100,"")),"Invoca 1 Guardia Real durante 3 rondas; máximo 1 guardia activo.")
       ));
       PERSONAJES.put("CAZADOR",List.of(
          new Habilidad("el_vigilante_de_la_noche","El Vigilante de la Noche","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("BONO_DANIO_CONTRA_NOCTURNOS",150,0,100,"")),"Multiplica x1,5 daño a Criaturas Nocturnas."),
          new Habilidad("escopetazo","Escopetazo","BASICA","UNO_ENEMIGO",0,0,7,7,0,0,1,1,List.of(),""),
          new Habilidad("bomba_de_humo","Bomba de Humo","HABILIDAD","TODOS_ENEMIGOS",7,4,0,0,0,0,0,0,List.of(new Efecto("PRECISION",-30,2,100,"-30% comunes; -15% jefes; con Penumbra -40%/-20%.")),"Reduce la precisión de los enemigos en porcentaje relativo, menos eficaz contra jefes."),
          new Habilidad("trampa_para_bobos","Trampa para Bobos","HABILIDAD","UNO_ENEMIGO",5,3,4,4,0,0,1,1,List.of(new Efecto("RALENTIZACION",30,2,100,"Reducción porcentual del 30% velocidad."),new Efecto("SANGRADO",2,1,100,"")),""),
          new Habilidad("crucifijo_con_ajo","Crucifijo con Ajo","HABILIDAD","TODOS_ENEMIGOS",9,5,0,0,0,0,0,0,List.of(new Efecto("RETROCESO",1,1,100,""),new Efecto("DEBILITADO",15,2,100,"-15% daño físico/mágico y ambas defensas.")),"Solo afecta a Criaturas Nocturnas; retrasa 1 acción y debilita durante 2 rondas.")
       ));
       ENEMIGOS.put("ZOMBIE",List.of(
          new Habilidad("mordida","Mordida","BASICA","UNO_ENEMIGO",0,0,5,5,0,0,1,1,List.of(),""),
          new Habilidad("rasguno_putrido","Rasguño Pútrido","HABILIDAD","UNO_ENEMIGO",3,2,3,3,1,1,1,1,List.of(new Efecto("PUTREFACCION",1,2,100,"")),""),
          new Habilidad("vomito","Vómito","HABILIDAD","VARIOS_ENEMIGOS",4,4,0,0,0,0,1,2,List.of(new Efecto("RALENTIZACION",3,2,100,"")),"")
       ));
       ENEMIGOS.put("ESQUELETO",List.of(
          new Habilidad("flechazo_esqueleto","Flechazo","BASICA","UNO_ENEMIGO",0,0,6,6,0,0,1,1,List.of(),""),
          new Habilidad("flechazo_espectral","Flechazo Espectral","HABILIDAD","UNO_ENEMIGO",4,3,3,3,2,2,1,1,List.of(new Efecto("ATADURA_ESPECTRAL",1,2,100,"")),""),
          new Habilidad("maldicion_de_la_cripta","Maldición de la Cripta","HABILIDAD","UNO_ENEMIGO",5,4,0,0,0,0,1,1,List.of(new Efecto("MALDITO",25,2,100,"")),"")
       ));
       ENEMIGOS.put("LOBO_SALVAJE",List.of(
          new Habilidad("mordisco","Mordisco","BASICA","UNO_ENEMIGO",0,0,4,4,0,0,1,1,List.of(),""),
          new Habilidad("garra_rabiosa","Garra Rabiosa","HABILIDAD","UNO_ENEMIGO",3,3,5,5,0,0,1,1,List.of(new Efecto("RABIA",25,2,100,"")),""),
          new Habilidad("aullido_de_la_manada","Aullido de la Manada","HABILIDAD","TODOS_ALIADOS",5,4,0,0,0,0,0,0,List.of(new Efecto("BONO_ESTADISTICAS",1,2,100,"+1 físico/mágico, defensas, velocidad y precisión; sin HP, recurso ni crítico.")),"")
       ));
       ENEMIGOS.put("HOMBRE_LOBO",List.of(
          new Habilidad("rasguno_licantropo","Rasguño Licántropo","BASICA","UNO_ENEMIGO",0,0,7,7,0,0,1,1,List.of(),""),
          new Habilidad("zarpazo_del_licantropo","Zarpazo del Licántropo","HABILIDAD","UNO_ENEMIGO",5,3,10,10,0,0,1,1,List.of(new Efecto("RETROCESO",1,1,100,"")),""),
          new Habilidad("aullido_del_alfa","Aullido del Alfa","HABILIDAD","TODOS_ALIADOS",7,5,0,0,0,0,0,0,List.of(new Efecto("BONO_ESTADISTICAS",3,2,100,"+3 físico/mágico, defensas, velocidad y precisión.")),"")
       ));
       ENEMIGOS.put("VAMPIRO",List.of(
          new Habilidad("sed_de_sangre","Sed de Sangre","BASICA","UNO_ENEMIGO",0,0,3,3,2,2,1,1,List.of(new Efecto("CURACION_AL_GOLPE",1,0,100,"")),""),
          new Habilidad("curacion_vampirica","Curación Vampírica","HABILIDAD","USUARIO",5,3,0,0,0,0,0,0,List.of(new Efecto("CURACION",5,0,100,"")),""),
          new Habilidad("encanto_vampirico","Encanto Vampírico","HABILIDAD","UNO_ENEMIGO",7,4,0,0,8,8,1,1,List.of(new Efecto("ENCANTO",1,2,100,"")),"La víctima no puede atacar a ese Vampiro durante 2 rondas.")
       ));
       ENEMIGOS.put("BRUJA",List.of(
          new Habilidad("pocion_envenenamiento","Poción de Envenenamiento","BASICA","VARIOS_ENEMIGOS",0,0,0,0,0,0,2,2,List.of(new Efecto("VENENO",1,2,100,"")),"Básico original sin daño instantáneo: Veneno 1 HP por ronda a dos objetivos."),
          new Habilidad("curacion_siniestra","Curación Siniestra","HABILIDAD","VARIOS_ALIADOS",7,4,0,0,0,0,2,2,List.of(new Efecto("CURACION",6,0,100,"")),"Cura 6 HP a los dos aliados con menor HP."),
          new Habilidad("proteccion_nocturna","Protección Nocturna","HABILIDAD","VARIOS_ALIADOS",8,4,0,0,0,0,2,2,List.of(new Efecto("BONO_AMBAS_DEFENSAS_PORCENTUAL",30,2,100,"")),"+30% defensas a dos aliados.")
       ));
       ENEMIGOS.put("DRAGON",List.of(
          new Habilidad("intimidante","Intimidante","PASIVA","TODOS_ENEMIGOS",0,0,0,0,0,0,0,0,List.of(new Efecto("BONO_ATAQUE",-2,0,100,"")),"-2 ataque del grupo rival durante la presencia del Dragón."),
          new Habilidad("garras_draconianas","Garras Draconianas","BASICA","UNO_ENEMIGO",0,0,8,8,0,0,1,1,List.of(),""),
          new Habilidad("aliento_de_fuego","Aliento de Fuego","HABILIDAD","VARIOS_ENEMIGOS",7,3,0,0,5,5,2,2,List.of(new Efecto("QUEMADURA",2,2,100,"")),""),
          new Habilidad("rugido_escamado","Rugido Escamado","HABILIDAD","VARIOS_ENEMIGOS",8,4,0,0,0,0,2,2,List.of(new Efecto("MIEDO",1,1,100,"")),""),
          new Habilidad("piel_draconiana","Piel Draconiana","HABILIDAD","USUARIO",9,5,0,0,0,0,0,0,List.of(new Efecto("BONO_DEFENSA_FISICA",4,2,100,""),new Efecto("BONO_DEFENSA_MAGICA",3,2,100,"")),""),
          new Habilidad("embestida_alada","Embestida Alada","HABILIDAD","TODOS_ENEMIGOS",5,2,3,3,0,0,0,0,List.of(),"")
       ));
       ENEMIGOS.put("GOLEM",List.of(
          new Habilidad("piel_de_piedra","Piel de Piedra","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("REDUCIR_DANIO_FISICO",20,0,100,""),new Efecto("ESQUIVAR_CRITICOS",10,0,100,"")),"-20% daño físico adicional; -10 puntos probabilidad crítica atacante."),
          new Habilidad("punetazo_de_piedra","Puñetazo de Piedra","BASICA","UNO_ENEMIGO",0,0,8,8,0,0,1,1,List.of(),""),
          new Habilidad("piedra_viviente","Piedra Viviente","HABILIDAD","USUARIO",7,5,0,0,0,0,0,0,List.of(new Efecto("CREAR_COPIA_AL_RECIBIR_DANIO",1,2,100,"Máximo 2 copias vivas; crea al recibir daño durante 2 rondas.")),""),
          new Habilidad("lanzamiento_de_piedra","Lanzamiento de Piedra","HABILIDAD","UNO_ENEMIGO",5,3,5,5,2,2,1,1,List.of(new Efecto("ATURDIMIENTO",1,1,100,"")),""),
          new Habilidad("endurecimiento","Endurecimiento","HABILIDAD","USUARIO",7,4,0,0,0,0,0,0,List.of(new Efecto("INVULNERABILIDAD",100,1,100,"")),""),
          new Habilidad("lanzamiento_de_lodo","Lanzamiento de Lodo","HABILIDAD","VARIOS_ENEMIGOS",4,3,0,0,2,2,2,2,List.of(new Efecto("RALENTIZACION",3,2,100,"")),"")
       ));
       ENEMIGOS.put("REY_VAMPIRO",List.of(
          new Habilidad("rejuvenecimiento_generacional","Rejuvenecimiento Generacional","PASIVA","USUARIO",0,0,0,0,0,0,0,0,List.of(new Efecto("REGENERAR_VIDA",5,3,100,""),new Efecto("BONO_DANIO_PORCENTUAL",15,0,100,"Solo contra personajes con Sangrado, no acumulable.")),""),
          new Habilidad("garras_del_rey_de_la_noche","Garras del Rey de la Noche","BASICA","UNO_ENEMIGO",0,0,5,5,4,4,1,1,List.of(new Efecto("SANGRADO",2,2,10,"")),""),
          new Habilidad("secuaces_nocturnos","Secuaces Nocturnos","HABILIDAD","NINGUNO",9,5,0,0,0,0,0,0,List.of(new Efecto("INVOCAR_MURCIELAGOS",2,3,100,"2 murciélagos, máximo 2 activos.")),""),
          new Habilidad("vision_vampirica","Visión Vampírica","HABILIDAD","UNO_ENEMIGO",6,4,0,0,3,3,1,1,List.of(new Efecto("CRITICO",30,3,100,"Puntos porcentuales de crítico.")),""),
          new Habilidad("succion_de_sangre","Succión de Sangre","HABILIDAD","UNO_ENEMIGO",8,3,0,0,5,5,1,1,List.of(new Efecto("SANGRADO",2,2,100,""),new Efecto("DEBILITADO",20,2,100,"-20% ambas defensas y -30% ataque.")),"")
       ));
       INVOCACIONES.put("GUARDIA_REAL",List.of(
          new Habilidad("golpe_guardia","Golpe del Guardia","BASICA","UNO_ENEMIGO",0,0,2,2,0,0,1,1,List.of(),"")
       ));
       INVOCACIONES.put("COPIA_GOLEM",List.of(
          new Habilidad("golpe_copia","Golpe de la Copia","BASICA","UNO_ENEMIGO",0,0,4,4,0,0,1,1,List.of(),"Mitad del Puñetazo de Piedra del Golem (8).")
       ));
       INVOCACIONES.put("MURCIELAGO",List.of(
          new Habilidad("mordida_murcielago","Mordida de Murciélago","BASICA","UNO_ENEMIGO",0,0,1,3,0,0,1,1,List.of(),"")
       ));
    }
    private CatalogoHabilidades(){}
    public static List<Habilidad> personaje(String tipo){return PERSONAJES.get(tipo);}
    public static List<Habilidad> enemigo(String tipo){return ENEMIGOS.get(tipo);}
    public static List<Habilidad> invocacion(String tipo){return INVOCACIONES.get(tipo);}
    public static Map<String,List<Habilidad>> personajes(){return new HashMap<>(PERSONAJES);}
    public static Map<String,List<Habilidad>> enemigos(){return new HashMap<>(ENEMIGOS);}
    public static Map<String,List<Habilidad>> invocaciones(){return new HashMap<>(INVOCACIONES);}
}
