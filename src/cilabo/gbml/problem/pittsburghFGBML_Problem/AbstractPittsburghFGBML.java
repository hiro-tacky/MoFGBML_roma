package cilabo.gbml.problem.pittsburghFGBML_Problem;

import org.uma.jmetal.problem.AbstractGenericProblem;
import org.uma.jmetal.problem.Problem;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public abstract class AbstractPittsburghFGBML <pittsburghSolutionObject extends PittsburghSolution<michiganSolution>,
		michiganSolution extends MichiganSolution<?>>
		extends AbstractGenericProblem <pittsburghSolutionObject> implements Problem<pittsburghSolutionObject>{

	protected DataSet train;

	protected MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder;

	/** 識別方式
	 * @see cilabo.fuzzy.classifier.classification
	 */
	protected Classifier<michiganSolution> classifier;

	/** Constructor */
	public AbstractPittsburghFGBML(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet train,
			MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder,
			Classifier<michiganSolution> classifier) {
		setNumberOfVariables(numberOfVariables);
		setNumberOfObjectives(numberOfObjectives);
		setNumberOfConstraints(numberOfConstraints);
		this.train = train;
		this.michiganSolutionBuilder = michiganSolutionBuilder;
		this.classifier = classifier;
	}

	public DataSet getTrain() {
		return train;
	}
}
