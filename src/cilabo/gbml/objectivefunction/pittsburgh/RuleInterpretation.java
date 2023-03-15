package cilabo.gbml.objectivefunction.pittsburgh;

import cilabo.fuzzy.knowledge.FuzzySet;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;

public final class RuleInterpretation <S extends PittsburghSolution<?>>{

	public RuleInterpretation() {}

	public Double function(S solution) {
		double ruleInterpretation = 0;
		for(int i = 0; i < solution.getNumberOfVariables(); i++) {
			MichiganSolution<?> michiganSolution = solution.getVariable(i);
			String attributeId = new NumberOfWinner<S>().getAttributeId();
			if((int)michiganSolution.getAttribute(attributeId) > 0) {
				ruleInterpretation += 1;
			}
			ruleInterpretation += michiganSolution.getRuleLength()*1e-4f;
			int[] fuzzyTermIDs = michiganSolution.getVariablesArray();
			for(int dim_i=0; dim_i<fuzzyTermIDs.length; dim_i++) {
				FuzzySet fuzzyTerm = Knowledge.getInstance().getFuzzySet(dim_i, fuzzyTermIDs[dim_i]);
				if(fuzzyTerm.divisionType == DIVISION_TYPE.entropyDivision) ruleInterpretation += 1e-8f;
			}
		}
		return ruleInterpretation;
	}

}
