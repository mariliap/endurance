package research.mpl.backend.smart.metaheuristics.network.node;

import research.mpl.backend.smart.metaheuristics.network.ActivationFunctionType;

import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Node wrapper for MLP training
 * @author Marilia
 *
 */
public class MultiLayerPerceptronNeuron extends Node{


	private double netValue;
	private Vector<Double> outputHistory;
	private ActivationFunctionType activationFuncionType;
	private double activationFunctionAlpha;

	private double gradient;
	private double outputError;
	
	public MultiLayerPerceptronNeuron() {
		this.gradient = 0;
		this.outputError = 9999999;
	}

	public double getGradient() {
		return this.gradient;
	}

	public void setGradient(double gradient) {
		this.gradient = gradient;
	}


	public double getOutputError() {
		return outputError;
	}

	public void setOutputError(double outputError) {
		this.outputError = outputError;
	}

    public void process(int t){

        evaluateNet(t);
        evaluateOutput(t);
    }

    protected void evaluateNet(int t){
        int previousTime = t - 1;

        this.netValue = 0;

        List<NodeConnection> connectionsIN = getConnectionsIN();
        for (NodeConnection connIN : connectionsIN){
            MultiLayerPerceptronNeuron neuronIN = ((MultiLayerPerceptronNeuron) connIN.getNodeIn());

            this.netValue += connIN.getValue() * neuronIN.getOutputedValueOfInstance(t);
//            if(neuronIN.getLayerOrder() < this.layerOrder) {
//                netValue += connIN.getValue() * neuronIN.getOutputedValueOfInstance(t);
//            } else{
//                netValue += connIN.getValue() * neuronIN.getOutputedValueOfInstance(previousTime);
//            }
        }
//        setNetValue(netValue);
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

}
