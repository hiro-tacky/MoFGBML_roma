package cilabo.gbml.objectivefunction.pittsburgh;

import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

/**
 * ルール数を返す．
 * @author Takigawa Hiroki
 *
 * @param <S>
 */
public final class NumberOfRules <S extends PittsburghSolution<?>>{

	public NumberOfRules() {}

	public Integer function(S solution) {
		return solution.getNumberOfVariables();
	}

}
