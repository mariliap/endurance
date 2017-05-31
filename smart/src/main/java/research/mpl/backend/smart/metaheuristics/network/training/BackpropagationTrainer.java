package research.mpl.backend.smart.metaheuristics.network.training;

import java.util.List;

import research.mpl.backend.smart.metaheuristics.network.MultiLayerPerceptronNetwork;
import research.mpl.backend.smart.metaheuristics.network.ProblemDataSet;
import research.mpl.backend.smart.metaheuristics.network.ProblemSample;
import research.mpl.backend.smart.metaheuristics.network.node.MultiLayerPerceptronNeuron;
import research.mpl.backend.smart.metaheuristics.network.node.NodeConnection;
import research.mpl.backend.smart.problems.networkBPRegression.ESNWeightsOptimizationProblem;
import research.mpl.backend.smart.util.EnduranceException;

public class BackpropagationTrainer extends TrainerRunnable {

	public BackpropagationTrainer(ESNWeightsOptimizationProblem problem, String resultsPath) {
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

		//Parameters
		int maxNumberOfEpochs = 10001;
		double learningRate = 0.05;		
		
		String resultsPath = getResultsPath();
		ESNWeightsOptimizationProblem esnProblem = (ESNWeightsOptimizationProblem) getProblem();

		ProblemDataSet dataset = esnProblem.getDataSet();
        MultiLayerPerceptronNetwork network = (MultiLayerPerceptronNetwork) esnProblem.getNetwork();
		
		boolean stopCondintionSatisfied = false;

		int currentEpoch = 0;
		double trainingError = 99999999; // mean square error
		double testError = 99999999; // mean square error
		double validationError = 99999999;
		double previousValidationError = 99999999;
		
		StringBuffer resultsBuffer = new StringBuffer();

		while (!stopCondintionSatisfied && currentEpoch < maxNumberOfEpochs) {

			trainingError = processTrainingEpoch(network, dataset, learningRate);
			learningRate *= (maxNumberOfEpochs - 1.0) / maxNumberOfEpochs;
			//validationError = validation(dataset, hiddenLayerNeurons, outputNeurons);

			if (validationError > previousValidationError) {

				stopCondintionSatisfied = true;

			}
			previousValidationError = validationError;

			if(currentEpoch%100 == 0) {
				resultsBuffer.append("Epoch: " + currentEpoch + "\tTraining Error: " + trainingError + "\n");
				System.out.println("Epoch: " + currentEpoch + "\tTraining Error: " + trainingError + "\n");// + "\tVal. Error: " + validationError);
			}
			currentEpoch++;
		}
		
		System.out.println(" \t\t TRAINING ERROR=  " + trainingError);
		System.out.println(" \t\t TEST ERROR=  " + testError);
		
		resultsBuffer.append(" \t\t TRAINING ERROR=  ");
		resultsBuffer.append(trainingError);
		resultsBuffer.append("\n");
		resultsBuffer.append(" \t\t TEST ERROR=  ");
		resultsBuffer.append(testError);
		resultsBuffer.append("\n");
		
		//eqmTeste = teste(dataset, hiddenLayerNeurons, outputNeurons);
		//System.out.println(" \t\t EQM_TESTE=  " + testError);

//		Solution solution = createSolutionFromCurrentWeightArray(esnProblem, outputNeurons);
//		esnProblem.evaluate(solution);// evaluate 1 epoch


//		System.out.println("\n\nObjectives values have been writen to file BestSolutionQuality. BEST FITNESS = " + solution.getFitness());
//		printContentToFile(solution.getFitness() + "", resultsPath + "/BestSolutionQuality.csv");
//		System.out.println("Variables values have been writen to file BestSolution");
		// printContentToFile(strBff.toString(), resultsPath + "/BestSolution.csv");

		System.out.println("THE END");
		resultsBuffer.append("THE END");
		
		printContentToFile(resultsBuffer.toString(), resultsPath);
	}

