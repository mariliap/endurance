package research.mpl.backend.smart.experiment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marilia Portela on 05/02/2017.
 */

@Entity
@Table(name = "EXPERIMENT")
public class Experiment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "experiment", orphanRemoval = true)
    private List<Simulation> simulations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "experiment", orphanRemoval = true)
    private List<ParameterValue> parameters = new ArrayList<>();

    private String name;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Simulation> getSimulations() {
        return simulations;
    }

    public void addSimulation(Simulation simulation) {
        simulations.add(simulation);
        simulation.setExperiment(this);
    }

    public void removeSimulation(Simulation simulation) {
        simulation.setExperiment(null);
        this.simulations.remove(simulation);
    }

    public List<ParameterValue> getExperimentParameters() {
        return parameters;
    }

    public void addExperimentParameter(ParameterValue parameter) {
        parameters.add(parameter);
        parameter.setExperiment(this);
    }

    public void removeParameter(ParameterValue parameter) {
        parameter.setExperiment(null);
        this.parameters.remove(parameter);
    }
}
