package research.mpl.backend.smart.metaheuristics.network.training;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import research.mpl.backend.smart.core.Problem;
import research.mpl.backend.smart.util.EnduranceException;

public abstract class TrainerRunnable implements Runnable {

	Problem problem = null;
	String resultsPath = null;

	public TrainerRunnable(Problem problem, String pathRede) {
		this.problem = problem;
		this.resultsPath = pathRede;
	}

	public abstract void execute() throws EnduranceException, ClassNotFoundException;

	public abstract String getNamePrefix();
	public abstract String getNameSuffix();
	
	public void run() {
		try {
			System.out.println("Starts " + getName() + "...\n");
			execute();

		} catch (EnduranceException jmException) {
			jmException.printStackTrace();
		} catch (ClassNotFoundException classNotFoundException) {
			classNotFoundException.printStackTrace();
		}
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public String getResultsPath() {
		return resultsPath;
	}

	public void setResultsPath(String resultsPath) {
		this.resultsPath = resultsPath;
	}
	
	public String getName() {
		return getNamePrefix() + this.getClass().getSimpleName() + getNameSuffix();
	}



	
	public void printContentToFile(String fileContent, String path) {
		try {
			/* Open the file */
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write(fileContent);
			bw.newLine();

			/* Close the file */
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
