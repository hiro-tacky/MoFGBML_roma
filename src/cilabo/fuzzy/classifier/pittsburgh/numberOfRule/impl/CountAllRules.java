package cilabo.fuzzy.classifier.pittsburgh.numberOfRule.impl;

import cilabo.fuzzy.classifier.pittsburgh.numberOfRule.NumberOfRule;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public class CountAllRules<PittsburghSolutionClass extends PittsburghSolution<?>>
		implements NumberOfRule<PittsburghSolutionClass> {

	@Override
	public int getNumberOfRule(PittsburghSolutionClass pittsburghSolution) {
		return pittsburghSolution.getNumberOfVariables();
	}
}
