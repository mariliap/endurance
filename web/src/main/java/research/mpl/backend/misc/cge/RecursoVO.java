package research.mpl.backend.misc.cge;

import java.math.BigDecimal;

/**
 * Created by Marilia Portela on 21/02/2017.
 */
public class RecursoVO {

    private BigDecimal valorRecurso;
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValorRecurso() {
        return valorRecurso;
    }

    public void setValorRecurso(BigDecimal valorRecurso) {
        this.valorRecurso = valorRecurso;
    }
}
