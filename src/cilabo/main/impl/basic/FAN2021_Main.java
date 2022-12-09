package cilabo.main.impl.basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.component.termination.Termination;
import org.uma.jmetal.component.termination.impl.TerminationByEvaluations;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.observer.impl.EvaluationObserver;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import cilabo.data.DataSet;
import cilabo.data.TrainTestDatasetManager;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.fuzzy.classifier.classification.impl.SingleWinnerRuleSelection;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.factory.impl.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_2_3_4_5;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.fuzzy.rule.antecedent.factory.impl.HeuristicRuleGenerationMethod;
import cilabo.fuzzy.rule.consequent.factory.impl.MoFGBML_Learning;
import cilabo.fuzzy.rule.impl.Rule_Basic;
import cilabo.gbml.algorithm.HybridMoFGBMLwithNSGAII;
import cilabo.gbml.objectivefunction.pittsburgh.ErrorRate;
import cilabo.gbml.operator.crossover.HybridGBMLcrossover;
import cilabo.gbml.operator.crossover.MichiganCrossover;
import cilabo.gbml.operator.crossover.PittsburghCrossover;
import cilabo.gbml.operator.mutation.PittsburghMutation;
import cilabo.gbml.problem.pittsburghFGBML_Problem.impl.PittsburghFGBML_Basic;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Basic;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.solution.pittsburghSolution.impl.PittsburghSolution_Basic;
import cilabo.main.Consts;
import cilabo.main.impl.multiTasking.CommandLineArgs;
import cilabo.utility.Output;
import cilabo.utility.Parallel;
import cilabo.utility.Random;
import xml.XML_manager;

/**
 * @version 1.0
 *
 * FAN2021時点
 */
