package cilabo.gbml.problem.pittsburghFGBML_Problem.impl;

import org.uma.jmetal.problem.Problem;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.gbml.objectivefunction.pittsburgh.ErrorRate;
import cilabo.gbml.objectivefunction.pittsburgh.NumberOfRules;
import cilabo.gbml.problem.pittsburghFGBML_Problem.AbstractPittsburghFGBML;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.pittsburghSolution.impl.PittsburghSolution_Basic;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;

public class PittsburghFGBML_Basic <michiganSolution extends MichiganSolution>
		extends AbstractPittsburghFGBML<PittsburghSolution_Basic<michiganSolution>, michiganSolution> implements Problem<PittsburghSolution_Basic<michiganSolution>>{

	public PittsburghFGBML_Basic(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet train,
			MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder,
			Classifier<michiganSolution> classifier) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints,
					train, michiganSolutionBuilder, classifier);
		this.setName("PittsburghFGBML_Basic");
	}

	@Override
	public void evaluate(PittsburghSolution_Basic<michiganSolution> solution) {
		/* The first objective */
		ErrorRate<PittsburghSolution_Basic<michiganSolution>> function1 = new ErrorRate<PittsburghSolution_Basic<michiganSolution>>();
		double f1 = function1.function(solution, train);
		/* The second objective */
		NumberOfRules<PittsburghSolution_Basic<michiganSolution>> function2 = new NumberOfRules<PittsburghSolution_Basic<michiganSolution>>();
		double f2 = function2.function(solution);

		solution.setObjective(0, f1);
		solution.setObjective(1, f2);

	}

	public void removeNoWinnerMichiganSolution(PittsburghSolution_Basic<michiganSolution> solution) {
		for(int i=0; i<solution.getNumberOfVariables(); i++) {
			if((int)solution.getVariable(i).getAttribute((new NumberOfWinner()).getAttributeId()) < 1) {
				solution.removeVariable(i); i--;
			}
		}
		//ルール数がゼロになった場合，初期個体性法を適用する
		while(solution.getNumberOfVariables() == 0) {
			solution.clearVariable(this.getNumberOfVariables());
			michiganSolution[] solutionArray = this.michiganSolutionBuilder.createMichiganSolutions(this.getNumberOfVariables());
			for(int i=0; i<this.getNumberOfVariables(); i++) {
				solution.setVariable(i, solutionArray[i]);
			}
			this.evaluate(solution);
			this.removeNoWinnerMichiganSolution(solution);
//			throw new ArithmeticException("This PittsburghSolution has no michiganSolution");
		}
	}

	@Override
	public PittsburghSolution_Basic<michiganSolution> createSolution() {
		PittsburghSolution_Basic<michiganSolution> pittsburghSolution = new PittsburghSolution_Basic<michiganSolution>(
				this.getNumberOfVariables(),
				this.getNumberOfObjectives(),
				this.getNumberOfConstraints(),
				this.michiganSolutionBuilder.copy(),
				this.classifier.copy());

		michiganSolution[] solutionArray = this.michiganSolutionBuilder.createMichiganSolutions(this.getNumberOfVariables());
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			pittsburghSolution.setVariable(i, solutionArray[i]);
		}
		return pittsburghSolution;
	}

	@Override
	public String toString() {
		return "PittsburghFGBML_Basic [michiganSolutionBuilder=" + michiganSolutionBuilder
				+ ", classifier=" + classifier + "]";
	}
}