	public double processTrainingEpoch(MultiLayerPerceptronNetwork network,
									   ProblemDataSet dataset,
									   double learningRate) {

		List<ProblemSample> trainingData = dataset.getTrainingSet();
		double mseEpoch = 0; // EQM para cada epoca

		for (int instanceIndex = 0; instanceIndex < trainingData.size(); instanceIndex++) {

			ProblemSample sample = dataset.getTrainingSet().get(instanceIndex);

			// FORWARD PROPAGATION PHASE (TRAINING) -------------------------------------------
			double[] outputs = network.forwardPropagateSignal(instanceIndex, sample);
			
			// Calculates ERROR for this instance
			double err = dataset.calculateErrorInstance(outputs, sample.getOutputs());
			mseEpoch += err;
			
			// BACK PROPAGATION PHASE (TRAINING) -----------------------------------------------

            MultiLayerPerceptronNeuron[] outputNeurons = network.getOutputNeurons();
			for (int neuronIdx = 0; neuronIdx < outputNeurons.length; neuronIdx++) {

				//Calculate and adds the output error
				double error = dataset.calculateUnitOutputError(
						outputs[neuronIdx], sample.getOutputValue(neuronIdx));
				outputNeurons[neuronIdx].setOutputError(error);

			}
			List<List<MultiLayerPerceptronNeuron>> layers = network.getLayers();
			MultiLayerPerceptronNeuron[] previousLayer = null;
			MultiLayerPerceptronNeuron[] currentLayer = null;
			
			for (int i = layers.size() - 1; i >= 0; i--) {
				
				List<MultiLayerPerceptronNeuron> layer = layers.get(i);
				currentLayer = new MultiLayerPerceptronNeuron[layer.size()];
				currentLayer = layer.toArray(currentLayer);
				
				propagateBackward(instanceIndex,
                                  currentLayer,
                                  previousLayer,
                                  learningRate);
				
				previousLayer = currentLayer;
				
			}
		} 

		mseEpoch = mseEpoch / trainingData.size();
//		System.out.println("mseEpoch: " + mseEpoch);
		return mseEpoch;

	}
	
	private void propagateBackward(
            int instanceIndex,
            MultiLayerPerceptronNeuron[] currentLayer,
            MultiLayerPerceptronNeuron[] previousLayer,
            double learningRate) {

        MultiLayerPerceptronNeuron neuronI = null;
        MultiLayerPerceptronNeuron neuronK = null;
        double error = 0;
        double weightKI = 0;
        double weightsVariation = 0;
		for (int i = 0; i < currentLayer.length; i++) {

            neuronI = currentLayer[i];
			error = 0;
			weightKI = 0;
            weightsVariation = 0;
			
			//Calculate neuron output error
			if (previousLayer == null){
				error = currentLayer[i].getOutputError();
			} else {

                for(NodeConnection nodeConnectionOut : currentLayer[i].getConnectionsOut()){
                    neuronK = (MultiLayerPerceptronNeuron) nodeConnectionOut.getNodeOut();
                    weightKI = nodeConnectionOut.getValue();
                    error = error + neuronK.getGradient() * weightKI;
                }
			}
			
			//Calculate gradient
			double activivationFuntionDerivative = neuronI.getOutputedValueOfInstance(instanceIndex)
					* (1 - neuronI.getOutputedValueOfInstance(instanceIndex));

            neuronI.setGradient(activivationFuntionDerivative * error);

            //---------------------------

            for(NodeConnection nodeConnectionIn : neuronI.getConnectionsIN()){
                neuronK = (MultiLayerPerceptronNeuron) nodeConnectionIn.getNodeIn();
                weightsVariation = learningRate
                        * neuronI.getGradient()
                        * neuronK.getOutputedValueOfInstance(instanceIndex);
                double updatedWeight = nodeConnectionIn.getValue() + weightsVariation;
                nodeConnectionIn.setValue(updatedWeight);
            }

		}
	}


	

	//
	// public static Solution createSolutionFromCurrentWeightArray(ESNWeightsOptimizationProblem problem, OutputEchoStateNeuron[] outputNeurons) {
	//
	// Solution solution = null;
	// try {
	// solution = new Solution(problem);
	// VariableDefinition[] decisionVariables = solution.getDecisionVariablesArray();
	//
	//
	// int index = 0;
	//
	// for (int i = 0; i < outputNeurons.length; i++) {
	// // decisionVariables[index].setValue(outputNeurons[i].getBias());
	// // index++;
	//
	// for (int j = 0; j < outputNeurons[i].getInputLayerSynapsesWeights().length; j++) {
	// double value = outputNeurons[i].getInputLayerSynapseWeight(j);
	// decisionVariables[index].setValue(value);
	// index++;
	// }
	//
	// for (int j = 0; j < outputNeurons[i].getReservoirSynapsesWeights().length; j++) {
	// double value = outputNeurons[i].getReservoirSynapseWeight(j);
	// decisionVariables[index].setValue(value);
	// index++;
	// }
	//
	// for (int j = 0; j < outputNeurons[i].getRecurrentSynapsesWeights().length; j++) {
	// double value = outputNeurons[i].getRecurrentSynapseWeight(j);
	// decisionVariables[index].setValue(value);
	// index++;
	// }
	// }
	// //-------
	// StringBuffer stbff = new StringBuffer();
	// stbff.append("Solution:");
	// for (int i = 0; i < decisionVariables.length; i++) {
	// stbff.append(decisionVariables[i]);
	// stbff.append("\t");
	// }
	// System.out.println(stbff.toString());
	// //------
	// } catch (EnduranceException jmException) {
	// jmException.printStackTrace();
	// } catch (ClassNotFoundException classNotFoundException) {
	// classNotFoundException.printStackTrace();
	// }
	//
	// return solution;
	// }

}
