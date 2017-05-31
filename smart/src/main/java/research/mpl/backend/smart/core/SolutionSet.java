
package research.mpl.backend.smart.core;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SolutionSet implements Serializable {

    protected List<Solution> solutionList;

    private int capacity = 0;

    public SolutionSet() {
        solutionList = new ArrayList<Solution>();
    }

    public SolutionSet(int maximumSize){
        capacity = maximumSize;
        solutionList = new ArrayList<Solution>();
    }

    public boolean add(Solution solution) {
        if (solutionList.size() == capacity) {
          return false;
        }
        solutionList.add(solution);
        return true;
    }

    public void remove(int i){
        solutionList.remove(i);
    }

    public void clear(){
        solutionList.clear();
    }

    public Solution getSolutionAt(int i) {
        return solutionList.get(i);
    }

    public void setSolutionAt(int position, Solution solution) {
        if (position > this.solutionList.size()) {
            solutionList.add(solution);
        } // if
        solutionList.remove(position);
        solutionList.add(position, solution);
    }

    public int getMaxSize(){
        return capacity;
    }


    public void sort(Comparator comparator){

        Collections.sort(solutionList,comparator);
    }

    public int size(){
        return solutionList.size();
    }

    public Iterator<Solution> iterator(){
        return solutionList.iterator();
    }

    public int indexBest(Comparator comparator){

        if ((solutionList == null) || (this.solutionList.isEmpty())) {
            return -1;
        }

        int index = 0;
        Solution bestKnown = solutionList.get(0);
        Solution candidateSolution;
        int flag;
        for (int i = 1; i < solutionList.size(); i++) {
            candidateSolution = solutionList.get(i);
            flag = comparator.compare(bestKnown, candidateSolution);
            if (flag == -1) {
                index = i;
                bestKnown = candidateSolution;
            }
        }

        return index;

    }


    public Solution best(Comparator comparator){
        int indexBest = indexBest(comparator);
        if (indexBest < 0) {
            return null;
        } else {
            return solutionList.get(indexBest);
        }

    }


    public int indexWorst(Comparator comparator){


        if ((solutionList == null) || (this.solutionList.isEmpty())) {
            return -1;
        }

        int index = 0;
        Solution worstKnown = solutionList.get(0), candidateSolution;
        int flag;
        for (int i = 1; i < solutionList.size(); i++) {
            candidateSolution = solutionList.get(i);
            flag = comparator.compare(worstKnown, candidateSolution);
            if (flag == -1) {
                index = i;
                worstKnown = candidateSolution;
            }
        }

        return index;

    }

    public Solution worst(Comparator comparator){

        int index = indexWorst(comparator);
        if (index < 0) {
            return null;
        } else {
            return solutionList.get(index);
        }

    }

    public SolutionSet union(SolutionSet solutionSet) {
        //Check the correct size. In development
        int newSize = this.size() + solutionSet.size();
        if (newSize < capacity)
            newSize = capacity;

        //Create a new population
        SolutionSet union = new SolutionSet(newSize);
        for (int i = 0; i < this.size(); i++) {
            union.add(this.getSolutionAt(i));
        } // for

        for (int i = this.size(); i < (this.size() + solutionSet.size()); i++) {
            union.add(solutionSet.getSolutionAt(i - this.size()));
        } // for

        return union;
    }

    /**
    * Writes the objective function values of the <code>Solution</code>
    * objects into the set in a file.
    * @param path The output file name
    */
    public void printObjectivesToFile(String path){
        try {
            /* Open the file */
            FileOutputStream fos   = new FileOutputStream(path)     ;
            OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
            BufferedWriter bw      = new BufferedWriter(osw)        ;

            for (int i = 0; i < solutionList.size(); i++) {
            //if (this.vector[i].getFitness()<1.0) {
                bw.write(solutionList.get(i).toString());
                bw.newLine();
            //}
            }

            /* Close the file */
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    } // printObjectivesToFile

    /**
    * Writes the decision variable values of the <code>Solution</code>
    * solutions objects into the set in a file.
    * @param path The output file name
    */
    public void printVariablesToFile(String path){
        try {
            /* Open the file */
            FileOutputStream fos   = new FileOutputStream(path)     ;
            OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
            BufferedWriter bw      = new BufferedWriter(osw)        ;

            int numberOfVariables = solutionList.get(0).getDecisionVariablesArray().length ;
            for (int i = 0; i < solutionList.size(); i++) {
            for (int j = 0; j < numberOfVariables; j++)
              bw.write(solutionList.get(i).getDecisionVariablesArray()[j].toString() + " ");
            bw.newLine();
            }

            /* Close the file */
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    } // printVariablesToFile

    /**
    * Copies the objectives of the solution set to a matrix
    * @return A matrix containing the objectives
    */
    public double [][] writeObjectivesToMatrix() {
        if (this.size() == 0) {
            return null;
        }
        double [][] objectives;
        objectives = new double[size()][getSolutionAt(0).numberOfObjectives()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < getSolutionAt(0).numberOfObjectives(); j++) {
            objectives[i][j] = getSolutionAt(i).getObjective(j);
            }
        }
        return objectives;
    }
}

