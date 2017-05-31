package research.mpl.backend.smart.metaheuristics.network;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Contains information such as connectivity matrix, number of neurons, etc.
 * @author Marilia
 *
 */
public class NetworkTopology {
	
	/** Indicates how the input layer is connected to the target layer*/
	public enum InputConnectionType {
		FULL_CONNECTED, RANDOMLY_CONNECTED, NOT_CONNECTED
	} 
	
	/** Indicates how the reservoir layer is connected to the target layer*/
	public enum ReservoirConnectionType {
		FULL_CONNECTED, RANDOMLY_CONNECTED, NOT_CONNECTED
	} 
	
	/** Indicates how the output layer is connected to the target layer*/
	public enum OutputConnectionType {
		FULL_CONNECTED, RANDOMLY_CONNECTED, NOT_CONNECTED
	}

    private int numInputs;
    private int numResNodes;
    private int numOutputs;

    private Random random;

    private int[][] reservoirConnectionMatrix;
    private int[][] outputConnectionMatrix;
    
	public NetworkTopology(int inputNodes, int reservoirNodes, int outputNodes) {

		this.numInputs = inputNodes;
        this.numResNodes = reservoirNodes;
        this.numOutputs = outputNodes;
        this.random = new Random(System.currentTimeMillis()); 

	}
	
	public void createReservoirConnectionMatrix(
            InputConnectionType inputConnectionType,
            ReservoirConnectionType reservoirConnectionType,
            OutputConnectionType outputConnectionType) {

        // the total possible number of inputs for each neuron
		int possibleNumberOfInputs = this.numInputs + this.numResNodes + this.numOutputs;

		this.reservoirConnectionMatrix = generateConnectionMatrixRandomlyConnected(
                                        numResNodes,
                                        possibleNumberOfInputs,
                                        inputConnectionType,
                                        reservoirConnectionType,
                                        outputConnectionType);
	}
	
	public void createOutputConnectionMatrix(
            InputConnectionType inputConnectionType,
			ReservoirConnectionType reservoirConnectionType,
			OutputConnectionType outputConnectionType) {

		// the total possible number of inputs for each neuron
		int possibleNumberOfInputs = this.numInputs + this.numResNodes + this.numOutputs;
		this.outputConnectionMatrix = generateConnectionMatrixRandomlyConnected(
										numOutputs,
										possibleNumberOfInputs,
										inputConnectionType,
										reservoirConnectionType,
										outputConnectionType);
	}

