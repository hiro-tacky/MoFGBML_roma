package cilabo.gbml.solution.util;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public class EqualsSolution {

	public static boolean equalsPittsburghSolution(PittsburghSolution solution1, PittsburghSolution solution2) {
		boolean flag = true;
		if(solution1.getNumberOfVariables() != solution2.getNumberOfVariables()){
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

	public static boolean equalsMichiganSolution(MichiganSolution solution1, MichiganSolution solution2) {
		boolean flag = true;
		if(!solution1.getVariables().equals(solution2.getVariables())){ flag = false; }
		if(!solution1.equalsClassLabel(solution2.getClassLabel())){ flag = false; }
		if(!solution1.getRuleWeight().equals(solution2.getRuleWeight())){ flag = false; }
		return flag;
	}
}
