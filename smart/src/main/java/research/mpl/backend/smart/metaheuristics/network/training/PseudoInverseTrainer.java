package research.mpl.backend.smart.metaheuristics.network.training;

import java.util.Arrays;

import jama.EigenvalueDecomposition;
import jama.Matrix;
import research.mpl.backend.smart.core.Problem;
import research.mpl.backend.smart.core.Solution;
import research.mpl.backend.smart.core.SolutionGenerator;
import research.mpl.backend.smart.metaheuristics.network.EchoStateNetwork;
import research.mpl.backend.smart.metaheuristics.network.NetworkTopology;
import research.mpl.backend.smart.metaheuristics.network.ProblemDataSet;
import research.mpl.backend.smart.metaheuristics.network.node.OutputEchoStateNeuron;
import research.mpl.backend.smart.metaheuristics.network.node.ReservoirEchoStateNeuron;
import research.mpl.backend.smart.problems.networkBPRegression.ESNWeightsOptimizationProblem;
import research.mpl.backend.smart.core.VariableValue;
import research.mpl.backend.smart.util.EnduranceException;
import research.mpl.backend.smart.util.TimeUtil;

public class PseudoInverseTrainer extends TrainerRunnable {

	public PseudoInverseTrainer(Problem problem, String resultsPath) {
		super(problem, resultsPath);
	}

	@Override
	public String getNamePrefix() {
		return "";
	}

	@Override
	public String getNameSuffix() {
		return "";
	}
	
