package cilabo.gbml.objectivefunction.pittsburgh;

import java.util.ArrayList;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Rejected;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.solution.util.attribute.ErroredPatternsAttribute;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;

public final class ErrorRate <S extends PittsburghSolution>{

	public ErrorRate() {}

	public double function(S solution, DataSet train) {
		// Classification
		int numberOfErrorPatterns = 0;

		String attributeId = new NumberOfWinner().getAttributeId();
		solution.clearAttributes(attributeId);
		ArrayList<Pattern> erroredPatterns = new ArrayList<Pattern>();
		for(int i = 0; i < train.getDataSize(); i++) {
			Pattern pattern = train.getPattern(i);
			MichiganSolution winnerSolution = solution.classify(pattern.getInputVector());

			// If output is rejected then continue next pattern.
			if(winnerSolution instanceof MichiganSolution_Rejected) {
				/* Add errored pattern Attribute */
				numberOfErrorPatterns++;
				continue;
			}

			/* If a winner rule correctly classify a pattern,
			 * then the winner rule's fitness will be incremented. */
			else if(!pattern.getTrueClass().equals(
					winnerSolution.getClassLabel()) ){
				numberOfErrorPatterns++;
				erroredPatterns.add(pattern);
			}else {
				int buf = (int) winnerSolution.getAttribute(attributeId);
				winnerSolution.setAttribute(attributeId, buf+1);
			}
		}

		solution.setAttribute(new ErroredPatternsAttribute<S>().getAttributeId(), erroredPatterns);

		double errorRate = numberOfErrorPatterns / (double)train.getDataSize();
		return errorRate;
	}
}
