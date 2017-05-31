package research.mpl.backend.smart.experiment;

import javax.persistence.*;

/**
 * Created by Marilia Portela on 05/02/2017.
 */

@Entity
@Table(name = "PARAMETER") //,
        //uniqueConstraints = { @UniqueConstraint( columnNames = { "parameterDefinition", "experiment" } ) } )
// This will be enforced by the database on an update or persist.
// You'd need to write your own custom validator if you wanted to enforce this using
// Hibernate Validator.
public class ParameterValue {

    private Long id;
    private ParameterDefinition parameterDefinition;
    private Experiment experiment;
    private Double value;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    public ParameterDefinition getParameterDefinition() {
        return this.parameterDefinition;
    }

    public void setParameterDefinition(ParameterDefinition definition) {
        this.parameterDefinition = definition;
    }

    @ManyToOne
    public Experiment getExperiment() {
        return this.experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    @Column(name = "value")
    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
