package cilabo.metric.multilabel;

import cilabo.data.DataSet;
import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.impl.Classifier_basic;
import cilabo.metric.Metric;

public class Fmeasure implements Metric {
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

		double recall = 0.0;
		double precision = 0.0;
		for(int p = 0; p < size; p++) {
			InputVector vector = dataset.getPattern(p).getInputVector();
			Integer[] trueClass = dataset.getPattern(p).getTrueClass().getClassVector();

			Integer[] classifiedClass = classifier.classify(vector)
					.getConsequent().getClassLabelValue()
					.getClassVector();

			precision += Precision.PrecisionMetric(classifiedClass, trueClass);
			recall += Recall.RecallMetric(classifiedClass, trueClass);
		}
		recall = recall/size;
		precision = precision/size;

		double Fmeasure;
		if((precision + recall) == 0) Fmeasure = 0;
		else {
			Fmeasure = (2.0 * recall * precision) / (recall + precision);
		}
		return 100.0 * Fmeasure;
	}

}
