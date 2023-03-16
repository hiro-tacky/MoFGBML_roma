package cilabo.gbml.problem.pittsburghFGBML_Problem.impl;

import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.w3c.dom.Element;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.pittsburgh.Classifier;
import cilabo.gbml.objectivefunction.pittsburgh.ErrorRate;
import cilabo.gbml.objectivefunction.pittsburgh.NumberOfRules;
import cilabo.gbml.problem.pittsburghFGBML_Problem.AbstractPittsburghProblem;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.builder.MichiganSolutionBuilder;
import cilabo.gbml.solution.pittsburghSolution.impl.PittsburghSolution_Basic;
import cilabo.main.ExperienceParameter.OBJECTIVES_FOR_PITTSBURGH;

public class PittsburghProblem_Basic <MichiganSolutionClass extends MichiganSolution<?>>
		extends AbstractPittsburghProblem<PittsburghSolution_Basic<MichiganSolutionClass>, MichiganSolutionClass>
		implements Problem<PittsburghSolution_Basic<MichiganSolutionClass>>{

	/** 目的関数の個数 number of objectives */
	private static final int numberOfObjectives = 2;
	/** 制約の個数 number of constraints */
	private static final int numberOfConstraints = 0;

	/** コンストラクタ．constructor
	 * @param numberOfVariables 遺伝子変数の個数 number of variables
	 * @param train 識別されるデータセット data set to be classified
	 * @param michiganSolutionBuilder Michigan型識別器ビルダー michigan solution builder
	 * @param classifier pittsuburgh型識別器のメソッドクラス．methods for pittsuburgh classifier
	 */
	public PittsburghProblem_Basic(
			int numberOfVariables,
			DataSet<?> train,
			MichiganSolutionBuilder<MichiganSolutionClass> michiganSolutionBuilder,
			Classifier<PittsburghSolution_Basic<MichiganSolutionClass>, MichiganSolutionClass> classifier) {
		super(numberOfVariables,
				PittsburghProblem_Basic.numberOfObjectives,
				PittsburghProblem_Basic.numberOfConstraints,
				train,
				michiganSolutionBuilder,
				classifier);
		this.setName("PittsburghClassificationProblem_Basic");
	}

	@Override
	public void evaluate(PittsburghSolution_Basic<MichiganSolutionClass> solution) {
		/* The first objective */
		ErrorRate<PittsburghSolution_Basic<MichiganSolutionClass>> objective1 =
				new ErrorRate<PittsburghSolution_Basic<MichiganSolutionClass>>(
						OBJECTIVES_FOR_PITTSBURGH.ErrorRateDtra.toInt());
		double f1 = objective1.calculateObjective(solution, train);
		/* The second objective */
		NumberOfRules<PittsburghSolution_Basic<MichiganSolutionClass>> objective2 =
				new NumberOfRules<PittsburghSolution_Basic<MichiganSolutionClass>>(
						OBJECTIVES_FOR_PITTSBURGH.NumberOfRule.toInt());
		double f2 = objective2.calculateObjective(solution);

		solution.setObjective(OBJECTIVES_FOR_PITTSBURGH.ErrorRateDtra.toInt(), f1);
		solution.setObjective(OBJECTIVES_FOR_PITTSBURGH.NumberOfRule.toInt(), f2);
	}

	@Override
	public PittsburghSolution_Basic<MichiganSolutionClass> createSolution() {
		PittsburghSolution_Basic<MichiganSolutionClass> pittsburghSolution = new PittsburghSolution_Basic<MichiganSolutionClass>(
				this.getNumberOfVariables(),
				this.getNumberOfObjectives(),
				this.getNumberOfConstraints(),
				this.michiganSolutionBuilder.copy(),
				this.classifier.copy());

		List<MichiganSolutionClass> solutionArray = this.michiganSolutionBuilder.createMichiganSolutions(this.getNumberOfVariables());
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			pittsburghSolution.setVariable(i, solutionArray.get(i));
		}
		return pittsburghSolution;
	}

	public PittsburghSolution_Basic<MichiganSolutionClass> createSolution(Element pittsburghSolutionEL) {
		PittsburghSolution_Basic<MichiganSolutionClass> pittsburghSolution = new PittsburghSolution_Basic<MichiganSolutionClass>(
				this.getNumberOfObjectives(),
				this.getNumberOfConstraints(),
				this.michiganSolutionBuilder.copy(),
				this.classifier.copy(),
				pittsburghSolutionEL);
		return pittsburghSolution;
	}

	@Override
	public String toString() {
		return "PittsburghFGBML_Basic [michiganSolutionBuilder=" + michiganSolutionBuilder
				+ ", classifier=" + classifier + "]";
	}
}
