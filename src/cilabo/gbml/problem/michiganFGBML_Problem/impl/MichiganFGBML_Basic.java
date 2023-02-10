package cilabo.gbml.problem.michiganFGBML_Problem.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import cilabo.data.DataSet;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.gbml.objectivefunction.michigan.RuleLength;
import cilabo.gbml.problem.michiganFGBML_Problem.AbstractMichiganFGBML;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Basic;
import cilabo.main.ExperienceParameter.OBJECTIVES_FOR_MICHIGAN;

public class MichiganFGBML_Basic <RuleObject extends Rule<?, ?, ?, ?, ?, ?>>
	extends AbstractMichiganFGBML <MichiganSolution_Basic<RuleObject>, RuleObject>{

	private List<WinnerSolution> winnerSolutionForEachPattern = new ArrayList<WinnerSolution>(this.getTrain().getDataSize());

	public MichiganFGBML_Basic(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet<?> train,
			RuleBuilder<RuleObject, ?, ?> ruleBuilder) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints,
				train, ruleBuilder);
		this.setName("MichiganFGBML_Problem_Basic");
		for(int i=0; i<this.getTrain().getDataSize(); i++) {
			winnerSolutionForEachPattern.add(new WinnerSolution());
		}
	}

	public MichiganFGBML_Basic(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet<?> train,
			List<Pair<Integer, Integer>> bounds,
			RuleBuilder<RuleObject, ?, ?> ruleBuilder) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints, train, bounds, ruleBuilder);
		this.setName("MichiganFGBML_Problem_Basic");
		for(int i=0; i<this.getTrain().getDataSize(); i++) {
			winnerSolutionForEachPattern.add(new WinnerSolution());
		}
	}

	@Override
	public void evaluate(MichiganSolution_Basic<RuleObject> solution) {

		/* The first objective */
		for(int i=0; i<train.getDataSize(); i++) {
			double fitnessValue = solution.getFitnessValue(train.getPattern(i).getAttributeVector());
			double maxFitnessValue = winnerSolutionForEachPattern.get(i).getMaxFitnessValue();
			if(maxFitnessValue < fitnessValue) {
				winnerSolutionForEachPattern.set(i, new WinnerSolution(fitnessValue, solution));
			}
		}
		/* The second objective */
		RuleLength<MichiganSolution_Basic<RuleObject>> RuleLengthFunc = new RuleLength<MichiganSolution_Basic<RuleObject>>();
		solution.setObjective(OBJECTIVES_FOR_MICHIGAN.RuleLength.toInt(), RuleLengthFunc.function(solution));
	}

	public void calculateNumberOfWinnerRule() {
		for(int i=0; i<train.getDataSize(); i++) {
			double buf = winnerSolutionForEachPattern.get(i).getSolution().getObjective(OBJECTIVES_FOR_MICHIGAN.FitnessValue.toInt());
			winnerSolutionForEachPattern.get(i).getSolution().setObjective(OBJECTIVES_FOR_MICHIGAN.FitnessValue.toInt(), buf+1);
		}
	}


	private class WinnerSolution{
		private double maxFitnessValue;
		private MichiganSolution_Basic<RuleObject> solution;

		public WinnerSolution(double maxFitnessValue, MichiganSolution_Basic<RuleObject> solution) {
			this.maxFitnessValue = maxFitnessValue;
			this.solution = solution;
		}

		public WinnerSolution() {}

		public double getMaxFitnessValue() {
			return maxFitnessValue;
		}

		public MichiganSolution_Basic<RuleObject> getSolution() {
			return solution;
		}

		public void setWinnerSolution(double maxFitnessValue, MichiganSolution_Basic<RuleObject> solution) {
			this.maxFitnessValue = maxFitnessValue;
			this.solution = solution;
		}
	}

	@Override
	public MichiganSolution_Basic<RuleObject> createSolution() {
		return new MichiganSolution_Basic<RuleObject>(
				this.getNumberOfVariables(),
				this.getNumberOfObjectives(),
				this.ruleBuilder
			);
	}

}
