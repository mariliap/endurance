package research.mpl.backend.misc;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DevolucaoRecursosServiceBean {

	Random random = new Random();

	public List<DevolucaoRecursosVO> buscarDevolucoesPorInstrumentoOrdenadasPorData(
			InstrumentoVO instrumentoVO) {

		List<DevolucaoRecursosVO> lista = gerarDevolucoes(2);

		return lista;
	}

	public List<RecursoVO> buscarRecursosDoInstrumento(InstrumentoVO instrumentoVO) {
		//Pesquisa as fontes de recurso do instrumento: contrapartida e repasses

		int listaNPsSefaz = 2;
		List<RecursoVO> listaRecursos = new ArrayList<RecursoVO>();
		String[] fontes = new String[]{"Tesouro","FUNDES"};

		for (int i = 0; i < listaNPsSefaz; i++) {
			RecursoVO recursoVO = new RecursoVO();
			recursoVO.setNome(fontes[i % fontes.length]);

			BigDecimal valorRecurso = new BigDecimal((random.nextInt(100)+100) * 10.00);
			if(listaRecursos.contains(recursoVO)){
				recursoVO = listaRecursos.get(listaRecursos.indexOf(recursoVO));
				valorRecurso = valorRecurso.add(recursoVO.getValorRecurso());
				recursoVO.setValorRecurso(valorRecurso);
			} else {
				recursoVO.setValorRecurso(valorRecurso);
				listaRecursos.add(recursoVO);
			}
		}

		RecursoVO recursoVO = new RecursoVO();
		recursoVO.setNome("Contrapartida");
//		recursoVO.setValorRecurso(contrapartidaService.obterTotalContrapartidaValidas(instrumentoVO));
		recursoVO.setValorRecurso(new BigDecimal((random.nextInt(100)+100) * 10.00));
		listaRecursos.add(recursoVO);
		return listaRecursos;
	}


	
	public String gerarNumeroDevolucaoRecurso() {
		StringBuffer numero = new StringBuffer("");

			Calendar dataAtual = Calendar.getInstance();
			numero.append(dataAtual.get(Calendar.YEAR));
			numero.append(String.format("%02d", dataAtual.get(Calendar.MONTH) + 1));
			numero.append(String.format("%02d",dataAtual.get(Calendar.DATE)));
//			numero.append(String.format("%09d", ordemBancariaBO.getNextCodeValue("ordem_bancaria_cod_seq")));

		
		return numero.toString();
	}
	
	private List<DevolucaoRecursosVO> gerarDevolucoes(int quantidade){
		
		List<DevolucaoRecursosVO> listaDevolucoes = new ArrayList<DevolucaoRecursosVO>();

		BigDecimal[] naoEfetivado = new BigDecimal[3];
		for (int i = 0; i < quantidade; i++) {
			DevolucaoRecursosVO devolucaoRecursoVO = new DevolucaoRecursosVO();
			devolucaoRecursoVO.setId(Long.valueOf(i));
			devolucaoRecursoVO.setDataDevolucao(new Date());

//			devolucaoRecursoVO.setValorASerDevolvido(new BigDecimal(500.00));

			
			SituacaoDevolucaoRecursosVO situacaoVO = new SituacaoDevolucaoRecursosVO();
			situacaoVO.setNome(SituacaoDevolucaoRecursosEnum.EFETIVADA.getNome());
			devolucaoRecursoVO.setSituacaoVO(situacaoVO);
			
			InstrumentoVO instrumentoVO = new InstrumentoVO();
//			instrumentoVO.setNumero("7845621");
//
//			PerfilUnidadeOrganizacionalVO perfilUnidade = new PerfilUnidadeOrganizacionalVO();
//			perfilUnidade.setSigla("STDS");
//			UnidadeOrganizacionalVO unidade = new UnidadeOrganizacionalVO();
//			unidade.setPerfilUnidadeOrganizacionalVO(perfilUnidade);
//
//			instrumentoVO.setUnidadeOrganizacionalVO(unidade);
//
//			PessoaVO pessoaVO = new PessoaVO();
//			pessoaVO.setNomeOuRazaoSocial("Municipio de Fortaleza");
//			ParceiroVO parceiroVO = new ParceiroVO();
//			parceiroVO.setPessoaVO(pessoaVO);
//			PlanoTrabalhoVO planoTrabalhoVO = new PlanoTrabalhoVO();
//			planoTrabalhoVO.setParceiroVO(parceiroVO);
//
//			instrumentoVO.setPlanoTrabalhoVO(planoTrabalhoVO);
			
			devolucaoRecursoVO.setInstrumentoVO(instrumentoVO);
			devolucaoRecursoVO.setNumero("D1454");
			
			List<DevolucaoParaFonteVO> listaRecursos = new ArrayList<DevolucaoParaFonteVO>();
			
			SituacaoOrdemBancariaVO situacaoObtTesouroVO = new SituacaoOrdemBancariaVO();
			situacaoObtTesouroVO.setCodigo(SituacaoOrdemBancariaEnum.EFETIVADA.getCodigo());
			
			SituacaoOrdemBancariaVO situacaoObtFundesVO = new SituacaoOrdemBancariaVO();
			situacaoObtFundesVO.setCodigo(SituacaoOrdemBancariaEnum.EFETIVADA.getCodigo());
			
			SituacaoOrdemBancariaVO situacaoObtContrapartidaVO = new SituacaoOrdemBancariaVO();
			situacaoObtContrapartidaVO.setCodigo(SituacaoOrdemBancariaEnum.EFETIVADA.getCodigo());
			
			OrdemBancariaVO ordemBancariaTesouro = new OrdemBancariaVO();
			ordemBancariaTesouro.setNumeroObt("20150106000000348");
			ordemBancariaTesouro.setSituacaoOrdemBancariaVO(situacaoObtTesouroVO);
			
			OrdemBancariaVO ordemBancariaFundes = new OrdemBancariaVO();
			ordemBancariaFundes.setNumeroObt("20141218000000081");
			ordemBancariaFundes.setSituacaoOrdemBancariaVO(situacaoObtFundesVO);
			
			OrdemBancariaVO ordemBancariaContrapartida = new OrdemBancariaVO();
			ordemBancariaContrapartida.setNumeroObt("20141216000000057");
			ordemBancariaContrapartida.setSituacaoOrdemBancariaVO(situacaoObtContrapartidaVO);
			
			
			DevolucaoParaFonteVO devolucaoParaFonteVOTesouro = new DevolucaoParaFonteVO();
			devolucaoParaFonteVOTesouro.setFonte("Tesouro");
			devolucaoParaFonteVOTesouro.setOrdemBancariaVO(ordemBancariaTesouro);
			ordemBancariaTesouro.setValorObt(new BigDecimal(random.nextInt(100)+1 * 5.00));

//			devolucaoParaFonteVOTesouro.setValorRecurso(new BigDecimal(1000.00));
//			devolucaoParaFonteVOTesouro.setValorDeDevolucaoParaRecurso(new BigDecimal(250.00));
			
			DevolucaoParaFonteVO devolucaoParaFonteVOFundes = new DevolucaoParaFonteVO();
			devolucaoParaFonteVOFundes.setFonte("FUNDES");
			devolucaoParaFonteVOFundes.setOrdemBancariaVO(ordemBancariaFundes);
			ordemBancariaFundes.setValorObt(new BigDecimal(random.nextInt(100)+1 * 5.00));
//			devolucaoParaFonteVOFundes.setValorRecurso(new BigDecimal(500.00));
//			devolucaoParaFonteVOFundes.setValorDeDevolucaoParaRecurso(new BigDecimal(125.00));
						
			DevolucaoParaFonteVO devolucaoParaFonteVOContrapartida = new DevolucaoParaFonteVO();
			devolucaoParaFonteVOContrapartida.setFonte("Contrapartida");
			devolucaoParaFonteVOContrapartida.setOrdemBancariaVO(ordemBancariaContrapartida);
//			ContrapartidaVO contrapartidaVO = new ContrapartidaVO();
			ordemBancariaContrapartida.setValorObt(new BigDecimal(random.nextInt(100)+1 * 5.00));
//			contrapartidaVO.setValorContrapartida(new BigDecimal(500.00));
//			devolucaoParaFonteVOContrapartida.setValorRecurso(new BigDecimal(500.00));
//			devolucaoParaFonteVOContrapartida.setValorDeDevolucaoParaRecurso(new BigDecimal(125.00));
				
			listaRecursos.add(devolucaoParaFonteVOTesouro);
			listaRecursos.add(devolucaoParaFonteVOFundes);
			listaRecursos.add(devolucaoParaFonteVOContrapartida);
			devolucaoRecursoVO.setListaDevolucaoParaFonteVO(listaRecursos);

			BigDecimal valorTotalDevolvido = BigDecimal.ZERO;
			for(DevolucaoParaFonteVO devolucaoParaFonteVO : devolucaoRecursoVO.getListaDevolucaoParaFonteVO()){
				valorTotalDevolvido =  valorTotalDevolvido.add(devolucaoParaFonteVO.getOrdemBancariaVO().getValorObt());
			}
			devolucaoRecursoVO.setValorASerDevolvido(valorTotalDevolvido);
			
			listaDevolucoes.add(devolucaoRecursoVO);
		}
		
		return listaDevolucoes;
	}
}
