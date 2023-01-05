package cilabo.util.evaluator;

import java.util.List;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import cilabo.gbml.problem.michiganFGBML_Problem.impl.MichiganFGBML_Basic;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.main.ExperienceParameter.OBJECTIVES_FOR_MICHIGAN;

public class MichiganSolutionListEvaluator_Basic<michiganSolution extends MichiganSolution> implements SolutionListEvaluator<michiganSolution> {

	@Override
	public void shutdown() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public List<michiganSolution> evaluate(List<michiganSolution> solutionList, Problem<michiganSolution> problem) {
		if(!(problem instanceof MichiganFGBML_Basic)) {throw new IllegalArgumentException();}

	    solutionList.stream().forEach(s -> problem.evaluate(s));
	    solutionList.stream().forEach(s -> s.setObjective(OBJECTIVES_FOR_MICHIGAN.FitnessValue.toInt(), 0));
	    ((MichiganFGBML_Basic) problem).calculateNumberOfWinnerRule();

		return solutionList;
	}
}
