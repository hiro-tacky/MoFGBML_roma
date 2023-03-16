package cilabo.fuzzy.classifier.pittsburgh.ruleLength.impl;

import cilabo.fuzzy.classifier.pittsburgh.numberOfRule.NumberOfRule;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public class CountAttributesWithoutDontCare <PittsburghSolutionClass extends PittsburghSolution<?>>
		implements NumberOfRule<PittsburghSolutionClass> {

	@Override
	public int getNumberOfRule(PittsburghSolutionClass pittsburghSolution) {
		int length = 0;
		for(int i = 0; i < pittsburghSolution.getNumberOfVariables(); i++) {
			for(int index: pittsburghSolution.getVariable(i).getVariablesArray()) {
				if(index != 0) length++;
			}
		}
		return length;
	}
}
