package research.mpl.backend.smart.metaheuristics.network.node;

import javax.persistence.*;

/**
 * Created by Marilia Portela on 16/03/2017.
 */

@Entity
@Table(name = "NODE_CONNECTION")
public class NodeConnection {


    private Long id;
    private double value;
    private Node nodeIn;
    private Node nodeOut;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @ManyToOne
    public Node getNodeIn() {
        return nodeIn;
    }

    public void setNodeIn(Node nodeIn) {
        this.nodeIn = nodeIn;
    }

    @ManyToOne
    public Node getNodeOut() {
        return nodeOut;
    }

    public void setNodeOut(Node nodeOut) {
        this.nodeOut = nodeOut;
    }
}
