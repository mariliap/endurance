//  PSO.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package research.mpl.backend.smart.metaheuristics.pso;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import research.mpl.backend.smart.operators.BestSolutionSelection;
import research.mpl.backend.smart.comparators.ObjectiveComparator;
import research.mpl.backend.smart.core.*;
import research.mpl.backend.smart.util.random.PseudoRandom;
import research.mpl.backend.smart.util.EnduranceException;

public class PSO extends Algorithm {

	private int particlesSize_;

	private int maxIterations_;

	private int iteration_;

	private SolutionSet particles;

	private Solution[] localBest_;

	private Solution[] globalBest_;

	private int[][] neighborhood;

	private double[][] speed_;

	private Operator polynomialMutation_;

	int evaluations_;

	Comparator comparator_;

	Operator findBestSolution_;

	double r1Max_;
	double r1Min_;
	double r2Max_;
	double r2Min_;
	double C1Max_;
	double C1Min_;
	double C2Max_;
	double C2Min_;
	double WMax_;
	double WMin_;
	double ChVel1_;
	double ChVel2_;

	
	String resultsPath = null;
	/**
	 * Constructor
	 * 
	 * @param problem
	 *            Problem to solve
	 */
	public PSO(Problem problem, String resultsPath) {
		super(problem);
		this.resultsPath = resultsPath;
		r1Max_ = 1.0;
		r1Min_ = 0.0;
		r2Max_ = 1.0;
		r2Min_ = 0.0;
		C1Max_ = 1.5;
		C1Min_ = 1.5;
		C2Max_ = 1.5;
		C2Min_ = 1.5;
		WMax_ = 0.9;
		WMin_ = 0.1;
		ChVel1_ = 1.0;
		ChVel2_ = 1.0;

		comparator_ = new ObjectiveComparator(0); // Single objective comparator
		HashMap parameters; // Operator parameters

		parameters = new HashMap();
		parameters.put("comparator", comparator_);
		findBestSolution_ = new BestSolutionSelection(parameters);

		evaluations_ = 0;
	} // Constructor

	private SolutionSet trueFront_;
	private double deltaMax_[];
	private double deltaMin_[];
	boolean success_;

	/**
	 * Initialize all parameter of the algorithm
	 */
	public void initParams() {
		particlesSize_ = ((Integer) getInputParameter("swarmSize")).intValue();
		maxIterations_ = ((Integer) getInputParameter("maxIterations")).intValue();

		polynomialMutation_ = operators_.get("mutation");

		iteration_ = 0;

		success_ = false;

		particles = new SolutionSet(particlesSize_);
		localBest_ = new Solution[particlesSize_];
		
		
		try {
			neighborhood = createNeighborhood(particlesSize_,10);//IF GLOBALBEST THIS GOES 'particlesSize_' instead of 10
		} catch (Exception e) {
			e.printStackTrace();
		}
		globalBest_ = new Solution[neighborhood.length];

		// Create the speed_ vector
		speed_ = new double[particlesSize_][problem_.getNumberOfVariables()];

		deltaMax_ = new double[problem_.getNumberOfVariables()];
		deltaMin_ = new double[problem_.getNumberOfVariables()];
		for (int i = 0; i < problem_.getNumberOfVariables(); i++) {
			deltaMax_[i] = (problem_.getVariableDefinition(i).getUpperBound() - problem_.getVariableDefinition(i).getLowerBound()) / 2.0;
			deltaMin_[i] = -deltaMax_[i];
		} // for
	} // initParams
	
	private int[][] createNeighborhood(int numIndv, int numGroups) throws Exception {
		if(numIndv%numGroups != 0) {
			throw new Exception("numGroups should be multitiple of numIndv");
		}
		int count = 0;
		int numIndvPerGroup = numIndv/numGroups;
		int[][] neighborhood = new int[numGroups][numIndvPerGroup];
		for (int g = 0; g < numGroups; g++) {
			for (int i = 0; i < numIndvPerGroup; i++) {
				neighborhood[g][i] = count;
				count++;
			}
		}
		
		return neighborhood;
	}

