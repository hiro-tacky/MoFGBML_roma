package cilabo.gbml.algorithm;

import java.util.List;
import java.util.Map;

import org.uma.jmetal.algorithm.impl.AbstractEvolutionaryAlgorithm;
import org.uma.jmetal.component.evaluation.Evaluation;
import org.uma.jmetal.component.initialsolutioncreation.InitialSolutionsCreation;
import org.uma.jmetal.component.replacement.Replacement;
import org.uma.jmetal.component.selection.MatingPoolSelection;
import org.uma.jmetal.component.termination.Termination;
import org.uma.jmetal.component.variation.Variation;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.util.observable.Observable;
import org.uma.jmetal.util.observable.ObservableEntity;

import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;

public class MichiganMoFGBML<michiganSolution extends AbstractMichiganSolution>
	extends AbstractEvolutionaryAlgorithm<michiganSolution, List<michiganSolution>>
	implements ObservableEntity {

	private int evaluations;
	private int populationSize;
	private int offspringPopulationSize;

	protected SelectionOperator<List<michiganSolution>, michiganSolution> selectionOperator;
	protected CrossoverOperator<michiganSolution> crossoverOperator;
	protected MutationOperator<michiganSolution> mutationOperator;

	private Map<String, Object> algorithmStatusData;

	private InitialSolutionsCreation<michiganSolution> initialSolutionsCreation;
	private Termination termination;
	private Evaluation<michiganSolution> evaluation ;
	private Replacement<michiganSolution> replacement;
	private Variation<michiganSolution> variation;
	private MatingPoolSelection<michiganSolution> selection;

	private long startTime;
	private long totalComputingTime;

	public MichiganMoFGBML(int evaluations, int populationSize, int offspringPopulationSize,
			SelectionOperator<List<michiganSolution>, michiganSolution> selectionOperator,
			CrossoverOperator<michiganSolution> crossoverOperator, MutationOperator<michiganSolution> mutationOperator,
			Map<String, Object> algorithmStatusData,
			InitialSolutionsCreation<michiganSolution> initialSolutionsCreation, Termination termination,
			Evaluation<michiganSolution> evaluation, Replacement<michiganSolution> replacement,
			Variation<michiganSolution> variation, MatingPoolSelection<michiganSolution> selection, long startTime,
			long totalComputingTime) {
		super();
		this.evaluations = evaluations;
		this.populationSize = populationSize;
		this.offspringPopulationSize = offspringPopulationSize;
		this.selectionOperator = selectionOperator;
		this.crossoverOperator = crossoverOperator;
		this.mutationOperator = mutationOperator;
		this.algorithmStatusData = algorithmStatusData;
		this.initialSolutionsCreation = initialSolutionsCreation;
		this.termination = termination;
		this.evaluation = evaluation;
		this.replacement = replacement;
		this.variation = variation;
		this.selection = selection;
		this.startTime = startTime;
		this.totalComputingTime = totalComputingTime;
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String getDescription() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected void initProgress() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void updateProgress() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected boolean isStoppingConditionReached() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	protected List<michiganSolution> createInitialPopulation() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected List<michiganSolution> evaluatePopulation(List<michiganSolution> population) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected List<michiganSolution> selection(List<michiganSolution> population) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected List<michiganSolution> reproduction(List<michiganSolution> population) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	protected List<michiganSolution> replacement(List<michiganSolution> population,
			List<michiganSolution> offspringPopulation) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<michiganSolution> getResult() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Observable<?> getObservable() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
