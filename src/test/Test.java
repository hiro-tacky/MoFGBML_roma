package test;

import java.io.File;
import java.util.ArrayList;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.fuzzy.classifier.classification.impl.SingleWinnerRuleSelection;
import cilabo.fuzzy.classifier.factory.ClassifierFactory;
import cilabo.fuzzy.classifier.factory.impl.FuzzyClassifierFactory;
import cilabo.fuzzy.classifier.factory.impl.LoadClassifierString;
import cilabo.fuzzy.classifier.impl.Classifier_basic;
import cilabo.fuzzy.classifier.operator.postProcessing.PostProcessing;
import cilabo.fuzzy.classifier.operator.postProcessing.factory.SimplePostProcessing;
import cilabo.fuzzy.classifier.operator.preProcessing.PreProcessing;
import cilabo.fuzzy.classifier.operator.preProcessing.factory.NopPreProcessing;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.factory.impl.HomoTriangleKnowledgeFactory;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_3;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.factory.impl.AllCombinationAntecedentFactory;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.impl.MoFGBML_Learning;
import cilabo.utility.GeneralFunctions;
import cilabo.utility.Input;

public class Test {
	public static void main(String[] args) {
//		checkLineSeparator();

//		checkClassifierToString();

		checkAddAll();
	}

	public static void checkAddAll() {
		ArrayList<Double> origin = new ArrayList<>();
		origin.add(1.0);
		origin.add(2.0);


		ArrayList<Double> newInstance = new ArrayList<>();
		newInstance.addAll(origin);

		origin.set(0, 2.0);
	}

	public static void checkClassifierToString() {
		Knowledge knowledge = makeTestKnowledge();
		Classifier_basic classifier = makeTestClassifier();

		String classifierString = classifier.toString();
		classifierString = GeneralFunctions.uniformLineSeparator(classifierString);
		String[] l = classifierString.split(System.lineSeparator());

//		ArrayList<String> lines = new ArrayList<>();
//		lines.addAll(Arrays.asList(l));
//
//		for(int i = 0; i < l.length; i++) {
//			if(!lines.get(i).equals(l[i])) {
//				System.out.println("order is destroyed");
//			}
//		}

//		System.out.println(classifierString);

		ClassifierFactory factory = LoadClassifierString.builder()
									.classifierString(classifierString)
									.knowledge(knowledge)
									.build();
		Classifier_basic newClassifier = (Classifier_basic)factory.create();


	}

	public static void checkLineSeparator() {
		String windows = "\r\n";
		String mac = "\n\r";
		String unix = "\n";

		String ln = windows;

		String a = "Hello" + unix + "world!";
		if(a.contains(windows)) {
			a = a.replace(windows, ln);
		}
		else if(a.contains(mac)) {
			a = a.replace(mac, ln);
		}
		else {
			a = a.replace(unix, ln);
		}

		System.out.print(a);

	}

	private static Knowledge makeTestKnowledge() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern1.txt";
		DataSet train = new DataSet();
		Input.inputDataSet(train, dataName);

		int dimension = train.getNdim();
		float[][] params = HomoTriangle_3.getParams();
		HomoTriangleKnowledgeFactory.builder()
								.dimension(dimension)
								.params(params)
								.build()
								.create();
		return Knowledge.getInstance();
	}

	private static Classifier_basic makeTestClassifier() {
		String sep = File.separator;
		String dataName = "dataset" + sep + "cilabo" + sep + "kadai5_pattern1.txt";
		DataSet train = new DataSet();
		Input.inputDataSet(train, dataName);

		int dimension = train.getNdim();
		float[][] params = HomoTriangle_3.getParams();
		HomoTriangleKnowledgeFactory.builder()
								.dimension(dimension)
								.params(params)
								.build()
								.create();

		PreProcessing preProcessing = new NopPreProcessing();

		AntecedentIndexFactory antecedentFactory = AllCombinationAntecedentFactory.builder()
												.build();
		int ruleNum = ((AllCombinationAntecedentFactory)antecedentFactory).getRuleNum();

		ConsequentFactory consequentFactory = MoFGBML_Learning.builder()
												.train(train)
												.build();

		PostProcessing postProcessing = new SimplePostProcessing();

		Classification classification = new SingleWinnerRuleSelection();

		ClassifierFactory factory = FuzzyClassifierFactory.builder()
										.preProcessing(preProcessing)
										.antecedentFactory(antecedentFactory)
										.consequentFactory(consequentFactory)
										.postProcessing(postProcessing)
										.classification(classification)
										.train(train)
										.ruleNum(ruleNum)
										.build();

		Classifier_basic classifier = (Classifier_basic)factory.create();
		return classifier;
	}
}