    public void createMLPConnectionMatrix(int[] numberOfNeuronsPerLayer) {
		
		int numberOfLayers = numberOfNeuronsPerLayer.length;
				
		int[][] neuronIndexesForEachLayer = new int [numberOfLayers][2];
		int index = 0;
		for (int i = 0; i < numberOfLayers; i++) {

            // initial index of layer i
			neuronIndexesForEachLayer[i][0] = index;

            // final index of layer i
			neuronIndexesForEachLayer[i][1] = index + numberOfNeuronsPerLayer[i] - 1;
			
			index = index + numberOfNeuronsPerLayer[i];
		}

        // the total possible number of inputs for each neuron
		int possibleNumberOfInputs = this.numInputs + this.numResNodes + this.numOutputs;
		this.reservoirConnectionMatrix = generateConnectionMatrixInLayers(
                                            numResNodes,
                                            possibleNumberOfInputs,
                                            neuronIndexesForEachLayer);
	}
	
	
	public int[][] generateConnectionMatrixRandomlyConnected(
            int numOfNeurons,
            int possibleNumberOfInputsForEachNeuron,
            InputConnectionType inputConnectionType,
            ReservoirConnectionType reservoirConnectionType,
            OutputConnectionType outputConnectionType) {
		
		int[][] connectionMatrix = new int[numOfNeurons][possibleNumberOfInputsForEachNeuron];
		
		
		// determine the existence of INPUT synapses for each neuron i
		for (int i = 0; i < numOfNeurons; i++) { 
			
			switch(inputConnectionType){
				case FULL_CONNECTED: 
					for (int j = 0; j < this.numInputs; j++) {
                        // synapse exists from "input" j to "neuron" i
						connectionMatrix[i][j] = 1;
					}
					break;
				case NOT_CONNECTED:
                    // synapse exists from "input" 0 to any "neuron" i, because input 0 is the bias
					connectionMatrix[i][0] = 1;
					for (int j = 1; j < this.numInputs; j++) {
                        //synapse doesn't exist from "input" j to "neuron" i
						connectionMatrix[i][j] = 0;
					}
					break;
				case RANDOMLY_CONNECTED:
                    // synapse exists from "input" 0 to any "neuron" i, because input 0 is the bias
					connectionMatrix[i][0] = 1;
					for (int j = 1; j < this.numInputs; j++) {
                        // 0 or 1 -> MAYBE synapse exists from "input" j to "neuron" i
						connectionMatrix[i][j] = this.random.nextInt(2);
					}
					break;
			}
			
			switch(reservoirConnectionType){
				case FULL_CONNECTED: 
					for (int j = this.numInputs; j < this.numInputs + this.numResNodes; j++) {
                        // synapse exists from "reservoir neuron" j to "neuron" i
						connectionMatrix[i][j] = 1;
					}
					break;
				case NOT_CONNECTED: 
					for (int j = this.numInputs; j < this.numInputs + this.numResNodes; j++) {
                        //synapse doesn't exist from "input" j to "neuron" i
						connectionMatrix[i][j] = 0;
					}
					break;
				case RANDOMLY_CONNECTED: 
					for (int j = this.numInputs; j < this.numInputs + this.numResNodes; j++) {
                        // 0 or 1 -> MAYBE synapse exists from "reservoir neuron" j to "neuron" i
						connectionMatrix[i][j] = this.random.nextInt(2);
					}
					break;
			}
			
			switch(outputConnectionType){
				case FULL_CONNECTED: 
					for (int j = this.numInputs + this.numResNodes; j < possibleNumberOfInputsForEachNeuron; j++) {
                        // synapse exists from "output neuron" j to "neuron" i
						connectionMatrix[i][j] = 1;
					}
					break;
				case NOT_CONNECTED: // There is no "back connection"
					for (int j = this.numInputs + this.numResNodes; j < possibleNumberOfInputsForEachNeuron; j++) {
                        //synapse doesn't exist from "input" j to "neuron" i
						connectionMatrix[i][j] = 0;
					}
					break;
				case RANDOMLY_CONNECTED: 
					for (int j = this.numInputs + this.numResNodes; j < possibleNumberOfInputsForEachNeuron; j++) {
                        // 0 or 1 -> MAYBE synapse exists from "output neuron" j to "neuron" i
						connectionMatrix[i][j] = this.random.nextInt(2);
					}
					break;
			}
			
		}
		
		
		return connectionMatrix;
	}