	@Override
	public void execute() throws EnduranceException, ClassNotFoundException {

		String resultsPath = getResultsPath();

		ESNWeightsOptimizationProblem esnProblem = (ESNWeightsOptimizationProblem) getProblem();

		ProblemDataSet dataset = esnProblem.getDataSet();
		EchoStateNetwork esn = (EchoStateNetwork) esnProblem.getNetwork();
		NetworkTopology networkTopology = esn.getNetworkTopology();
		ReservoirEchoStateNeuron[] reservoirNeurons = esn.getReservoirNeurons();

		// Configure echo state property
		configureEchoStateForInternalWeightsMatrix(reservoirNeurons);

		Solution solution = createSolutionFromCurrentWeightArray(esnProblem, esn.getOutputNeurons());
		esnProblem.evaluate(solution);// evaluate 1 epoch

		int numberOfInstances = dataset.getTrainingSet().size();
		int numberOfReservoirNeurons = networkTopology.getNumResNodes();
		int numberOfInputsIncludingBias = networkTopology.getNumInputs(); // including
		// bias
		int numberOfInputs = numberOfInputsIncludingBias - 1; // without bias
		int numberOfOutputs = networkTopology.getNumOutputs();

		int tInicial = (int) (0.1 * numberOfInstances);

		double[][] arrayMatrixS = new double[numberOfInstances - tInicial][numberOfReservoirNeurons + numberOfInputsIncludingBias];
		// http://www.scholarpedia.org/article/Echo_state_network

		for (int t = tInicial; t < numberOfInstances; t++) {

			// Add reservoirNeurons' outputs to the matrix S
			for (int i = 0; i < reservoirNeurons.length; i++) {
				arrayMatrixS[t - tInicial][i] = reservoirNeurons[i].getOutputedValueOfInstance(t);// neuron-i's output for instance t
			}

			// Add inputs to the matrix S
			int inputIndex = reservoirNeurons.length;

			// Bias input "1" should be considered as input column in matrix S?
			arrayMatrixS[t - tInicial][inputIndex] = 1;
			inputIndex++;

			for (int j = 0; j < numberOfInputs; j++) {
				arrayMatrixS[t - tInicial][inputIndex + j] = dataset.getTrainingSet().get(t).getInputValue(j);// inputs of instance t
			}
		}
		printMatrixToConsole("Matrix S", arrayMatrixS);

		// Create matrix D
		double[][] arrayMatrixD = new double[numberOfInstances - tInicial][numberOfOutputs];
		for (int t = tInicial; t < numberOfInstances; t++) {
			arrayMatrixD[t - tInicial] = dataset.getTrainingSet().get(t).getOutputArray(); // desired
			// outputs
		}
		printMatrixToConsole("Matrix D", arrayMatrixD);

		Matrix matrixS = new Matrix(arrayMatrixS);
		System.out.println("\nObtaining pseudoInverse of S...");

		long initTime = System.currentTimeMillis();
		Matrix pseudoInverseOfMatrixS = matrixS.inverse();
		long estimatedTime = System.currentTimeMillis() - initTime;
		TimeUtil.printExecutionTime(estimatedTime, "Generate pseudoInverseOfMatrixS");
		printMatrixToConsole("PseusoInverse of S", pseudoInverseOfMatrixS);

		Matrix matrixD = new Matrix(arrayMatrixD);
		System.out.println("Multiplying pseudoInverse of matrix S by matrix D...");

		initTime = System.currentTimeMillis();
		Matrix outputWeightsMatrix = (pseudoInverseOfMatrixS.times(matrixD)).transpose();
		estimatedTime = System.currentTimeMillis() - initTime;
		TimeUtil.printExecutionTime(estimatedTime, "Generate outputWeightsMatrix");
		printMatrixToConsole("Matriz resultante", outputWeightsMatrix);

		String lineSeparator = System.getProperty("line.separator");
		StringBuffer strBff = new StringBuffer();

		SolutionGenerator solutionGenerator = new SolutionGenerator(esnProblem);
		solution = new Solution(solutionGenerator, esnProblem.getNumberOfObjectives());
		VariableValue[] decisionVariables = solution.getDecisionVariablesArray();

		// double biasWeight = 0.0; // TODO set bias value
		// decisionVariables[0].setValue(biasWeight);

		for (int i = 0; i < outputWeightsMatrix.getRowDimension(); i++) {

			for (int j = 0; j < outputWeightsMatrix.getColumnDimension(); j++) {
				// Since j = 0 corresponds to the Bias value in the
				// "decisionVariables" array, we start in j = 1
				// decisionVariables[j+1].setValue(outputWeightsMatrix.getSolutionAt(i,
				// j));

				decisionVariables[j].setValue(outputWeightsMatrix.get(i, j));
				strBff.append(outputWeightsMatrix.get(i, j));
				strBff.append(";");

				System.out.print(outputWeightsMatrix.get(i, j) + "\t");
			}
			System.out.print("");
			strBff.append(lineSeparator);
		}

		problem.evaluate(solution);

		// ////////////

		System.out.println("\n\nObjectives values have been writen to file BestSolutionQuality. BEST FITNESS = " + solution.getFitness());
		printContentToFile(solution.getFitness() + "", resultsPath + "/BestSolutionQuality.csv");
		System.out.println("Variables values have been writen to file BestSolution");
		printContentToFile(strBff.toString(), resultsPath + "/BestSolution.csv");

		System.out.println("THE END");
	}

