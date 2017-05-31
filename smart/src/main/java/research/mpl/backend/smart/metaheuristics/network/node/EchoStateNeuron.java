package research.mpl.backend.smart.metaheuristics.network.node;

import research.mpl.backend.smart.metaheuristics.network.ActivationFunctionType;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * Created by Marilia Portela on 16/03/2017.
 */
public abstract class EchoStateNeuron extends Node{


    private double netValue;
    private Vector<Double> outputHistory;
    private ActivationFunctionType activationFuncionType;
    private double activationFunctionAlpha;




    /** Input signals **/
    private double[] neuronInputSignals;

    /** Index of input layer entries (origins of connection) **/
    private int[] inputLayerSynapsesSources;

    /** List of Ids (indexes) of neurons of the same layer that are connected to this neuron (origins of connection)
     *
     *  Index of the neuron in the corresponding layer array
     *  "Layer arrays" always start at 0 and ends at the number of neurons in the layer - 1.
     *  For example, if we have a layer that goes [0,1,2,3,4,5,6,7,8,9]
     *  The sources could be [1,3,4] **/
    private int[] recurrentSynapsesSources;

    /** Input layer entries weights **/
    private double[] inputLayerSynapsesWeights;

    /** Recurrent synapses weights **/
    private double[] recurrentSynapsesWeights;


    public EchoStateNeuron(Long id, int[] inputLayerSynapsesSources, int[] recurrentSynapsesSources,
                ActivationFunctionType activationFuncionType, double activationFunctionAlpha) {

        setId(id);
        this.inputLayerSynapsesSources = inputLayerSynapsesSources;
        this.recurrentSynapsesSources = recurrentSynapsesSources;

        this.neuronInputSignals = new double[inputLayerSynapsesSources.length];//bias is a input source
        this.inputLayerSynapsesWeights = new double[inputLayerSynapsesSources.length];
        this.recurrentSynapsesWeights = new double[recurrentSynapsesSources.length];

        this.activationFuncionType = activationFuncionType;
        this.activationFunctionAlpha = activationFunctionAlpha;

        this.outputHistory = new Vector<Double>();

    }

    public void initializeWeightsRandomly(Random randomGenerator) {

        //Obs: Bias is the first item of the input...
        inputLayerSynapsesWeights[0] = 1;
        for (int i = 1; i < inputLayerSynapsesWeights.length; i++) {
            inputLayerSynapsesWeights[i] =  randomGenerator.nextDouble();//*2 - 1; //Generate number between -1 and 1
        }

        for (int j = 0; j < recurrentSynapsesWeights.length; j++) {
            recurrentSynapsesWeights[j] = randomGenerator.nextDouble();//*2 - 1; //Generate number between -1 and 1
        }
    }

    @Override
    public abstract EchoStateNeuron clone() ;

    public abstract int[] getReservoirSynapsesSources();
    public abstract int[] getOutputSynapsesSources();

    public abstract double getReservoirSynapseWeight(int pos);
    public abstract void setReservoirSynapseWeight(double value, int pos);
    public abstract void setOutputSynapseWeight(double value, int pos);

    protected abstract void evaluateNet(int t, double[] inputs,
                                        EchoStateNeuron[] reservoirNodes,
                                        EchoStateNeuron[] outputNodes);

    protected abstract int getNumberOfWeights();



    public void process(int t, double[] inputs,
                        EchoStateNeuron[] reservoirNodes,
                        EchoStateNeuron[] outputNodes){
        int sourceIndex;

        for (int i = 0; i < inputLayerSynapsesWeights.length; i++) {
            sourceIndex = inputLayerSynapsesSources[i];
            setNeuronInputSignal(i, inputs[sourceIndex]);
        }
        evaluateNet(t, inputs, reservoirNodes, outputNodes);
        evaluateOutput(t);
    }

    private void evaluateOutput(int t) {
        switch (activationFuncionType) {
            case SIGNAL:
                if (netValue > 0) {
                    addToOutputedValuesOfInstance(t, 1.0);
                } else {
                    addToOutputedValuesOfInstance(t, 0.0);
                }
                break;
            case SIGMOIDE:
                double value = (1 / (1 + Math.exp(-activationFunctionAlpha * netValue)));
                addToOutputedValuesOfInstance(t, value );
                break;
            case LINEAR_PARTS:
                if (netValue <= 0) {
                    addToOutputedValuesOfInstance(t, 0.0);
                } else if (netValue >= activationFunctionAlpha) {
                    addToOutputedValuesOfInstance(t, 1.0);
                } else {
                    addToOutputedValuesOfInstance(t, netValue + activationFunctionAlpha);
                }
                break;
            default:
                break;
        }
    }



