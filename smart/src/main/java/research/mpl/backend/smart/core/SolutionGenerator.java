package research.mpl.backend.smart.core;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolutionGenerator {
	
	public Problem problem;

  	public SolutionGenerator(Problem problem) {
        this.problem = problem ;
    }
    

	public List<VariableValue> createVariables(Solution solution){
//        VariableValue[] variables = new VariableValue[problem.getNumberOfVariables()];
        List<VariableValue> variables = new ArrayList<VariableValue>();
        Random random = new Random();

        VariableValue variable = null;
        for (int var = 0; var < problem.getNumberOfVariables(); var++) {
            VariableDefinition definition = problem.getVariableDefinition(var);
            variable = new VariableValue();
            variable.setValue(
                    random.nextDouble()
                            * (definition.getUpperBound() - definition.getLowerBound())
                            + definition.getLowerBound());
            variable.setSolution(solution);
            variable.setDefinition(definition);
            variables.add(variable);
        }
        return variables ;
	}
	  
}