	public void configureEchoStateForInternalWeightsMatrix(ReservoirEchoStateNeuron[] reservoirNeurons) {

		int numberOfInternalWeights = reservoirNeurons.length;
		double[][] internalWeightsMatrix = new double[reservoirNeurons.length][numberOfInternalWeights];

		ReservoirEchoStateNeuron reservoirNeuron = null;
		int[] recurrentSynapsesSources = null;
		int sourceIndex = 0;

		for (int i = 0; i < reservoirNeurons.length; i++) {// for each reservoir neuron
			reservoirNeuron = reservoirNeurons[i];

			// ids of neurons connected to i. Ex:[0,2,3,5] (obs:they are always ordered)
			recurrentSynapsesSources = reservoirNeuron.getRecurrentSynapsesSources();
			sourceIndex = 0;

			// search the reservoir layer
			for (int j = 0; j < reservoirNeurons.length; j++) {

				// if neuron j is connected to neuron i
				if (sourceIndex < recurrentSynapsesSources.length && j == recurrentSynapsesSources[sourceIndex]) {
					// getSolutionAt the weight corresponding to j
					internalWeightsMatrix[i][j] = reservoirNeuron.getRecurrentSynapseWeight(sourceIndex);
					sourceIndex++;
				} else {
					internalWeightsMatrix[i][j] = 0;
				}
			}
		}

		Matrix matrixW = new Matrix(internalWeightsMatrix);

		printMatrixToConsole("Matrix of internal weights W", matrixW);

		EigenvalueDecomposition eigenValuesDecomp = null;
		double[] realEigenvalues = null;
		double[] imaginaryEigenvalues = null;
		boolean echoStatePropertyAchieved = false;
		boolean maxAttemptsAchieved = false;
		int attemptsCounter = 0;
		
		while (!echoStatePropertyAchieved && !maxAttemptsAchieved) {
			
    		eigenValuesDecomp = new EigenvalueDecomposition(matrixW);
    		realEigenvalues = eigenValuesDecomp.getRealEigenvalues();
    		imaginaryEigenvalues = eigenValuesDecomp.getImagEigenvalues();
    
    		printVectorToConsole("Real eigenvalues of marix W", realEigenvalues);
    		printVectorToConsole("Imaginary eigenvalues of marix W", imaginaryEigenvalues);
    		
    		double spectralRadiusOfW = -1;
    		for (int i = 0; i < realEigenvalues.length; i++) {
    			if(Math.abs(realEigenvalues[i]) > spectralRadiusOfW){
    				spectralRadiusOfW = Math.abs(realEigenvalues[i]);
    			}
    		}
		
    		if (spectralRadiusOfW < 1) {
    			echoStatePropertyAchieved = true;
    			System.out.println("Echo state property condition met");
    		} else {
    			System.out.println("Echo state property condition NOT met");
    			
    			if (attemptsCounter < 1) {
        			System.out.println("Trying to set echo state property condition by matrix scaling...");
        			double scalingValue = 1/spectralRadiusOfW;
        			matrixW = matrixW.times(scalingValue);
        			
        			attemptsCounter++;
    			} else {
    				maxAttemptsAchieved = true;
    				System.out.println("CanNOT try to meet echo state property condition again...");
    			}
    		}
		}
		
		// Setting new weights to neurons connections
		for (int i = 0; i < reservoirNeurons.length; i++) {// for each reservoir neuron
			reservoirNeuron = reservoirNeurons[i];

			// ids of neurons connected to i. Ex:[0,2,3,5] (obs:they are always ordered)
			recurrentSynapsesSources = reservoirNeuron.getRecurrentSynapsesSources();
			sourceIndex = 0;

			// search the reservoir layer
			for (int j = 0; j < reservoirNeurons.length; j++) {

				// if neuron j is connected to neuron i
				if (sourceIndex < recurrentSynapsesSources.length && j == recurrentSynapsesSources[sourceIndex]) {
					// getSolutionAt the weight corresponding to j
					internalWeightsMatrix[i][j] = reservoirNeuron.getRecurrentSynapseWeight(sourceIndex);
					sourceIndex++;
					
					if(sourceIndex < recurrentSynapsesSources.length){
						
					}
				} else {
					internalWeightsMatrix[i][j] = 0;
				}
			}
		}
	}

	public Solution createSolutionFromCurrentWeightArray(ESNWeightsOptimizationProblem problem, OutputEchoStateNeuron[] outputNeurons) {

		Solution solution = null;
		try {
			SolutionGenerator solutionGenerator = new SolutionGenerator(problem);
			solution = new Solution(solutionGenerator, problem.getNumberOfObjectives());
			VariableValue[] decisionVariables = solution.getDecisionVariablesArray();

			int index = 0;

			for (int i = 0; i < outputNeurons.length; i++) {
				// decisionVariables[index].setValue(outputNeurons[i].getBias());
				// index++;

				for (int j = 0; j < outputNeurons[i].getInputLayerSynapsesWeights().length; j++) {
					double value = outputNeurons[i].getInputLayerSynapseWeight(j);
					decisionVariables[index].setValue(value);
					index++;
				}

				for (int j = 0; j < outputNeurons[i].getReservoirSynapsesWeights().length; j++) {
					double value = outputNeurons[i].getReservoirSynapseWeight(j);
					decisionVariables[index].setValue(value);
					index++;
				}

				for (int j = 0; j < outputNeurons[i].getRecurrentSynapsesWeights().length; j++) {
					double value = outputNeurons[i].getRecurrentSynapseWeight(j);
					decisionVariables[index].setValue(value);
					index++;
				}
			}
			// -------
			StringBuffer stbff = new StringBuffer();
			stbff.append("\nSolution:");
			for (int i = 0; i < decisionVariables.length; i++) {
				stbff.append(decisionVariables[i]);
				stbff.append("\t");
			}
			stbff.append("\nNumber of variables:");
			stbff.append(decisionVariables.length);
			System.out.println(stbff.toString());
			// ------
		} catch (ClassNotFoundException classNotFoundException) {
			classNotFoundException.printStackTrace();
		}

		return solution;
	}

