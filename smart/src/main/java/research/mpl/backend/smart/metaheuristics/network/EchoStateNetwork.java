package research.mpl.backend.smart.metaheuristics.network;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.Random;

// import org.apache.log4j.Logger;

import research.mpl.backend.smart.metaheuristics.network.node.OutputEchoStateNeuron;
import research.mpl.backend.smart.metaheuristics.network.node.ReservoirEchoStateNeuron;
import research.mpl.backend.smart.util.EnduranceException;

/**
 * 
 * @author Marilia
 */
public class EchoStateNetwork extends MetaHeuristicsNetwork{

//	private static Logger logger = Logger.getLogger(EchoStateNetwork.class.getName());

	private double[] inputs;

	private ReservoirEchoStateNeuron[] reservoirNeurons;

	private OutputEchoStateNeuron[] outputNeurons;

	private NetworkTopology networkTopology;

	/** **/
	private int[][] reservoirWeightsMatrix;

	/** **/
	private int[][] outputsWeightsMatrix;


	public EchoStateNetwork(NetworkTopology networkTopology, ProblemDataSet problemDataSet) {

		this.networkTopology = networkTopology;

		this.inputs = new double[this.networkTopology.getNumInputs()];
		this.reservoirNeurons = new ReservoirEchoStateNeuron[this.networkTopology.getNumResNodes()];
		this.outputNeurons = new OutputEchoStateNeuron[this.networkTopology.getNumOutputs()];

	}

    @Override
	public void buildNetwork() throws EnduranceException {

		Random random = new Random(System.currentTimeMillis());

		long initialId = (long)this.networkTopology.getNumInputs(); // If network has 8 inputs (whose ids are 0 to 7),
		// neurons
		// id should start in 8.

		for (int i = 0; i < this.reservoirNeurons.length; i++) {

			int[] inputSynapsesSources = this.networkTopology.getSourcesFromInputLayerToReservoirNeuron(i);//obs: it already considers bias as an input...
			int[] recurrentSynapsesSources = this.networkTopology.getSourcesFromReservoirLayerToReservoirNeuron(i);
			int[] backSynapsesSources = this.networkTopology.getSourcesFromOutputLayerToReservoirNeuron(i);

			if (this.reservoirNeurons[i] == null) {

				this.reservoirNeurons[i] = new ReservoirEchoStateNeuron(initialId, inputSynapsesSources, recurrentSynapsesSources, backSynapsesSources,
						ActivationFunctionType.SIGMOIDE, Math.random());
				this.reservoirNeurons[i].initializeWeightsRandomly(random);

				initialId++;
			}
		}

		for (int i = 0; i < this.outputNeurons.length; i++) {

			int[] inputSynapsesSources = this.networkTopology.getSourcesFromInputLayerToOutputNeuron(i);
			int[] reservoirSynapsesSources = this.networkTopology.getSourcesFromReservoirLayerToOutputNeuron(i);
			int[] recurrentSynapsesSources = this.networkTopology.getSourcesFromOutputLayerToOutputNeuron(i);

			if (outputNeurons[i] == null) {
				outputNeurons[i] = new OutputEchoStateNeuron(initialId, inputSynapsesSources, recurrentSynapsesSources, reservoirSynapsesSources,
						ActivationFunctionType.SIGMOIDE, Math.random());
				this.outputNeurons[i].initializeWeightsRandomly(random);

				initialId++;
			}
		}
	}

	public String toString() {

		StringBuffer strbff = new StringBuffer();
		strbff.append("");

		for (int i = 0; i < this.reservoirNeurons.length; i++) {
			strbff.append(this.reservoirNeurons[i].toString());
			strbff.append("\n");
		}
		strbff.append("\n");
		for (int i = 0; i < this.outputNeurons.length; i++) {
			strbff.append(this.outputNeurons[i].toString());
			strbff.append("\n");
		}

		return strbff.toString();
	}

	private Integer getSourceIndex(int[] sourcesArray, int j) {
		Integer sourceIndex = null;
		for (int index = 0; index < sourcesArray.length; index++) {
			if (sourcesArray[index] == j) {
				sourceIndex = new Integer(index);
			}
		}
		return sourceIndex;
	}

    @Override
	public double[] forwardPropagateSignal(int t, ProblemSample instance) {
				
		this.inputs = new double[instance.getNumberOfInputs() + 1];
		this.inputs[0] = 1; // set "bias" input value, which is "1"
		for (int i = 0; i < instance.getNumberOfInputs(); i++) {
			this.inputs[i + 1] = instance.getInputValue(i);
		}

		for (ReservoirEchoStateNeuron neuron : reservoirNeurons) {
			neuron.process(t, inputs, reservoirNeurons, outputNeurons);
		}

		double[] outputs = new double[outputNeurons.length];
		for (int i = 0; i < outputNeurons.length; i++) {
			outputNeurons[i].process(t, inputs, reservoirNeurons, outputNeurons);
			outputs[i] = outputNeurons[i].getOutputedValueOfInstance(t);
		}

		return outputs;
	}

