package research.mpl.backend.smart.metaheuristics.network;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import research.mpl.backend.smart.metaheuristics.network.training.BackpropagationTrainer;
import research.mpl.backend.smart.metaheuristics.network.training.PSOTrainer;
import research.mpl.backend.smart.metaheuristics.network.training.PseudoInverseTrainer;
import research.mpl.backend.smart.metaheuristics.network.training.TrainerRunnable;
import research.mpl.backend.smart.problems.networkBPRegression.ESNWeightsOptimizationProblem;

import javax.inject.Inject;

public class Execution {

    @Inject
    private ESNWeightsOptimizationProblem eSNWeightsOptimizationProblem;

	private enum TrainingMechanism {
		PSEUDOINVERSE, PSO, BACKPROPAGATION
	}

	public static void main(String[] args) {
        Execution execution = new Execution();
		execution.start();
		

		// try {
		//
		// Runtime.getRuntime().exec("cmd /c start runRScript.bat"); // With the
		// start command, a separate command window will be opened, and any
		// output from the batch file will be displayed there.
		// //OR
		// Runtime.getRuntime().exec("cmd /c runRScript.bat");// Without the
		// start command, you can read the output from the subprocess in Java if
		// desired.
		//
		// //On UNIX-likes you have the shebang (#!) at the start of a file to
		// specify the program that executes it.
		//
		// //"optparse" is a command line parser library used with Rscript to
		// write "#!" shebang scripts that accept short and long flag/options.
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// http://eclipsesource.com/blogs/2009/10/02/executable-wars-with-jetty/
	}

	public void start(){
		String resultspath = null;
		try {

			Properties experimentsConfiguration = new Properties();
			InputStream experimentsConfigurationFilePath =
					Execution.class.getResourceAsStream("/experiments.properties");
			experimentsConfiguration.load(experimentsConfigurationFilePath);

			resultspath = experimentsConfiguration.getProperty("resultspath");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Parameters ----------------------------------------------------------
		TrainingMechanism trainingMechanism = TrainingMechanism.BACKPROPAGATION;
		boolean updateInternalLayerWeights;
		//----------------------------------------------------------------------
		ESNWeightsOptimizationProblem problem = null;
		TrainerRunnable trainer = null;

		switch (trainingMechanism) {

			case PSEUDOINVERSE:
				updateInternalLayerWeights = false;
//				problem = new ESNWeightsOptimizationProblem(updateInternalLayerWeights);
				trainer = new PseudoInverseTrainer(problem, resultspath);
				break;

			case PSO:
				updateInternalLayerWeights = true;
//				problem = new ESNWeightsOptimizationProblem(updateInternalLayerWeights);
				trainer = new PSOTrainer(problem, resultspath);
				break;

			case BACKPROPAGATION:
				updateInternalLayerWeights = true;
				problem = eSNWeightsOptimizationProblem;
                        //new ESNWeightsOptimizationProblem(updateInternalLayerWeights);
                eSNWeightsOptimizationProblem.start(updateInternalLayerWeights);
				trainer = new BackpropagationTrainer(problem, resultspath);
				break;
		}

		MetaHeuristicsNetwork network = problem.getNetwork();

		network.printNetworkTopologyToFile(resultspath + "/NetworkTopologyStructure.csv");
		network.printNetworkWeightsToFile(resultspath + "/NetworkTopologyWeights.csv");
		System.out.println("Network topology have been written to file NetworkTopology");

		resultspath += "/" + problem.getInternalLayerType() + " - " + trainer.getName() + " - "
				+ problem.getNetwork().getNetworkTopology().getNumResNodes() + " NEURONIOS - "
				+ problem.getDataSet().getNumberOfPatterns()+" AMOSTRAS - SIM ";

		System.out.println("Resultpath:\n" + resultspath);
		String resultpathTemp = null;

		for (int i = 0; i < 1; i++) {
			resultpathTemp = resultspath + i +".txt" ;

			trainer.setResultsPath(resultpathTemp);

			trainer.run();
		}
	}

}
