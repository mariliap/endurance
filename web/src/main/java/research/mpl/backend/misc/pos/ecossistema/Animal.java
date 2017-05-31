package research.mpl.backend.misc.pos.ecossistema;

/**
 * Created by Marilia Portela on 26/03/2017.
 */
public abstract class Animal {

    private float probabilidadeDeMovimentacao;
    private float probabilidadeDeAcasalamento;
    private int posicao;

    public Animal(float probabilidadeDeMovimentacao, float probabilidadeDeAcasalamento) {
        this.probabilidadeDeMovimentacao = probabilidadeDeMovimentacao;
        this.probabilidadeDeAcasalamento = probabilidadeDeAcasalamento;
    }

    public abstract boolean seVizinhoEhPresa(Ecossistema ecossistema, int posicao);
    public abstract boolean seVizinhoEhDaMesmaEspecie(Ecossistema ecossistema, int posicao);
    public abstract boolean seVizinhoEhPredador(Ecossistema ecossistema, int posicao);
    public abstract Animal obterFilhote();

    public void mover(Ecossistema ecossistema){

        int posicaoAdjacente = escolherPosicao();

        if(ecossistema.isPosicaoValida(posicaoAdjacente)) {

            if (ecossistema.isEspacoVazio(posicaoAdjacente)) {

                ecossistema.atualizarPosicao(this, posicaoAdjacente);

            } else if (seVizinhoEhPresa(ecossistema, posicaoAdjacente)) {

                comer(ecossistema, posicaoAdjacente);

            } else if (seVizinhoEhDaMesmaEspecie(ecossistema, posicaoAdjacente)) {

                acasalar(ecossistema);

            } else if (seVizinhoEhPredador(ecossistema, posicaoAdjacente)) {

                morrer(ecossistema);
            }
        }
    }

    public int escolherPosicao(){
        if(Math.random() < 0.5){
            return this.posicao + 1;
        } else {
            return this.posicao - 1;
        }
    }

    public void comer(Ecossistema ecossistema, int posicao){
        ecossistema.deletarAnimal(posicao);
    }

    public void acasalar(Ecossistema ecossistema){
        if(Math.random() < this.probabilidadeDeAcasalamento){
            ecossistema.criarAnimalEmPosicaoVazia(obterFilhote());
        }
    }

    public void morrer(Ecossistema ecossistema){
        ecossistema.deletarAnimal(this.posicao);
    }

    public float getProbabilidadeDeMovimentacao() {
        return probabilidadeDeMovimentacao;
    }

    public void setProbabilidadeDeMovimentacao(float probabilidadeDeMovimentacao) {
        this.probabilidadeDeMovimentacao = probabilidadeDeMovimentacao;
    }

    public float getProbabilidadeDeAcasalamento() {
        return probabilidadeDeAcasalamento;
    }

    public void setProbabilidadeDeAcasalamento(float probabilidadeDeAcasalamento) {
        this.probabilidadeDeAcasalamento = probabilidadeDeAcasalamento;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}