	private int getneighborhood(int particleIndex) {
		for (int group = 0; group < neighborhood.length; group++) {
			for (int k = 0; k < neighborhood[0].length; k++) {
				if(particleIndex == neighborhood[group][k]) {
					return group;
				}
			}
		}
		return 0;
	}
	
	// Adaptive inertia
	private double inertiaWeight(int iter, int miter, double wmax, double wmin) {
		// return wmax; // - (((wmax-wmin)*(double)iter)/(double)miter);
		return wmax - (((wmax - wmin) * (double) iter) / (double) miter);
	} // inertiaWeight

	// constriction coefficient (M. Clerc)
	private double constrictionCoefficient(double c1, double c2) {
		double rho = c1 + c2;
		// rho = 1.0 ;
		if (rho <= 4) {
			return 1.0;
		} else {
			return 2 / Math.abs((2 - rho - Math.sqrt(Math.pow(rho, 2.0) - 4.0 * rho)));
		}
	} // constrictionCoefficient

	// velocity bounds
	private double velocityConstriction(double v, double[] deltaMax, double[] deltaMin, int variableIndex,
			int particleIndex) throws IOException {

		return v;
		/*
		 * 
		 * //System.out.println("v: " + v + "\tdmax: " + dmax + "\tdmin: " +
		 * dmin) ; double result;
		 * 
		 * double dmax = deltaMax[variableIndex]; double dmin =
		 * deltaMin[variableIndex];
		 * 
		 * result = v;
		 * 
		 * if (v > dmax) { result = dmax; }
		 * 
		 * if (v < dmin) { result = dmin; }
		 * 
		 * return result;
		 */
	} // velocityConstriction

	/**
	 * Update the speed of each particle
	 * 
	 * @throws EnduranceException
	 */
	private void computeSpeed(int iter, int miter) throws EnduranceException, IOException {
		double r1, r2;
		// double W ;
		double C1, C2;
		double wmax, wmin, deltaMax, deltaMin;
		
		//MUDEI
		//XReal bestGlobal = new XReal(globalBest_);

		for (int i = 0; i < particlesSize_; i++) {
			VariableValue[] particle = particles.getSolutionAt(i).getDecisionVariablesArray();
			VariableValue[] bestParticle = localBest_[i].getDecisionVariablesArray();
			VariableValue[] bestGlobal = globalBest_[getneighborhood(i)].getDecisionVariablesArray();

			// int bestIndividual =
			// (Integer)findBestSolution_.execute(particles) ;

			C1Max_ = 2.5;
			C1Min_ = 1.5;
			C2Max_ = 2.5;
			C2Min_ = 1.5;

			r1 = PseudoRandom.randDouble(r1Min_, r1Max_);
			r2 = PseudoRandom.randDouble(r2Min_, r2Max_);
			C1 = PseudoRandom.randDouble(C1Min_, C1Max_);
			C2 = PseudoRandom.randDouble(C2Min_, C2Max_);
			// W = PseudoRandom.randDouble(WMin_, WMax_);
			//

			WMax_ = 0.9;
			WMin_ = 0.9;
			ChVel1_ = 1.0;
			ChVel2_ = 1.0;

			C1 = 2.5;
			C2 = 1.5;

			wmax = WMax_;
			wmin = WMin_;
			/*
			 * for (int var = 0; var < particle.size(); var++) { //Computing the
			 * velocity of this particle speed_[i][var] =
			 * velocityConstriction(constrictionCoefficient(C1, C2) *
			 * (inertiaWeight(iter, miter, wmax, wmin) * speed_[i][var] + C1 *
			 * r1 * (bestParticle.getValue(var) - particle.getValue(var)) + C2 *
			 * r2 * (bestGlobal.getValue(var) - particle.getValue(var))),
			 * deltaMax_, deltaMin_, //[var], var, i); }
			 */
			C1 = 1.5;
			C2 = 1.5;
			double W = 0.9;
			for (int var = 0; var < particle.length; var++) {
				// Computing the velocity of this particle
				speed_[i][var] = inertiaWeight(iter, miter, wmax, wmin) * speed_[i][var] + C1 * r1
						* (bestParticle[var].getValue() - particle[var].getValue()) + C2 * r2
						* (bestGlobal[var].getValue() - particle[var].getValue());
			}
		}
	} // computeSpeed

