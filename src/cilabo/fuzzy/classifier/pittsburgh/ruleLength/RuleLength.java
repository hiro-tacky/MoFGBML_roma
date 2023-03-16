package cilabo.fuzzy.classifier.pittsburgh.ruleLength;

import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public interface RuleLength<PittsburghSolutionClass extends PittsburghSolution<?>> {
	public int getRuleLength(PittsburghSolutionClass pittsburghSolution);
}
