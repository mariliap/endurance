///*
// * ****************************************************************************
// * Copyright (c) 2013
// * Todos os direitos reservados, com base nas leis brasileiras de copyright
// * Este software � confidencial e de propriedade intelectual da UFPE
// * ****************************************************************************
// * Projeto: BONS - Brazilian Optical Network Simulator
// * Arquivo: NeuralNetworkProblem.java
// * ****************************************************************************
// * Hist�rico de revis�es
// * Nome				Data		Descri��o
// * ****************************************************************************
// * Danilo Ara�jo	11/05/2013		Vers�o inicial
// * ****************************************************************************
// */
//package research.mpl.backend.smart.problems.networkBPRegression;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.LineNumberReader;
//import java.text.NumberFormat;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Vector;
//
//import server.jmetal.core.Solution;
//import server.jmetal.core.Variable;
//import server.jmetal.encodings.solutionType.RealSolutionType;
//import server.jmetal.metaheuristics.singleObjective.rna.mlp.AvaliadorErroEmq;
//import server.jmetal.metaheuristics.singleObjective.rna.mlp.ProblemSample;
//import server.jmetal.metaheuristics.singleObjective.rna.mlp.RedeNeural;
//import server.jmetal.util.EnduranceException;
///**
// *
// * @author Danilo
// * @since 11/05/2013
// */
//public class MultiLayerPerceptronProblem extends NNTrainingProblem {
//	private RedeNeural rna;
//	private AvaliadorErroEmq ae = new AvaliadorErroEmq();
//	private List<ProblemSample> padroesTreinamento = new Vector<>();
//
//	private double minBP;
//
//	private double maxBP;
//
//	/**
//	 *
//	 * @param numInputs
//	 *            Numero de entradas da rede neural
//	 * @param hiddenUnits
//	 *            N�mero de neur�nios na camada escondida
//	 */
//	public MultiLayerPerceptronProblem(int numInputs, int hiddenUnits, double minBP, double maxBP) {
//		System.out.println("Criando a rede neural");
//		rna = new RedeNeural(numInputs, 1, hiddenUnits, true);
//		rna.inicializarNeuronios();
//		this.minBP = minBP;
//		this.maxBP = maxBP;
//		System.out.println("Lendo dados");
//		lerDados(padroesTreinamento, rna, 8000, minBP, maxBP);
//		numberOfVariables_ = (numInputs + 1) * hiddenUnits + hiddenUnits + 1;
//		numberOfObjectives_ = 1;
//		numberOfConstraints_ = 0;
//		problemName = "MLP with 1 hidden layer and 1 output";
//
//		upperLimit_ = new double[numberOfVariables_];
//		lowerLimit_ = new double[numberOfVariables_];
//		for (int var = 0; var < numberOfVariables_; var++) {
//			lowerLimit_[var] = -20;
//			upperLimit_[var] = 20;
//		}
//
//		try {
//			solutionGenerator_ = new RealSolutionType(this);
//		} catch (ClassNotFoundException e) {
//		}
//	}
//
//	/**
//	 * Evaluates a solution
//	 *
//	 * @param solution
//	 *            The solution to evaluate
//	 * @throws EnduranceException
//	 */
//	public void evaluate(Solution solution) throws EnduranceException {
//		Variable[] decisionVariables = solution.getDecisionVariablesArray();
//		double[] pesos = new double[decisionVariables.length];
//		for (int var = 0; var < numberOfVariables_; var++) {
//			pesos[var] = decisionVariables[var].getValue();
//		}
//		rna.atualizarPesos(pesos);
//		double emq = rna.getMedia(padroesTreinamento, false);
////		System.out.printf("Avalia��o de indiv�duo = %.8f \n", emq);
//		solution.setObjective(0, emq);
//	}
//
//	@Override
//	public void saveNetwork(Solution solution, String path) throws EnduranceException {
//		Variable[] decisionVariables = solution.getDecisionVariablesArray();
//		double[] pesos = new double[decisionVariables.length];
//		for (int var = 0; var < numberOfVariables_; var++) {
//			pesos[var] = decisionVariables[var].getValue();
//		}
//		rna.atualizarPesos(pesos);
//		rna.salvarRede(path);
//	}
//
//	/**
//	 * @param padroesTreinamento
//	 * @param padroesValidacao
//	 */
//	private static void lerDados(List<ProblemSample> padroesTreinamento, RedeNeural rede, int maxPadroes, double minBP, double axBP) {
//		String linha = null;
//		String[] values = null;
//		NumberFormat nf = NumberFormat.getInstance();
//		int padroesA = 0;
//		int padroesB = 0;
//		int padroesC = 0;
//		int padroesD = 0;
//
//		double pb = 0;
//		double w = 0;
//		double oxc = 0;
//		double algebraicConectivity = 0;
//		double naturalConectivity = 0;
//		double density = 0;
//		double averageDegree = 0;
//		double averagePathLength = 0;
//		double clusteringCoefficient = 0;
//		double diameter = 0;
//		double entropy = 0;
//		double cost = 0;
//		int count = 0;
//		//trocar pela pasta da sua m�quina
//		File dirBase = new File("D:\\Workspaces\\WorkspaceSTS_3.3.0\\experimentos");
//		File subDir = null;
//		FileReader fr = null;
//		LineNumberReader lnr = null;
//		lblExt: for (String dir : dirBase.list()) {
//			subDir = new File(dirBase.getAbsolutePath() + File.separator + dir);
//			if (subDir.isDirectory()) {
//				for (String arq : subDir.list()) {
//					if (arq.contains("_a_")) {
//						continue;
//					}
//					try {
//						fr = new FileReader(new File(subDir.getAbsolutePath() + File.separator + arq));
//						lnr = new LineNumberReader(fr);
//						linha = lnr.readLine();
//						lblInt: while (linha != null) {
//							// System.out.println(linha);
//							values = linha.split(";");
//							cost = nf.parse(values[0]).doubleValue();
//							pb = nf.parse(values[1]).doubleValue();
//							if (pb <=  minBP|| pb > maxPadroes) {
//								linha = lnr.readLine();
//								continue lblInt;
//							}
////							if (pb < .25) {
////								if (padroesA == maxPadroes/4){
////									System.out.println("Padr�es da classe A lidos");
////								}
////								if (padroesA > maxPadroes / 4) {
////									linha = lnr.readLine();
////									continue lblInt;
////								} else {
////									padroesA++;
////								}
////							} else if (pb > .75) {
////								if (padroesD == maxPadroes/4){
////									System.out.println("Padr�es da classe D lidos");
////								}
////								if (padroesD > maxPadroes / 4) {
////									linha = lnr.readLine();
////									continue lblInt;
////								} else {
////									padroesD++;
////								}
////							} else if (pb > .50) {
////								if (padroesC == maxPadroes/4){
////									System.out.println("Padr�es da classe C lidos");
////								}
////								if (padroesC > maxPadroes / 4) {
////									linha = lnr.readLine();
////									continue lblInt;
////								} else {
////									padroesC++;
////								}
////							} else {
////								if (padroesB == maxPadroes/4){
////									System.out.println("Padr�es da classe B lidos");
////								}
////								if (padroesB > maxPadroes / 4) {
////									linha = lnr.readLine();
////									continue lblInt;
////								} else {
////									padroesB++;
////								}
////							}
//							w = nf.parse(values[2]).doubleValue();
//							oxc = nf.parse(values[3]).doubleValue();
//							algebraicConectivity = nf.parse(values[4]).doubleValue();
//							naturalConectivity = nf.parse(values[5]).doubleValue();
//							density = nf.parse(values[6]).doubleValue();
//							averageDegree = nf.parse(values[7]).doubleValue();
//							averagePathLength = nf.parse(values[8]).doubleValue();
//							clusteringCoefficient = nf.parse(values[9]).doubleValue();
//							diameter = nf.parse(values[10]).doubleValue();
//							entropy = nf.parse(values[11]).doubleValue();
//
//							// remover outliers
//							if (algebraicConectivity > 0 && diameter > 0 && diameter < (Integer.MAX_VALUE - 1)
//									&& cost < 20141 && pb < 1) {
//								padroesTreinamento.add(new ProblemSample(new double[] { w, oxc,
//										algebraicConectivity, naturalConectivity, averagePathLength,
//										clusteringCoefficient, diameter, entropy }, new double[] { pb }));
//								count++;
//								if (count > maxPadroes) {
//									break lblExt;
//								}
//							}
//
//							linha = lnr.readLine();
//						}
//
//						fr.close();
//						lnr.close();
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}
//
//		// normalizar
//		double[] minValues = new double[8];
//		double[] maxValues = new double[8];
//		Arrays.fill(minValues, Double.MAX_VALUE);
//		Arrays.fill(maxValues, Double.MIN_VALUE);
//		rede.setMinValues(minValues);
//		rede.setMaxValues(maxValues);
//		for (ProblemSample padrao : padroesTreinamento) {
//			for (int i = 0; i < padrao.getInputs().length; i++) {
//				if (padrao.getInputs()[i] < minValues[i]) {
//					minValues[i] = padrao.getInputs()[i];
//				}
//				if (padrao.getInputs()[i] > maxValues[i]) {
//					maxValues[i] = padrao.getInputs()[i];
//				}
//			}
//		}
//		for (ProblemSample padrao : padroesTreinamento) {
//			for (int i = 0; i < padrao.getInputs().length; i++) {
//				padrao.setInputValue(i, (padrao.getInputs()[i] - minValues[i]) / (maxValues[i] - minValues[i]));
//			}
//		}
//		System.out.println("Max/min values:");
//		for (int i = 0; i < minValues.length; i++) {
//			System.out.printf("%.4f %.4f\n", minValues[i], maxValues[i]);
//		}
//		System.out.println();
//	}
//
//}
