package research.mpl.backend.smart.core;

import java.io.Serializable;
import java.util.List;

import research.mpl.backend.smart.util.EnduranceException;

import javax.persistence.*;

@Entity
public abstract class Problem implements Serializable {

//  /**
//   * Defines the default precision of binary-coded variables
//   */
//  private final static int DEFAULT_PRECISSION = 16;

    private Long id;
    private int numberOfVariables;
    private int numberOfObjectives;
    private int numberOfConstraints;
    private String problemName;
    private List<VariableDefinition> variableDefinition;
  
//  /**
//   * Stores the number of bits used by binary-coded variables (e.g., BinaryReal
//   * variables). By default, they are initialized to DEFAULT_PRECISION)
//   */
//  private int    [] precision;
//
//  /**
//   * Stores the length of each variable when applicable (e.g., Binary and
//   * Permutation variables)
//   */
//  private int[] length;


    public Problem() {
        // solutionGenerator = null ;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public int getNumberOfVariables() {
      return numberOfVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    @Column
    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    @Column
    public int getNumberOfConstraints() {
        return numberOfConstraints;
    }

    public void setNumberOfConstraints(int numberOfConstraints) {
        this.numberOfConstraints = numberOfConstraints;
    }

    @Column
    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    @ManyToMany
    public List<VariableDefinition> getVariableDefinition() {
        return variableDefinition;
    }

    public VariableDefinition getVariableDefinition(int position) {
        return variableDefinition.get(position);
    }

    public void setVariableDefinition(List<VariableDefinition> variableDefinition) {
        this.variableDefinition = variableDefinition;
    }

    public void setVariableDefinition(int position, VariableDefinition variableDefinition) {
        this.variableDefinition.set(position,variableDefinition);
    }

    public abstract void evaluate(Solution solution) throws EnduranceException;
    
//   /**
//   * Evaluates the overall constraint violation of a <code>Solution</code>
//   * object.
//   * @param solution The <code>Solution</code> to evaluate.
//   */
//  public void evaluateConstraints(Solution solution) throws EnduranceException {
//    // The default behavior is to do nothing. Only constrained problems have to
//    // re-define this method
//  }

//  /**
//   * Returns the number of bits that must be used to encode binary-real variables
//   * @return the number of bits.
//   */
//  public int getPrecision(int var) {
//    return precision[var] ;
//  } // getPrecision
//
//  /**
//   * Returns array containing the number of bits that must be used to encode
//   * binary-real variables.
//   * @return the number of bits.
//   */
//  public int [] getPrecision() {
//    return precision;
//  } // getPrecision
//
//  /**
//   * Sets the array containing the number of bits that must be used to encode
//   * binary-real variables.
//   * @param precision The array
//   */
//  public void setPrecision(int [] precision) {
//    this.precision = precision;
//  } // getPrecision
//
//  /**
//   * Returns the length of the variable.
//   * @return the variable length.
//   */
//  public int getLength(int var) {
//    if (length == null)
//      return DEFAULT_PRECISSION;
//    return length[var] ;
//  } // getLength
//
//  /**
//   * Sets the type of the variables of the problem.
//   * @param type The type of the variables
//   */
//  public void setSolutionType(SolutionGenerator type) {
//    solutionGenerator = type;
//  } // setSolutionType
//
//  /**
//   * Returns the type of the variables of the problem.
//   * @return type of the variables of the problem.
//   */
//  public SolutionGenerator getSolutionType() {
//    return solutionGenerator;
//  } // getSolutionType
//

//  /**
//   * Returns the number of bits of the solutions of the problem
//   * @return The number of bits solutions of the problem
//   */
//    public int getNumberOfBits() {
//    int result = 0;
//    for (int var = 0; var < numberOfVariables; var++) {
//      result += getLength(var);
//    }
//    return result;
//    } // getNumberOfBits();


}
