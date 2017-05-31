package research.mpl.backend.smart.metaheuristics.network.training;

import research.mpl.backend.smart.core.Algorithm;
import research.mpl.backend.smart.core.Problem;
import research.mpl.backend.smart.core.SolutionSet;
import research.mpl.backend.smart.metaheuristics.pso.PSO;

import research.mpl.backend.smart.util.EnduranceException;
import research.mpl.backend.smart.util.TimeUtil;

public class PSOTrainer extends TrainerRunnable{
	

	public PSOTrainer(Problem problem, String resultsPath) {
		super(problem, resultsPath);
	}

	@Override
	public String getNamePrefix() {
		return "Local";
	}

	@Override
	public String getNameSuffix() {
		return "";
	}
	
	@Override
	public void execute() throws EnduranceException, ClassNotFoundException {
		
		Problem problem = getProblem();
		String resultsPath = getResultsPath();
		
		Algorithm algorithm;
		algorithm = new PSO(problem, resultsPath);

		// Algorithm parameters
		algorithm.setInputParameter("swarmSize", 30);
		algorithm.setInputParameter("maxIterations", 3001);//334

		//addMutationOperator(algorithm);// "Turbulence" operator

		// Execute the Algorithm
		long initTime = System.currentTimeMillis();
		
		SolutionSet population = algorithm.execute();
		
		long estimatedTime = System.currentTimeMillis() - initTime;

        TimeUtil.printExecutionTime(estimatedTime,getName());
		

//		/* Log messages */
//		Properties props = new Properties();
//		try {
//			InputStream filepath
//                  = getClass().getResourceAsStream("/main/resources/experiments.properties");
//			//FileInputStream filepath
//                  = new FileInputStream("src/main/resources/experiments.properties");
//			props.load(filepath);
//			String resultspath = props.getProperty("resultspath");
			
//			System.out.println("Objectives values have been written to file BestSolutionQuality");
//			population.printObjectivesToFile(resultsPath + "/BestSolutionQuality.txt");
//			System.out.println("Variables values have been written to file BestSolution");
//			population.printVariablesToFile(resultsPath + "/BestSolution.txt");
			
			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}



//		problem.salvarRede(population.getSolutionAt(0), pathRede);
	}

//	private void addMutationOperator(Algorithm algorithm){
//		HashMap parameters = new HashMap();
//		parameters.put("probability", 1.0 / problem.getNumberOfVariables());
//		parameters.put("distributionIndex", 20.0);
//		Mutation mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);
//
//		algorithm.addOperator("mutation", mutation);
//	}
	
}
