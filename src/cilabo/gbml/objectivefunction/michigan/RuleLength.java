package cilabo.gbml.objectivefunction.michigan;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public final class RuleLength  <S extends MichiganSolution> {

	public int function(S solution) {
		return solution.getRuleLength();
	}
}