    private void addToOutputedValuesOfInstance(int t, double value){
        if(t >= outputHistory.size()){
            outputHistory.add(value);//adds new value when it's the first epoch the instance is being shown
        } else {
            outputHistory.set(t, value);//replaces old value when it's NOT the first epoch the instance is being shown
        }
    }

    public double getOutputedValueOfInstance(int t) {// "t" is the ID of instance
        double output = 0;
        if(t >= 0) {
            output = outputHistory.get(t);
        }

        return output;
    }

    public void setOutputedValueOfInstance(int t, double outputValue) {
        this.outputHistory.set(t, outputValue);
    }

    public Vector<Double> getOutputedValuesOfPresentedInstances() {
        return this.outputHistory;
    }

    public void setOutputedValuesOfPresentedInstances(Vector<Double> outputValues) {
        this.outputHistory = outputValues;
    }



    public double[] getNeuronInputSignals() {
        return neuronInputSignals;
    }

    public double getNeuronInputSignal(int pos) {
        return neuronInputSignals[pos];
    }

    public void setNeuronInputSignals(double[] neuronInputSignals) {
        this.neuronInputSignals = neuronInputSignals;
    }

    public void setNeuronInputSignal(int pos, double neuronInputSignal) {
        this.neuronInputSignals[pos] = neuronInputSignal;
    }



    public double[] getInputLayerSynapsesWeights() {
        return inputLayerSynapsesWeights;
    }

    public double getInputLayerSynapseWeight(int pos) {
        return inputLayerSynapsesWeights[pos];
    }

    public void setInputLayerSynapsesWeights(double[] weights) {
        this.inputLayerSynapsesWeights = weights;
    }

    public void setInputLayerSynapseWeight(double weight, int pos) {
        this.inputLayerSynapsesWeights[pos] = weight;
    }



    public double[] getRecurrentSynapsesWeights() {
        return recurrentSynapsesWeights;
    }

    public double getRecurrentSynapseWeight(int pos) {
        return recurrentSynapsesWeights[pos];
    }

    public void setRecurrentSynapsesWeights(double[] weights) {
        this.recurrentSynapsesWeights = weights;
    }

    public void setRecurrentSynapseWeight(double weight, int pos) {
        this.recurrentSynapsesWeights[pos] = weight;
    }



    public int[] getInputLayerSynapsesSources() {
        return inputLayerSynapsesSources;
    }

    public void setInputLayerSynapsesSources(int[] indexes) {
        this.inputLayerSynapsesSources = indexes;
    }

    public int getInputLayerSynapseSource(int pos) {
        return inputLayerSynapsesSources[pos];
    }

    public void setInputLayerSynapseSource(int index, int pos) {
        this.inputLayerSynapsesSources[pos] = index;
    }



    public int[] getRecurrentSynapsesSources() {
        return recurrentSynapsesSources;
    }

    public void setRecurrentSynapsesSources(int[] indexes) {
        this.recurrentSynapsesSources = indexes;
    }

    public int getRecurrentSynapseSource(int pos) {
        return recurrentSynapsesSources[pos];
    }

    public void setRecurrentSynapseSource(int index, int pos) {
        this.recurrentSynapsesSources[pos] = index;
    }



    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }



    public ActivationFunctionType getActivationFuncionType() {
        return activationFuncionType;
    }

    public void setActivationFuncionType(ActivationFunctionType activationFuncionType) {
        this.activationFuncionType = activationFuncionType;
    }



    public double getActivationFunctionAlpha() {
        return activationFunctionAlpha;
    }

    public void setActivationFunctionAlpha(double activationFunctionAlpha) {
        this.activationFunctionAlpha = activationFunctionAlpha;
    }


    @Override
    public String toString() {

        return " "
                + "inputLayerSynapsesSources=" + Arrays.toString(inputLayerSynapsesSources)
                + "\n\t\t "
                + "inputLayerSynapsesWeights=" + Arrays.toString(inputLayerSynapsesWeights)
                + "\n\t\t "
                + "recurrentSynapsesSources=" + Arrays.toString(recurrentSynapsesSources)
                + "\n\t\t "
                + "recurrentSynapsesWeights=" + Arrays.toString(recurrentSynapsesWeights)
                + "\n\t\t "
                + "signalInputs=" + Arrays.toString(neuronInputSignals)
                + "\n\t\t "
                + "netValue=" + netValue
                + "\n\t\t "
                + "outputValue=" + (outputHistory.size()!=0? outputHistory.get(outputHistory.size()-1) : "[]");
    }

}