	public void printMatrixToConsole(String title, Matrix matrix) {
		// Get max string size for each column
		String temp = null;
		int[] maxStringSizeOfColumn = new int[matrix.getColumnDimension()];
		Arrays.fill(maxStringSizeOfColumn, Integer.MIN_VALUE);
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {

				temp = String.valueOf(matrix.get(i, j));
				if (temp.length() > maxStringSizeOfColumn[j]) {
					maxStringSizeOfColumn[j] = temp.length();
				}
			}
		}
		// Create string
		StringBuffer stbff = new StringBuffer();
		stbff.append("\n------ ");
		stbff.append(title);
		stbff.append(" ------\n");
		if (matrix.getRowDimension() <= 100 && matrix.getColumnDimension() <= 100) {
			for (int i = 0; i < matrix.getRowDimension(); i++) {
				for (int j = 0; j < matrix.getColumnDimension(); j++) {
					stbff.append(String.format("%1$" + maxStringSizeOfColumn[j] + "s", matrix.get(i, j)));
					stbff.append("\t");
				}
				stbff.append("\n");
			}
		} else {
			stbff.append("Matrix is too large to be printed");
		}
		stbff.append("\n");
		stbff.append("Dimension = ");
		stbff.append(matrix.getRowDimension());
		stbff.append(" x ");
		stbff.append(matrix.getColumnDimension());
		stbff.append("\n");

		System.out.println(stbff.toString());
	}

	public void printMatrixToConsole(String title, double[][] matrix) {
		// Get max string size for each column
		String temp = null;
		int[] maxStringSizeOfColumn = new int[matrix[0].length];
		Arrays.fill(maxStringSizeOfColumn, Integer.MIN_VALUE);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {

				temp = String.valueOf(matrix[i][j]);
				if (temp.length() > maxStringSizeOfColumn[j]) {
					maxStringSizeOfColumn[j] = temp.length();
				}
			}
		}
		// Create string
		StringBuffer stbff = new StringBuffer();
		stbff.append("\n ------");
		stbff.append(title);
		stbff.append(" ------\n");
		if (matrix.length <= 100 && matrix[0].length <= 100) {
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					stbff.append(matrix[i][j]);
					stbff.append("\t");
				}
				stbff.append("\n");
			}
		} else {
			stbff.append("Matrix is too large to be printed");
		}
		stbff.append("\n");
		stbff.append("Dimension = ");
		stbff.append(matrix.length);
		stbff.append(" x ");
		stbff.append(matrix[0].length);
		stbff.append("\n");

		System.out.println(stbff.toString());
	}

	public void printVectorToConsole(String title, double[] vector) {

		StringBuffer stbff = new StringBuffer();
		stbff.append("\n ------");
		stbff.append(title);
		stbff.append(" ------\n");
		if (vector.length <= 100) {
			for (int i = 0; i < vector.length; i++) {
				stbff.append(vector[i]);
				stbff.append("\t");
			}
			stbff.append("\n");
		} else {
			stbff.append("Vector is too large to be printed");
		}

		stbff.append("\n");
		stbff.append("Dimension = ");
		stbff.append(vector.length);
		stbff.append("\n");

		System.out.println(stbff.toString());
	}
}
