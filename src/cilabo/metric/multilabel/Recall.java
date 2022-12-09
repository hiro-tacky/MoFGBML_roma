package cilabo.metric.multilabel;

import cilabo.data.DataSet;
import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.impl.Classifier_basic;
import cilabo.metric.Metric;

public class Recall implements Metric {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	/**
	 * @param classifier : FuzzyClassifier
	 * @param dataset : DataSet
	 * @return Double
	 */
	@Override
	public Double metric(Object... objects) {
		Classifier_basic classifier = null;
		DataSet dataset = null;
		for(Object object : objects) {
			if(object.getClass() == Classifier_basic.class) {
				classifier = (Classifier_basic)object;
			}
			else if(object.getClass() == DataSet.class) {
				dataset = (DataSet)object;
			}
			else {
				(new IllegalArgumentException()).printStackTrace();
				return null;
			}
		}

		if(classifier != null && dataset != null) {
			return metric(classifier, dataset);
		}
		else {
			return null;
		}
	}

	public Double metric(Classifier_basic classifier, DataSet dataset) {
		double size = dataset.getDataSize();

		double recall = 0;
		for(int p = 0; p < size; p++) {
			InputVector vector = dataset.getPattern(p).getInputVector();
			Integer[] trueClass = dataset.getPattern(p).getTrueClass().getClassVector();

			Integer[] classifiedClass = classifier.classify(vector)
					.getConsequent().getClassLabelValue()
					.getClassVector();

			recall += RecallMetric(classifiedClass, trueClass);
		}
		return 100.0 * recall/size;
	}


	/**
	 * <h1>Calculate Recall for Multi-Label Classification</h1>
	 * @param classified : Integer[]
	 * @param trueClass : Integer[]
	 * @return double : Recall
	 */
	public static double RecallMetric(Integer[] classified, Integer[] trueClass) {
		double correctAssociate = 0.0;	//Number of "1"s in both trueClass and classifiedClass;
		double answerAssociate = 0.0;	//Number of "1"s in trueClass;
		for(int i = 0; i < classified.length; i++) {
			if(classified[i] == 1 && trueClass[i] == 1) {
				correctAssociate++;
			}
			if(trueClass[i] == 1) {
				answerAssociate++;
			}
		}
		if(answerAssociate == 0) {
			return 0;
		}
		return correctAssociate / answerAssociate;
	}

}
