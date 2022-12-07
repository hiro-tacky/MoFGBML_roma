package cilabo.gbml.objectivefunction.michigan;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.main.ExperienceMethods.ObjectivesIndexMichigan;

public final class RuleLength  <S extends MichiganSolution> {

	public void function(S solution) {
		int buf =solution.getRuleLength();
		solution.setObjective(ObjectivesIndexMichigan.RuleLength.toInt(), buf);
	}
}
