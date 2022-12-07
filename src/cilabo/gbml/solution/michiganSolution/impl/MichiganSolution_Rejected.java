package cilabo.gbml.solution.michiganSolution.impl;

import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public class MichiganSolution_Rejected extends AbstractMichiganSolution implements MichiganSolution {

	protected MichiganSolution_Rejected(int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder ruleBuilder) {
		super(numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	private static MichiganSolution_Rejected instance = new MichiganSolution_Rejected(0, 0, null);

	public static MichiganSolution_Rejected getInstance() {
		if(instance == null) {
			instance = new MichiganSolution_Rejected(0, 0, null);
		}
		return instance;
	}

	@Override
	public Integer getVariable(int index) {
		return null;
	}

	@Override
	public MichiganSolution copy() {
		return this.getInstance();
	}
}
