package research.mpl.backend.smart.metaheuristics.network.node;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "NODE")
public abstract class Node implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "nodeIn")
    private List<NodeConnection> connectionsIN;

    @OneToMany(mappedBy = "nodeOut")
    private List<NodeConnection> connectionsOut;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<NodeConnection> getConnectionsIN() {
        return connectionsIN;
    }

    public void setConnectionsIN(List<NodeConnection> connectionsIN) {
        this.connectionsIN = connectionsIN;
    }

    public List<NodeConnection> getConnectionsOut() {
        return connectionsOut;
    }

    public void setConnectionsOut(List<NodeConnection> connectionssOut) {
        this.connectionsOut = connectionssOut;
    }

}
