package research.mpl.backend.misc.pos.polinomio;

/**
 * Created by Marilia Portela on 27/03/2017.
 */
public class Polinomio {

    private int[] coef;
    private int coef1;
    private int grau;

    public Polinomio(int coef, int grau) {
        this.coef1 = coef;
        this.grau = grau;
    }

    public Polinomio(int[] coef, int grau) {
        this.coef = coef;
        this.grau = grau;
    }

    public Polinomio derivada() {

        int[] coefDerivara = new int[this.coef.length -1];
        for (int i = 0; i < coefDerivara.length; i++) {
            coefDerivara[i] = coef[i] * (grau - i);
        }

        Polinomio derivada = new Polinomio(coefDerivara, grau - 1);
        return derivada;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < coef.length; i++) {
            sb.append(coef[i]);
            if(grau - i >0 ) {
                sb.append("x");
                sb.append("^");
                sb.append(grau - i);

            }

            if(grau - i > 0) {
                sb.append(" + ");
            }
        }
        return sb.toString();
    }
}
