package cilabo.fuzzy.classifier.pittsburgh.numberOfRule;

import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public interface NumberOfRule<PittsburghSolutionClass extends PittsburghSolution<?>> {
	public int getNumberOfRule(PittsburghSolutionClass pittsburghSolution);
}
