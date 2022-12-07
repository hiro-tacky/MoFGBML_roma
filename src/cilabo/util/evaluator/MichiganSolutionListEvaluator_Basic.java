package cilabo.util.evaluator;

import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import cilabo.gbml.problem.michiganFGBML_Problem.impl.MichiganFGBML_Basic;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.main.ExperienceMethods.ObjectivesIndexMichigan;

public class MichiganSolutionListEvaluator_Basic<michiganSolution extends AbstractMichiganSolution> implements SolutionListEvaluator<michiganSolution> {

	@Override
	public void shutdown() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public List<michiganSolution> evaluate(List<michiganSolution> solutionList, Problem<michiganSolution> problem) {
		if(!(problem instanceof MichiganFGBML_Basic)) {throw new IllegalArgumentException();}

	    solutionList.stream().forEach(s -> problem.evaluate(s));
	    solutionList.stream().forEach(s -> s.setObjective(ObjectivesIndexMichigan.FitnessValue.toInt(), 0));
	    ((MichiganFGBML_Basic) problem).calculateNumberOfWinnerRule();

		return solutionList;
	}
}
