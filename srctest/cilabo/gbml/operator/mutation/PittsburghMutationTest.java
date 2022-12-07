package cilabo.gbml.operator.mutation;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.data.DataSet;
import cilabo.data.TrainTestDatasetManager;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.fuzzy.classifier.classification.impl.SingleWinnerRuleSelection;
import cilabo.fuzzy.classifier.impl.RuleBasedClassifier;
import cilabo.fuzzy.knowledge.factory.impl.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_2_3_4_5;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.fuzzy.rule.antecedent.factory.impl.HeuristicRuleGenerationMethod;
import cilabo.fuzzy.rule.consequent.factory.impl.MoFGBML_Learning;
import cilabo.fuzzy.rule.impl.Rule_Basic;
import cilabo.gbml.problem.pittsburghFGBML_Problem.impl.PittsburghFGBML_Basic;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Basic;
import cilabo.gbml.solution.pittsburghSolution.impl.PittsburghSolution_Basic;
import cilabo.main.Consts;

class PittsburghMutationTest {

	static PittsburghFGBML_Basic<MichiganSolution_Basic<Rule_Basic>, Rule_Basic> problem;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// Load consts.properties
		Consts.set("consts");

		String sep = File.separator;
		// Load "Iris" dataset
		String trainFileName = "dataset" + sep + "iris" + sep + "a0_0_iris-10tra.dat";
		String testFileName = "dataset" + sep + "iris" + sep + "a0_0_iris-10tst.dat";
		TrainTestDatasetManager.getInstance().loadTrainTestFiles(trainFileName, testFileName);

		DataSet train = TrainTestDatasetManager.getInstance().getTrains().get(0);
		DataSet test = TrainTestDatasetManager.getInstance().getTests().get(0);

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

		Integer[] samplingIndex = new Integer[train.getDataSize()];
		for(int i=0; i<train.getDataSize(); i++) { samplingIndex[i]=i; }
		RuleBuilder<Rule_Basic> ruleBuilder = new Rule_Basic.RuleBuilder_Basic(
				new HeuristicRuleGenerationMethod(train, samplingIndex),
				new MoFGBML_Learning(train));

		MichiganSolutionBuilder<MichiganSolution_Basic<Rule_Basic>, Rule_Basic> michiganSolutionBuilder
		= new MichiganSolution_Basic.MichiganSolutionBuilder_Basic<Rule_Basic>(
				bounds_Michigan,
				numberOfObjectives_Michigan,
				numberOfConstraints_Michigan,
				ruleBuilder);

		Classification<Rule_Basic> classification = new SingleWinnerRuleSelection<Rule_Basic>();
		Classifier<Rule_Basic> classifier = new RuleBasedClassifier<Rule_Basic>(classification);
		problem = new PittsburghFGBML_Basic<MichiganSolution_Basic<Rule_Basic>, Rule_Basic>(
				numberOfvariables_Pittsburgh,
				numberOfObjectives_Pittsburgh,
				numberOfConstraints_Pittsburgh,
				train,
				michiganSolutionBuilder,
				classifier);
	}

	@Test
	void testDoMutation() {
		PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>, Rule_Basic> solution = problem.createSolution();
	}

}
