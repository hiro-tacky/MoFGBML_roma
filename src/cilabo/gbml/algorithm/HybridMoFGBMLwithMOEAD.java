package cilabo.gbml.algorithm;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.uma.jmetal.algorithm.impl.AbstractEvolutionaryAlgorithm;
import org.uma.jmetal.component.evaluation.Evaluation;
import org.uma.jmetal.component.evaluation.impl.SequentialEvaluation;
import org.uma.jmetal.component.initialsolutioncreation.InitialSolutionsCreation;
import org.uma.jmetal.component.initialsolutioncreation.impl.RandomSolutionsCreation;
import org.uma.jmetal.component.replacement.Replacement;
import org.uma.jmetal.component.selection.MatingPoolSelection;
import org.uma.jmetal.component.selection.impl.PopulationAndNeighborhoodMatingPoolSelection;
import org.uma.jmetal.component.termination.Termination;
import org.uma.jmetal.component.variation.Variation;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.aggregativefunction.AggregativeFunction;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.neighborhood.impl.WeightVectorNeighborhood;
import org.uma.jmetal.util.observable.Observable;
import org.uma.jmetal.util.observable.ObservableEntity;
import org.uma.jmetal.util.observable.impl.DefaultObservable;
import org.uma.jmetal.util.sequencegenerator.SequenceGenerator;
import org.uma.jmetal.util.sequencegenerator.impl.IntegerPermutationGenerator;
import org.w3c.dom.Element;

import cilabo.data.DataSetManager;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.gbml.component.replacement.MOEADReplacementoForPittsburgh;
import cilabo.gbml.component.variation.CrossoverAndMutationAndPittsburghLearningVariation;
import cilabo.gbml.problem.pittsburghFGBML_Problem.AbstractPittsburghProblem;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.util.aggregativefunction.impl.TschebyscheffForGBML;
import cilabo.main.Consts;
import cilabo.util.fileoutput.PittsburghSolutionListOutput;
import xml.XML_TagName;
import xml.XML_manager;

public class HybridMoFGBMLwithMOEAD<S extends PittsburghSolution<?>> extends AbstractEvolutionaryAlgorithm<S, List<S>> implements ObservableEntity {
	protected int evaluations;
	protected int populationSize;
	protected int offspringPopulationSize;
	protected String outputRootDir;
	protected int frequency;

	protected InitialSolutionsCreation<S> initialSolutionsCreation;
	protected Termination termination;
	protected Evaluation<S> evaluation;
	protected Replacement<S> replacement;
	protected Variation<S> variation;
	protected MatingPoolSelection<S> selection;

	protected long startTime;
	protected long totalComputingTime;

	protected Map<String, Object> algorithmStatusData;
	protected Observable<Map<String, Object>> observable;

	/** Constructor */
	public HybridMoFGBMLwithMOEAD(
			Problem<S> problem,
			int populationSize,
			int frequency,
			String outputRootDir,
			double neighborhoodSelectionProbability,
			int maximumNumberOfReplacedSolutions,
			int neighborhoodSize,
			CrossoverOperator<S> crossoverOperator,
			MutationOperator<S> mutationOperator,
			Termination termination) {

		this.populationSize = populationSize;
		this.problem = problem;

		this.offspringPopulationSize = 1;
		this.outputRootDir = outputRootDir;
		this.frequency = frequency;

		SequenceGenerator<Integer> subProblemIdGenerator =
		new IntegerPermutationGenerator(populationSize);

		this.initialSolutionsCreation = new RandomSolutionsCreation<S>(problem, populationSize);

		this.variation =
			new CrossoverAndMutationAndPittsburghLearningVariation<S>(
					offspringPopulationSize, crossoverOperator, mutationOperator);

		WeightVectorNeighborhood<S> neighborhood =
			new WeightVectorNeighborhood<>(
					populationSize,
					neighborhoodSize);

		int numberOfRequiredParents = ((CrossoverAndMutationAndPittsburghLearningVariation)variation)
				.getCrossover().getNumberOfRequiredParents();

		this.selection =
				new PopulationAndNeighborhoodMatingPoolSelection<S>(
						numberOfRequiredParents,
						subProblemIdGenerator,
						neighborhood,
						neighborhoodSelectionProbability,
						true);

		AggregativeFunction aggregativeFunction =
				new TschebyscheffForGBML(
						Consts.MAX_RULE_NUM,
						DataSetManager.getInstance().getTrains().get(0).getDataSetSize());

		this.replacement =
				new MOEADReplacementoForPittsburgh(
						(PopulationAndNeighborhoodMatingPoolSelection) selection,
						neighborhood,
						aggregativeFunction,
						subProblemIdGenerator,
						maximumNumberOfReplacedSolutions);

		this.termination = termination ;
		this.evaluation = new SequentialEvaluation<>();

		this.algorithmStatusData = new HashMap<>();

		this.observable = new DefaultObservable<>("Hybrid MoFGBML with MOEA/D algorithm");
	}

