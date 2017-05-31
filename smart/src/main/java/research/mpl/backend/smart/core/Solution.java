
package research.mpl.backend.smart.core;

import research.mpl.backend.smart.experiment.SimulationStep;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SOLUTION")
public class Solution implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private SimulationStep simulationStep;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solution", orphanRemoval = true)
    private List<VariableValue> variables;

    private double [] objective;

    private int numberOfObjectives;

    private double fitness;

    private double overallConstraintViolationByTheSolution;

    private int  numberOfViolatedConstraints ;


    /**
    * Used in algorithm AbYSS, this field is intended to be used to know
    * when a <code>Solution</code> is marked.
    */
    private boolean marked ;

    /**
    * Stores the so called rank of the solution. Used in NSGA-II
    */
    private int rank ;

    /**
    * This field is intended to be used to know the location of
    * a solution into a <code>SolutionSet</code>. Used in MOCell
    */
    private int location;

    /**
    * Stores the distance to his k-nearest neighbor into a
    * <code>SolutionSet</code>. Used in SPEA2.
    */
    private double kDistance;

    /**
    * Stores the crowding distance of the the solution in a
    * <code>SolutionSet</code>. Used in NSGA-II.
    */
    private double crowdingDistance;

    /**
    * Stores the distance between this solution and a <code>SolutionSet</code>.
    * Used in AbySS.
    */
    private double distanceToSolutionSet;

    public Solution() {
        marked = false;
        overallConstraintViolationByTheSolution = 0.0;
        numberOfViolatedConstraints = 0;
        variables = null;
        objective = null;
    }

    /**
    * Constructor
    * @param numberOfObjectives Number of objectives of the solution
    *
    * This constructor is used mainly to read objective values from a file to
    * variables of a SolutionSet to apply quality indicators
    */
    public Solution(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
        objective = new double[numberOfObjectives];
    }

    public Solution(SolutionGenerator solutionGenerator,int numberOfObjectives)
            throws ClassNotFoundException{

        this.numberOfObjectives = numberOfObjectives ;
        objective = new double[this.numberOfObjectives] ;

        // Setting initial values
        fitness = 0.0 ;
        kDistance = 0.0 ;
        crowdingDistance = 0.0 ;
        distanceToSolutionSet = Double.POSITIVE_INFINITY ;

        variables = solutionGenerator.createVariables(this) ;
    }


    public Solution(Solution solution) {

        numberOfObjectives = solution.numberOfObjectives();
        objective = new double[numberOfObjectives];
        for (int i = 0; i < objective.length;i++) {
            objective[i] = solution.getObjective(i);
        }

        variables = copyVariables(solution.variables) ;
        overallConstraintViolationByTheSolution = solution.getOverallConstraintViolationByTheSolution();
        numberOfViolatedConstraints = solution.getNumberOfViolatedConstraints();
        distanceToSolutionSet = solution.getDistanceToSolutionSet();
        crowdingDistance = solution.getCrowdingDistance();
        kDistance = solution.getKDistance();
        fitness = solution.getFitness();
        marked = solution.isMarked();
        rank = solution.getRank();
        location = solution.getLocation();
    }

    public List<VariableValue> copyVariables(List<VariableValue> vars) {
        List<VariableValue> variables = new ArrayList<VariableValue>();

        for (int var = 0; var < vars.size(); var++) {
            variables.set(var, vars.get(var).copy());
        }

        return variables ;
    }

  /**
   * Sets the distance between this solution and a <code>SolutionSet</code>.
   * The value is stored in <code>distanceToSolutionSet</code>.
   * @param distance The distance to a solutionSet.
   */
  public void setDistanceToSolutionSet(double distance){
    distanceToSolutionSet = distance;
  } // SetDistanceToSolutionSet

  /**
   * Gets the distance from the solution to a <code>SolutionSet</code>. 
   * <b> REQUIRE </b>: this method has to be invoked after calling 
   * <code>setDistanceToPopulation</code>.
   * @return the distance to a specific solutionSet.
   */
  public double getDistanceToSolutionSet(){
    return distanceToSolutionSet;
  } // getDistanceToSolutionSet


  /** 
   * Sets the distance between the solution and its k-nearest neighbor in 
   * a <code>SolutionSet</code>. The value is stored in <code>kDistance</code>.
   * @param distance The distance to the k-nearest neighbor.
   */
  public void setKDistance(double distance){
    kDistance = distance;
  } // setKDistance

  /** 
   * Gets the distance from the solution to his k-nearest nighbor in a 
   * <code>SolutionSet</code>. Returns the value stored in
   * <code>kDistance</code>. <b> REQUIRE </b>: this method has to be invoked
   * after calling <code>setKDistance</code>.
   * @return the distance to k-nearest neighbor.
   */
  public double getKDistance(){
    return kDistance;
  } // getKDistance

  /**
   * Sets the crowding distance of a solution in a <code>SolutionSet</code>.
   * The value is stored in <code>crowdingDistance</code>.
   * @param distance The crowding distance of the solution.
   */  
  public void setCrowdingDistance(double distance){
    crowdingDistance = distance;
  } // setCrowdingDistance


  /** 
   * Gets the crowding distance of the solution into a <code>SolutionSet</code>.
   * Returns the value stored in <code>crowdingDistance</code>.
   * <b> REQUIRE </b>: this method has to be invoked after calling 
   * <code>setCrowdingDistance</code>.
   * @return the distance crowding distance of the solution.
   */
  public double getCrowdingDistance(){
    return crowdingDistance;
  } // getCrowdingDistance


  public void setFitness(double fitness) {
    this.fitness = fitness;
  } // setFitness

  public double getFitness() {
    return fitness;
  } // getFitness


  public void setObjective(int i, double value) {
    objective[i] = value;
  } // setObjective

  public double getObjective(int i) {
    return objective[i];
  } // getObjective


  public int numberOfObjectives() {
    if (objective == null)
      return 0 ;
    else
      return numberOfObjectives;
  } // numberOfObjectives


  public int numberOfVariables() {
    return variables.size() ;
  } // numberOfVariables

  /** 
   * Returns a string representing the solution.
   * @return The string.
   */
  public String toString() {
    String aux="";
    for (int i = 0; i < this.numberOfObjectives; i++)
      aux = aux + this.getObjective(i) + " ";

    return aux;
  } // toString


    public VariableValue[] getDecisionVariablesArray() {
        VariableValue[] variablesArray = new VariableValue[this.variables.size()];
        for (int i = 0; i < this.variables.size(); i++){
            variablesArray[i] = this.variables.get(i);
        }
        return variablesArray;
    }


  public void setDecisionVariables(VariableValue[] variables) {
    variables = variables ;
  }


  public boolean isMarked() {
    return this.marked;
  }

  public void marks() {
    this.marked = true;
  }

  public void unMarks() {
    this.marked = false;
  }

  public void setRank(int value){
    this.rank = value;
  }

  public int getRank(){
    return this.rank;
  }


  public void setOverallConstraintViolationByTheSolution(double value) {
    this.overallConstraintViolationByTheSolution = value;
  } // setOverallConstraintViolationByTheSolution

  public double getOverallConstraintViolationByTheSolution() {
    return this.overallConstraintViolationByTheSolution;
  }  //getOverallConstraintViolationByTheSolution


  public void setNumberOfViolatedConstraints(int value) {
    this.numberOfViolatedConstraints = value;
  } //setNumberOfViolatedConstraints

  public int getNumberOfViolatedConstraints() {
    return this.numberOfViolatedConstraints;
  } // getNumberOfViolatedConstraints

  /**
   * Sets the location of the solution into a solutionSet. 
   * @param location The location of the solution.
   */
  public void setLocation(int location) {
    this.location = location;
  } // setLocation

  /**
   * Gets the location of this solution in a <code>SolutionSet</code>.
   * <b> REQUIRE </b>: This method has to be invoked after calling
   * <code>setLocation</code>.
   * @return the location of the solution into a solutionSet
   */
  public int getLocation() {
    return this.location;
  } // getLocation


  /** 
   * Returns the aggregative value of the solution
   * @return The aggregative value.
   */
  public double getAggregativeValue() {
    double value = 0.0;                
    for (int i = 0; i < numberOfObjectives(); i++){            
      value += getObjective(i);
    }                
    return value;
  } // getAggregativeValue

//  /**
//   * Returns the number of bits of the chromosome in case of using a binary
//   * representation
//   * @return The number of bits if the case of binary variables, 0 otherwise
//   */
//  public int getNumberOfBits() {
//    int bits = 0 ;
//
//    for (int i = 0;  i < variables.length  ; i++)
//	    try {
//	      if ((variables[i].getVariableType() == Class.forName("jmetal.base.variable.Binary")) ||
//	          (variables[i].getVariableType() == Class.forName("jmetal.base.variable.BinaryReal")))
//	        bits += ((Binary)(variables[i])).getNumberOfBits() ;
//      } catch (ClassNotFoundException e) {
//	      // TODO Auto-generated catch block
//	      e.printStackTrace();
//      }
//
//    return bits ;
//  } // getNumberOfBits

} // Solution
