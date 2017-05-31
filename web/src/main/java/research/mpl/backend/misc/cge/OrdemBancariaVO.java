package research.mpl.backend.misc.cge;

import java.math.BigDecimal;

/**
 * Created by Marilia Portela on 21/02/2017.
 */
public class OrdemBancariaVO {

    private String numeroObt;
    private BigDecimal valorObt;
    private SituacaoOrdemBancariaVO situacaoOrdemBancariaVO;

    public String getNumeroObt() {
        return numeroObt;
    }

    public void setNumeroObt(String numeroObt) {
        this.numeroObt = numeroObt;
    }

    public BigDecimal getValorObt() {
        return valorObt;
    }

    public void setValorObt(BigDecimal valorObt) {
        this.valorObt = valorObt;
    }

    public SituacaoOrdemBancariaVO getSituacaoOrdemBancariaVO() {
        return situacaoOrdemBancariaVO;
    }

    public void setSituacaoOrdemBancariaVO(SituacaoOrdemBancariaVO situacaoOrdemBancariaVO) {
        this.situacaoOrdemBancariaVO = situacaoOrdemBancariaVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdemBancariaVO that = (OrdemBancariaVO) o;

        if (!numeroObt.equals(that.numeroObt)) return false;
        if (!valorObt.equals(that.valorObt)) return false;
        return situacaoOrdemBancariaVO.equals(that.situacaoOrdemBancariaVO);

    }

    @Override
    public int hashCode() {
        int result = numeroObt.hashCode();
        result = 31 * result + valorObt.hashCode();
        result = 31 * result + situacaoOrdemBancariaVO.hashCode();
        return result;
    }
}
