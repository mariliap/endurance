package research.mpl.backend.smart.metaheuristics.network;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class ProblemSample {

    private Long id;
	private List<Double> inputs;
	private List<Double> outputs;

    public ProblemSample(){}

	public ProblemSample(double[] inputs, double[] outputs) {
        this.inputs = new ArrayList<Double>();
		for (int i = 0; i < inputs.length; i++){
			this.inputs.add(inputs[i]);
		}
        this.outputs = new ArrayList<Double>();
		for (int i = 0; i < outputs.length; i++){
			this.outputs.add(outputs[i]);
		}
	}
	
	public String toString() { 
		StringBuffer strbff = new StringBuffer();
		strbff.append("[");
		strbff.append(this.inputs.get(0));
		for (int i = 1; i < this.inputs.size(); i++) {
			strbff.append(", ");
			strbff.append(this.inputs.get(i));
		}
		strbff.append("] - [");
		strbff.append(this.outputs.get(0));
		for (int i = 1; i < this.outputs.size(); i++) {
			strbff.append(", ");
			strbff.append(this.outputs.get(i));
		}
		strbff.append("]");
		
		return strbff.toString();
	}

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @ElementCollection
	public List<Double> getInputs() {
		return this.inputs;
	}

	public void setInputs(List<Double> inputs) {
        this.inputs = inputs;
	}

//    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @ElementCollection
	public List<Double> getOutputs() {
		return this.outputs;
	}

	public void setOutputs(List<Double> outputs) {
       this.outputs = outputs;
	}


    public void setInputValue(int position, double value){
        this.inputs.set(position, value);
    }

    public double getInputValue(int position){
        return this.inputs.get(position);
    }

    public void setOutputValue(int position, double value){
        this.outputs.set(position, value);
    }

    public double getOutputValue(int position){
        return this.outputs.get(position);
    }


    @Transient
    public void setInputArray(double[] values){
        for (int i = 0; i < values.length; i++){
            this.inputs.add(values[i]);
        }
    }

    @Transient
    public double[] getInputArray(){
        double[] inputsArray = new double[this.inputs.size()];
        for (int i = 0; i < this.inputs.size(); i++){
            inputsArray[i] = this.inputs.get(i);
        }
        return inputsArray;
    }

    @Transient
    public void setOutputArray(double[] values){
        for (int i = 0; i < values.length; i++){
            this.outputs.add(values[i]);
        }
    }

    @Transient
    public double[] getOutputArray(){
        double[] outputArray = new double[this.outputs.size()];
        for (int i = 0; i < this.outputs.size(); i++){
            outputArray[i] = this.outputs.get(i);
        }
        return outputArray;
    }

    @Transient
    public int getNumberOfInputs() {
        return this.inputs.size();
    }

    @Transient
    public int getNumberOfOutputs() {
        return this.outputs.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProblemSample that = (ProblemSample) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (inputs != null ? !inputs.equals(that.inputs) : that.inputs != null) return false;
        return !(outputs != null ? !outputs.equals(that.outputs) : that.outputs != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (inputs != null ? inputs.hashCode() : 0);
        result = 31 * result + (outputs != null ? outputs.hashCode() : 0);
        return result;
    }
}
