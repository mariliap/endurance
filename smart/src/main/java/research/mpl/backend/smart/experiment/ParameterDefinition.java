package research.mpl.backend.smart.experiment;

import javax.persistence.*;

/**
 * Created by Marilia Portela on 05/02/2017.
 */

@Entity
@Table(name = "PARAMETER_DEFINITION")
public class ParameterDefinition {

    private Long id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