	/**
	 * 
	 * @param numOfNeurons
	 * @param possibleNumberOfInputsForEachNeuron
	 * @param neuronIndexesForEachLayer (For example, Layer 0 - starts in neuron "0" and ends in neuron "5", Layer 1 - starts in neuron "6" and ends in neuron "9")
	 * @return
	 */
	public int[][] generateConnectionMatrixInLayers(
            int numOfNeurons,
            int possibleNumberOfInputsForEachNeuron,
            int[][] neuronIndexesForEachLayer){
		
		int[][] connectionMatrix = new int[numOfNeurons][possibleNumberOfInputsForEachNeuron];
		
		// determine the existence of INPUT synapses for each neuron i
		for (int i = 0; i < numOfNeurons; i++) {

			// If neuron "i" is in the First Layer
			if (i >= neuronIndexesForEachLayer[0][0] && i <= neuronIndexesForEachLayer[0][1]) {
				for (int j = 0; j < this.numInputs; j++) {
                    // synapse exists from "input" j to "neuron" i
					connectionMatrix[i][j] = 1;
				}
				for (int j = this.numInputs; j < possibleNumberOfInputsForEachNeuron; j++) {
                    // synapse doesn't exist from any other neuron j to "neuron" i
					connectionMatrix[i][j] = 0;
				}
			}

			// If neuron "i" is in any other hidden layer
			for (int k = 1; k < neuronIndexesForEachLayer.length; k++) {

				if (i >= neuronIndexesForEachLayer[k][0] && i <= neuronIndexesForEachLayer[k][1]) {

					for (int j = 0; j < this.numInputs; j++) {
                        // synapse doesn't exist from "input" j to "neuron" i
						connectionMatrix[i][j] = 0;
					}

					for (int j = this.numInputs; j < neuronIndexesForEachLayer[k][0]; j++) {
                        // synapse doesn't exist from "input" j to "neuron" i
						connectionMatrix[i][j] = 0;
					}

					for (int j = neuronIndexesForEachLayer[k][0]; j <= neuronIndexesForEachLayer[k][1]; j++) {
                        // synapse doesn't exist from "reservoir neuron" j to "neuron" i
						connectionMatrix[i][j] = 1;
					}

					for (int j = 1 + neuronIndexesForEachLayer[k][1]; j <= possibleNumberOfInputsForEachNeuron; j++) {
                        // synapse doesn't exist from "reservoir neuron" j to "neuron" i
						connectionMatrix[i][j] = 0;
					}
				}

			}
		}
		
		
		return connectionMatrix;
	}

	private int[] getSourcesFromInputLayer(int neuronIndex, int[][] layerConnectionMatrix){
		
		int counter = 0;
		int ini = 0;
		int total = this.numInputs;
		
		for (int synapseIndex = ini; synapseIndex < total; synapseIndex++) {
			if (layerConnectionMatrix[neuronIndex][synapseIndex] == 1){
				counter++;
			}
		}
		
		int[] sources = new int[counter];
		
		if(sources.length != 0) {
			for (int sourceIndex = ini, i = 0; sourceIndex < total; sourceIndex++) {
				
				if (layerConnectionMatrix[neuronIndex][sourceIndex] == 1){
					sources[i] = sourceIndex;
					i++;
				}
				
			}
		}
		return sources;
	}
	
	private int[] getSourcesFromReservoirLayer(int neuronIndex, int[][] layerConnectionMatrix){
		
		int counter = 0;
		int ini = this.numInputs;
		int total = this.numInputs + this.numResNodes;
		
		for (int synapseIndex = ini; synapseIndex < total; synapseIndex++) {
			if (layerConnectionMatrix[neuronIndex][synapseIndex] == 1){
				counter++;
			}
		}
		
		int[] sources = new int[counter];
		
		if(sources.length != 0) {
			for (int sourceIndex = ini, i = 0; sourceIndex < total; sourceIndex++) {
				if (layerConnectionMatrix[neuronIndex][sourceIndex] == 1){
					sources[i] = sourceIndex - ini; //index of the neuron in the reservoir layer array
					i++;
				}
			}
		}
		return sources;
	}

	private int[] getSourcesFromOutputLayer(int neuronIndex, int[][] layerConnectionMatrix){
		
		int counter = 0;
		int ini = this.numInputs + this.numResNodes;
		int total = this.numInputs + this.numResNodes + this.numOutputs;
		
		for (int synapseIndex = ini; synapseIndex < total; synapseIndex++) {
			if (layerConnectionMatrix[neuronIndex][synapseIndex] == 1){
				counter++;
			}
		}
		
		int[] sources = new int[counter];
		
		if(sources.length != 0) {
			for (int sourceIndex = ini, i = 0; sourceIndex < total; sourceIndex++) {
				if (layerConnectionMatrix[neuronIndex][sourceIndex] == 1){
					sources[i] = sourceIndex - ini; //index of the neuron in the output layer array
					i++;
				}
			}
		}
		return sources;
	}

    public int[] getSourcesFromInputLayerToReservoirNeuron(int neuronIndex){
		return getSourcesFromInputLayer(neuronIndex, this.reservoirConnectionMatrix);
	}
	
