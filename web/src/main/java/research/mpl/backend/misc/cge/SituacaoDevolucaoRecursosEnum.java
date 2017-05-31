package research.mpl.backend.misc.cge;

/**
 * Created by Marilia Portela on 21/02/2017.
 */
public enum SituacaoDevolucaoRecursosEnum {

    PENDENTE_AUTORIZACAO(1, "pendente"),
    AUTORIZADA(2, "autorizada"),
    NAO_AUTORIZADA(3, "não autorizada"),
    TRANSMITIDA(4,"transmitida"),
    EFETIVADA(5, "efetivada"),
    NAO_EFETIVADA(6, "não efetivada"),
    CANCELADA(7, "cancelada");

    private Integer codigo;
    private String nome;

    SituacaoDevolucaoRecursosEnum(Integer codigo, String nome){
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
