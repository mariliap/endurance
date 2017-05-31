package research.mpl.backend.smart.metaheuristics.network.node;

import research.mpl.backend.smart.metaheuristics.network.ActivationFunctionType;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ReservoirEchoStateNeuron extends EchoStateNeuron {
	
	/** Index of back synapses neurons (sources) **/
	private int[] backSynapsesSources;
	
	/** Back synapses weights **/
	private double[] backSynapsesWeights; 
	
	/** 
	 * Constructor
	 * @param inputLayerSynapsesSources
	 * @param recurrentSynapsesSources
	 * @param backSynapsesSources
	 * @param activationFuncionType
	 * @param activationFunctionAlpha
	 */
	public ReservoirEchoStateNeuron(Long id,
									int[] inputLayerSynapsesSources,
									int[] recurrentSynapsesSources,
									int[] backSynapsesSources,
									ActivationFunctionType activationFuncionType,
									double activationFunctionAlpha) {
		
		super(id, inputLayerSynapsesSources,
				recurrentSynapsesSources,
				activationFuncionType,
				activationFunctionAlpha);
		this.backSynapsesSources = backSynapsesSources;
		this.backSynapsesWeights = new double[backSynapsesSources.length];
		
	}
	
	/** Initialize weights randomly**/
	public void initializeWeightsRandomly(Random randomGenerator) {
		
		super.initializeWeightsRandomly(randomGenerator);
		
		for (int i = 0; i < backSynapsesWeights.length; i++) {
			backSynapsesWeights[i] =  randomGenerator.nextDouble();//*2 - 1; //Generate number between -1 and 1
		}
	}

	@Override
	protected void evaluateNet(int t, double[] inputs,
							   EchoStateNeuron[] reservoirNodes,
							   EchoStateNeuron[] outputNodes) {
		
		int previousTime = t - 1;
		
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
			netValue += reservoirNodes[sourceIndex].getOutputedValueOfInstance(previousTime) * recurrentSynapsesWeights[i];
		}
		
		for (int i = 0; i < backSynapsesWeights.length; i++) {
			sourceIndex = this.backSynapsesSources[i];
			netValue += outputNodes[sourceIndex].getOutputedValueOfInstance(previousTime) * backSynapsesWeights[i];
		}
//		netValue += getBias(); Bias is part of the the input now...
		
		setNetValue(netValue);
	}
	
	public ReservoirEchoStateNeuron clone()  {
		ReservoirEchoStateNeuron clone = new ReservoirEchoStateNeuron(getId(),
													getInputLayerSynapsesSources().clone(),
													getRecurrentSynapsesSources().clone(),
													getBackSynapsesSources().clone(),
													getActivationFuncionType(),
													getActivationFunctionAlpha());
		
		clone.setInputLayerSynapsesWeights(getInputLayerSynapsesWeights().clone());
		clone.setRecurrentSynapsesWeights(getRecurrentSynapsesWeights().clone());
		clone.setBackSynapsesWeights(getBackSynapsesWeights().clone());
		
		clone.setNetValue(getNetValue());
		clone.setOutputedValuesOfPresentedInstances((Vector<Double>)getOutputedValuesOfPresentedInstances().clone());
		
		return clone;
	}
	
	public int getNumberOfWeights() {
		return getInputLayerSynapsesWeights().length + getRecurrentSynapsesWeights().length + getBackSynapsesWeights().length;
	}

	public int[] getBackSynapsesSources() {
		return backSynapsesSources;
	}

	public void setBackSynapsesSources(int[] backSynapsesSources) {
		this.backSynapsesSources = backSynapsesSources;
	}

	public double[] getBackSynapsesWeights() {
		return backSynapsesWeights;
	}
	
	public double getBackSynapseWeight(int pos) {
		return backSynapsesWeights[pos];
	}

	public void setBackSynapsesWeights(double[] backSynapsesWeights) {
		this.backSynapsesWeights = backSynapsesWeights;
	}
	
	public void setBackSynapseWeight(double weight, int pos) {
		this.backSynapsesWeights[pos] = weight;
	}
	
	
	
	/**
	 * The same as getBackSynapsesSources()
	 * @return
	 */
	public int[] getOutputSynapsesSources() {
		return backSynapsesSources;
	}
	
	/**
	 * The same as setBackSynapseWeight() because this is a reservoir neuron
	 * @return
	 */
	public void setOutputSynapseWeight(double weight, int pos) {
		this.backSynapsesWeights[pos] = weight;
	}
	
	/**
	 * The same as getRecurrentSynapsesSources() because this is a reservoir neuron
	 * @return
	 */
	public int[] getReservoirSynapsesSources() {
		return getRecurrentSynapsesSources();
	}
	
	/**
	 * The same as getRecurrentSynapseWeight() because this is a reservoir neuron
	 * @return
	 */
	public double getReservoirSynapseWeight(int pos) {
		return getRecurrentSynapseWeight(pos);
	}
	
	/**
	 * The same as setRecurrentSynapseWeight() because this is a reservoir neuron
	 * @return
	 */
	public void setReservoirSynapseWeight(double weight, int pos) {
		setRecurrentSynapseWeight(weight, pos);
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "ReservoirEchoStateNeuron: backSynapsesSources=" + Arrays.toString(backSynapsesSources)
				+ ", backSynapsesWeights=" + Arrays.toString(backSynapsesWeights) 
				+ "\n--------------- "+ super.toString();
	}
	
}
