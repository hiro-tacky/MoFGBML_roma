package cilabo.gbml.solution.pittsburghSolution;

import org.uma.jmetal.solution.Solution;
import org.w3c.dom.Element;

import cilabo.data.InputVector;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;

public interface PittsburghSolution <michiganSolution extends MichiganSolution>
	extends Solution<michiganSolution> {

	@Override
	public void setVariable(int index, michiganSolution value);

	@Override
	public michiganSolution getVariable(int index);

	public void clearAttributes(String attributeID);

	public void removeVariable(int index);

	public void addVariable(michiganSolution value);

	public void clearVariable();

	public void clearVariable(int numberOfVariables);

	public MichiganSolution classify(InputVector x);

	public Element toElement();

	@Override
	public PittsburghSolution<michiganSolution> copy();

	public void learning();

	public MichiganSolutionBuilder<michiganSolution> getMichiganSolutionBuilder();
}
