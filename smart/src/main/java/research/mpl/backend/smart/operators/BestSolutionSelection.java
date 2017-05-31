package research.mpl.backend.smart.operators;

import research.mpl.backend.smart.core.SolutionSet;

import java.util.Comparator;
import java.util.HashMap;

/**
 * This class implements a selection operator used for selecting the best 
 * solution in a SolutionSet according to a given comparator
 */
public class BestSolutionSelection extends Selection {
  
	// Comparator
  private Comparator comparator_;
    
  public BestSolutionSelection(HashMap<String, Object> parameters) {
  	super(parameters) ;

  	comparator_ = null ;
  	
  	if (parameters.get("comparator") != null)
  		comparator_ = (Comparator) parameters.get("comparator") ;  		
  }

  /**
   * Constructor
   * @param comparator
   */
  //public BestSolutionSelection(Comparator comparator) {
  //	comparator_ = comparator ;
  //}
  
  /**
  * Performs the operation
  * @param object Object representing a SolutionSet.
  * @return the best solution found
  */
  public Object execute(Object object) {
    SolutionSet solutionSet     = (SolutionSet)object;
    
    if (solutionSet.size() == 0) {
      return null;
    }
    int bestSolution ;
    
    bestSolution = 0 ;
   	
    for (int i = 1; i < solutionSet.size(); i++) {
    	if (comparator_.compare(solutionSet.getSolutionAt(i),
                solutionSet.getSolutionAt(bestSolution)) < 0)
    		bestSolution = i ;
    } // for
    
    return bestSolution ;    
  } // Execute     
} // BestObjectiveSelection