	/**
	 * Update the position of each particle
	 * 
	 * @throws EnduranceException
	 */
	private void computeNewPositions() throws EnduranceException {
		for (int i = 0; i < particlesSize_; i++) {
			VariableValue[] particle = particles.getSolutionAt(i).getDecisionVariablesArray();
			// VariableValue particle = new VariableValue(particles.getSolutionAt(i));
			// particle.move(speed_[i]);
			for (int var = 0; var < particle.length; var++) {
				particle[var].setValue(particle[var].getValue() + speed_[i][var]);

				if (particle[var].getValue() < problem_.getVariableDefinition(var).getLowerBound()) {
					particle[var].setValue(problem_.getVariableDefinition(var).getLowerBound());
					speed_[i][var] = speed_[i][var] * ChVel1_; //
				}
				if (particle[var].getValue() > problem_.getVariableDefinition(var).getUpperBound()) {
					particle[var].setValue(problem_.getVariableDefinition(var).getUpperBound());
					speed_[i][var] = speed_[i][var] * ChVel2_; //
				}

			}
		}
	} // computeNewPositions

	/**
	 * Apply a mutation operator to some particles in the swarm
	 * 
	 * @throws EnduranceException
	 */
	private void mopsoMutation(int actualIteration, int totalIterations) throws EnduranceException {
		for (int i = 0; i < particles.size(); i++) {
			if ((i % 6) == 0)
				polynomialMutation_.execute(particles.getSolutionAt(i));
			// if (i % 3 == 0) { //particles mutated with a non-uniform
			// mutation %3
			// nonUniformMutation_.execute(particles.getSolutionAt(i));
			// } else if (i % 3 == 1) { //particles mutated with a uniform
			// mutation operator
			// uniformMutation_.execute(particles.getSolutionAt(i));
			// } else //particles without mutation
			// ;
		}
	} // mopsoMutation

