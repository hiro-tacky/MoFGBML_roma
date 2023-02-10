package cilabo.gbml.problem.pittsburghFGBML_Problem.impl;

import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.w3c.dom.Element;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.gbml.objectivefunction.pittsburgh.ErrorRate;
import cilabo.gbml.objectivefunction.pittsburgh.RuleInterpretation;
import cilabo.gbml.problem.pittsburghFGBML_Problem.AbstractPittsburghFGBML;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.pittsburghSolution.impl.PittsburghSolution_Basic;
import cilabo.main.ExperienceParameter.OBJECTIVES_FOR_PITTSBURGH;

public class PittsburghFGBML_ObjectiveFunction2 <michiganSolution extends MichiganSolution<?>>
		extends AbstractPittsburghFGBML<PittsburghSolution_Basic<michiganSolution>, michiganSolution> implements Problem<PittsburghSolution_Basic<michiganSolution>>{

	public PittsburghFGBML_ObjectiveFunction2(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet<?> train,
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
		RuleInterpretation<PittsburghSolution_Basic<michiganSolution>> function2 = new RuleInterpretation<PittsburghSolution_Basic<michiganSolution>>();
		double f2 = function2.function(solution);

		solution.setObjective(OBJECTIVES_FOR_PITTSBURGH.ErrorRateDtra.toInt(), f1);
		solution.setObjective(OBJECTIVES_FOR_PITTSBURGH.NumberOfRule.toInt(), f2);

	}

	@Override
	public PittsburghSolution_Basic<michiganSolution> createSolution() {
		PittsburghSolution_Basic<michiganSolution> pittsburghSolution = new PittsburghSolution_Basic<michiganSolution>(
				this.getNumberOfVariables(),
				this.getNumberOfObjectives(),
				this.getNumberOfConstraints(),
				this.michiganSolutionBuilder.copy(),
				this.classifier.copy());

		List<michiganSolution> solutionArray = this.michiganSolutionBuilder.createMichiganSolutions(this.getNumberOfVariables());
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			pittsburghSolution.setVariable(i, solutionArray.get(i));
		}
		return pittsburghSolution;
	}

	public PittsburghSolution_Basic<michiganSolution> createSolution(Element pittsburghSolutionNode) {

		PittsburghSolution_Basic<michiganSolution> pittsburghSolution = new PittsburghSolution_Basic<michiganSolution>(
				this.getNumberOfObjectives(),
				this.getNumberOfConstraints(),
				this.michiganSolutionBuilder.copy(),
				this.classifier.copy(),
				pittsburghSolutionNode);
		return pittsburghSolution;
	}

	@Override
	public String toString() {
		return "PittsburghFGBML_Basic [michiganSolutionBuilder=" + michiganSolutionBuilder
				+ ", classifier=" + classifier + "]";
	}
}
