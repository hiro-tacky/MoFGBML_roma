package cilabo.fuzzy.rule.consequent.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.AbstractConsequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import xml.XML_TagName;
import xml.XML_manager;

public class Consequent_Basic extends AbstractConsequent <ClassLabel_Basic, Integer, RuleWeight_Basic, Double>
	implements ClassLabel<Integer>, RuleWeight<Double>{

	/** 入力された結論部クラスとルール重みを持つインスタンスを生成する
	 * @param classLabel 代入される結論部クラス
	 * @param ruleWeight 代入されるルール重み
	 */
	public Consequent_Basic(ClassLabel_Basic classLabel, RuleWeight_Basic ruleWeight) {
		super(classLabel, ruleWeight);
	}

	public Consequent_Basic(int classLabel, double ruleWeight) {
		super(new ClassLabel_Basic(classLabel), new RuleWeight_Basic(ruleWeight));
	}

	@Override
	public Consequent_Basic copy() {
		return new Consequent_Basic(this.classLabel.copy(), this.ruleWeight.copy());
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
		Element consequent = XML_manager.getInstance().createElement(XML_TagName.consequent);

		//結論部クラス
		Element classLabel = this.classLabel.toElement();
		XML_manager.getInstance().addElement(consequent, classLabel);

		//ルール重み
		Element ruleWeight = this.ruleWeight.toElement();
		XML_manager.getInstance().addElement(consequent, ruleWeight);

		return consequent;
	}

}
