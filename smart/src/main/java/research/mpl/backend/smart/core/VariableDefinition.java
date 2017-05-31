package research.mpl.backend.smart.core;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "VARIABLE_DEFINITION")
public class VariableDefinition implements Serializable {

    private Long id;
    private String identifierName;
    private double lowerBound;
    private double upperBound;

    public VariableDefinition() {
    }

    public VariableDefinition(String identifierName, double lowerBound, double upperBound){
        this.identifierName = identifierName;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Id
    @GeneratedValue
    @Column(name="id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="identifierName")
    public String getIdentifierName() {
        return identifierName;
    }

    public void setIdentifierName(String identifierName) {
        this.identifierName = identifierName;
    }

    @Column(name="lowerBound")
    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound)  {
        this.lowerBound = lowerBound;
    }

    @Column(name="upperBound")
    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public VariableDefinition copy(){
        VariableDefinition newCopy = new VariableDefinition();
        newCopy.setId(this.id);
        newCopy.setIdentifierName(this.identifierName);
        newCopy.setLowerBound(this.lowerBound);
        newCopy.setUpperBound(this.upperBound);
        return newCopy;
    }
}
