//package research.mpl.backend.smart.util.database;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//import org.apache.commons.io.FileUtils;
//import research.mpl.backend.smart.metaheuristics.network.Execution;
//
//
///**
// * R commands temp <- read.table ... sapply(temp,class) sapply(Geracao, class) //Geracao is a column which.max( matrix[,2] )
// */
////https://github.com/qinwf/awesome-R
//
///* D3.js as a visualization tool
//https://github.com/mbostock/d3/wiki/Gallery
//*/
//public class Analysis {
//
//	private static final String FORMATO_ARQUIVO_TEXTO = ".txt";
//	private static final String FORMATO_ARQUIVO_IMAGEM = ".png";
//	private static final String EXECUCAO_LABEL = " - SIM ";
//	private static final String DESCRICAO_LABEL = " - DESCRIPTION";
//	private static final String CONVERGENCIA_EM_MEDIA_LABEL = "-ConvergenciaEmMedia";
//	private static final String CONVERGENCIA_EM_BOX_PLOT_LABEL = "-ConvergenciaEmBoxPlot";
//	private static final String ASPAS = "\"";
//	private static final String TAB = "\\t";
//
//	private int numExecucoesPorExperimento;
//
//	private CustomFileUtils arquivo = null;
//	private String diretorioResultados = null;
//
//	public Analysis(CustomFileUtils arquivo, String resultspath, int numExecucoesPorExperimento) {
//		this.arquivo = arquivo;
//		// this.diretorioResultados = Experimento.DIRETORIO_RESULTADOS + sigla + "/";
//		this.diretorioResultados = resultspath;
//		this.numExecucoesPorExperimento = numExecucoesPorExperimento;
//	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//
//		// analisarAG();
//		// analisarPSO();
//		analisarPSOIris();
//	}
//
//	public static void analisarAG() {
//		CustomFileUtils arquivo = new CustomFileUtils("");
//
//		Analysis analise = new Analysis(arquivo, "AG", 30);
//
//		String[] experimentos = { "ElitismoAritmetica", "ElitismoDiscreta", "TorneioAritmetica", "TorneioDiscreta" };
//
//		analise.getInfoConvergenciaEmMedia(experimentos);
//		analise.getGraficoConvergenciaEmMedia(experimentos, true);
//		analise.getGraficoConvergenciaEmMedia(experimentos, false);
//
//		String[][] experimentosDS = { { "ElitismoAritmetica", "Elitismo & Rep. Aritmetica (AG1)" },
//				{ "ElitismoDiscreta", "Elitismo & Rep. Discreta N-Point (AG2)" }, { "TorneioAritmetica", "Torneio & Rep. Aritmetica (AG3)" },
//				{ "TorneioDiscreta", "Torneio & Rep. Discreta N-Point (AG4)" } };
//
//		analise.getInfoBoxPlot(experimentosDS, "ResultadosExperimentos");
//		analise.getGraficoBoxplotResultados("ResultadosExperimentos");
//	}
//
//	public static void analisarPSO() {
//		CustomFileUtils arquivo = new CustomFileUtils("");
//		Analysis analise = new Analysis(arquivo, "PSO", 30);
//
//		// String[] experimentos = {"PSO1", "PSO2", "PSO3", "PSO4", "PSO5", "PSO6", "PSO7", "PSO8", "PSO9"};
//		String[] experimentos = { "PSO10", "PSO11", "PSO4", "PSO12" };
//
//		analise.getInfoConvergenciaEmMedia(experimentos);
//		analise.getGraficoConvergenciaEmMedia(experimentos, true);
//		analise.getGraficoConvergenciaEmMedia(experimentos, false);
//
//		analise.getInfoConvergenciaEmBoxPlot(experimentos);
//		analise.getGraficoConvergenciaEmBoxPlot(experimentos, true);
//		analise.getGraficoConvergenciaEmBoxPlot(experimentos, false);
//
//		// String[][] experimentosDS = { {"PSO1", "PSO 1"},
//		// {"PSO2", "PSO 2"},
//		// {"PSO3", "PSO 3"},
//		// {"PSO4", "PSO 4"},
//		// {"PSO5", "PSO 5"},
//		// {"PSO6", "PSO 6"},
//		// {"PSO7", "PSO 7"},
//		// {"PSO8", "PSO 8"},
//		// {"PSO9", "PSO 9"}};
//
//		String[][] experimentosDS = { { "PSO10", "PSO 10" }, { "PSO11", "PSO 11" }, { "PSO4", "PSO 4" }, { "PSO12", "PSO 12" } };
//
//		analise.getInfoBoxPlot(experimentosDS, "ResultadosExperimentos");
//		analise.getGraficoBoxplotResultados("ResultadosExperimentos");
//	}
//
//	public static void analisarPSOIris() {
//		// CustomFileUtils arquivo = new CustomFileUtils("");
//		// Analysis analise = new Analysis(arquivo, PSO.SIGLA, 10);
//
//		// String[] experimentos = {"PSOIris1", "PSOIris2"};
//
//		// analise.getInfoConvergenciaEmMedia(experimentos);
//		// analise.getGraficoConvergenciaEmMedia(experimentos);
//		//
//		// analise.getInfoConvergenciaEmBoxPlot(experimentos);
//		// analise.getGraficoConvergenciaEmBoxPlot(experimentos);
//		//
//		//
//		// String[][] experimentosDS = { {"PSOIris1", "PSO Local"},
//		// {"PSOIris2", "PSO Global"}};
//
//		// analise.getInfoBoxPlot(experimentosDS, "ResultadosExperimentos");
//		// analise.getGraficoBoxplotResultados("ResultadosExperimentos");
//
//		//
//		// analise.psoBaseTeste(experimentosDS);
//		// analise.getGraficoBoxplotResultados("ResultadoPSO_Training");
//		// analise.getGraficoBoxplotResultados("ResultadoPSO_Test");
//
//		String resultspath = null;
//		try {
//
//			Properties experimentsConfiguration = new Properties();
//			InputStream experimentsConfigurationFilePath
//					= Execution.class.getResourceAsStream("/main/resources/experiments.properties");
//			experimentsConfiguration.load(experimentsConfigurationFilePath);
//
//			resultspath = experimentsConfiguration.getProperty("resultspath");
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		String fileNameBackPropagation = "MLP_1_LAYER - BackpropagationTrainer - 5 NEURONIOS - 32000 AMOSTRAS";
//		String fileNamePSO = "MLP_1_LAYER - PSOTrainer - 5 NEURONIOS - 32000 AMOSTRAS";
//
//		int numExecucoesPorExperimento = 10;
//
//		CustomFileUtils arquivo = new CustomFileUtils("");
//		Analysis analise = new Analysis(arquivo, resultspath, numExecucoesPorExperimento);
//
//		String[] experimentos2 = { fileNameBackPropagation, fileNamePSO };
//
//		analise.getInfoConvergenciaEmMedia(experimentos2);
//		analise.getGraficoConvergenciaEmMedia(experimentos2, true);
//		analise.getGraficoConvergenciaEmMedia(experimentos2, false);
//
//		analise.getInfoConvergenciaEmBoxPlot(experimentos2);
//		analise.getGraficoConvergenciaEmBoxPlot(experimentos2, true);
//		analise.getGraficoConvergenciaEmBoxPlot(experimentos2, false);
//
//		// String[][] experimentosDS2 = { {"BACKPROPAGATION", "BackPropagation"}};
//		//
//		// analise.getInfoBoxPlot(experimentosDS2, "ResultadosExperimentos");
//		// analise.getGraficoBoxplotResultados("ResultadosExperimentos");
//	}
//
//	// public void psoBaseTeste(String[][] experimentos){
//	//
//	// String arquivosBase = null;
//	// String arquivoResultadoTraining = diretorioResultados + "ResultadoPSO_Training" + FORMATO_ARQUIVO_TEXTO;;
//	// String arquivoResultadoTest = diretorioResultados + "ResultadoPSO_Test" + FORMATO_ARQUIVO_TEXTO;;
//	//
//	// int numberOfdimensions = ClassificacaoIris.NUMBER_OF_INPUTS * ClassificacaoIris.NUMBER_OF_NEURONS_L1
//	// + ClassificacaoIris.NUMBER_OF_NEURONS_L1 * ClassificacaoIris.NUMBER_OF_CLASSES;
//	// double[] weights = new double[numberOfdimensions];
//	//
//	// ClassificacaoIris classificacaoIrisBaseTraining = new ClassificacaoIris(numberOfdimensions, SampleSetType.TRAINING_SET);
//	// ClassificacaoIris classificacaoIrisBaseTest = new ClassificacaoIris(numberOfdimensions, SampleSetType.TEST_SET);
//	// double eqmsTraining[] = new double[experimentos.length];
//	// double eqmsTest[] = new double[experimentos.length];
//	// StringBuffer eqmsTrainingStr = new StringBuffer();
//	// StringBuffer eqmsTestStr = new StringBuffer();
//	//
//	// eqmsTrainingStr.append("Resultado\tExperimento\n");
//	// eqmsTestStr.append("Resultado\tExperimento\n");
//	//
//	// for (int i = 0; i < experimentos.length; i++) {
//	// arquivosBase = diretorioResultados + experimentos[i][0] + "/" + experimentos[i][0] + EXECUCAO_LABEL;
//	// for(int k = 0; k < numExecucoesPorExperimento; k++){
//	// String temp = this.arquivo.openFile(arquivosBase+ k + FORMATO_ARQUIVO_TEXTO);
//	// // System.out.println(arquivosBase+ k + FORMATO_ARQUIVO_TEXTO);
//	// // System.out.println(temp.split("f")[1]);
//	// String temp2 = temp.split("# ")[1];
//	// String temp3 = (temp2.substring(1)).split("]")[0];
//	// String[] weightsStr = temp3.split("\t");
//	//
//	// for (int j = 0; j < weights.length; j++) {
//	// weights[j] = Double.parseDouble(weightsStr[j]);
//	// }
//	//
//	// eqmsTraining[i] = classificacaoIrisBaseTraining.calculateFitness(weights);
//	// eqmsTest[i] = classificacaoIrisBaseTest.calculateFitness(weights);
//	//
//	// eqmsTrainingStr.append(eqmsTraining[i]);
//	// eqmsTrainingStr.append("\t");
//	// eqmsTrainingStr.append(experimentos[i][1]);
//	// eqmsTrainingStr.append("\n");
//	//
//	// eqmsTestStr.append(eqmsTest[i]);
//	// eqmsTestStr.append("\t");
//	// eqmsTestStr.append(experimentos[i][1]);
//	// eqmsTestStr.append("\n");
//	// }
//	// }
//	// this.arquivo.saveFile(eqmsTrainingStr.toString(), arquivoResultadoTraining);
//	// this.arquivo.saveFile(eqmsTestStr.toString(), arquivoResultadoTest);
//	//
//	// }
//
//	/**
//	 * Cria arquivo de compilaÃ§Ã£o de resultados a fim de criar um grÃ¡fico boxplot
//	 */
//	public void getInfoBoxPlot(String[][] experimentos, String nomeCustomFileUtilsResultados) {
//
//		String arquivosBase = null;
//		String arquivoDescricao = null;
//		String arquivoResultado =
//				diretorioResultados + nomeCustomFileUtilsResultados + FORMATO_ARQUIVO_TEXTO;
//
//		// O primeiro elemento do arquivo é o fitness da população inicial, e o restante corresponde aos fitness dos ciclos
//		// IMPORTANTE: Deve-se usar nÃºmero de ciclos + 1, se o arquivo tiver o fitness da populaÃ§Ã£o inicial (i.e. se o arquivo comeÃ§ar com "c0")
//		int totalCiclos = 0;
//
//		StringBuffer melhoresFitness = new StringBuffer();
//		melhoresFitness.append("Resultado\tExperimento\n"); // header
//
//		for (int i = 0; i < experimentos.length; i++) {
//			arquivoDescricao = diretorioResultados + "/" + experimentos[i][0] + DESCRICAO_LABEL + FORMATO_ARQUIVO_TEXTO;
//			arquivosBase = diretorioResultados + "/" + experimentos[i][0] + EXECUCAO_LABEL;
//
//			totalCiclos = getNumCiclos(arquivoDescricao, arquivosBase);
//			melhoresFitness.append(getMelhoresFitness(arquivosBase, totalCiclos, true, experimentos[i][1]));
//		}
//
//		if (this.arquivo.fileExists(arquivoResultado)) {
//			File f1 = new File(arquivoResultado);
//			f1.delete();
//		}
//
//		System.out.println(melhoresFitness.toString());
//		this.arquivo.saveFile(melhoresFitness.toString(), arquivoResultado);
//	}
//
//	/**
//	 * Extrai a lista dos valores encontrados por cada execuÃ§Ã£o de um experimento
//	 *
//	 * @param arquivosBase
//	 * @param plotEtiqueta
//	 * @param etiqueta
//	 * @return String com lista dos valores encontrados por cada execuÃ§Ã£o do experimento
//	 */
//	public String getMelhoresFitness(String arquivosBase, int numLinhas, boolean plotEtiqueta, String etiqueta) {
//
//		String[][] experimentos = new String[numExecucoesPorExperimento][numLinhas];
//		for (int i = 0; i < numExecucoesPorExperimento; i++) {
//			experimentos[i] = this.arquivo.readFile(numLinhas, arquivosBase + i + FORMATO_ARQUIVO_TEXTO);
//		}
//
//		String temp = null;
//		StringBuffer melhoresCadaExecucao = new StringBuffer();
//
//		for (int i = 0; i < numExecucoesPorExperimento; i++) {
//			// pega a Ãºltima linha, referente ao resultado da Ãºltima execuÃ§Ã£o (ciclo) do experimento i
//			temp = experimentos[i][numLinhas - 1];
//			melhoresCadaExecucao.append(temp.split("=")[1]);
//			if (plotEtiqueta) {
//				melhoresCadaExecucao.append("\t");
//				melhoresCadaExecucao.append(etiqueta);
//			}
//			melhoresCadaExecucao.append("\n");
//		}
//
//		return melhoresCadaExecucao.toString();
//	}
//
//	public void getInfoConvergenciaEmBoxPlot(String[] experimentos) {
//
//		String arquivoDescricao = null;
//		String arquivosBase = null;
//		String arquivoResultado = null;
//
//		int intervaloCiclosParaAnalise = 1;// 1000;// Analisa resultados a cada 1000 ciclos da execuÃ§Ã£o
//		int totalCiclos = 0;
//		for (int i = 0; i < experimentos.length; i++) {
//			arquivoDescricao = diretorioResultados + "/" + experimentos[i] + DESCRICAO_LABEL + FORMATO_ARQUIVO_TEXTO;
//
//			arquivosBase = diretorioResultados + "/" + experimentos[i] + EXECUCAO_LABEL;
//
//			arquivoResultado = diretorioResultados + "/" + experimentos[i] + CONVERGENCIA_EM_BOX_PLOT_LABEL + FORMATO_ARQUIVO_TEXTO;
//
//			totalCiclos = getNumCiclos(arquivoDescricao, arquivosBase);
//			getConvergenciaMelhoresIndvEmBoxPlot(arquivosBase, arquivoResultado, numExecucoesPorExperimento, totalCiclos, intervaloCiclosParaAnalise);
//		}
//	}
//
//	public void getConvergenciaMelhoresIndvEmBoxPlot(String arquivosBase, String arquivoResultado, int numExecucoes, int totalCiclos,
//			int intervaloCiclosParaAnalise) {
//
//		String[][] execucoes = new String[numExecucoes][totalCiclos];
//		for (int i = 0; i < numExecucoes; i++) {
//			execucoes[i] = this.arquivo.readFile(totalCiclos, arquivosBase + i + FORMATO_ARQUIVO_TEXTO);
//		}
//
//		String temp = null;
//		StringBuffer melhorValorDoCicloExecucao = new StringBuffer();
//		melhorValorDoCicloExecucao.append("Fitness\tCiclo\n");
//		int c = 0;
//		while (c < totalCiclos) {
//			for (int i = 0; i < numExecucoes; i++) {
//				// pega a linha referente ao melhor resultado do ciclo "c" do experimento "i"
//				temp = execucoes[i][c];
//				melhorValorDoCicloExecucao.append(temp.split("Training Error: ")[1]);
//
//				melhorValorDoCicloExecucao.append("\t");
//				melhorValorDoCicloExecucao.append(c * 100);
//
//				melhorValorDoCicloExecucao.append("\n");
//			}
//			System.out.println(c);
//
//			if ((c + intervaloCiclosParaAnalise == totalCiclos) && intervaloCiclosParaAnalise != 1) {
//				c = totalCiclos - 1;
//			} else {
//				c += intervaloCiclosParaAnalise;
//			}
//		}
//
//		this.arquivo.saveFile(melhorValorDoCicloExecucao.toString(), arquivoResultado);
//	}
//
//	public void getInfoConvergenciaEmMedia(String[] experimentos) {
//
//		String arquivoDescricao = null;
//		String arquivosBase = null;
//		String arquivoResultado = null;
//		int totalCiclos = 0;
//		for (int i = 0; i < experimentos.length; i++) {
//			arquivoDescricao = diretorioResultados + "/" + experimentos[i] + DESCRICAO_LABEL + FORMATO_ARQUIVO_TEXTO;
//			arquivosBase = diretorioResultados + "/" + experimentos[i] + EXECUCAO_LABEL;
//			arquivoResultado = diretorioResultados + "/" + experimentos[i] + CONVERGENCIA_EM_MEDIA_LABEL + FORMATO_ARQUIVO_TEXTO;
//
//			totalCiclos = getNumCiclos(arquivoDescricao, arquivosBase);
//			getConvergenciaMelhoresIndvEmMedia(arquivosBase, arquivoResultado, numExecucoesPorExperimento, totalCiclos);
//		}
//	}
//
//	public void getConvergenciaMelhoresIndvEmMedia(String arquivosBase, String arquivoResultado, int numExecucoes, int numCiclos) {
//		System.out.println(numCiclos);
//		String[][] experimentos = new String[numExecucoes][numCiclos];
//		for (int i = 0; i < numExecucoes; i++) {
//			System.out.println(arquivosBase + i + FORMATO_ARQUIVO_TEXTO);
//			experimentos[i] = this.arquivo.readFile(numCiclos, arquivosBase + i + FORMATO_ARQUIVO_TEXTO);
//		}
//
//		String linhaExp = null;
//		String[] linhaExpArray = new String[2];
//		double soma = 0;
//		double[] mediaLinha = new double[numCiclos];
//		for (int linha = 0; linha < numCiclos; linha++) {
//			soma = 0;
//			for (int e = 0; e < numExecucoes; e++) {
//				linhaExp = experimentos[e][linha];
//
//				linhaExpArray = linhaExp.split("Training Error: ");
//				soma += Double.parseDouble(linhaExpArray[1]);
//			}
//			mediaLinha[linha] = (double) (soma / (double) numExecucoes);
//		}
//
//		StringBuffer strB = new StringBuffer();
//		strB.append("Geracao\tMedia_do_Melhor_Fitness_da_Geracao_em_" + numExecucoes + "_Execucoes\n");
//		for (int i = 0; i < numCiclos; i++) {
//			strB.append(i * 100);
//			strB.append("\t");
//			strB.append(mediaLinha[i]);
//			strB.append("\n");
//		}
//
//		this.arquivo.saveFile(strB.toString(), arquivoResultado);
//	}
//
//	public int getNumCiclos(String arquivoDescricao, String nomeBaseArquivo) {
//		String descricaoExperimento = this.arquivo.openFile(arquivoDescricao);
//		String temp = descricaoExperimento.split("Cycles")[1];
//		temp = temp.split("\n")[0];
//		temp = temp.split(" = ")[1];
//		int condicaoParada = Integer.parseInt(temp);
//
//		// IMPORTANTE: Deve-se usar nÃºmero de ciclos + 1, se o arquivo tiver o fitness da populaÃ§Ã£o inicial (i.e. se o arquivo comeÃ§ar com "c0")
//		String execucao = this.arquivo.openFile(nomeBaseArquivo + 0 + FORMATO_ARQUIVO_TEXTO);
//		String primeiroCiclo = execucao.split("=")[0];
//		// if(primeiroCiclo.contains("c0")){
//		// condicaoParada = condicaoParada + 1;
//		// }
//		return condicaoParada;
//	}
//
//	public void getGraficoBoxplotResultados(String nomeCustomFileUtilsResultados) {
//		try {
//			RCaller caller = new RCaller();
//			caller.setRscriptExecutable("C:\\Program Files\\R\\R-3.1.0\\bin\\Rscript.exe");
//
//			RCode code = new RCode();
//			code.clear();
//
//			String arquivoResultado = diretorioResultados + nomeCustomFileUtilsResultados + FORMATO_ARQUIVO_TEXTO;
//			String comandoR1 = "experimentos <- read.table(file=" + getArgument(arquivoResultado) + ", header=TRUE, sep=" + getArgument(TAB) + ", colClasses="
//					+ getArgument("numeric") + ")";
//
//			String comandoR3 = "boxplot(Resultado~Experimento, data=experimentos, main=" + getArgument("Desempenho de diferentes experimentos") + ", xlab="
//					+ getArgument("Experimento") + ", ylab=" + getArgument("Fitness") + ")";
//
//			code.addRCode(comandoR1);
//
//			File file = code.startPlot();
//			System.out.println("Plot will be saved to : " + file);
//
//			code.addRCode(comandoR3);
//			code.endPlot();
//
//			caller.setRCode(code);
//			caller.runAndReturnResult("experimentos");
//
//			// System.out.println("--------------------------------------------");
//			// System.out.println("R-Code:");
//			// System.out.println(code.getCode().toString());
//			// System.out.println("--------------------------------------------");
//			// System.out.println("Available results from experimentos object:");
//			// System.out.println(caller.getParser().getXMLFileAsString());
//			// System.out.println("--------------------------------------------");
//			//
//			//
//			// String[] results = caller.getParser().getAsStringArray("Resultado");
//			// for (int i=0;i<results.length;i++) {
//			// System.out.print(results[i]+", ");
//			// }
//
//			// code.showPlot(file);
//			File targetFile = new File(diretorioResultados + "ComparativoDosExperimentos_" + nomeCustomFileUtilsResultados + FORMATO_ARQUIVO_IMAGEM);
//			FileUtils.copyFile(file, targetFile);
//
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//
//	public void getGraficoConvergenciaEmBoxPlot(String[] experimentos, boolean isLogScale) {
//		try {
//			RCaller caller = new RCaller();
//			caller.setRscriptExecutable("C:\\Program Files\\R\\R-3.1.0\\bin\\Rscript.exe");
//			// caller.setRscriptExecutable("C:\\CustomFileUtilss de programas\\R\\R-2.15.2\\bin\\Rscript.exe");
//			// Globals.detect_current_rscript();
//			// caller.setRscriptExecutable(Globals.Rscript_current);
//
//			RCode code = new RCode();
//			code.clear();
//
//			File[] files = new File[experimentos.length];
//			String comandoRead = null;
//			String comandoPlot = null;
//			String arquivoConvergencia = null;
//
//			String tempVar = null;
//			String log = isLogScale ? ", log=" + getArgument("y") : "";
//			for (int i = 0; i < experimentos.length; i++) {
//				arquivoConvergencia = diretorioResultados + "/" + experimentos[i] + CONVERGENCIA_EM_BOX_PLOT_LABEL + FORMATO_ARQUIVO_TEXTO;
//				// tempVar = experimentos[i].replaceAll("\\s","").replaceAll("-","_") + "_conv";
//				tempVar = "data_conv";
//				comandoRead = tempVar + " <- read.table(file=" + getArgument(arquivoConvergencia) + ", header=TRUE, sep=" + getArgument(TAB) + ", colClasses="
//						+ getArgument("numeric") + ")";
//				comandoPlot = "boxplot(Fitness~Ciclo, data=" + tempVar + ", main="
//						+ getArgument("Melhor Fitness em " + numExecucoesPorExperimento + " Execucoes do Experimento (por Ciclo)") + ", xlab="
//						+ getArgument("Ciclo") + ", ylab=" + getArgument("Fitness") + log + ")";
//
//				code.addRCode(comandoRead);
//				files[i] = code.startPlot();
//				code.addRCode(comandoPlot);
//				code.endPlot();
//			}
//			caller.setRCode(code);
//			caller.runOnly();
//
//			// System.out.println("--------------------------------------------");
//			// System.out.println("R-Code:");
//			// System.out.println(code.getCode().toString());
//			// System.out.println("--------------------------------------------");
//			// System.out.println("Available results from experimentos object:");
//			// System.out.println(caller.getParser().getXMLFileAsString());
//			// System.out.println("--------------------------------------------");
//			//
//			//
//			// String[] results = caller.getParser().getAsStringArray("Resultado");
//			// for (int i=0;i<results.length;i++) {
//			// System.out.print(results[i]+", ");
//			// }
//			String logLabel = isLogScale ? "-LogScaleY" : "";
//			for (int i = 0; i < files.length; i++) {
//				File targetFile = new File(diretorioResultados + "/" + experimentos[i] + CONVERGENCIA_EM_BOX_PLOT_LABEL + logLabel + FORMATO_ARQUIVO_IMAGEM);
//				FileUtils.copyFile(files[i], targetFile);
//			}
//
//			// code.showPlot(file);
//			// File targetFile = new File(diretorioResultados + "Results_Mar" + FORMATO_ARQUIVO_IMAGEM );
//			// FileUtils.copyFile(file, targetFile);
//
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//
//	public void getGraficoConvergenciaEmMedia(String[] experimentos, boolean isLogScale) {
//		try {
//			RCaller caller = new RCaller();
//			// caller.setRscriptExecutable("C:\\Program Files\\R\\R-3.1.0\\bin\\Rscript.exe");
//
//			caller.setRscriptExecutable("C:\\Program Files\\R\\R-3.1.0\\bin\\x64\\Rscript.exe");
//
//			// Globals.detect_current_rscript();
//			// caller.setRscriptExecutable(Globals.Rscript_current);
//
//			RCode code = new RCode();
//			code.clear();
//
//			File[] files = new File[experimentos.length];
//			String comandoRead = null;
//			String comandoPlot = null;
//			String arquivoConvergencia = null;
//			String tempVar = null;
//			String log = isLogScale ? ", log=" + getArgument("y") : "";
//			for (int i = 0; i < experimentos.length; i++) {
//				arquivoConvergencia = diretorioResultados + "/" + experimentos[i] + CONVERGENCIA_EM_MEDIA_LABEL + FORMATO_ARQUIVO_TEXTO;
//				// tempVar = experimentos[i].replaceAll("\\s","").replaceAll("-","_") + "_conv";
//				tempVar = "data_conv";
//				comandoRead = tempVar + " <- read.table(file=" + getArgument(arquivoConvergencia) + ", header=TRUE, sep=" + getArgument(TAB) + ", colClasses="
//						+ getArgument("numeric") + ")";
//				comandoPlot = "plot(" + tempVar + ", type=" + getArgument("l") + log + ")";
//
//				code.addRCode(comandoRead);
//				files[i] = code.startPlot();
//				code.addRCode(comandoPlot);
//				code.endPlot();
//			}
//			caller.setRCode(code);
//			caller.runOnly();
//			// caller.runAndReturnResult(experimentos[0]+ "_conv");
//			// System.out.println(code.getCode().toString());
//			// System.out.println(caller.getParser().getXMLFileAsString());
//
//			String logLabel = isLogScale ? "-LogScaleY" : "";
//			for (int i = 0; i < files.length; i++) {
//				File targetFile = new File(diretorioResultados + "/" + experimentos[i] + CONVERGENCIA_EM_MEDIA_LABEL + logLabel + FORMATO_ARQUIVO_IMAGEM);
//				FileUtils.copyFile(files[i], targetFile);
//			}
//
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//	}
//
//	private String getArgument(String arg) {
//		return ASPAS + arg + ASPAS;
//
//	}
//
//	// public void testeValiarveis(){
//	// RandomGenerator random = new RandomGenerator();
//	// try{
//	// /*
//	// * Creating RCaller
//	// *
//	// * http://www.mhsatman.com/rcaller.php
//	// * http://code.google.com/p/rcaller/wiki/Examples
//	// * install.packages("Runiversal")
//	// * TODO tarefas:
//	// * RCaller (fazer funcionar os jÃ¡ gerados)
//	// *
//	// * LOG4J
//	// * EclipseLatex
//	// * Mendeley
//	// *
//	// * Animations
//	// * http://cran.r-project.org/web/packages/animation/index.html
//	// */
//	// RCaller caller = new RCaller();
//	// RCode code = new RCode();
//	// /*
//	// * Full path of the Rscript. Rscript is an executable file shipped with R.
//	// * It is something like C:\\Program File\\R\\bin.... in Windows
//	// */
//	// caller.setRscriptExecutable("C:\\Arquivos de programas\\R\\R-2.15.2\\bin\\Rscript.exe");
//	//
//	// /* We are creating a random data from a normal distribution
//	// * with zero mean and unit variance with size of 100
//	// */
//	// double[] data = new double[100];
//	// for (int i=0;i<data.length;i++){
//	// data[i] = random.getGaussianRandom();
//	// }
//	//
//	// /*
//	// * We are transferring the double array to R
//	// */
//	// code.addDoubleArray("x", data);
//	//
//	// /*
//	// * Adding R Code
//	// */
//	// code.addRCode("my.mean<-mean(x)");
//	// code.addRCode("my.var<-var(x)");
//	// code.addRCode("my.sd<-sd(x)");
//	// code.addRCode("my.min<-min(x)");
//	// code.addRCode("my.max<-max(x)");
//	// code.addRCode("my.standardized<-scale(x)");
//	//
//	// /*
//	// * Combining all of them in a single list() object
//	// */
//	// code.addRCode("my.all<-list(mean=my.mean, variance=my.var, sd=my.sd, min=my.min, max=my.max, std=my.standardized)");
//	//
//	// /*
//	// * We want to handle the list 'my.all'
//	// */
//	// caller.setRCode(code);
//	// caller.runAndReturnResult("my.all");
//	//
//	// double[] results;
//	//
//	// /*
//	// * Retrieving the 'mean' element of list 'my.all'
//	// */
//	// results = caller.getParser().getAsDoubleArray("mean");
//	// System.out.println("Mean is "+results[0]);
//	//
//	// /*
//	// * Retrieving the 'variance' element of list 'my.all'
//	// */
//	// results = caller.getParser().getAsDoubleArray("variance");
//	// System.out.println("Variance is "+results[0]);
//	//
//	// /*
//	// * Retrieving the 'sd' element of list 'my.all'
//	// */
//	// results = caller.getParser().getAsDoubleArray("sd");
//	// System.out.println("Standard deviation is "+results[0]);
//	//
//	// /*
//	// * Retrieving the 'min' element of list 'my.all'
//	// */
//	// results = caller.getParser().getAsDoubleArray("min");
//	// System.out.println("Minimum is "+results[0]);
//	//
//	// /*
//	// * Retrieving the 'max' element of list 'my.all'
//	// */
//	// results = caller.getParser().getAsDoubleArray("max");
//	// System.out.println("Maximum is "+results[0]);
//	//
//	// /*
//	// * Retrieving the 'std' element of list 'my.all'
//	// */
//	// results = caller.getParser().getAsDoubleArray("std");
//	//
//	// /*
//	// * Now we are retrieving the standardized form of vector x
//	// */
//	// System.out.println("Standardized x is ");
//	// for (int i=0;i<results.length;i++) {
//	// System.out.print(results[i]+", ");
//	// }
//	// System.out.println("\n");
//	// }catch(Exception e){
//	// e.printStackTrace();
//	// }
//	// }
//	public void testePlot() {
//		RCaller caller = new RCaller();
//		caller.setRscriptExecutable("C:\\Arquivos de programas\\R\\R-2.15.2\\bin\\Rscript.exe");
//
//		RCode code = new RCode();
//		code.clear();
//
//		double[] numbers = new double[] { 1, 4, 3, 5, 6, 10 };
//
//		code.addDoubleArray("x", numbers);
//		try {
//			File file = code.startPlot();
//			System.out.println("Plot will be saved to : " + file);
//			code.addRCode("plot.ts(x)");
//			code.endPlot();
//
//			caller.setRCode(code);
//			caller.runOnly();
//			code.showPlot(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
