package research.mpl.backend.smart.experiment;

import javax.persistence.*;

/**
 * Created by Marilia Portela on 05/02/2017.
 */

@Entity
@Table(name = "SIMULATION_STEP")
public class SimulationStep {

    private Long id;
    private Simulation simulation;
//    private Double partialResult;
//    private List<Solution> solutionList = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }


//    @Column(name="partialResult")
//    public Double getPartialResult() {
//        return partialResult;
//    }
//
//    public void setPartialResult(Double partialResult) {
//        this.partialResult = partialResult;
//    }
//
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "simulationStep", orphanRemoval = true)
//    public List<Solution> getSolutionList() {
//        return solutionList;
//    }
//
//    public void setSolutionList(List<Solution> solutionList) {
//        this.solutionList = solutionList;
//    }
}
