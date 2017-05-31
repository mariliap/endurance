package research.mpl.backend.smart.util.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import research.mpl.backend.smart.metaheuristics.network.Execution;


public class ResultsUtils {

	private static final String FORMATO_ARQUIVO_TEXTO = ".txt";
	private static final String FORMATO_ARQUIVO_IMAGEM = ".png";
	private static final String EXECUCAO_LABEL = " - SIM ";
	//private static final String EXECUCAO_LABEL = "-Execucao=";
	private static final String DESCRICAO_LABEL = " - DESCRIPTION";
	private static final String CONVERGENCIA_EM_MEDIA_LABEL = "-ConvergenciaEmMedia";
	private static final String CONVERGENCIA_EM_BOX_PLOT_LABEL = "-ConvergenciaEmBoxPlot";
	private static final String ASPAS = "\"";
	private static final String TAB = "\\t";
	
	public void ConvertResultsToStandardFormat() {
		
	}
	
	
	public static void main(String[] args) {
		

		String resultspath = null;
		try {

			Properties experimentsConfiguration = new Properties();
			InputStream experimentsConfigurationFilePath = Execution.class.getResourceAsStream("/main/resources/experiments.properties");
			experimentsConfiguration.load(experimentsConfigurationFilePath);

			resultspath = experimentsConfiguration.getProperty("resultspath");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int numExecucoes = 10;
		int numTrackedIterations =  31;
		CustomFileUtils fileUtils = new CustomFileUtils();
		String experimentName = "MLP_1_LAYER - BackpropagationTrainer - 5 NEURONIOS - 32000 AMOSTRAS" +EXECUCAO_LABEL;
		String newExperimentName = experimentName.replaceAll("\\s","").replaceAll("-","_") ;
		String filepath = resultspath + "/" + experimentName;
		String newFilepath = resultspath + "/" + newExperimentName;
		
//		for (int i = 0; i < numExecucoes; i++) {
//			
//			fileUtils.openFile(filepath + i + ".txt");
//			System.out.println(filepath + i + ".txt");
//			//experimentos[i] = fileUtils.readFile(numTrackedIterations, filepath + i + ".txt");
//		}
		try {
    		for (int i = 0; i < numExecucoes; i++) {
    			File file = new File(filepath + i + FORMATO_ARQUIVO_TEXTO);
    	        File targetFile = new File(newFilepath + i + FORMATO_ARQUIVO_TEXTO);
    	        
    	        FileUtils.copyFile(file, targetFile);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
