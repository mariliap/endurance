package research.mpl.backend.misc.cge;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DevolucaoRecursosController {

	private DevolucaoRecursosServiceBean devolucaoRecursosService = new DevolucaoRecursosServiceBean();
	
	private DevolucaoRecursosVO devolucaoRecursosVO = new DevolucaoRecursosVO();
	
	private InstrumentoVO instrumentoVO = new InstrumentoVO();

	List<RecursoVO> listaRecursosInstrumento = new ArrayList<RecursoVO>();

	///////////////M�todos utilizados pela p�gina de edi��o/////////////////
    public void carregarRecursosDoInstrumento(){
        listaRecursosInstrumento = devolucaoRecursosService.buscarRecursosDoInstrumento(instrumentoVO);
    }


	public void gerarDevolucaoParaInstrumento(){
		devolucaoRecursosVO.setInstrumentoVO(instrumentoVO);

//	       List<RecursoVO> listaRecursosInstrumento
//                = devolucaoRecursosService.buscarRecursosDoInstrumento(instrumentoVO);

        List<DevolucaoParaFonteVO> listaDevolucaoParaFonteV0 = new ArrayList<>();
        for(RecursoVO recursoVO : listaRecursosInstrumento) {
            DevolucaoParaFonteVO devolucaoParaFonteVO = new DevolucaoParaFonteVO();
            devolucaoParaFonteVO.setFonte(recursoVO.getNome());
            devolucaoParaFonteVO.setRecursoVO(recursoVO);

            OrdemBancariaVO ordemBancaria = new OrdemBancariaVO();
            SituacaoOrdemBancariaVO situacaoObt = new SituacaoOrdemBancariaVO();
            situacaoObt.setCodigo(SituacaoOrdemBancariaEnum.PENDENTE_AUTORIZACAO.getCodigo());

            ordemBancaria.setSituacaoOrdemBancariaVO(situacaoObt);
            ordemBancaria.setValorObt(new BigDecimal(0.00));
            devolucaoParaFonteVO.setOrdemBancariaVO(ordemBancaria);

            listaDevolucaoParaFonteV0.add(devolucaoParaFonteVO);
        }
        devolucaoRecursosVO.setListaDevolucaoParaFonteVO(listaDevolucaoParaFonteV0);
	}

	public List<DevolucaoRecursosVO> buscarDevolucoesPorInstrumentoOrdenadasPorData(){
		List<DevolucaoRecursosVO> listaDevolucoesIntrumento =
				devolucaoRecursosService.buscarDevolucoesPorInstrumentoOrdenadasPorData(instrumentoVO);
		return listaDevolucoesIntrumento;
	}
	
	public void calcularValorDevolucaoPorFonteDeRecurso(
			List<DevolucaoRecursosVO> listaDevolucoesIntrumento){
		try {
			if(listaRecursosInstrumento != null){
				
				//Preenche mapValorDevolucaoNaoEfetivadaPorFonte, que indica 
				//o quanto n�o foi efetivado em devolu��es anteriores para uma fonte de recurso
				HashMap<String, BigDecimal> mapValorDevolucaoNaoEfetivadaPorFonte =
						definirValoresNaoEfetivadosParaCadaFonte(listaDevolucoesIntrumento);
				
				//Calcula o total de recursos recebidos pelo instrumento
				BigDecimal valorTotalEmPendenciaDePagamento = BigDecimal.ZERO; 
				for(BigDecimal valorNaoEfetivadoFonte : mapValorDevolucaoNaoEfetivadaPorFonte.values()){
					valorTotalEmPendenciaDePagamento = valorTotalEmPendenciaDePagamento.add(valorNaoEfetivadoFonte).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
                System.out.println("Total pendente: "+valorTotalEmPendenciaDePagamento);
				
				BigDecimal valorDevolucao = devolucaoRecursosVO.getValorASerDevolvido().setScale(2, BigDecimal.ROUND_HALF_UP);
				BigDecimal saldoParaDistribuir = valorDevolucao.subtract(valorTotalEmPendenciaDePagamento).setScale(2, BigDecimal.ROUND_HALF_UP);
				
				//Calcula o saldo de devolu��o a distribuir para cada recurso
				if(saldoParaDistribuir.compareTo(BigDecimal.ZERO) >= 0){
					
					//Preenche mapValorDevolucaoEfetivadaPorFonte, que indica 
					//o valor total comprometido para devolucao em devolu��es anteriores para cada fonte de recurso
					HashMap<String, BigDecimal> mapValoresJaComprometidosParaCadaFonte = 
							definirValoresJaComprometidosParaCadaFonte(listaDevolucoesIntrumento);
					
					//Calcula o total de recursos recebidos pelo instrumento
					BigDecimal totalRecursosRecebidos = BigDecimal.ZERO; 
					for(RecursoVO recursoVO : listaRecursosInstrumento){
						totalRecursosRecebidos 
							= totalRecursosRecebidos
								.add(recursoVO.getValorRecurso().setScale(2, BigDecimal.ROUND_HALF_UP))
								.setScale(2, BigDecimal.ROUND_HALF_UP);
					}
					totalRecursosRecebidos.setScale(2, BigDecimal.ROUND_HALF_UP);
					
					//Calcula o total ja pago ou ja comprometidos para devolucao
					BigDecimal valorTotalJaDevolvidoOuJaComprometidoParaDevolucao = BigDecimal.ZERO; 
					for(BigDecimal valorJaComprometidosDaFonte : mapValoresJaComprometidosParaCadaFonte.values()){
						valorTotalJaDevolvidoOuJaComprometidoParaDevolucao = valorTotalJaDevolvidoOuJaComprometidoParaDevolucao.add(valorJaComprometidosDaFonte).setScale(2, BigDecimal.ROUND_HALF_UP);
					}

					valorTotalJaDevolvidoOuJaComprometidoParaDevolucao = valorTotalJaDevolvidoOuJaComprometidoParaDevolucao.add(valorTotalEmPendenciaDePagamento).setScale(2, BigDecimal.ROUND_HALF_UP);
					
					BigDecimal valorTotalContribuicoes
						= totalRecursosRecebidos.setScale(2, BigDecimal.ROUND_HALF_UP)
							.subtract(valorTotalJaDevolvidoOuJaComprometidoParaDevolucao.setScale(2, BigDecimal.ROUND_HALF_UP))
							.setScale(2, BigDecimal.ROUND_HALF_UP);
					
					for(RecursoVO recursoVO : listaRecursosInstrumento){

						BigDecimal valorNaoEfetivadoFonte = BigDecimal.ZERO;
						if(mapValorDevolucaoNaoEfetivadaPorFonte.get(recursoVO.getNome()) != null){
							valorNaoEfetivadoFonte = mapValorDevolucaoNaoEfetivadaPorFonte.get(recursoVO.getNome());
						}


						BigDecimal valorJaDevolvidoOuJaComprometidoParaDevolucaoFonte = BigDecimal.ZERO; 
						if(mapValoresJaComprometidosParaCadaFonte.get(recursoVO.getNome()) != null){
							valorJaDevolvidoOuJaComprometidoParaDevolucaoFonte =
                                    mapValoresJaComprometidosParaCadaFonte.get(recursoVO.getNome());
						}
						valorJaDevolvidoOuJaComprometidoParaDevolucaoFonte
								= valorJaDevolvidoOuJaComprometidoParaDevolucaoFonte
								.add(valorNaoEfetivadoFonte)
								.setScale(2, BigDecimal.ROUND_HALF_UP);

						BigDecimal valorContribuicaoDoRecurso 
							= recursoVO.getValorRecurso().setScale(2, BigDecimal.ROUND_HALF_UP)
								.subtract(valorJaDevolvidoOuJaComprometidoParaDevolucaoFonte.setScale(2, BigDecimal.ROUND_HALF_UP))
								.setScale(2, BigDecimal.ROUND_HALF_UP);
						
						
						BigDecimal proporcaoDoSaldoQueORecursoDeveReceber 
							= valorContribuicaoDoRecurso.divide(valorTotalContribuicoes, 2, BigDecimal.ROUND_HALF_UP);  
						

						BigDecimal valorADevolverParaRecurso 
							= valorNaoEfetivadoFonte
							.add(proporcaoDoSaldoQueORecursoDeveReceber
									.multiply(saldoParaDistribuir)
									.setScale(2, BigDecimal.ROUND_HALF_UP))
							.setScale(2, BigDecimal.ROUND_HALF_UP);

                        DevolucaoParaFonteVO devolucaoParaFonteVO =
                                devolucaoRecursosVO.getDevolucaoParaFonteVO(recursoVO.getNome());
						devolucaoParaFonteVO.getOrdemBancariaVO().setValorObt(valorADevolverParaRecurso);
					}
				} else {
					for(RecursoVO recursoVO : listaRecursosInstrumento){
						
						BigDecimal valorNaoEfetivadoFonte = BigDecimal.ZERO; 
						if(mapValorDevolucaoNaoEfetivadaPorFonte.get(recursoVO.getNome()) != null){
							valorNaoEfetivadoFonte = mapValorDevolucaoNaoEfetivadaPorFonte.get(recursoVO.getNome());
                        }

                        BigDecimal proporcaoDoValorPendenteDaFonteEmRelacaoAoTotalPendentePagamento
							= valorNaoEfetivadoFonte
								.divide(valorTotalEmPendenciaDePagamento, 2, BigDecimal.ROUND_HALF_UP)
								.setScale(2, BigDecimal.ROUND_HALF_UP);
					
						BigDecimal valorADevolverParaRecurso 
							= proporcaoDoValorPendenteDaFonteEmRelacaoAoTotalPendentePagamento
								.multiply(valorDevolucao)
								.setScale(2, BigDecimal.ROUND_HALF_UP);

                        DevolucaoParaFonteVO devolucaoParaFonteVO =
                                devolucaoRecursosVO.getDevolucaoParaFonteVO(recursoVO.getNome());
						devolucaoParaFonteVO.getOrdemBancariaVO().setValorObt(valorADevolverParaRecurso);
					}
				}
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Boolean isSituacaoOrdemBancariaQueComprometeSaldo (SituacaoOrdemBancariaVO situacao){
		if(situacao.getCodigo().equals(SituacaoOrdemBancariaEnum.PENDENTE_AUTORIZACAO.getCodigo())
				|| situacao.getCodigo().equals(SituacaoOrdemBancariaEnum.AUTORIZADA.getCodigo())
				|| situacao.getCodigo().equals(SituacaoOrdemBancariaEnum.TRANSMITIDA.getCodigo())
				|| situacao.getCodigo().equals(SituacaoOrdemBancariaEnum.EFETIVADA.getCodigo())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public HashMap<String, BigDecimal> definirValoresNaoEfetivadosParaCadaFonte(List<DevolucaoRecursosVO> listaDevolucoesIntrumento){
		HashMap<String, BigDecimal> mapValorDevolucaoNaoEfetivadaPorFonte = 
				new HashMap<String, BigDecimal>();
		
		if(listaDevolucoesIntrumento != null){
			for (DevolucaoRecursosVO devolucaoRecursosVO : listaDevolucoesIntrumento) {
				for(DevolucaoParaFonteVO recursoDevolucao : devolucaoRecursosVO.getListaDevolucaoParaFonteVO()){
					
					if(mapValorDevolucaoNaoEfetivadaPorFonte.get(recursoDevolucao.getFonte()) == null){
						mapValorDevolucaoNaoEfetivadaPorFonte.put(recursoDevolucao.getFonte(), BigDecimal.ZERO);
					}
					
					BigDecimal valorNaoEfetivadoFonte = mapValorDevolucaoNaoEfetivadaPorFonte.get(recursoDevolucao.getFonte());
					if(recursoDevolucao.getOrdemBancariaVO().getSituacaoOrdemBancariaVO().getCodigo()
							.equals(SituacaoOrdemBancariaEnum.NAO_EFETIVADA.getCodigo())){
						
						if(valorNaoEfetivadoFonte.compareTo(recursoDevolucao.getOrdemBancariaVO().getValorObt()) < 0){
							valorNaoEfetivadoFonte = recursoDevolucao.getOrdemBancariaVO().getValorObt().setScale(2, BigDecimal.ROUND_HALF_UP);
						}
						 									 							
					} else if(isSituacaoOrdemBancariaQueComprometeSaldo(recursoDevolucao.getOrdemBancariaVO().getSituacaoOrdemBancariaVO())){
						
						valorNaoEfetivadoFonte = valorNaoEfetivadoFonte
								.subtract(recursoDevolucao.getOrdemBancariaVO().getValorObt().setScale(2, BigDecimal.ROUND_HALF_UP))
								.setScale(2, BigDecimal.ROUND_HALF_UP);
						if(valorNaoEfetivadoFonte.compareTo(BigDecimal.ZERO) < 0){
							valorNaoEfetivadoFonte = BigDecimal.ZERO;
						}
					}
					mapValorDevolucaoNaoEfetivadaPorFonte.put(recursoDevolucao.getFonte(), valorNaoEfetivadoFonte);
				}
			}
		}

        for (String fonte : mapValorDevolucaoNaoEfetivadaPorFonte.keySet()){
            System.out.print(" Recurso: " + fonte
                    + " Debito : "+mapValorDevolucaoNaoEfetivadaPorFonte.get(fonte));
        }
        System.out.print("\n");
		return mapValorDevolucaoNaoEfetivadaPorFonte;
	}
	
	public HashMap<String, BigDecimal> definirValoresJaComprometidosParaCadaFonte(List<DevolucaoRecursosVO> listaDevolucoesIntrumento){
		
		HashMap<String, BigDecimal> mapValorDevolucaoEfetivadaPorFonte = 
				new HashMap<String, BigDecimal>();
		
		if(listaDevolucoesIntrumento != null){
			for (DevolucaoRecursosVO devolucaoRecursosVO : listaDevolucoesIntrumento) {
				for(DevolucaoParaFonteVO recursoDevolucao : devolucaoRecursosVO.getListaDevolucaoParaFonteVO()){
					
					if(mapValorDevolucaoEfetivadaPorFonte.get(recursoDevolucao.getFonte()) == null){
						mapValorDevolucaoEfetivadaPorFonte.put(recursoDevolucao.getFonte(), BigDecimal.ZERO);
					}
					
					BigDecimal valorComprometidoparaFonte = mapValorDevolucaoEfetivadaPorFonte.get(recursoDevolucao.getFonte());
					
					if(!recursoDevolucao.getOrdemBancariaVO().getSituacaoOrdemBancariaVO().getCodigo()
							.equals(SituacaoOrdemBancariaEnum.CANCELADA.getCodigo())
                            && !recursoDevolucao.getOrdemBancariaVO().getSituacaoOrdemBancariaVO().getCodigo()
								.equals(SituacaoOrdemBancariaEnum.NAO_AUTORIZADA.getCodigo())) {
                        //Obs: OBTs de devolu��o N�O efetivadas comprometem saldo de devolu��o, portanto entram neste calculo.
						
						valorComprometidoparaFonte = valorComprometidoparaFonte
								.add(recursoDevolucao.getOrdemBancariaVO().getValorObt().setScale(2, BigDecimal.ROUND_HALF_UP))
								.setScale(2, BigDecimal.ROUND_HALF_UP);
						mapValorDevolucaoEfetivadaPorFonte.put(recursoDevolucao.getFonte(), valorComprometidoparaFonte);
					}
				}
			}
		}
		
		return mapValorDevolucaoEfetivadaPorFonte;
	}

	public DevolucaoRecursosVO getDevolucaoRecursosVO() {
		return devolucaoRecursosVO;
	}

	public void setDevolucaoRecursosVO(DevolucaoRecursosVO devolucaoRecursosVO) {
		this.devolucaoRecursosVO = devolucaoRecursosVO;
	}

	public InstrumentoVO getInstrumentoVO() {
		return instrumentoVO;
	}

	public void setInstrumentoVO(InstrumentoVO instrumentoVO) {
		this.instrumentoVO = instrumentoVO;
	}
}