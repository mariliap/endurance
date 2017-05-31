package research.mpl.backend.smart.core;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "VARIABLE_VALUE")
public class VariableValue implements Serializable {

    private Long id;
    private double value;
    private Solution solution;
    private VariableDefinition definition;

    @Id
    @GeneratedValue
    @Column(name="id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    @OneToOne
    public VariableDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(VariableDefinition definition) {
        this.definition = definition;
    }

    @Column(name="value")
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public VariableValue copy(){
        VariableValue newCopy = new VariableValue();
        newCopy.setSolution(this.solution);
        newCopy.setDefinition(this.definition);
        newCopy.setValue(this.value);
        return newCopy;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }

    @Override
    public boolean equals(Object obj){
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