public class FAN2021_Main {
	public static void main(String[] args) throws JMetalException, FileNotFoundException {
		String sep = File.separator;

		/* ********************************************************* */
		System.out.println();
		System.out.println("==== INFORMATION ====");
		System.out.println("main: " + FAN2021_Main.class.getCanonicalName());
		String version = "1.0";
		System.out.println("version: " + version);
		System.out.println();
		System.out.println("Algorithm: Hybrid-style Multiobjective Fuzzy Genetics-Based Machine Learning");
		System.out.println("EMOA: NSGA-II");
		System.out.println();
		/* ********************************************************* */
		// Load consts.properties
		Consts.set("consts");
		// make result directory
		Output.mkdirs(Consts.ROOTFOLDER);

		// set command arguments to static variables
		CommandLineArgs.loadArgs(CommandLineArgs.class.getCanonicalName(), args);
		// Output constant parameters
		String fileName = Consts.EXPERIMENT_ID_DIR + sep + "Consts.txt";
		Output.writeln(fileName, Consts.getString(), true);
		Output.writeln(fileName, CommandLineArgs.getParamsString(), true);

		// Initialize ForkJoinPool
		Parallel.getInstance().initLearningForkJoinPool(CommandLineArgs.parallelCores);

		System.out.println("Processors: " + Runtime.getRuntime().availableProcessors() + " ");
		System.out.print("args: ");
		for(int i = 0; i < args.length; i++) {
			System.out.print(args[i] + " ");
		}


		System.out.println();
		System.out.println("=====================");
		System.out.println();

		/* ********************************************************* */
		System.out.println("==== EXPERIMENT =====");
		Date start = new Date();
		System.out.println("START: " + start);

		/* Random Number ======================= */
		Random.getInstance().initRandom(Consts.RAND_SEED);
		JMetalRandom.getInstance().setSeed(Consts.RAND_SEED);

		/* Load Dataset ======================== */
		TrainTestDatasetManager.getInstance().loadTrainTestFiles(CommandLineArgs.trainFile, CommandLineArgs.testFile);

		/* Run MoFGBML algorithm =============== */
		DataSet train = TrainTestDatasetManager.getInstance().getTrains().get(0);
		DataSet test = TrainTestDatasetManager.getInstance().getTests().get(0);


		/** XML ファイル出力ようインスタンスの生成*/
		XML_manager.getInstance();
		XML_manager.getInstance().setDtst(test);

		HybridStyleMoFGBML(train, test);
		/* ===================================== */

		try {
			XML_manager.output(Consts.EXPERIMENT_ID_DIR);
		} catch (TransformerException | IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		Date end = new Date();
		System.out.println("END: " + end);
		System.out.println("=====================");
		/* ********************************************************* */

		System.exit(0);
	}

	/**
	 *
	 */
	public static void HybridStyleMoFGBML(DataSet train, DataSet test) {
		Random.getInstance().initRandom(2022);
		String sep = File.separator;

		int dimension = train.getNdim();
		float[][] params = HomoTriangle_2_3_4_5.getParams();
		HomoTriangleKnowledgeFactory.builder()
			.dimension(dimension)
			.params(params)
			.build()
			.create();

		List<Pair<Integer, Integer>> bounds_Michigan = AbstractMichiganSolution.makeBounds();
		int numberOfObjectives_Michigan = 2;
		int numberOfConstraints_Michigan = 0;

		int numberOfvariables_Pittsburgh = Consts.INITIATION_RULE_NUM;
		int numberOfObjectives_Pittsburgh = 2;
		int numberOfConstraints_Pittsburgh = 0;

		RuleBuilder<Rule_Basic> ruleBuilder = new Rule_Basic.RuleBuilder_Basic(
				new HeuristicRuleGenerationMethod(train),
				new MoFGBML_Learning(train));

		MichiganSolutionBuilder<MichiganSolution_Basic<Rule_Basic>> michiganSolutionBuilder
			= new MichiganSolution_Basic.MichiganSolutionBuilder_Basic<Rule_Basic>(
					bounds_Michigan,
					numberOfObjectives_Michigan,
					numberOfConstraints_Michigan,
					ruleBuilder);

		Classification<MichiganSolution_Basic<Rule_Basic>> classification = new SingleWinnerRuleSelection<MichiganSolution_Basic<Rule_Basic>>();
		/* MOP: Multi-objective Optimization Problem */
		Problem<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>> problem =
				new PittsburghFGBML_Basic<MichiganSolution_Basic<Rule_Basic>>(
						numberOfvariables_Pittsburgh,
						numberOfObjectives_Pittsburgh,
						numberOfConstraints_Pittsburgh,
						train,
						michiganSolutionBuilder,
						classification);


		/* Crossover: Hybrid-style GBML specific crossover operator. */
		double crossoverProbability = 1.0;
		/* Michigan operation */
		CrossoverOperator<PittsburghSolution> michiganX
				= new MichiganCrossover(Consts.MICHIGAN_CROSS_RT, train);
		/* Pittsburgh operation */
		CrossoverOperator<PittsburghSolution> pittsburghX
				= new PittsburghCrossover(Consts.PITTSBURGH_CROSS_RT);
		/* Hybrid-style crossover */
		CrossoverOperator<PittsburghSolution> crossover
				= new HybridGBMLcrossover(crossoverProbability, Consts.MICHIGAN_OPE_RT,
																				michiganX, pittsburghX);
		/* Mutation: Pittsburgh-style GBML specific mutation operator. */
		MutationOperator<PittsburghSolution> mutation
				= new PittsburghMutation(train);

		/* Termination: Number of total evaluations */
		Termination termination = new TerminationByEvaluations(Consts.terminateEvaluation);

		//knowlwdge出力用
		XML_manager.addElement(XML_manager.getRoot(), Knowledge.getInstance(). knowledgeToElement());

		/* Algorithm: Hybrid-style MoFGBML with NSGA-II */
		HybridMoFGBMLwithNSGAII<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>> algorithm
			= new HybridMoFGBMLwithNSGAII<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>>(problem,
											Consts.populationSize,
											Consts.offspringPopulationSize,
											Consts.outputFrequency,
											Consts.EXPERIMENT_ID_DIR,
											crossover,
											mutation,
											termination);

		/* Running observation */
		EvaluationObserver evaluationObserver = new EvaluationObserver(Consts.outputFrequency);
		algorithm.getObservable().register(evaluationObserver);

		/* === GA RUN === */
		algorithm.run();
		/* ============== */

		/* Non-dominated solutions in final generation */
		List<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>> nonDominatedSolutions = algorithm.getResult();
	    new SolutionListOutput(nonDominatedSolutions)
        	.setVarFileOutputContext(new DefaultFileOutputContext(Consts.EXPERIMENT_ID_DIR+sep+"VAR.csv", ","))
        	.setFunFileOutputContext(new DefaultFileOutputContext(Consts.EXPERIMENT_ID_DIR+sep+"FUN.csv", ","))
        	.print();

	    // Test data
	    ArrayList<String> strs = new ArrayList<>();
	    String str = "pop,test";
	    strs.add(str);

	    for(int i = 0; i < nonDominatedSolutions.size(); i++) {
	    	PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>> solution = nonDominatedSolutions.get(i);
			ErrorRate<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>> function1
				= new ErrorRate<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>>();
			double errorRate = function1.function(solution, test);

	    	str = String.valueOf(i);
	    	str += "," + errorRate;
	    	strs.add(str);
	    }
	    String fileName = Consts.EXPERIMENT_ID_DIR + sep + "results.csv";
	    Output.writeln(fileName, strs, false);

		return;
	}
}
