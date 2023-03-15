package cilabo.gbml.objectivefunction.pittsburgh;

import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

/**
 * ルール数を返す．
 * @author Takigawa Hiroki
 *
 * @param <S>
 */
public final class NumberOfRules <S extends PittsburghSolution<?>>{

	public int objectiveID;

	public NumberOfRules(int objectiveID) {
		this.objectiveID = objectiveID;
	}

	public Integer calculateObjective(S solution) {
		return solution.getNumberOfRule();
	}

}
