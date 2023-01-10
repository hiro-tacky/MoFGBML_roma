package cilabo.gbml.component.initialsolutionscreation;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.uma.jmetal.component.initialsolutioncreation.InitialSolutionsCreation;

import cilabo.data.DataSet;
import cilabo.data.TrainTestDatasetManager;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.fuzzy.classifier.classification.impl.SingleWinnerRuleSelection;
import cilabo.fuzzy.classifier.impl.Classifier_basic;
import cilabo.fuzzy.knowledge.factory.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
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
import xml.XML_reader;

class InitialSolutionsCreationFromXMLTest {

	private static XML_reader XML_reader;
	private static InitialSolutionsCreation<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>>
		initialSolutionsCreation;
	private static List<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>> population;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		TrainTestDatasetManager.getInstance().loadTrainTestFiles("dataset/pima/a0_0_pima-10tra.dat", "dataset/pima/a0_0_pima-10tst.dat");
		DataSet train = TrainTestDatasetManager.getInstance().getTrains().get(0);

		Parameters parameters = new Parameters(train, train.getNdim());
		HomoTriangleKnowledgeFactory KnowledgeFactory = new HomoTriangleKnowledgeFactory(train.getNdim(), parameters);
		KnowledgeFactory.create();

		RuleBuilder<Rule_Basic> ruleBuilder = new Rule_Basic.RuleBuilder_Basic(
				new HeuristicRuleGenerationMethod(train),
				new MoFGBML_Learning(train));

		List<Pair<Integer, Integer>> bounds_Michigan = AbstractMichiganSolution.makeBounds();
		MichiganSolutionBuilder<MichiganSolution_Basic<Rule_Basic>> michiganSolutionBuilder
			= new MichiganSolution_Basic.MichiganSolutionBuilder_Basic<Rule_Basic>(
					bounds_Michigan, 2, 0, ruleBuilder);

		Classification<MichiganSolution_Basic<Rule_Basic>> classification = new SingleWinnerRuleSelection<MichiganSolution_Basic<Rule_Basic>>();

		Classifier<MichiganSolution_Basic<Rule_Basic>> classifier = new Classifier_basic<>(classification);

		/* MOP: Multi-objective Optimization Problem */
		PittsburghFGBML_Basic<MichiganSolution_Basic<Rule_Basic>> problem =
				new PittsburghFGBML_Basic<MichiganSolution_Basic<Rule_Basic>>(
						60, 2, 0, null, michiganSolutionBuilder, classifier);

		try {
			String ln = File.separator;
			XML_reader = new XML_reader("results" + ln + "mixedKB" + ln + "pima" + ln + "trial00" + ln + Consts.XML_FILE_NAME + ".xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialSolutionsCreation = new InitialSolutionsCreationFromXML<PittsburghSolution_Basic<MichiganSolution_Basic<Rule_Basic>>>(
				problem, XML_reader.getPopulation(Consts.TERMINATE_EVALUATION));
		population = initialSolutionsCreation.create();
	}

	@Test
	void test() {
//		System.out.println(population.toString());
	}
}
