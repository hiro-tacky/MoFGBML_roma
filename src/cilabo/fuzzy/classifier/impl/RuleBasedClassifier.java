package cilabo.fuzzy.classifier.impl;

import java.util.ArrayList;

import org.w3c.dom.Element;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.fuzzy.rule.Rule;
import xml.XML_TagName;
import xml.XML_manager;

/**ルールベース識別器
 * @author hirot
 */
public class RuleBasedClassifier<RuleObject extends Rule> extends Classifier<RuleObject> {

	/** 空のインスタンスを生成します <br> Constructs an empty instance of class */
	public RuleBasedClassifier(Classification<RuleObject> classification) {
		super(classification);
	}

	/**  Copy constructor */
	public RuleBasedClassifier(RuleBasedClassifier<RuleObject> classifier) {
		super(classifier.classification);
		this.ruleSet = new ArrayList<Rule>(classifier.getRuleNum());
		for(int i = 0; i < ruleSet.size(); i++) {
			ruleSet.set(i, classifier.getRule(i).copy());
		}
	}

	/** 識別を行う
	 * @see cilabo.fuzzy.classifier.Classifier#classify(cilabo.data.InputVector
	 */
	@Override
	public Rule classify(InputVector vector) {
		return this.classification.classify(this, vector);
	}

	@Override
	public RuleBasedClassifier<RuleObject> copy() {
		return new RuleBasedClassifier<RuleObject>(this);
	}

	@Override
	public RuleBasedClassifier<RuleObject> build() {
		RuleBasedClassifier<RuleObject> ruleBasedClassifier = new RuleBasedClassifier<RuleObject>(this);
		ruleBasedClassifier.clear();
		return ruleBasedClassifier;
	}

	@Override
	public String toString() {
		return "RuleBasedClassifier [ruleSet=" + ruleSet + ", classification=" + classification.getClass().getSimpleName() + "]";
	}

	public Element toElemnt() {
		Element classifier = XML_manager.createElement(XML_TagName.classifier);
		for(Rule rule: this.ruleSet) {
			XML_manager.addElement(classifier, rule.toElement());
		}
		return classifier;
	}
}
