package research.mpl.backend.smart.metaheuristics.network;

import research.mpl.backend.smart.metaheuristics.network.node.MultiLayerPerceptronNeuron;
import research.mpl.backend.smart.metaheuristics.network.node.NodeConnection;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import research.mpl.backend.smart.util.EnduranceException;
import research.mpl.backend.smart.util.random.RandomGenerator;

/**
 * 
 * @author Marilia
 */
public class MultiLayerPerceptronNetwork extends MetaHeuristicsNetwork{

	private static Logger logger = LogManager.getLogger(MultiLayerPerceptronNetwork.class.getName());

	private double[] inputs;

    private MultiLayerPerceptronNeuron[] inputNeurons;

	private MultiLayerPerceptronNeuron[] ïntermediateNeurons;

	private MultiLayerPerceptronNeuron[] outputNeurons;

	private NetworkTopology networkTopology;

    private List<List<MultiLayerPerceptronNeuron>> layers;


	public MultiLayerPerceptronNetwork(NetworkTopology networkTopology) {

		this.networkTopology = networkTopology;

		this.inputs = new double[this.networkTopology.getNumInputs()];

        this.inputNeurons = new MultiLayerPerceptronNeuron[this.networkTopology.getNumInputs()];
		this.ïntermediateNeurons = new MultiLayerPerceptronNeuron[this.networkTopology.getNumResNodes()];
		this.outputNeurons = new MultiLayerPerceptronNeuron[this.networkTopology.getNumOutputs()];

        this.layers = new ArrayList<>();
	}

    @Override
    public void buildNetwork() throws EnduranceException{
        buildStructure();
        initializeWeights();
    }

