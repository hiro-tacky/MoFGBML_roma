package cilabo.fuzzy.classifier.pittsburgh.ruleLength.impl;

import cilabo.fuzzy.classifier.pittsburgh.ruleLength.RuleLength;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public class CountAllAttributes<PittsburghSolutionClass extends PittsburghSolution<?>>
		implements RuleLength<PittsburghSolutionClass> {

	@Override
	public int getRuleLength(PittsburghSolutionClass pittsburghSolution) {
		int length = 0;
		for(int i = 0; i < pittsburghSolution.getNumberOfVariables(); i++) {
			length += pittsburghSolution.getVariable(i).getVariables().size();
		}
		return length;
	}
}