	public int[] getSourcesFromReservoirLayerToReservoirNeuron(int neuronIndex){ // getSolutionAt recurrent connection sources
		return getSourcesFromReservoirLayer(neuronIndex, this.reservoirConnectionMatrix);
	}
	
	public int[] getSourcesFromOutputLayerToReservoirNeuron(int neuronIndex){
		return getSourcesFromOutputLayer(neuronIndex, this.reservoirConnectionMatrix);
	}
	

	public int[] getSourcesFromInputLayerToOutputNeuron(int neuronIndex){
		return getSourcesFromInputLayer(neuronIndex, this.outputConnectionMatrix);
	}
	
	
	public int[] getSourcesFromReservoirLayerToOutputNeuron(int neuronIndex){
		return getSourcesFromReservoirLayer(neuronIndex, this.outputConnectionMatrix);
	}
	
	public int[] getSourcesFromOutputLayerToOutputNeuron(int neuronIndex){ // getSolutionAt recurrent connection sources
		return getSourcesFromOutputLayer(neuronIndex, this.outputConnectionMatrix);
		
	}

	public int getNumInputs() {
		return numInputs;
	}

	public void setNumInputs(int numInputs) {
		this.numInputs = numInputs;
	}

	public int getNumResNodes() {
		return numResNodes;
	}

	public void setNumResNodes(int numResNodes) {
		this.numResNodes = numResNodes;
	}

	public int getNumOutputs() {
		return numOutputs;
	}

	public void setNumOutputs(int numOutputs) {
		this.numOutputs = numOutputs;
	}
	
	public String reservoirLayerToString() {
		StringBuffer strbff = new StringBuffer();
		String lineSeparator = System.getProperty("line.separator");
		
		strbff.append("WR;");
		for (int j = 0; j < reservoirConnectionMatrix[0].length; j++) {
			strbff.append(j);
			strbff.append(";");
		}
		
		strbff.append(lineSeparator);
		for (int i = 0; i < reservoirConnectionMatrix.length; i++) {
			strbff.append(this.numInputs + i);
			strbff.append(";");
			for (int j = 0; j < reservoirConnectionMatrix[i].length; j++) {
				strbff.append(reservoirConnectionMatrix[i][j]);
				strbff.append(";");
			}
			strbff.append(lineSeparator);
		}
		return strbff.toString();
	}
	
	public String outputLayerToString() {
		StringBuffer strbff = new StringBuffer();
		String lineSeparator = System.getProperty("line.separator");
		
		strbff.append("WO;");
		for (int j = 0; j < outputConnectionMatrix[0].length; j++) {
			strbff.append(j);
			strbff.append(";");
		}
		
		strbff.append(lineSeparator);
		for (int i = 0; i < outputConnectionMatrix.length; i++) {
			strbff.append(this.numInputs + this.numResNodes + i);
			strbff.append(";");
			for (int j = 0; j < outputConnectionMatrix[i].length; j++) {
				strbff.append(outputConnectionMatrix[i][j]);
				strbff.append(";");
			}
			strbff.append(lineSeparator);
		}
		return strbff.toString();
	}
	
	 public void printTopologyToFile(String path){
		    try {
		      /* Open the file */
		      FileOutputStream fos   = new FileOutputStream(path)     ;
		      OutputStreamWriter osw = new OutputStreamWriter(fos)    ;
		      BufferedWriter bw      = new BufferedWriter(osw)        ;
		                        
		      String reservoirStructure = reservoirLayerToString();
		      
		      String outputLayerStructure = outputLayerToString();
		    		      
		      bw.write(reservoirStructure);
		      
		      bw.newLine();
		      bw.newLine();
		      
		      bw.write(outputLayerStructure);
		      
		      bw.newLine();
		      bw.newLine();
		      
		      
		      /* Close the file */
		      bw.close();
		    }catch (IOException e) {
		      e.printStackTrace();
		    }
		  } 

}
