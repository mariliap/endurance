package research.mpl.backend.misc.pos.ecossistema;

/**
 * Created by Marilia Portela on 26/03/2017.
 */
public class EcossistemaTeste {

    public static void main (String[] args){
        Ecossistema eco = new Ecossistema(10);
        eco.print();
        while (!eco.apenasUrsos()){
            eco.iniciar();
            eco.print();
        }
    }
}
