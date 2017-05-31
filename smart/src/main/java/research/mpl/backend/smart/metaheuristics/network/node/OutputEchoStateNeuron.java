package research.mpl.backend.smart.metaheuristics.network.node;

import research.mpl.backend.smart.metaheuristics.network.ActivationFunctionType;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class OutputEchoStateNeuron extends EchoStateNeuron {

	/** Index of reservoir synapses neurons (sources) **/
	private int[] reservoirSynapsesSources;

	/** Reservoir synapses weights **/
	private double[] reservoirSynapsesWeights; 
	
	
	private double[] inputsFromReservoir; //Useful to show on toString
	/** 
	 * Constructor
	 * @param inputLayerSynapsesSources
	 * @param recurrentSynapsesSources
	 * @param reservoirSynapsesSources
	 * @param activationFuncionType
	 * @param activationFunctionAlpha
	 */
	public OutputEchoStateNeuron(Long id,
								 int[] inputLayerSynapsesSources,
								 int[] recurrentSynapsesSources,
								 int[] reservoirSynapsesSources,
								 ActivationFunctionType activationFuncionType,
								 double activationFunctionAlpha) {
		
		super(id, inputLayerSynapsesSources,
				recurrentSynapsesSources,
				activationFuncionType,
				activationFunctionAlpha);
		this.reservoirSynapsesSources = reservoirSynapsesSources;
		this.reservoirSynapsesWeights = new double[reservoirSynapsesSources.length];
		this.inputsFromReservoir = new double[reservoirSynapsesSources.length];
		
	}

	public void initializeWeightsRandomly(Random randomGenerator) {
		
		super.initializeWeightsRandomly(randomGenerator);
		
		for (int i = 0; i < reservoirSynapsesWeights.length; i++) {
			reservoirSynapsesWeights[i] = randomGenerator.nextDouble();//*2 - 1; //Generate number between -1 and 1
		}
		
	}

	@Override
	protected void evaluateNet(int t, double[] inputs,
							   EchoStateNeuron[] reservoirNodes,
							   EchoStateNeuron[] outputNodes) {
		
		int previousIteration = t - 1;
		
		double[] neuronInputSignals = getNeuronInputSignals();
		
		int[] recurrentSynapsesSources = getRecurrentSynapsesSources();
		
		double netValue = 0;
		
		double[] inputLayerSynapsesWeights = getInputLayerSynapsesWeights();
		double[] recurrentSynapsesWeights = getRecurrentSynapsesWeights();
		
		int sourceIndex;
		for (int i = 0; i < inputLayerSynapsesWeights.length; i++) {
			netValue += neuronInputSignals[i] * inputLayerSynapsesWeights[i];
		}
		
		for (int i = 0; i < recurrentSynapsesWeights.length; i++) {
			sourceIndex = recurrentSynapsesSources[i];
			netValue += outputNodes[sourceIndex].getOutputedValueOfInstance(previousIteration) * recurrentSynapsesWeights[i];
		}
		
		for (int i = 0; i < reservoirSynapsesWeights.length; i++) {
			sourceIndex = this.reservoirSynapsesSources[i];
			inputsFromReservoir[i] = reservoirNodes[sourceIndex].getOutputedValueOfInstance(t);
			netValue += reservoirNodes[sourceIndex].getOutputedValueOfInstance(t) * reservoirSynapsesWeights[i];
		}
		//netValue += getBias(); Bias is part of the the input now...
		
		setNetValue(netValue);
	}
	
	public OutputEchoStateNeuron clone()  {
		OutputEchoStateNeuron clone = new OutputEchoStateNeuron(getId(),
												getInputLayerSynapsesSources().clone(),
												getRecurrentSynapsesSources().clone(),
												getReservoirSynapsesSources().clone(),
												getActivationFuncionType(),
												getActivationFunctionAlpha());
		
		clone.setInputLayerSynapsesWeights(getInputLayerSynapsesWeights().clone());
		clone.setRecurrentSynapsesWeights(getRecurrentSynapsesWeights().clone());
		clone.setReservoirSynapsesWeights(getReservoirSynapsesWeights().clone());
		
		clone.setNetValue(getNetValue());
		clone.setOutputedValuesOfPresentedInstances((Vector<Double>)getOutputedValuesOfPresentedInstances().clone());
		
		return clone;
	}
	
	public int getNumberOfWeights() {
		// Bias is part of the the input now...
		
		// bias weight + input weights 
		//return 1 + getInputLayerSynapsesWeights().length + getReservoirSynapsesWeights().length + getRecurrentSynapsesWeights().length;
		
		return getInputLayerSynapsesWeights().length + getReservoirSynapsesWeights().length + getRecurrentSynapsesWeights().length;
	}

	public int[] getReservoirSynapsesSources() {
		return reservoirSynapsesSources;
	}

	public void setReservoirSynapsesSources(int[] reservoirSynapsesSources) {
		this.reservoirSynapsesSources = reservoirSynapsesSources;
	}



	public double[] getReservoirSynapsesWeights() {
		return reservoirSynapsesWeights;
	}
	
	public double getReservoirSynapseWeight(int pos) {
		return reservoirSynapsesWeights[pos];
	}

	public void setReservoirSynapsesWeights(double[] reservoirSynapsesWeights) {
		this.reservoirSynapsesWeights = reservoirSynapsesWeights;
	}
	
	public void setReservoirSynapseWeight(double weight, int pos) {
		this.reservoirSynapsesWeights[pos] = weight;
	}



	/**
	 * The same as getRecurrentSynapsesSources() because this is an output neuron
	 * @return
	 */
	public int[] getOutputSynapsesSources() {
		return getRecurrentSynapsesSources();
	}
	
	/**
	 * The same as setRecurrentSynapseWeight() because this is an output neuron
	 * @return
	 */
	public void setOutputSynapseWeight(double weight, int pos) {
		setRecurrentSynapseWeight(weight, pos);
	}
		

	@Override
	public String toString() {
		return "OutputEchoStateNeuron:    reservoirSynapsesSources=" + Arrays.toString(reservoirSynapsesSources)
				+ "\n---------------  "
				+ "reservoirSynapsesWeights=" + Arrays.toString(reservoirSynapsesWeights)
				+ "\n\t\t" 
				+ "inputsFromReservoir=" +  Arrays.toString(inputsFromReservoir)
				+ "\n\t\t "
				+ super.toString();
	}


	
}