	private void buildStructure() throws EnduranceException{

		Random random = new Random(System.currentTimeMillis());

		long initialId = 0;
        int[] inputSynapsesSources = null;
        int[] reservoirSynapsesSources = null;
        int[] outputSynapsesSources = null;
        int sourceIndex = 0;

		for (int i = 0; i < this.networkTopology.getNumInputs(); i++) {
            //obs: it already considers bias as an input...
			if (this.inputNeurons[i] == null) {

				this.inputNeurons[i] = new MultiLayerPerceptronNeuron();
                this.inputNeurons[i].setId(initialId);
                this.inputNeurons[i].setActivationFuncionType(ActivationFunctionType.SIGMOIDE);
                this.inputNeurons[i].setActivationFunctionAlpha(Math.random());
                this.inputNeurons[i].setConnectionsIN(null);
                this.inputNeurons[i].setConnectionsOut(new ArrayList<NodeConnection>());
				initialId++;
			}
		}

		for (int i = 0; i < this.networkTopology.getNumResNodes(); i++) {

			if (this.ïntermediateNeurons[i] == null) {

                //obs: it already considers bias as an input...
                inputSynapsesSources =
                        this.networkTopology.getSourcesFromInputLayerToReservoirNeuron(i);

                reservoirSynapsesSources =
                        this.networkTopology.getSourcesFromReservoirLayerToReservoirNeuron(i);

                outputSynapsesSources =
                        this.networkTopology.getSourcesFromOutputLayerToReservoirNeuron(i);

                if(outputSynapsesSources.length != 0
                    || (inputSynapsesSources.length != 0
                        && reservoirSynapsesSources.length != 0)
                    || (inputSynapsesSources.length == 0
                        && reservoirSynapsesSources.length == 0
                        && outputSynapsesSources.length == 0)) {
                    throw new EnduranceException("Neuron isn't respecting layer hierarchy");
                }


                this.ïntermediateNeurons[i] = new MultiLayerPerceptronNeuron();

                this.ïntermediateNeurons[i].setId(initialId);
                this.ïntermediateNeurons[i].setActivationFuncionType(ActivationFunctionType.SIGMOIDE);
                this.ïntermediateNeurons[i].setActivationFunctionAlpha(Math.random());
                this.ïntermediateNeurons[i].setConnectionsIN(new ArrayList<>());
                this.ïntermediateNeurons[i].setConnectionsOut(new ArrayList<>());

                for (int j = 0; j < inputSynapsesSources.length; j++) {

                    NodeConnection connection = new NodeConnection();
                    sourceIndex = inputSynapsesSources[j];
                    connection.setNodeIn(this.inputNeurons[sourceIndex]);
                    connection.setNodeOut(this.ïntermediateNeurons[i]);
                    this.inputNeurons[sourceIndex].getConnectionsOut().add(connection);
                    this.ïntermediateNeurons[i].getConnectionsIN().add(connection);
                }

                for (int j = 0; j < reservoirSynapsesSources.length; j++) {

                    NodeConnection connection = new NodeConnection();
                    sourceIndex = reservoirSynapsesSources[j];

                    if(sourceIndex >= i){//todo: sourceIndex >= indexInicioLayer
                        throw new EnduranceException("Neuron isn't respecting layer hierarchy");
                    }

                    connection.setNodeIn(this.ïntermediateNeurons[sourceIndex]);
                    connection.setNodeOut(this.ïntermediateNeurons[i]);
                    this.ïntermediateNeurons[sourceIndex].getConnectionsOut().add(connection);
                    this.ïntermediateNeurons[i].getConnectionsIN().add(connection);
                }

				initialId++;
			}
		}

		for (int i = 0; i < this.outputNeurons.length; i++) {

			inputSynapsesSources =
                    this.networkTopology.getSourcesFromInputLayerToOutputNeuron(i);

			reservoirSynapsesSources =
                    this.networkTopology.getSourcesFromReservoirLayerToOutputNeuron(i);

			outputSynapsesSources =
                    this.networkTopology.getSourcesFromOutputLayerToOutputNeuron(i);

            if(outputSynapsesSources.length != 0
                    || (inputSynapsesSources.length != 0
                        && reservoirSynapsesSources.length != 0)
                    || (inputSynapsesSources.length == 0
                        && reservoirSynapsesSources.length == 0
                        && outputSynapsesSources.length == 0)) {
                throw new EnduranceException("Neuron isn't respecting layer hierarchy");
            }

			if (outputNeurons[i] == null) {

                this.outputNeurons[i] = new MultiLayerPerceptronNeuron();

                this.outputNeurons[i].setId(initialId);
                this.outputNeurons[i].setActivationFuncionType(ActivationFunctionType.SIGMOIDE);
                this.outputNeurons[i].setActivationFunctionAlpha(Math.random());
                this.outputNeurons[i].setConnectionsIN(new ArrayList<>());
                this.outputNeurons[i].setConnectionsOut(null);

                for (int j = 0; j < reservoirSynapsesSources.length; j++) {

                    NodeConnection connection = new NodeConnection();
                    sourceIndex = reservoirSynapsesSources[j];

                    connection.setNodeIn(this.ïntermediateNeurons[sourceIndex]);
                    connection.setNodeOut(this.outputNeurons[i]);
                    this.ïntermediateNeurons[i].getConnectionsOut().add(connection);
                    this.outputNeurons[i].getConnectionsIN().add(connection);
                }
				initialId++;
			}
		}

        //Define the layers of the MLP network
        this.layers.add(Arrays.asList(this.inputNeurons));

        List<MultiLayerPerceptronNeuron> newLayer = new ArrayList<>();
        int index = 0;
        while(index < this.ïntermediateNeurons.length) {

            MultiLayerPerceptronNeuron neuron = this.ïntermediateNeurons[index];
            for (NodeConnection nodeConnectionIn : neuron.getConnectionsIN()) {
                if(this.layers.get(this.layers.size()-1).contains(nodeConnectionIn.getNodeIn())){
                    newLayer.add(neuron);
                    break;
                }
            }

            if(!newLayer.contains(neuron)){
                this.layers.add(newLayer);
                newLayer = new ArrayList<>();
            } else {
                index++;
            }
        }
        this.layers.add(Arrays.asList(this.outputNeurons));
	}

    private void initializeWeights(){

        RandomGenerator randomGenerator = new RandomGenerator();

        for (MultiLayerPerceptronNeuron intermediateNeuron : this.ïntermediateNeurons) {

            for (NodeConnection nodeConnection : intermediateNeuron.getConnectionsIN()) {
                //Generate number between -1 and 1);
                nodeConnection.setValue(randomGenerator.nextDouble()*2 - 1);
            }
        }

        for (MultiLayerPerceptronNeuron outputNeuron: this.outputNeurons) {
            for (NodeConnection nodeConnection : outputNeuron.getConnectionsIN()) {
                //Generate number between -1 and 1);
                nodeConnection.setValue(randomGenerator.nextDouble()*2 - 1);
            }
        }

    }

