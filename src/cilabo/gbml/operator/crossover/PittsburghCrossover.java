package cilabo.gbml.operator.crossover;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.pseudorandom.BoundedRandomGenerator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.main.Consts;
import cilabo.utility.GeneralFunctions;
import cilabo.utility.Random;

public class PittsburghCrossover implements CrossoverOperator<PittsburghSolution<?>> {
	private double crossoverProbability;
	private RandomGenerator<Double> crossoverRandomGenerator;
	BoundedRandomGenerator<Integer> selectRandomGenerator;

	/** Constructor */
	public PittsburghCrossover(double crossoverProbability) {
		this(
			crossoverProbability,
			() -> JMetalRandom.getInstance().nextDouble(),
			(a, b) -> JMetalRandom.getInstance().nextInt(a, b));
	}

	/** Constructor */
	public PittsburghCrossover(
			double crossoverProbability, RandomGenerator<Double> randomGenerator) {
		this(
			crossoverProbability,
			randomGenerator,
			BoundedRandomGenerator.fromDoubleToInteger(randomGenerator));
	}

	/** Constructor */
	public PittsburghCrossover(
			double crossoverProbability,
			RandomGenerator<Double> crossoverRandomGenerator,
			BoundedRandomGenerator<Integer> selectRandomGenerator) {
		if(crossoverProbability < 0) {
			throw new JMetalException("Crossover probability is negative: " + crossoverProbability);
		}
		this.crossoverProbability = crossoverProbability;
		this.crossoverRandomGenerator = crossoverRandomGenerator;
		this.selectRandomGenerator = selectRandomGenerator;
	}

	/* Getter */
	@Override
	public double getCrossoverProbability() {
		return this.crossoverProbability;
	}

	/* Setter */
	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	@Override
	public int getNumberOfRequiredParents() {
		return 2;
	}

	@Override
	public int getNumberOfGeneratedChildren() {
		return 1;
	}

	@Override
	public List<PittsburghSolution<?>> execute(List<PittsburghSolution<?>> solutions) {
		Check.isNotNull(solutions);
		Check.that(solutions.size() == 2, "There must be two parents instead of " + solutions.size());
		return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1));
	}

	/**
	 * 後件部の学習はここでは行わない
	 * @param probability
	 * @param _parent1
	 * @param _parent2
	 * @return
	 */
	public List<PittsburghSolution<?>> doCrossover(double probability, PittsburghSolution<?> parent1, PittsburghSolution<?> parent2) {
		// Cast IntegerSolution to PittsburghSolution
		if(parent1.getNumberOfVariables() < 1) {System.err.println("incorrect input: number Of Rules is less than 1");}
		if(parent2.getNumberOfVariables() < 1) {System.err.println("incorrect input: number Of Rules is less than 1");}
		List<PittsburghSolution<?>> offspring = new ArrayList<>();

		/* Do crossover */
		if(crossoverRandomGenerator.getRandomValue() < probability) {
			/** Number of rules inherited from parent1.  */
			int N1 = selectRandomGenerator.getRandomValue(0, parent1.getNumberOfVariables());
			/** Number of rules inherited from parent2.  */
			int N2 = selectRandomGenerator.getRandomValue(0, parent2.getNumberOfVariables());

			// Reduciong excess of rules
			if((N1+N2) > Consts.MAX_RULE_NUM) {
				int delNum = (N1+N2) - Consts.MAX_RULE_NUM;
				for(int i = 0; i < delNum; i++) {
					if( N1 > 0 && N2 > 0){
						if(selectRandomGenerator.getRandomValue(0, 1) == 0) {
							N1--;
						}
						else {
							N2--;
						}
					}
					else if(N1 == 0 && N2 > 0) {
						N2--;
					}
					else if(N1 > 0 && N2 == 0) {
						N1--;
					}
					else {
						break;
					}
				}
			}

			//
			if((N1+N2) < Consts.MIN_RULE_NUM) {
				int lackNum = Consts.MIN_RULE_NUM - (N1+N2);
				for(int i = 0; i < lackNum; i++) {

					if( N1 < parent1.getNumberOfVariables() &&
						N2 < parent2.getNumberOfVariables())
					{
						if(selectRandomGenerator.getRandomValue(0, 1) == 0) {
							N1++;
						}
						else {
							N2++;
						}
					}
					else if(N1 >= parent1.getNumberOfVariables() &&
							N2 < parent2.getNumberOfVariables()) {
						N2++;
					}
					else if(N1 < parent1.getNumberOfVariables() &&
							N2 >= parent2.getNumberOfVariables()) {
						N1++;
					}
					else {
						break;
					}
				}
			}

			List<MichiganSolution> michiganPopulation = new ArrayList<>();

			// Select inherited rules
			int ruleNum = parent1.getNumberOfVariables();
			Integer[] index1 = GeneralFunctions.samplingWithout(ruleNum, N1, Random.getInstance().getGEN());
			ruleNum = parent2.getNumberOfVariables();
			Integer[] index2 = GeneralFunctions.samplingWithout(ruleNum, N2, Random.getInstance().getGEN());

			// Inheriting
			for(int i = 0; i < index1.length; i++) {
				int index = index1[i];
				michiganPopulation.add(parent1.getVariable(index).copy());
			}
			for(int i = 0; i < index2.length; i++) {
				int index = index2[i];
				michiganPopulation.add(parent2.getVariable(index).copy());
			}

			PittsburghSolution child = parent1.copy();
			child.clearVariable(michiganPopulation.size());

			for(int i=0; i<michiganPopulation.size(); i++) {
				child.setVariable(i, michiganPopulation.get(i));
			}
			offspring.clear();
			offspring.add(child);
		}
		/* Don't crossover */
		else {
			offspring.add(parent1.copy());
			offspring.add(parent2.copy());
			int index = selectRandomGenerator.getRandomValue(0,  1);
			offspring.remove(index);
		}

		if(offspring.get(0).getNumberOfVariables() < 1) {System.err.println("incorrect input: number Of Rules is less than 1");}
		return offspring;
	}
}
