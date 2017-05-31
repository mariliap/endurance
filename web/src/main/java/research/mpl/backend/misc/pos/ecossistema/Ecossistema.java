package research.mpl.backend.misc.pos.ecossistema;

import java.util.*;

/**
 * Created by Marilia Portela on 26/03/2017.
 */
public class Ecossistema {

    private Animal[] rio;
    List<Integer> posicoesVazias;

    public Ecossistema(int tamanho) {
        this.rio = new Animal[tamanho];
        int numPredadores = Math.round(tamanho * 0.2f);
        int numPresas = Math.round(tamanho * 0.3f);

        posicoesVazias = new ArrayList<Integer>();
        for (int i = 0; i < tamanho; i++) {
            posicoesVazias.add(new Integer(i));
        }

        for (int i = 0; i < numPredadores; i++) {
            criarAnimalEmPosicaoVazia(new Urso(0.5f, 0));
        }

        for (int i = 0; i < numPresas; i++) {
            criarAnimalEmPosicaoVazia(new Peixe(0.5f, 1));
        }
    }

    public void iniciar(){
        Random rn = new Random();

        Animal animal = null;
        for (int i = 0; i < rio.length; i++) {

            if(rio[i] != null) {
                animal = rio[i];
                float numAleatorio = rn.nextFloat();
                if (numAleatorio < animal.getProbabilidadeDeMovimentacao()) {
                    animal.mover(this);
                }
            }
        }
    }

    public boolean isPosicaoValida(int posicao){
        if(posicao < rio.length && posicao >= 0) {
            return true;
        }
        return false;
    }

    public boolean isEspacoVazio(int posicao){
        if(posicao < rio.length && posicao >= 0) {
            return rio[posicao] == null;
        }
        return false;
    }

    public void atualizarPosicao(Animal animal, int posicaoNova){
        posicoesVazias.add(new Integer(animal.getPosicao()));
        rio[animal.getPosicao()] = null;
        posicoesVazias.remove(new Integer(posicaoNova));
        rio[posicaoNova] = animal;
        animal.setPosicao(posicaoNova);
    }

    public void criarAnimalEmPosicaoVazia(Animal animal){
        if(!posicoesVazias.isEmpty()) {
            Collections.shuffle(posicoesVazias);
            this.rio[posicoesVazias.get(0)] = animal;
            animal.setPosicao(posicoesVazias.get(0));
            posicoesVazias.remove(posicoesVazias.get(0));
        }
    }

    public void deletarAnimal(int posicao){

        this.rio[posicao] = null;
        posicoesVazias.add(new Integer(posicao));
    }

    public Animal obterAnimal(int posicao){

        return this.rio[posicao];
    }

    public boolean apenasUrsos(){
        for (int i = 0; i <  rio.length; i++) {
            if(rio[i] instanceof Peixe){
                return false;
            }
        }
        return true;
    }

    public void print(){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < rio.length; i++) {
            if(rio[i] == null) {
                sb.append("NULL ");
            } else {
                sb.append(rio[i]);
            }

            if (i != rio.length - 1) {
                sb.append(",");
            }
        }
        System.out.println(sb.toString());
    }
}