	public String toString() {

		StringBuffer strbff = new StringBuffer();
		strbff.append("");

		for (int i = 0; i < this.ïntermediateNeurons.length; i++) {
			strbff.append(this.ïntermediateNeurons[i].toString());
			strbff.append("\n");
		}
		strbff.append("\n");
		for (int i = 0; i < this.outputNeurons.length; i++) {
			strbff.append(this.outputNeurons[i].toString());
			strbff.append("\n");
		}

		return strbff.toString();
	}


    @Override
	public double[] forwardPropagateSignal(int t, ProblemSample instance) {

        // set "bias" input value, which is "1"
		this.inputNeurons[0].setOutputedValueOfInstance(t,1);

		for (int i = 0; i < instance.getInputs().size(); i++) {
			this.inputNeurons[i + 1].setOutputedValueOfInstance(t, instance.getInputValue(i));
		}

		for (MultiLayerPerceptronNeuron neuron : ïntermediateNeurons) {
			neuron.process(t);
		}

		double[] outputs = new double[outputNeurons.length];
		for (int i = 0; i < outputNeurons.length; i++) {
			outputNeurons[i].process(t);
			outputs[i] = outputNeurons[i].getOutputedValueOfInstance(t);
		}

		return outputs;
	}

    @Override
	public void setNetworkWeights(double[] weights, boolean updateReservoirLayer) {

		int synapsesCounter = 0;

		if (updateReservoirLayer) {
			// Update reservoir neurons' weights
			for (int i = 0; i < this.ïntermediateNeurons.length; i++) {

				for (NodeConnection nodeConnection : this.ïntermediateNeurons[i].getConnectionsIN()) {
                    nodeConnection.setValue(weights[synapsesCounter]);
					synapsesCounter++;
				}
			}
		}

		// Update output neurons' weights
		for (int i = 0; i < outputNeurons.length; i++) {

            for (NodeConnection nodeConnection : this.outputNeurons[i].getConnectionsIN()) {
                nodeConnection.setValue(weights[synapsesCounter]);
                synapsesCounter++;
            }

		}

	}

	public MultiLayerPerceptronNeuron[] getÏntermediateNeurons() {
		return ïntermediateNeurons;
	}

	public MultiLayerPerceptronNeuron[] getOutputNeurons() {
		return outputNeurons;
	}

    @Override
    public int getNumberOfVariableWeights(boolean intermedateLayerUpdatable){

        int numOfDecisionVariables = 0;
        for (int i = 0; i < this.outputNeurons.length; i++) {
            numOfDecisionVariables += this.outputNeurons[i].getConnectionsIN().size();
        }

        if (intermedateLayerUpdatable) {
            for (int i = 0; i < this.ïntermediateNeurons.length; i++) {
                numOfDecisionVariables += this.ïntermediateNeurons[i].getConnectionsIN().size();
            }
        }

        return numOfDecisionVariables;
    }