    @Override
	public void setNetworkWeights(double[] weights, boolean updateReservoirLayer) {

		int synapsesCounter = 0;

		if (updateReservoirLayer) {
			// Update reservoir neurons' weights
			for (int i = 0; i < this.reservoirNeurons.length; i++) {

				for (int j = 0; j < this.reservoirNeurons[i].getInputLayerSynapsesWeights().length; j++) {
					this.reservoirNeurons[i].setInputLayerSynapseWeight(weights[synapsesCounter], j);
					synapsesCounter++;
				}

				for (int j = 0; j < this.reservoirNeurons[i].getRecurrentSynapsesWeights().length; j++) {
					this.reservoirNeurons[i].setRecurrentSynapseWeight(weights[synapsesCounter], j);
					synapsesCounter++;
				}

				for (int j = 0; j < this.reservoirNeurons[i].getBackSynapsesWeights().length; j++) {
					this.reservoirNeurons[i].setBackSynapseWeight(weights[synapsesCounter], j);
					synapsesCounter++;
				}

			}
		}
		// Update output neurons' weights
		for (int i = 0; i < outputNeurons.length; i++) {

			// this.outputNeurons[i].setBias(weights[biasIndex]); Bias is part of the the input now...
			// inputSynapsesIndex = biasIndex + 1;

			for (int j = 0; j < this.outputNeurons[i].getInputLayerSynapsesWeights().length; j++) {
				this.outputNeurons[i].setInputLayerSynapseWeight(weights[synapsesCounter], j);
				synapsesCounter++;
			}

			for (int j = 0; j < this.outputNeurons[i].getReservoirSynapsesWeights().length; j++) {
				this.outputNeurons[i].setReservoirSynapseWeight(weights[synapsesCounter], j);
				synapsesCounter++;
			}

			for (int j = 0; j < this.outputNeurons[i].getRecurrentSynapsesWeights().length; j++) {
				this.outputNeurons[i].setRecurrentSynapseWeight(weights[synapsesCounter], j);
				synapsesCounter++;
			}

			// biasIndex = outputNeurons[i].getNumberOfWeights();

		}

	}

    @Override
	public int getNumberOfVariableWeights(boolean intermedateLayerUpdatable){

		int numOfDecisionVariables = 0;
		for (int i = 0; i < outputNeurons.length; i++) {
			numOfDecisionVariables += outputNeurons[i].getNumberOfWeights();
		}

		if (intermedateLayerUpdatable) {
			for (int i = 0; i < reservoirNeurons.length; i++) {
				numOfDecisionVariables += reservoirNeurons[i].getNumberOfWeights();
			}
		}

		return numOfDecisionVariables;
	}

	public ReservoirEchoStateNeuron[] getReservoirNeurons() {
		return reservoirNeurons;
	}

	public OutputEchoStateNeuron[] getOutputNeurons() {
		return outputNeurons;
	}

    @Override
	public NetworkTopology getNetworkTopology() {
		return networkTopology;
	}

    @Override
	public void printNetworkTopologyToFile(String path) {
		this.networkTopology.printTopologyToFile(path);
	}

