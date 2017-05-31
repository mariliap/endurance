//  PSO_main.java
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

import research.mpl.backend.smart.core.Algorithm;
import research.mpl.backend.smart.core.Problem;
import research.mpl.backend.smart.core.SolutionSet;
import research.mpl.backend.smart.problems.math.Griewank;
import research.mpl.backend.smart.util.EnduranceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


/**
 * Class for configuring and running a single-objective PSO algorithm
 */
public class PSO_main {
  public static Logger      logger_ ;      // Logger object
  public static FileHandler fileHandler_ ; // FileHandler object


  public static void main(String [] args) 
  		throws EnduranceException, IOException, ClassNotFoundException {
    
//    QualityIndicator indicators ;
        
    HashMap  parameters ;

    fileHandler_ = new FileHandler("PSO_main.log"); 
    logger_.addHandler(fileHandler_) ;


    Problem   problem = new Griewank("Real", 20);

    Algorithm algorithm = new PSO(problem, null) ;
    
    // Algorithm parameters
    algorithm.setInputParameter("swarmSize",50);
    algorithm.setInputParameter("maxIterations",5000);
    
    parameters = new HashMap() ;
    parameters.put("probability", 1.0/problem.getNumberOfVariables()) ;
    parameters.put("distributionIndex", 20.0) ;

//    mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);
//    algorithm.addOperator("mutation", mutation);

    // Execute the Algorithm 
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    logger_.info("Variables values have been writen to file VAR");
    population.printVariablesToFile("VAR");                         
  } //main
} // PSO_main
