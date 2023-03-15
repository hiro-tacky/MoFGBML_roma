package cilabo.gbml.solution.util;

import java.util.Objects;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public class EqualsSolution {

	public static boolean equalsPittsburghSolution(PittsburghSolution<?> solution1, PittsburghSolution<?> solution2) {
		boolean flag = true;
		if(solution1.getNumberOfVariables() != solution2.getNumberOfVariables()){
			flag = false;
		}else if(Objects.isNull(solution1) || Objects.isNull(solution2)) {
			flag = false;
		}else {
			for(int i=0; i<solution1.getNumberOfVariables(); i++) {
				if(!EqualsSolution.equalsMichiganSolution(solution1.getVariable(i), solution2.getVariable(i))) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static boolean equalsMichiganSolution(MichiganSolution<?> solution1, MichiganSolution<?> solution2) {
		boolean flag = true;
		if(!solution1.getVariables().equals(solution2.getVariables())){ flag = false; }
		if(!solution1.getRule().getConsequent().getClassLabel().equals(solution2.getRule().getConsequent().getClassLabel())){ flag = false; }
		if(!solution1.getRule().getConsequent().getRuleWeight().equals(solution2.getRule().getConsequent().getRuleWeight())){ flag = false; }
		return flag;
	}
}
