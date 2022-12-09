package cilabo.metric;

import cilabo.fuzzy.classifier.impl.Classifier_basic;

public class RuleLength implements Metric {
	// ************************************************************
	// Fields

	// ************************************************************
	// Constructor

	// ************************************************************
	// Methods

	/**
	 * @param classifier : FuzzyClassifier
	 * @return Integer
	 */
	@Override
	public Integer metric(Object...objects) {
		if(objects[0].getClass() == Classifier_basic.class) {
			Classifier_basic classifier = (Classifier_basic)objects[0];
			return metric(classifier);
		}
		else {
			(new IllegalArgumentException()).printStackTrace();
			return null;
		}
	}

	public Integer metric(Classifier_basic classifier) {
		return classifier.getRuleLength();
	}
}
