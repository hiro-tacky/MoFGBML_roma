package cilabo.gbml.operator.mutation;

import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import cilabo.data.DataSet;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.utility.Random;

public class PittsburghMutation<pittsburghSolution extends PittsburghSolution<michiganSolution>, michiganSolution extends MichiganSolution<?>>
	implements MutationOperator<pittsburghSolution> {
	private double mutationProbability;
	private RandomGenerator<Double> randomGenerator;
	private BoundedRandomGenerator<Integer> intRandomGenerator;
	private DataSet<?> train;

	/** Constructor */
	public PittsburghMutation(DataSet<?> train) {
		this(1.0, train);
	}

	/** Constructor */
	public PittsburghMutation(double mutationProbability, DataSet<?> train) {
		this(mutationProbability, train,
			 () -> JMetalRandom.getInstance().nextDouble(),
			 (a, b) -> JMetalRandom.getInstance().nextInt(a, b));
	}

	/** Constructor */
	public PittsburghMutation(double mutationProbability, DataSet<?> train, RandomGenerator<Double> randomGenerator) {
		this(
			mutationProbability, train, randomGenerator,
			BoundedRandomGenerator.fromDoubleToInteger(randomGenerator));
	}

	/** Constructor */
	public PittsburghMutation(double mutationProbability, DataSet<?> train, RandomGenerator<Double> randomGenerator, BoundedRandomGenerator<Integer> intRandomGenerator) {
		if (mutationProbability < 0) {
			throw new JMetalException("Mutation probability is negative: " + mutationProbability);
		}
		this.mutationProbability = mutationProbability;
		this.train = train;
		this.randomGenerator = randomGenerator;
		this.intRandomGenerator = intRandomGenerator;
	}

	/* Getter */
	@Override
	public double getMutationProbability() {
		return mutationProbability;
	}

	/** Execute() method
	 * @param solution IntegerSolution : PittsburghSolutionを前提
	 */
	@Override
	public pittsburghSolution execute(pittsburghSolution solution) {
		Check.isNotNull(solution);
		Check.that(solution instanceof PittsburghSolution, "The argument must be class: " + PittsburghSolution.class.getCanonicalName());

		doMutation(mutationProbability, solution);
		return solution;
	}

	/**
	 * 後件部の学習をここでは行わない.
	 * @param probability
	 * @param solution PittsburghSolution
	 */
	public void doMutation(double probability, pittsburghSolution solution) {
		int numberOfRules = solution.getNumberOfVariables();
		if(numberOfRules < 1) {System.err.println("incorrect input: number Of Rules is less than 1 @" + this.getClass().getSimpleName());}
		int dimension = train.getNumberOfDimension();

		for(int rule_i = 0; rule_i < numberOfRules; rule_i++) {
			/* Perform muation for rule_i */
			if(Random.getInstance().getGEN().nextInt(numberOfRules) == 0) {/* Probability for each rule := 1/NumberOfRules */
				/* Decide which demension is performed mutation. */
				int mutatedDimension = Random.getInstance().getGEN().nextInt(dimension);

				int variableBefore = solution.getVariable(rule_i).getVariable(mutatedDimension);

				/* To judge which mutatedDimension is categorical or numerical  */
				double variableOfRandomPattern = train
												.getPattern(Random.getInstance().getGEN().nextInt(train.getDataSetSize()))
					  							.getAttributeValue(mutatedDimension);
				/* Attribute is Numeric */
				if(variableOfRandomPattern >= 0.0) {
					int numberOfCandidates = Knowledge.getInstance().getNumberOfFuzzySet(mutatedDimension);
					if(numberOfCandidates <= 1) {break;}
					//既に入力済みのファジィセットのインデックス以外の値をランダムに決定し入力する
					int newFuzzySet = intRandomGenerator.getRandomValue(0, numberOfCandidates-2);
					if(newFuzzySet < (int)solution.getVariable(rule_i).getVariable(mutatedDimension)) {
						solution.getVariable(rule_i).setVariable(mutatedDimension, newFuzzySet);
					}else {
						solution.getVariable(rule_i).setVariable(mutatedDimension, newFuzzySet+1);
					}
					solution.getVariable(rule_i).learning();

					if(solution.getVariable(rule_i).getRule().getConsequent().getClassLabel().isRejectedClassLabel()) {
						solution.getVariable(rule_i).setVariable(mutatedDimension, variableBefore);
						solution.getVariable(rule_i).learning();
					}
				}
				/* Attribute is categorical */
				else {
					solution.getVariable(rule_i).setVariable( mutatedDimension, (int)variableOfRandomPattern);
					solution.getVariable(rule_i).learning();

					if(solution.getVariable(rule_i).getRule().getConsequent().getClassLabel().isRejectedClassLabel()) {
						solution.getVariable(rule_i).setVariable(mutatedDimension, variableBefore);
						solution.getVariable(rule_i).learning();
					}
				}
			}
		}
	}
}
