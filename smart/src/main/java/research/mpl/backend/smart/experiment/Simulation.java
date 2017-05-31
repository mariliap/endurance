package research.mpl.backend.smart.experiment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marilia Portela on 05/02/2017.
 */

@Entity
@Table(name = "SIMULATION")
public class Simulation {

    private Long id;
    private Experiment experiment;
    private List<SimulationStep> stepList = new ArrayList<>();
    private Double result;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "simulation", orphanRemoval = true)
    public List<SimulationStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<SimulationStep> stepList) {
        this.stepList = stepList;
    }

    @Column(name = "result")
    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
