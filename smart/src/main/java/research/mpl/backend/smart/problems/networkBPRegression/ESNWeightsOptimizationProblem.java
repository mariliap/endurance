package research.mpl.backend.smart.problems.networkBPRegression;

import java.util.ArrayList;
import java.util.List;

import research.mpl.backend.smart.core.Solution;
import research.mpl.backend.smart.core.VariableDefinition;
import research.mpl.backend.smart.core.VariableValue;
import research.mpl.backend.smart.metaheuristics.network.*;
import research.mpl.backend.smart.util.EnduranceException;

import javax.inject.Inject;

public class ESNWeightsOptimizationProblem extends NNTrainingProblem {

	    	
    InternalLayerType internalLayerType = null;


	MetaHeuristicsNetwork network = null;
//	ProblemDataSet dataSet = null;
	
	boolean updateInternalLayerWeights;

	@Inject
	private ProblemDataSet dataSet;

//	public ESNWeightsOptimizationProblem(){
//		start(true);
//	}
//	public ESNWeightsOptimizationProblem(boolean updateInternalLayerWeights) {
//		start(updateInternalLayerWeights);
//
//	}

	public void start(boolean updateInternalLayerWeights) {

		try {
			this.dataSet.init();

            // falsa quando usar PSO, false quando usar PseudoInversa
			this.updateInternalLayerWeights = updateInternalLayerWeights;

			int numInputs = this.dataSet.getNumInputs() + 1; // number of inputs + bias
			int numOutputs = this.dataSet.getNumOutputs();
			int numResNodes = 0;


			this.internalLayerType = InternalLayerType.MLP_1_LAYER;

			NetworkTopology networkTopology = null;

			switch (this.internalLayerType) {
				case MLP_1_LAYER:
					numResNodes = 5;
					networkTopology = new NetworkTopology(numInputs, numResNodes, numOutputs);

					// Generate input connection matrix for MLP hidden layer neurons.
					// Lines represent neurons, and columns represent input sources.
					int numberOfHiddenLayers = 1;
					int[] numberOfNeuronsPerHiddenLayer = new int[numberOfHiddenLayers];
					// layer "0" has "numResNodes" neurons
					numberOfNeuronsPerHiddenLayer[0] = numResNodes;
					networkTopology.createMLPConnectionMatrix(numberOfNeuronsPerHiddenLayer);

					// generate input connection matrix for output neurons.
					// Lines represent neurons, and columns represent input sources.
					networkTopology.createOutputConnectionMatrix(
							NetworkTopology.InputConnectionType.NOT_CONNECTED,
							NetworkTopology.ReservoirConnectionType.FULL_CONNECTED,
							NetworkTopology.OutputConnectionType.NOT_CONNECTED);

					network = new MultiLayerPerceptronNetwork(networkTopology);
					break;
				case RECURRENT:
					numResNodes = 50;
					networkTopology = new NetworkTopology(numInputs, numResNodes, numOutputs);

					// Generate input connection matrix for reservoir neurons.
					// Lines represent neurons, and columns represent input sources.
					networkTopology.createReservoirConnectionMatrix(
							NetworkTopology.InputConnectionType.FULL_CONNECTED,
							NetworkTopology.ReservoirConnectionType.RANDOMLY_CONNECTED,
							NetworkTopology.OutputConnectionType.NOT_CONNECTED);

					// Generate input connection matrix for output neurons.
					// Lines represent neurons, and columns represent input sources.
					networkTopology.createOutputConnectionMatrix(
							NetworkTopology.InputConnectionType.FULL_CONNECTED,
							NetworkTopology.ReservoirConnectionType.FULL_CONNECTED,
							NetworkTopology.OutputConnectionType.NOT_CONNECTED);

					network = new EchoStateNetwork(networkTopology, this.dataSet);
					break;

			}
			network.buildNetwork();


			super.setNumberOfVariables(
					network.getNumberOfVariableWeights(this.updateInternalLayerWeights));
			super.setNumberOfObjectives(1);
			super.setNumberOfConstraints(0);
			super.setProblemName("Echo state network to solve regression problem of BP");

			List<VariableDefinition> variableDefinitionList = new ArrayList<VariableDefinition>();
			for (int var = 0; var < getNumberOfVariables(); var++) {
				VariableDefinition definition = new VariableDefinition("weight" + var, -20.0, 20.0);
				variableDefinitionList.add(definition);
			}
			super.setVariableDefinition(variableDefinitionList);

	//		try {
	//			super.solutionGenerator_ = new RealSolutionType(this);
	//		} catch (ClassNotFoundException e) {
	//
	//		}

			System.out.println(getProblemName());
			System.out.println("Samples size = " + this.dataSet.getTrainingSet().size());
			System.out.println("Network - Number of inputs (considering bias as an input)= " + numInputs);
			System.out.println("Network - Number of neurons in the RES = " + numResNodes);
			System.out.println("Network - Number of neurons in the OUT Layer = " + numOutputs);
			System.out.println("Network - Number of decision variables (weights)  = "
					+ getNumberOfVariables()
					+ "(considering bias as an 'always there' input of value '1' "
					+ "which has its own a weight for each connection)");

		} catch (EnduranceException ee){
			ee.printStackTrace();
		}
	}
	public void evaluate(Solution solution) throws EnduranceException {
		
		VariableValue[] decisionVariables = solution.getDecisionVariablesArray();
		double[] weights = new double[decisionVariables.length];
		
		for (int var = 0; var < getNumberOfVariables(); var++) {
			weights[var] = decisionVariables[var].getValue();
			//System.out.print(weights[var] + ";");
		}
		//System.out.println("");
		
		network.setNetworkWeights(weights, this.updateInternalLayerWeights);
		
		//Evaluate epoch
		double epochError = 0;
		double instanceError = 0;
		List<ProblemSample> samples = this.dataSet.getTrainingSet();
		ProblemSample sample = null;
		double[] outputs = null;
		
		for (int instanceIndex = 0; instanceIndex < samples.size(); instanceIndex++) {
			
			outputs = network.forwardPropagateSignal(
					instanceIndex,
					samples.get(instanceIndex));

			instanceError = this.dataSet.calculateErrorInstance(
					outputs,
					samples.get(instanceIndex).getOutputs());

			epochError += instanceError;
		}
		
		epochError = epochError / samples.size();
		
		
//		System.out.println("Evaluate Solution EQM = " + eqm);
//		System.out.printf("Individual evaluation = %.8f \n", emq);
		solution.setObjective(0, epochError);
		solution.setFitness(epochError);
	}
	
