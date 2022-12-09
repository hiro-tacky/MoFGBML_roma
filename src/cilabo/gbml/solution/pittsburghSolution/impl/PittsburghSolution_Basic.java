package cilabo.gbml.solution.pittsburghSolution.impl;

import java.util.HashMap;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.pittsburghSolution.AbstractPittsburghSolution;

public final class PittsburghSolution_Basic <michiganSolution extends MichiganSolution>
		extends AbstractPittsburghSolution<michiganSolution>{

	public PittsburghSolution_Basic(int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder,
			Classifier<michiganSolution> classifier) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints,
				michiganSolutionBuilder, classifier);
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			michiganSolution michiganSolution = michiganSolutionBuilder.createMichiganSolution();
			this.setVariable(i, michiganSolution);
		}
	}

	private PittsburghSolution_Basic(PittsburghSolution_Basic<michiganSolution> solution) {
	    super(solution.getNumberOfVariables(), solution.getNumberOfObjectives(), solution.getNumberOfConstraints(),
	    		solution.michiganSolutionBuilder, solution.classifier.copy());

	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	      setVariable(i, (michiganSolution) solution.getVariable(i).copy());
	    }

	    for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
	      setObjective(i, solution.getObjective(i));
	    }

	    for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
	      setConstraint(i, solution.getConstraint(i));
	    }

	    this.attributes = new HashMap<>(solution.attributes);
	}

	@Override
	public PittsburghSolution_Basic<michiganSolution> copy() {
		return new PittsburghSolution_Basic<michiganSolution>(this);
	}

	@Override
	public MichiganSolution classify(InputVector x) {
		return this.classifier.classify(this.getVariables(), x);
	}

	@Override
	public String toString() {
		String ln = System.getProperty("line.separator");
		String str = "PittsburghSolution_Basic";

		str += ", {" + String.format("Objectives[%d]=%.4f..", 0, this.getObjective(0));
		for(int i=1; i<this.getNumberOfObjectives(); i++) {
			str += String.format(", Objectives[%d]=%.4f..", i, this.getObjective(i));
		}
		str += "}" + ln;

		for(michiganSolution tmp: this.variables) {
			str += " ->" + tmp + ln;
		}
		str += "[classifier=" + classifier + ", attributes=" + attributes + "]" + ln + ln;
		return str;
	}

}
