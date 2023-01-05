package cilabo.fuzzy.rule.consequent.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.AbstractConsequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import xml.XML_TagName;
import xml.XML_manager;

public class Consequent_Basic extends AbstractConsequent <ClassLabel_Basic, Integer, RuleWeight_Basic, Double>{

	/** 入力された結論部クラスとルール重みを持つインスタンスを生成する
	 * @param classLabel 代入される結論部クラス
	 * @param ruleWeight 代入されるルール重み
	 */
	public Consequent_Basic(ClassLabel_Basic classLabel, RuleWeight_Basic ruleWeight) {
		super(classLabel, ruleWeight);
	}

	@Override
	public Consequent_Basic copy() {
		return new Consequent_Basic(this.classLabel.copy(), this.ruleWeight.copy());
	}

	@Override
	public ClassLabel_Basic getClassLabel() {
		return this.classLabel;
	}

	@Override
	public RuleWeight_Basic getRuleWeight() {
		return this.ruleWeight;
	}

	@Override
	public boolean equals(ClassLabel<Integer> classLabel) {
		return this.classLabel.equals(classLabel);
	}

	@Override
	public Integer getClassLabelValue() {
		return this.classLabel.getClassLabelValue();
	}

	@Override
	public void setClassLabelValue(Integer classLabelValue) {
		this.classLabel.setClassLabelValue(classLabelValue);
	}

	@Override
	public Integer getClassLabelInteger() {
		return this.classLabel.getClassLabelInteger();
	}

	@Override
	public boolean equalsValueOf(Integer classLabelValue) {
		return this.classLabel.equalsValueOf(classLabelValue);
	}

	@Override
	public Double getRuleWeightValue() {
		return this.ruleWeight.getRuleWeightValue();
	}

	@Override
	public void setRuleWeightValue(Double ruleWeightValue) {
		this.ruleWeight.setRuleWeightValue(ruleWeightValue);
	}

	@Override
	public String toString() {
		String str = null;

		str += String.format("class:[%s]", this.classLabel.toString()); //ClassLabel
		str += " ";
		str += String.format("weight:[%s]", this.ruleWeight.toString()); //RuleWeight

		return str;
	}

	@Override
	public Element toElement() {
		//後件部
		Element consequent = XML_manager.createElement(XML_TagName.consequent);

		//結論部クラス
		Element classLabel = this.classLabel.toElement();
		XML_manager.addElement(consequent, classLabel);

		//ルール重み
		Element ruleWeight = this.ruleWeight.toElement();
		XML_manager.addElement(consequent, ruleWeight);

		return consequent;
	}

}