	/**
	 * Empty constructor that creates an empty instance. It is intended to allow the definition of different subclass
	 * constructors. It is up to the developer the correct creation of the algorithm components.
	 */
	public HybridMoFGBMLwithMOEAD() {
	}

	@Override
	public void run() {
		startTime = System.currentTimeMillis();
		/* === START === */
		List<S> offspringPopulation;
		List<S> matingPopulation;

		/* Step 1. 初期個体群生成 - Initialization Population */
		population = createInitialPopulation();
		/* Step 2. 初期個体群評価 - Initial Population Evaluation */
		population = evaluatePopulation(population);
		/* 未勝利個体削除*/
		population = removeNoWinnerMichiganSolution(population);
		/* JMetal progress initialization */

		initProgress();

		Element population_ = XML_manager.getInstance().createElement(XML_TagName.population);
		for(S solution: this.getResult()) {
			XML_manager.getInstance().addElement(population_, solution.toElement());
		}
		Element generations_ = XML_manager.getInstance().createElement(XML_TagName.generations, XML_TagName.evaluation, String.valueOf(0));
		//knowlwdge出力用
		XML_manager.getInstance().addElement(generations_, Knowledge.getInstance().toElement());
		XML_manager.getInstance().addElement(generations_, population_);
    	XML_manager.getInstance().addElement(XML_manager.getInstance().getRoot(), generations_);

		/* GA loop */
		while(!isStoppingConditionReached()) {

			/* 親個体選択 - Mating Selection */
			matingPopulation = selection(population);
			/* 子個体群生成 - Offspring Generation */
			offspringPopulation = reproduction(matingPopulation);
			/* 子個体群評価 - Offsprign Evaluation */
			offspringPopulation = evaluatePopulation(offspringPopulation);
			/* 未勝利個体削除*/
			offspringPopulation = removeNoWinnerMichiganSolution(offspringPopulation);
			/* 個体群更新・環境選択 - Environmental Selection */
			population = replacement(population, offspringPopulation);
			/* JMetal progress update */
			updateProgress();
		}
		totalComputingTime = System.currentTimeMillis() - startTime;
	}

	@Override
	protected void initProgress() {
		evaluations = populationSize;

		algorithmStatusData.put("EVALUATIONS", evaluations);
		algorithmStatusData.put("POPULATION", population);
		algorithmStatusData.put("COMPUTING_TIME", System.currentTimeMillis() - startTime);

		observable.setChanged();
		observable.notifyObservers(algorithmStatusData);
	}

