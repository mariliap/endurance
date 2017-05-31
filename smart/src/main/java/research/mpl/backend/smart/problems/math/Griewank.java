//  Griewank.java
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

package research.mpl.backend.smart.problems.math;


import research.mpl.backend.smart.core.Problem;
import research.mpl.backend.smart.core.Solution;
import research.mpl.backend.smart.core.VariableDefinition;
import research.mpl.backend.smart.core.VariableValue;
import research.mpl.backend.smart.util.EnduranceException;

import java.util.ArrayList;
import java.util.List;

public class Griewank extends Problem {


    public Griewank(String solutionType, Integer numberOfVariables)  throws ClassNotFoundException {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(1);
        setNumberOfConstraints(0);
        setProblemName("Griewank");

        List<VariableDefinition> variableDefinitionList = new ArrayList<VariableDefinition>();

        for (int var = 0; var < numberOfVariables; var++){
            VariableDefinition definition = new VariableDefinition();
            definition.setLowerBound(-600.0);
            definition.setUpperBound(600.0);
            variableDefinitionList.add(definition);
        }
        setVariableDefinition(variableDefinitionList);
        
//        if (solutionType.compareTo("BinaryReal") == 0)
//            solutionGenerator_ = new BinaryRealSolutionType(this) ;
//        else if (solutionType.compareTo("Real") == 0)
//            solutionGenerator_ = new RealSolutionType(this) ;
//        else {
//            System.out.println("Error: solution type " + solutionType + " invalid") ;
//            System.exit(-1) ;
//        }
      } // Griewank

    public void evaluate(Solution solution) throws EnduranceException {
        VariableValue[] decisionVariables  = solution.getDecisionVariablesArray();

        double sum  = 0.0;
        double mult = 1.0;
        double d    = 4000.0;
        for (int var = 0; var < super.getNumberOfVariables(); var++) {
            sum += decisionVariables[var].getValue() *
                 decisionVariables[var].getValue() ;
            mult *= Math.cos(decisionVariables[var].getValue()/Math.sqrt(var+1)) ;
        }

        solution.setObjective(0, 1.0/d * sum - mult + 1.0) ;
    }
}

