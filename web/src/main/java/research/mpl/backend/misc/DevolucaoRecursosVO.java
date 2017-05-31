package research.mpl.backend.misc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class DevolucaoRecursosVO{

	private static final long serialVersionUID = 1L;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String numero;
	private Date dataDevolucao;
	private Date dataAutorizacao;
	private BigDecimal valorASerDevolvido;
	private InstrumentoVO instrumentoVO;
	private SituacaoDevolucaoRecursosVO situacaoVO;
	private List<DevolucaoParaFonteVO> listaDevolucaoParaFonteVO;
	
	private Date dataDevolucaoFiltroInicio;
	private Date dataDevolucaoFiltroFinal;


	public DevolucaoParaFonteVO getDevolucaoParaFonteVO(String nomeRecurso){
		for(DevolucaoParaFonteVO devolucaoParaFonteVO : listaDevolucaoParaFonteVO){
			if(devolucaoParaFonteVO.getFonte().equalsIgnoreCase(nomeRecurso))
				return devolucaoParaFonteVO;
		}
		return null;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}


	public Date getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public Date getDataAutorizacao() {
		return dataAutorizacao;
	}

	public void setDataAutorizacao(Date dataAutorizacao) {
		this.dataAutorizacao = dataAutorizacao;
	}

	public InstrumentoVO getInstrumentoVO() {
		return instrumentoVO;
	}

	public void setInstrumentoVO(InstrumentoVO instrumentoVO) {
		this.instrumentoVO = instrumentoVO;
	}

	public BigDecimal getValorASerDevolvido() {
		return valorASerDevolvido;
	}

	public void setValorASerDevolvido(BigDecimal valorASerDevolvido) {
		this.valorASerDevolvido = valorASerDevolvido;
	}

	public List<DevolucaoParaFonteVO> getListaDevolucaoParaFonteVO() {
		return listaDevolucaoParaFonteVO;
	}

	public void setListaDevolucaoParaFonteVO(List<DevolucaoParaFonteVO> listaDevolucaoParaFonteVO) {
		this.listaDevolucaoParaFonteVO = listaDevolucaoParaFonteVO;
	}

	public SituacaoDevolucaoRecursosVO getSituacaoVO() {
		return situacaoVO;
	}

	public void setSituacaoVO(SituacaoDevolucaoRecursosVO situacaoVO) {
		this.situacaoVO = situacaoVO;
	}

	public Date getDataDevolucaoFiltroInicio() {
		return dataDevolucaoFiltroInicio;
	}

	public void setDataDevolucaoFiltroInicio(Date dataDevolucaoFiltroInicio) {
		this.dataDevolucaoFiltroInicio = dataDevolucaoFiltroInicio;
	}

	public Date getDataDevolucaoFiltroFinal() {
		return dataDevolucaoFiltroFinal;
	}

	public void setDataDevolucaoFiltroFinal(Date dataDevolucaoFiltroFinal) {
		this.dataDevolucaoFiltroFinal = dataDevolucaoFiltroFinal;
	}

	
}