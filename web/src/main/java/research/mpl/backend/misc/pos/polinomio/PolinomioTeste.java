package research.mpl.backend.misc.pos.polinomio;

/**
 * Created by Marilia Portela on 27/03/2017.
 */
public class PolinomioTeste {

    public static void main(String[] args){
        int[] coef = {3,2,1,1,7};
        Polinomio polinomio = new Polinomio(coef, 4);
        System.out.println(polinomio.toString());

        Polinomio derivada = polinomio.derivada();
        System.out.println(derivada.toString());
    }
}
