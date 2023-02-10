package cilabo.gbml.solution.pittsburghSolution;

import java.util.List;

import org.uma.jmetal.solution.AbstractSolution;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.fuzzy.classifier.Classifier;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import xml.XML_TagName;

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
		List<michiganSolution> michiganSolutionList = michiganSolutionBuilder.createMichiganSolutions(numberOfVariables);
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			this.setVariable(i, michiganSolutionList.get(i));
		}
	}

	public AbstractPittsburghSolution(int numberOfObjectives,
			int numberOfConstraints,
			MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder,
			Classifier<michiganSolution> classifier,
			Element pittsburghSolution) {
		super(pittsburghSolution.getElementsByTagName(XML_TagName.michiganSolution.toString()).getLength(),
				numberOfObjectives, numberOfConstraints);
		this.michiganSolutionBuilder = michiganSolutionBuilder;
		this.classifier = classifier;
		NodeList michiganSolutionList = pittsburghSolution.getElementsByTagName(XML_TagName.michiganSolution.toString());
		for(int i=0; i<michiganSolutionList.getLength(); i++) {
			michiganSolution michiganSolution = michiganSolutionBuilder.createMichiganSolution((Element)michiganSolutionList.item(i));
			this.setVariable(i, michiganSolution);
		}
	}

	@Override
	public MichiganSolutionBuilder<michiganSolution> getMichiganSolutionBuilder() {
		return this.michiganSolutionBuilder;
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
	public void clearVariables() {
		this.variables.clear();
	}

	@Override
	public void clearAttributes() {
		this.attributes.clear();
	}

	@Override
	public void learning() {
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			this.variables.get(i).learning();
		}
	}
}
