package cilabo.gbml.operator.mutation;

import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public class MichiganMutation <michiganSolution extends MichiganSolution<?>> implements MutationOperator<michiganSolution> {
	private double mutationProbability;
	private RandomGenerator<Double> randomGenerator;
	private BoundedRandomGenerator<Integer> intRandomGenerator;
	private DataSet<?> data;

	  /** Constructor */
	  public MichiganMutation(double mutationProbability, DataSet<?> data) {
	    this(mutationProbability, data,
	    	 () -> JMetalRandom.getInstance().nextDouble(),
	    	 (a, b) -> JMetalRandom.getInstance().nextInt(a, b));
	  }

	  /** Constructor */
	  public MichiganMutation(double mutationProbability, DataSet<?> data, RandomGenerator<Double> randomGenerator) {
		  this(
			mutationProbability, data,
			randomGenerator,
			BoundedRandomGenerator.fromDoubleToInteger(randomGenerator));
	  }

	  /** Constructor */
	  public MichiganMutation(double mutationProbability, DataSet<?> data, RandomGenerator<Double> randomGenerator, BoundedRandomGenerator<Integer> intRandomGenerator) {
	    if (mutationProbability < 0) {
	      throw new JMetalException("Mutation probability is negative: " + mutationProbability);
	    }
	    this.mutationProbability = mutationProbability;
	    this.data = data;
	    this.randomGenerator = randomGenerator;
	    this.intRandomGenerator = intRandomGenerator;
	  }

	  /* Getter */
	  @Override
	  public double getMutationProbability() {
	    return mutationProbability;
	  }

	  /** Execute() method */
	  @Override
	  public michiganSolution execute(michiganSolution solution) {
		  Check.isNotNull(solution);

		  doMutation(mutationProbability, solution);
		  return solution;
	  }

	  /**
	   * Perform the mutation operation
	   *
	   * @param probability Mutation setProbability
	   * @param solution The solution to mutate
	   */
	  public void doMutation(double probability, michiganSolution solution) {
		  for(int i = 0; i < solution.getNumberOfVariables(); i++) {
			// To judge which attribute i is categorical or numerical.
			Pattern<?> randPattern = data.getPattern(intRandomGenerator.getRandomValue(0, data.getDataSetSize()-1));


			// Decide new variable
			int fuzzySetNum = Knowledge.getInstance().getNumberOfFuzzySet(i);
			if(fuzzySetNum <= 1) {return;}
			int newFuzzySet = intRandomGenerator.getRandomValue(0, fuzzySetNum-2);

			if(randomGenerator.getRandomValue() < probability) {
				if(randPattern.getAttributeValue(i) >= 0) {
					// Numerical attribute:
					//変更前がnewFuzzySetと同値だった場合の回避用処理
					if(newFuzzySet < (int)solution.getVariable(i)) {
						solution.setVariable(i, newFuzzySet);
					}
					else {
						solution.setVariable(i, newFuzzySet+1);
					}
				}
				else {
					// Categorical attribute:
					solution.setVariable(i, (int)randPattern.getAttributeValue(i));
				}

			}
		  }
	  }
}