    @Override
	public void printNetworkWeightsToFile(String path) {

		DecimalFormat decimalFormat = new DecimalFormat("##0.000000000000000;-##0.000000000000000");

		StringBuffer strbff = new StringBuffer();
		StringBuffer columnline = new StringBuffer();
		String lineSeparator = System.getProperty("line.separator");

		strbff.append("WR;");
		//strbff.append("bias;");

		for (int j = 0; j < this.inputs.length; j++) {
			strbff.append(j);
			strbff.append(";");
		}
		for (int j = 0; j < this.reservoirNeurons.length; j++) {
			strbff.append(this.reservoirNeurons[j].getId());
			strbff.append(";");
		}
		for (int j = 0; j < this.outputNeurons.length; j++) {
			strbff.append(this.outputNeurons[j].getId());
			strbff.append(";");
		}

		strbff.append(lineSeparator);
		strbff.append(columnline);
		strbff.append(lineSeparator);

		for (int i = 0; i < this.reservoirNeurons.length; i++) {
			strbff.append(this.reservoirNeurons[i].getId());
			strbff.append(";");

			double[] inputLayerSynapsesWeights = this.reservoirNeurons[i].getInputLayerSynapsesWeights();
			int[] inputLayerSynapsesSources = this.reservoirNeurons[i].getInputLayerSynapsesSources();
			double[] recurrentSynapsesWeights = this.reservoirNeurons[i].getRecurrentSynapsesWeights();
			int[] recurrentSynapsesSources = this.reservoirNeurons[i].getRecurrentSynapsesSources();
			double[] backSynapsesWeights = this.reservoirNeurons[i].getBackSynapsesWeights();
			int[] backSynapsesSources = this.reservoirNeurons[i].getBackSynapsesSources();

			for (int j = 0; j < this.inputs.length; j++) {
				// If neuron has "j" as an synapse source from the input layer
				Integer sourceIndex = getSourceIndex(inputLayerSynapsesSources, j);
				if (sourceIndex != null) {
					strbff.append(decimalFormat.format(inputLayerSynapsesWeights[sourceIndex]));
					strbff.append(";");
				} else {
					strbff.append(decimalFormat.format(0.0));
					strbff.append(";");
				}
			}

			for (int j = 0; j < this.reservoirNeurons.length; j++) {
				// If neuron has "neuronJ" as an synapse source from the reservoir layer
				Integer sourceIndex = getSourceIndex(recurrentSynapsesSources, j);
				if (sourceIndex != null) {
					strbff.append(decimalFormat.format(recurrentSynapsesWeights[sourceIndex]));
					strbff.append(";");
				} else {
					strbff.append(decimalFormat.format(0.0));
					strbff.append(";");
				}
			}

			for (int j = 0; j < this.outputNeurons.length; j++) {
				// If neuron has "neuronJ" as an synapse source from the output layer
				Long neuronJ = this.outputNeurons[j].getId();
				Integer sourceIndex = getSourceIndex(backSynapsesSources, j);
				if (sourceIndex != null) {
					strbff.append(decimalFormat.format(backSynapsesWeights[sourceIndex]));
					strbff.append(";");
				} else {
					strbff.append(decimalFormat.format(0.0));
					strbff.append(";");
				}
			}

			strbff.append(lineSeparator);
		}

		strbff.append(lineSeparator);
		strbff.append(lineSeparator);

		strbff.append("WO;");
		//strbff.append("bias;");
		for (int j = 0; j < this.inputs.length; j++) {
			strbff.append(j);
			strbff.append(";");
		}
		for (int j = 0; j < this.reservoirNeurons.length; j++) {
			strbff.append(this.reservoirNeurons[j].getId());
			strbff.append(";");
		}
		for (int j = 0; j < this.outputNeurons.length; j++) {
			strbff.append(this.outputNeurons[j].getId());
			strbff.append(";");
		}

		strbff.append(lineSeparator);

		for (int i = 0; i < this.outputNeurons.length; i++) {

			strbff.append(this.outputNeurons[i].getId());
			strbff.append(";");

			double[] inputLayerSynapsesWeights = this.outputNeurons[i].getInputLayerSynapsesWeights();
			int[] inputLayerSynapsesSources = this.outputNeurons[i].getInputLayerSynapsesSources();
			double[] reservoirSynapsesWeights = this.outputNeurons[i].getReservoirSynapsesWeights();
			int[] reservoirSynapsesSources = this.outputNeurons[i].getReservoirSynapsesSources();
			double[] recurrentSynapsesWeights = this.outputNeurons[i].getRecurrentSynapsesWeights();
			int[] recurrentSynapsesSources = this.outputNeurons[i].getRecurrentSynapsesSources();

			for (int j = 0; j < this.inputs.length; j++) {
				// If neuron has "j" as an synapse source from the input layer
				Integer sourceIndex = getSourceIndex(inputLayerSynapsesSources, j);
				if (sourceIndex != null) {
					strbff.append(decimalFormat.format(inputLayerSynapsesWeights[sourceIndex]));
					strbff.append(";");
				} else {
					strbff.append(decimalFormat.format(0.0));
					strbff.append(";");
				}
			}

			for (int j = 0; j < this.reservoirNeurons.length; j++) {
				// If neuron has "neuronJ" as an synapse source from the reservoir layer
				Integer sourceIndex = getSourceIndex(reservoirSynapsesSources, j);
				if (sourceIndex != null) {
					strbff.append(decimalFormat.format(reservoirSynapsesWeights[sourceIndex]));
					strbff.append(";");
				} else {
					strbff.append(decimalFormat.format(0.0));
					strbff.append(";");
				}
			}

			for (int j = 0; j < this.outputNeurons.length; j++) {
				// If neuron has "neuronJ" as an synapse source from the output layer
				Integer sourceIndex = getSourceIndex(recurrentSynapsesSources, j);
				if (sourceIndex != null) {
					strbff.append(decimalFormat.format(recurrentSynapsesWeights[sourceIndex]));
					strbff.append(";");
				} else {
					strbff.append(decimalFormat.format(0.0));
					strbff.append(";");
				}
			}

			strbff.append(lineSeparator);
		}

		strbff.append(lineSeparator);
		strbff.append(lineSeparator);

		try {
			/* Open the file */
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write(strbff.toString());
			bw.newLine();

			/* Close the file */
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
