package research.mpl.backend.misc.cge;

/**
 * Created by Marilia Portela on 21/02/2017.
 */
public enum SituacaoOrdemBancariaEnum {
    PENDENTE_AUTORIZACAO(1),
    AUTORIZADA(2),
    NAO_AUTORIZADA(3),
    TRANSMITIDA(4),
    EFETIVADA(5),
    NAO_EFETIVADA(6),
    CANCELADA(7);

    private Integer codigo;

    SituacaoOrdemBancariaEnum(Integer codigo){
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
