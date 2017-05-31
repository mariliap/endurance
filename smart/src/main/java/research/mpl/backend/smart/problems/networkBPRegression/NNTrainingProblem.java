package research.mpl.backend.smart.problems.networkBPRegression;

import research.mpl.backend.smart.core.Problem;
import research.mpl.backend.smart.core.Solution;
import research.mpl.backend.smart.util.EnduranceException;

public abstract class NNTrainingProblem extends Problem {

	public enum InternalLayerType{
	    MLP_1_LAYER, RECURRENT
	}
	
	public abstract void evaluate(Solution solution) throws EnduranceException;
	
	public void saveNetwork(Solution solution, String path) throws EnduranceException {

	}

}
