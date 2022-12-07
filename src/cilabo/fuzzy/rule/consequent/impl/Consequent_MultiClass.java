package cilabo.fuzzy.rule.consequent.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.AbstractConsequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Multi;
import cilabo.main.impl.multiTasking.MultiTasking;
import xml.XML_TagName;
import xml.XML_manager;

/** 複数クラスラベル と 複数ルール重み を持つ後件部に関するクラス
 * @author hirot
 */
@MultiTasking
public class Consequent_MultiClass extends AbstractConsequent <ClassLabel_Multi, Integer[], RuleWeight_Multi, Double[]>{

	/** 入力された結論部クラスとルール重みを持つインスタンスを生成する
	 * @param classLabel 代入される結論部クラス
	 * @param ruleWeight 代入されるルール重み
	 */
	public Consequent_MultiClass(ClassLabel_Multi classLabel, RuleWeight_Multi ruleWeight) {
		super(classLabel, ruleWeight);
	}

	@Override
	public Consequent_MultiClass copy() {
		return new Consequent_MultiClass(this.classLabel.copy(), this.ruleWeight.copy());
	}

	@Override
	public ClassLabel_Multi getClassLabel() {
		return this.classLabel;
	}

	@Override
	public RuleWeight_Multi getRuleWeight() {
		return this.ruleWeight;
	}

	@Override
	public boolean equals(ClassLabel<Integer[]> classLabel) {
		return this.classLabel.equals(classLabel);
	}

	@Override
	public Integer[] getClassLabelValue() {
		return this.classLabel.getClassLabelValue();
	}

	@Override
	public void setClassLabelValue(Integer[] classLabelValue) {
		this.classLabel.setClassLabelValue(classLabelValue);
	}

	@Override
	public boolean equalsValueOf(Integer[] classLabelValue) {
		return this.classLabel.equalsValueOf(classLabelValue);
	}

	@Override
	public Double[] getRuleWeightValue() {
		return this.ruleWeight.getRuleWeightValue();
	}

	@Override
	public void setRuleWeightValue(Double[] ruleWeightValue) {
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
		XML_manager.addElement(consequent, this.classLabel.toElement());

		//ルール重み
		XML_manager.addElement(consequent, this.ruleWeight.toElement());

		return consequent;
	}
}
