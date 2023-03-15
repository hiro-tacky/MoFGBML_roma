package cilabo.gbml.objectivefunction.pittsburgh;

import java.util.ArrayList;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.solution.util.attribute.ErroredPatternsAttribute;
import cilabo.gbml.solution.util.attribute.NumberOfClassifiedPatterns;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;

/**
 * 誤識別率計算評価関数
 * @author Takigawa Hiroki
 *
 * @param <S>
 */
public final class ErrorRate <S extends PittsburghSolution<?>>{

	public int objectiveID;

	public ErrorRate(int objectiveID) {
		this.objectiveID = objectiveID;
	}

	/**
	 * 識別不能ルールは誤識別として処理
	 * @param solution
	 * @param train
	 * @return
	 */
	public double calculateObjective(S solution, DataSet<?> train) {
		// Classification
		int numberOfErrorPatterns = 0;

		String attributeId = new NumberOfWinner<S>().getAttributeId();
		String attributeIdFitness = new NumberOfClassifiedPatterns<S>().getAttributeId();
		for(MichiganSolution<?> michiganSolution: solution.getVariables()) {
			michiganSolution.setAttribute(attributeId, 0);
			michiganSolution.setAttribute(attributeIdFitness, 0);
		}

		ArrayList<Pattern<?>> erroredPatterns = new ArrayList<Pattern<?>>();
		for(int i = 0; i < train.getDataSetSize(); i++) {
			Pattern<?> pattern = train.getPattern(i);
			MichiganSolution<?> winnerSolution = solution.classify(pattern);

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
			if(!pattern.getTargetClass().equals(
					winnerSolution.getRule().getConsequent().getClassLabel()) ){
				numberOfErrorPatterns++;
				erroredPatterns.add(pattern);
			}else {
				int buf2 = (int) winnerSolution.getAttribute(attributeIdFitness);
				winnerSolution.setAttribute(attributeIdFitness, buf2+1);
			}
		}

		solution.setAttribute(new ErroredPatternsAttribute<S>().getAttributeId(), erroredPatterns);

		double errorRate = numberOfErrorPatterns / (double)train.getDataSetSize();
		return errorRate;
	}

	public int getObjectiveID() {
		return objectiveID;
	}

	public void setObjectiveID(int objectiveID) {
		this.objectiveID = objectiveID;
	}
}
