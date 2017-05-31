package research.mpl.backend.common.data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Marilia Portela on 15/01/2017.
 */

//Cria classe com métodos de acessibilidade para buscas (ex. prazo final e inicial de um atributo data)
@Searchable
//Cria getters e setters
@Accessible
@Entity
@Table(name = "example")
public class EntityExample extends GenericEntity{

    private Integer numero;

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