    public List<List<MultiLayerPerceptronNeuron>> getLayers() {
        return layers;
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

        for (MultiLayerPerceptronNeuron inputNeuron : this.inputNeurons) {
			strbff.append(inputNeuron.getId());
			strbff.append(";");
		}
        for (MultiLayerPerceptronNeuron ïntermediateNeuron : this.ïntermediateNeurons) {
			strbff.append(ïntermediateNeuron.getId());
			strbff.append(";");
		}
        for (MultiLayerPerceptronNeuron outputNeuron : this.outputNeurons) {
			strbff.append(outputNeuron.getId());
			strbff.append(";");
		}

		strbff.append(lineSeparator);
		strbff.append(columnline);
		strbff.append(lineSeparator);

        NodeConnection nodeConnectionMatch = null;
        for (int i = 0; i < this.ïntermediateNeurons.length; i++) {

            strbff.append(this.ïntermediateNeurons[i].getId());
            strbff.append(";");


            for (MultiLayerPerceptronNeuron inputNeuron : inputNeurons) {

                nodeConnectionMatch = null;

                List<NodeConnection> connectionsIn =
                        this.ïntermediateNeurons[i].getConnectionsIN();

                for(NodeConnection nodeConnection : connectionsIn){
                    if(nodeConnection.getNodeIn().getId().equals(inputNeuron.getId())){
                        nodeConnectionMatch = nodeConnection;
                        break;
                    }
                }
                if(nodeConnectionMatch != null){
                    strbff.append(decimalFormat.format(nodeConnectionMatch.getValue()));
                    strbff.append(";");
                } else {
                    strbff.append(decimalFormat.format(0.0));
                    strbff.append(";");
                }
            }

            for (int j = 0; j < this.ïntermediateNeurons.length; j++) {

                nodeConnectionMatch = null;

                List<NodeConnection> connectionsIn =
                        this.ïntermediateNeurons[i].getConnectionsIN();

                for(NodeConnection nodeConnection : connectionsIn){
                    if(nodeConnection.getNodeIn().getId().equals(this.ïntermediateNeurons[j].getId())){
                        nodeConnectionMatch = nodeConnection;
                        break;
                    }
                }
                if(nodeConnectionMatch != null){
                    strbff.append(decimalFormat.format(nodeConnectionMatch.getValue()));
                    strbff.append(";");
                } else {
                    strbff.append(decimalFormat.format(0.0));
                    strbff.append(";");
                }
            }

            for (int j = 0; j < this.outputNeurons.length; j++) {
                nodeConnectionMatch = null;

                List<NodeConnection> connectionsIn =
                        this.ïntermediateNeurons[i].getConnectionsIN();

                for(NodeConnection nodeConnection : connectionsIn){
                    if(nodeConnection.getNodeIn().getId().equals(this.outputNeurons[j].getId())){
                        nodeConnectionMatch = nodeConnection;
                        break;
                    }
                }
                if(nodeConnectionMatch != null){
                    strbff.append(decimalFormat.format(nodeConnectionMatch.getValue()));
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
		for (int j = 0; j < this.ïntermediateNeurons.length; j++) {
			strbff.append(this.ïntermediateNeurons[j].getId());
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

            for (MultiLayerPerceptronNeuron inputNeuron : inputNeurons) {

                nodeConnectionMatch = null;

                List<NodeConnection> connectionsIn =
                        this.outputNeurons[i].getConnectionsIN();

                for(NodeConnection nodeConnection : connectionsIn){
                    if(nodeConnection.getNodeIn().getId().equals(inputNeuron.getId())){
                        nodeConnectionMatch = nodeConnection;
                        break;
                    }
                }
                if(nodeConnectionMatch != null){
                    strbff.append(decimalFormat.format(nodeConnectionMatch.getValue()));
                    strbff.append(";");
                } else {
                    strbff.append(decimalFormat.format(0.0));
                    strbff.append(";");
                }
            }

            for (int j = 0; j < this.ïntermediateNeurons.length; j++) {

                nodeConnectionMatch = null;

                List<NodeConnection> connectionsIn =
                        this.outputNeurons[i].getConnectionsIN();

                for(NodeConnection nodeConnection : connectionsIn){
                    if(nodeConnection.getNodeIn().getId().equals(this.ïntermediateNeurons[j].getId())){
                        nodeConnectionMatch = nodeConnection;
                        break;
                    }
                }
                if(nodeConnectionMatch != null){
                    strbff.append(decimalFormat.format(nodeConnectionMatch.getValue()));
                    strbff.append(";");
                } else {
                    strbff.append(decimalFormat.format(0.0));
                    strbff.append(";");
                }
            }

            for (int j = 0; j < this.outputNeurons.length; j++) {
                nodeConnectionMatch = null;

                List<NodeConnection> connectionsIn =
                        this.outputNeurons[i].getConnectionsIN();

                for(NodeConnection nodeConnection : connectionsIn){
                    if(nodeConnection.getNodeIn().getId().equals(this.outputNeurons[j].getId())){
                        nodeConnectionMatch = nodeConnection;
                        break;
                    }
                }
                if(nodeConnectionMatch != null){
                    strbff.append(decimalFormat.format(nodeConnectionMatch.getValue()));
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
