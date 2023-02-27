package cilabo.fuzzy.rule.consequent.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.AbstractConsequent;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Multi;
import cilabo.main.impl.multiTasking.MultiTasking;
import xml.XML_TagName;
import xml.XML_manager;

/** 複数クラスラベル と 複数ルール重み を持つ後件部に関するクラス
 * @author hirot
 */
@MultiTasking
public final class Consequent_MultiClass extends AbstractConsequent <ClassLabel_Multi, Integer[], RuleWeight_Multi, Double[]>{

	/** 入力された結論部クラスとルール重みを持つインスタンスを生成する
	 * @param classLabel 代入される結論部クラス
	 * @param ruleWeight 代入されるルール重み
	 */
	public Consequent_MultiClass(ClassLabel_Multi classLabel, RuleWeight_Multi ruleWeight) {
		super(classLabel, ruleWeight);
	}

	public Consequent_MultiClass(Integer[] classLabel, Double[] ruleWeight) {
		super(new ClassLabel_Multi(classLabel), new RuleWeight_Multi(ruleWeight));
	}

	@Override
	public Consequent_MultiClass copy() {
		return new Consequent_MultiClass(this.classLabel.copy(), this.ruleWeight.copy());
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
