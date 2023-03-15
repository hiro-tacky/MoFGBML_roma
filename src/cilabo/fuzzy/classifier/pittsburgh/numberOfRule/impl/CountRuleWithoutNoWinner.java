package cilabo.fuzzy.classifier.pittsburgh.numberOfRule.impl;

import cilabo.fuzzy.classifier.pittsburgh.numberOfRule.NumberOfRule;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public class CountRuleWithoutNoWinner<PittsburghSolutionClass extends PittsburghSolution<?>>
		implements NumberOfRule<PittsburghSolutionClass> {

	@Override
	public int getNumberOfRule(PittsburghSolutionClass pittsburghSolution) {
		int count = 0;
		for(MichiganSolution<?> michiganSolution: pittsburghSolution.getVariables()) {
			count++;
		}
		return count;
	}

}