	public MetaHeuristicsNetwork getNetwork() {
		return this.network;
	}
	
	public ProblemDataSet getDataSet() {
		return this.dataSet;
	}

	public InternalLayerType getInternalLayerType() {
		return internalLayerType;
	}


    public void testProcess(Boolean updateInternalLayerWeights) {
        Solution solution = null;
        try {

            // solution = createSolutionFromCurrentWeightArray(problem,
            // problem.getNetwork().getOutputNeurons());

            evaluate(solution);
            System.out.println("Fitness:" + solution.getObjective(0));
            System.out.println("------------------------");
            for (int i = 0; i < 5; i++) {
                solution = createSolutionRandomly(updateInternalLayerWeights);
                evaluate(solution);
                System.out.println("Fitness:" + solution.getObjective(0));
                System.out.println("\n");
            }

        } catch (EnduranceException jmException) {
            jmException.printStackTrace();
        }
    }

    public Solution createSolutionRandomly(Boolean updateInternalLayerWeights) {

        Solution solution = new Solution();
        VariableValue[] decisionVariables = solution.getDecisionVariablesArray();

        for (int i = 0; i < decisionVariables.length; i++) {
            decisionVariables[i].setValue(Math.random() * 2 - 1);

        }
        // -------
        StringBuffer stbff = new StringBuffer();
        stbff.append("Solution:");
        for (int i = 0; i < decisionVariables.length; i++) {
            stbff.append(decisionVariables[i]);
            stbff.append("\t");
        }
        System.out.println(stbff.toString());
        // ------


        return solution;
    }
}