	@Override
	protected void updateProgress() {
		evaluations += offspringPopulationSize;
	    algorithmStatusData.put("EVALUATIONS", evaluations);
	    algorithmStatusData.put("POPULATION", population);
	    algorithmStatusData.put("COMPUTING_TIME", System.currentTimeMillis() - startTime);

	    observable.setChanged();
	    observable.notifyObservers(algorithmStatusData);

	    String sep = File.separator;
	    Integer evaluations = (Integer)algorithmStatusData.get("EVALUATIONS");
	    if(evaluations != null) {
	    	if(evaluations * 10 % frequency == 0 && evaluations % frequency != 0) System.out.print(". ");
	    	if(evaluations % frequency == 0) {
	    		System.out.print(" ->");
	    		for(int i=0; i<getPopulation().get(0).getNumberOfObjectives(); i++) {
	    			double tmp=0;
	    			for(int j=0; j<getPopulation().size(); j++) {
	    				tmp += getPopulation().get(j).getObjective(i);
	    			}
	    			tmp /= getPopulation().size();
		    		System.out.print(String.format("objectives[%d]: %.8f.. ", i, tmp));
	    		}
	    		System.out.println(); System.out.println();

	    	    new PittsburghSolutionListOutput((List<PittsburghSolution<?>>) this.getResult())
		            .setVarFileOutputContext(new DefaultFileOutputContext(outputRootDir + sep + String.format("VAR-%010d.csv", evaluations), ","))
		            .setFunFileOutputContext(new DefaultFileOutputContext(outputRootDir + sep + String.format("FUN-%010d.csv", evaluations), ","))
		            .print();

	    		Element population = XML_manager.getInstance().createElement(XML_TagName.population);

	    		for(S solution: this.getResult()) {
	    			Element pittsburghSolution = solution.toElement();
	    			XML_manager.getInstance().addElement(population, pittsburghSolution);
	    		}

	    		Element generations = XML_manager.getInstance().createElement(XML_TagName.generations, XML_TagName.evaluation, String.valueOf(evaluations));

	    		//knowlwdge出力用
	    		XML_manager.getInstance().addElement(generations, Knowledge.getInstance().toElement());
	    		XML_manager.getInstance().addElement(generations, population);
		    	XML_manager.getInstance().addElement(XML_manager.getInstance().getRoot(), generations);
	    	}
	    }
		else {
			JMetalLogger.logger.warning(getClass().getName()
			+ ": The algorithm has not registered yet any info related to the EVALUATIONS key");
		}
	}

	@Override
	protected boolean isStoppingConditionReached() {
		return termination.isMet(algorithmStatusData);
	}

	@Override
	protected List<S> createInitialPopulation() {
		return initialSolutionsCreation.create();
	}

	@Override
	protected List<S> evaluatePopulation(List<S> population) {
		return evaluation.evaluate(population, getProblem());
	}

	/**
	 * This method iteratively applies a {@link SelectionOperator} to the population to fill the
	 * mating pool population.
	 *
	 * @param population
	 * @return The mating pool population
	 */
	@Override
	protected List<S> selection(List<S> population) {
		List<S> matingPool = selection.select(population);

		Check.that(
			matingPool.size() == variation.getMatingPoolSize(),
			"The mating pool size is "
			+ matingPool.size()
			+ " instead of "
			+ variation.getMatingPoolSize());

		return matingPool;
	}

	/**
	 * This methods iteratively applies a {@link CrossoverOperator} a {@link MutationOperator} to the
	 * population to create the offspring population. The population size must be divisible by the
	 * number of parents required by the {@link CrossoverOperator}; this way, the needed parents are
	 * taken sequentially from the population.
	 *
	 * <p>The number of solutions returned by the {@link CrossoverOperator} must be equal to the
	 * offspringPopulationSize state variable
	 *
	 * @param matingPool
	 * @return The new created offspring population
	 */
	@Override
	protected List<S> reproduction(List<S> matingPool) {
		return variation.variate(population, matingPool);
	}

	@Override
	protected List<S> replacement(
	List<S> population, List<S> offspringPopulation) {
		return replacement.replace(population, offspringPopulation) ;
	}

	protected List<S> removeNoWinnerMichiganSolution(List<S> population) {
		/* 未勝利個体削除*/
	    IntStream.range(0, population.size())
	        .forEach(i -> ((AbstractPittsburghProblem)problem).removeNoWinnerMichiganSolution(population.get(i)));
		return population;
	}

	@Override
	public List<S> getResult() {
		return population;
	}

	@Override
	public String getName() {
		return "MOEA/D";
	}

	@Override
	public String getDescription() {
		return "MOEA/D";
	}

	public Map<String, Object> getAlgorithmStatusData() {
		return algorithmStatusData;
	}

	@Override
	public Observable<Map<String, Object>> getObservable() {
		return observable;
	}

	public long getTotalComputingTime() {
		return totalComputingTime;
	}

	public long getEvaluations() {
		return evaluations;
	}

	public HybridMoFGBMLwithMOEAD setEvaluation(Evaluation<S> evaluation) {
		this.evaluation = evaluation;

		return this;
	}

	public HybridMoFGBMLwithMOEAD setInitialSolutionsCreation(
		InitialSolutionsCreation<S> initialSolutionsCreation) {
		this.initialSolutionsCreation = initialSolutionsCreation;

		return this;
	}
}
