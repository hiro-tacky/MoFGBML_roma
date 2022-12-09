package cilabo.metric.multilabel;

import cilabo.data.DataSet;
import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.impl.Classifier_basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.metric.Metric;

public class SubsetAccuracy implements Metric {
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
		double correct = 0;
		for(int p = 0; p < size; p++) {
			InputVector vector = dataset.getPattern(p).getInputVector();
			ClassLabel_Basic trueClass = dataset.getPattern(p).getTrueClass();

			ClassLabel_Basic classifiedClass = classifier.classify(vector).getConsequent().getClassLabelValue();

			if( trueClass.toString().equals( classifiedClass.toString() )) {
				correct += 1;
			}
		}

		return 100.0 * correct/size;
	}


}
