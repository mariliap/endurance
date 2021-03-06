package research.mpl.backend.misc.pos.ecossistema;

/**
 * Created by Marilia Portela on 26/03/2017.
 */
public class Urso extends Animal {

    Class[] predadores = {};
    Class[] presas = {Peixe.class};

    public Urso(float probabilidadeDeMovimentacao, float probabilidadeDeAcasalamento) {
        super(probabilidadeDeMovimentacao, probabilidadeDeAcasalamento);
    }

    @Override
    public boolean seVizinhoEhPresa(Ecossistema ecossistema, int posicao) {
        Animal vizinho = ecossistema.obterAnimal(posicao);
        for (int i = 0; i < presas.length; i++) {
            if(presas[i].isInstance(vizinho)){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean seVizinhoEhDaMesmaEspecie(Ecossistema ecossistema, int posicao) {
        Animal vizinho = ecossistema.obterAnimal(posicao);
        if(this.getClass().isInstance(vizinho)){
                return true;
        }
        return false;
    }

    @Override
    public boolean seVizinhoEhPredador(Ecossistema ecossistema, int posicao) {
        Animal vizinho = ecossistema.obterAnimal(posicao);
        for (int i = 0; i < predadores.length; i++) {
            if(predadores[i].isInstance(vizinho)){
                return true;
            }
        }

        return false;
    }

    @Override
    public Animal obterFilhote() {
        return new Urso(getProbabilidadeDeMovimentacao(), getProbabilidadeDeAcasalamento());
    }

    @Override
    public String toString() {
        return "URSO ";
    }
}
