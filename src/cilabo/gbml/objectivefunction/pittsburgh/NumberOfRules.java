package cilabo.gbml.objectivefunction.pittsburgh;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;

public final class NumberOfRules <S extends PittsburghSolution>{

	public NumberOfRules() {}

	public Integer function(S solution) {
		int numberOfRules = 0;
		for(int i = 0; i < solution.getNumberOfVariables(); i++) {
			MichiganSolution michiganSolution = solution.getVariable(i);
			String attributeId = new NumberOfWinner().getAttributeId();
			if((int)michiganSolution.getAttribute(attributeId) > 0) {
				numberOfRules++;
			}
		}
		return numberOfRules;
	}

}