	/**
	 * Runs of the SMPSO algorithm.
	 * 
	 * @return a <code>SolutionSet</code> that is a set of non dominated
	 *         solutions as a result of the algorithm execution
	 * @throws EnduranceException
	 */
	public SolutionSet execute() throws EnduranceException, ClassNotFoundException {
		initParams();

		success_ = false;
        SolutionGenerator solutionGenerator = new SolutionGenerator(problem_);
		
		// ->Step 1 (and 3) Create the initial population and evaluate
		for (int i = 0; i < particlesSize_; i++) {
			Solution particle = new Solution(solutionGenerator, problem_.getNumberOfObjectives());
			problem_.evaluate(particle);
			evaluations_++;
			particles.add(particle);
			int neighborhood = getneighborhood(i);
			if ((globalBest_[neighborhood] == null) || (particle.getObjective(0) < globalBest_[neighborhood].getObjective(0)))
				globalBest_[neighborhood] = new Solution(particle);
		}

		// -> Step2. Initialize the speed_ of each particle to 0
		for (int i = 0; i < particlesSize_; i++) {
			for (int j = 0; j < problem_.getNumberOfVariables(); j++) {
				speed_[i][j] = 0.0;
			}
		}

		// -> Step 6. Initialize the memory of each particle
		for (int i = 0; i < particles.size(); i++) {
			Solution particle = new Solution(particles.getSolutionAt(i));
			localBest_[i] = particle;
		}

		StringBuffer resultsBuffer = new StringBuffer();
		
		// -> Step 7. Iterations ..
		while (iteration_ < maxIterations_) {
			int bestIndividual = (Integer) findBestSolution_.execute(particles);
			try {
				// Compute the speed_
				computeSpeed(iteration_, maxIterations_);
			} catch (IOException ex) {
				Logger.getLogger(PSO.class.getName()).log(Level.SEVERE, null, ex);
			}

			// Compute the new positions for the particles
			computeNewPositions();

			// Mutate the particles
			// mopsoMutation(iteration_, maxIterations_);

			// Evaluate the new particles in new positions
			for (int i = 0; i < particles.size(); i++) {
				Solution particle = particles.getSolutionAt(i);
				problem_.evaluate(particle);
				evaluations_++;
			}

			// Actualize the memory of this particle
			for (int i = 0; i < particles.size(); i++) {
				// int flag = comparator_.compare(particles.getSolutionAt(i),
				// localBest_[i]);
				// if (flag < 0) { // the new particle is best_ than the older
				// remember
				if ((particles.getSolutionAt(i).getObjective(0) < localBest_[i].getObjective(0))) {
					Solution particle = new Solution(particles.getSolutionAt(i));
					localBest_[i] = particle;
				} // if
				int neighborhood = getneighborhood(i);
				if ((particles.getSolutionAt(i).getObjective(0) < globalBest_[neighborhood].getObjective(0))) {
    					Solution particle = new Solution(particles.getSolutionAt(i));
    					globalBest_[neighborhood] = particle;
    					
    			}// if
			}
			bestIndividual = (Integer) findBestSolution_.execute(particles);
			Solution sol = particles.getSolutionAt(bestIndividual);
			
//			System.out.println("Iteracao = " + iteration_);
//			for (int i = 0; i < particles.size(); i++) {
//				XReal particle = new XReal(particles.getSolutionAt(i));
//				System.out.print("Particle " + i + ": ");
//				for (int j = 0; j < particle.size(); j++) {
//					System.out.print(particle.getValue(j) + ";");
//				}
//				System.out.println("\n"+particles.getSolutionAt(i).getFitness() + "!");
//			}
//			System.out.print("");
			
			//((RNATrainingProblem) problem).saveNetwork(sol, "rna_pso.txt");
			
			
			if(iteration_%100 == 0) {
				resultsBuffer.append("Epoch: " + iteration_ + "\tTraining Error: " + sol.getObjective(0) + "\n");
    			System.out.printf("Epoch: %d \tTraining Error: %.8f \n", iteration_, sol.getObjective(0));
    			
    			VariableValue[] decisionVariables = sol.getDecisionVariablesArray();
    			System.out.print("Particle " + bestIndividual + ": ");
    			for (int var = 0; var < decisionVariables.length; var++) {
    				System.out.print(decisionVariables[var].getValue() + ";");
    			}
    			System.out.println("");
			}
			iteration_++;
		}

		// Return a population with the best individual 
		SolutionSet resultPopulation = new SolutionSet(1);
		int bestIndividual = (Integer) findBestSolution_.execute(particles);
		Solution sol = particles.getSolutionAt(bestIndividual);
		resultPopulation.add(sol);
		
		//Save process
		resultsBuffer.append(" \t\t TRAINING ERROR=  ");
		resultsBuffer.append(sol.getObjective(0));
		resultsBuffer.append("\n");
		resultsBuffer.append(" \t\t TEST ERROR=  ");
		resultsBuffer.append(99999);
		resultsBuffer.append("\n");
		resultsBuffer.append("THE END");
		
		printContentToFile(resultsBuffer.toString(), resultsPath);
		
		System.out.println("THE END");

		return resultPopulation;
	} // execute
	
	
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
} // PSO
