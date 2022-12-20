package cilabo.gbml.solution.pittsburghSolution;

import java.util.ArrayList;

import org.uma.jmetal.solution.AbstractSolution;

import cilabo.fuzzy.classifier.Classifier;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;

public abstract class AbstractPittsburghSolution <michiganSolution extends MichiganSolution<?>>
		extends AbstractSolution<michiganSolution>
		implements PittsburghSolution<michiganSolution>{

	/** 識別器 */
	protected Classifier<michiganSolution> classifier;
	public MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder;

	/** Constructor */
	protected AbstractPittsburghSolution(int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder,
			Classifier<michiganSolution> classifier) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints);
		this.michiganSolutionBuilder = michiganSolutionBuilder;
		this.classifier = classifier;
	}

	@Override
	public void removeVariable(int index) {
		this.variables.remove(index);
	}

	@Override
	public void addVariable(michiganSolution value) {
		this.variables.add(value);
	}

	@Override
	public void clearVariable() {
		this.variables.clear();
	}

	@Override
	public void clearVariable(int numberOfVariables) {
		this.variables = new ArrayList<>(numberOfVariables);
		for (int i = 0; i < numberOfVariables; i++) {
			variables.add(i, null);
		}
	}

	@Override
	public void clearAttributes(String attributeID) {
		for(michiganSolution michiganSolution_i: this.variables) {
			michiganSolution_i.setAttribute(attributeID, 0);
		}
	}

	@Override
	public void learning() {
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			this.variables.get(i).learning();
		}
	}

	public MichiganSolutionBuilder<michiganSolution> getMichiganSolutionBuilder() {
		return this.michiganSolutionBuilder;
	}
}
