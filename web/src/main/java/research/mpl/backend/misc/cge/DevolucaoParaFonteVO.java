package research.mpl.backend.misc.cge;

import java.math.BigDecimal;

/**
 * Created by Marilia Portela on 21/02/2017.
 */
public class DevolucaoParaFonteVO {

    private String fonte;
    private OrdemBancariaVO ordemBancariaVO;
    private RecursoVO recursoVO;
//    private BigDecimal valorRecurso;
    private BigDecimal valorDeDevolucaoParaRecurso;

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public OrdemBancariaVO getOrdemBancariaVO() {
        return ordemBancariaVO;
    }

    public void setOrdemBancariaVO(OrdemBancariaVO ordemBancariaVO) {
        this.ordemBancariaVO = ordemBancariaVO;
    }

    public RecursoVO getRecursoVO() {
        return recursoVO;
    }

    public void setRecursoVO(RecursoVO recursoVO) {
        this.recursoVO = recursoVO;
    }

    //    public BigDecimal getValorRecurso() {
//        return valorRecurso;
//    }
//
//    public void setValorRecurso(BigDecimal valorRecurso) {
//        this.valorRecurso = valorRecurso;
//    }

    public BigDecimal getValorDeDevolucaoParaRecurso() {
        return valorDeDevolucaoParaRecurso;
    }

    public void setValorDeDevolucaoParaRecurso(BigDecimal valorDeDevolucaoParaRecurso) {
        this.valorDeDevolucaoParaRecurso = valorDeDevolucaoParaRecurso;
    }
}
