package cilabo.gbml.objectivefunction.pittsburgh;

import java.util.ArrayList;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.solution.util.attribute.ErroredPatternsAttribute;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;

/**
 * 誤識別率計算評価関数
 * @author Takigawa Hiroki
 *
 * @param <S>
 */
public final class ErrorRate <S extends PittsburghSolution<?>>{

	public ErrorRate() {}

	/**
	 * 識別不能ルールは誤識別として処理
	 * @param solution
	 * @param train
	 * @return
	 */
	public double function(S solution, DataSet<?> train) {
		// Classification
		int numberOfErrorPatterns = 0;

		String attributeId = new NumberOfWinner<S>().getAttributeId();
		for(MichiganSolution<?> michiganSolution: solution.getVariables()) {
			michiganSolution.setAttribute(attributeId, 0);
		}

		ArrayList<Pattern<?>> erroredPatterns = new ArrayList<Pattern<?>>();
		for(int i = 0; i < train.getDataSize(); i++) {
			Pattern<?> pattern = train.getPattern(i);
			MichiganSolution<?> winnerSolution = solution.classify(pattern.getAttributeVector());

			// If output is rejected then continue next pattern.
			if(winnerSolution == null) {
				/* Add errored pattern Attribute */
				numberOfErrorPatterns++;
				erroredPatterns.add(pattern);
				continue;
			}

			int buf = (int) winnerSolution.getAttribute(attributeId);
			winnerSolution.setAttribute(attributeId, buf+1);

			/* If a winner rule correctly classify a pattern,
			 * then the winner rule's fitness will be incremented. */
			if(!pattern.getTargetClass().equalsClassLabel(
					winnerSolution.getClassLabel()) ){
				numberOfErrorPatterns++;
				erroredPatterns.add(pattern);
			}
		}

		solution.setAttribute(new ErroredPatternsAttribute<S>().getAttributeId(), erroredPatterns);

		double errorRate = numberOfErrorPatterns / (double)train.getDataSize();
		return errorRate;
	}
}
