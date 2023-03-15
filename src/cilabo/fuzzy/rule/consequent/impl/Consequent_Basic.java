package cilabo.fuzzy.rule.consequent.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.AbstractConsequent;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * 標準的後件部クラス basic consequent class
 * @author Takigawa Hiroki
 */
public final class Consequent_Basic extends AbstractConsequent <ClassLabel_Basic, RuleWeight_Basic>{

	/** コンストラクタ．constructor
	 * @param classLabel 結論部ラベルクラス conclusion label class
	 * @param ruleWeight ルール重みクラス rule weight class
	 */
	public Consequent_Basic(ClassLabel_Basic classLabel, RuleWeight_Basic ruleWeight) {
		super(classLabel, ruleWeight);
	}

	/** コンストラクタ．constructor
	 * @param classLabel 結論部ラベルクラス conclusion label class
	 * @param ruleWeight ルール重みクラス rule weight class
	 */
	public Consequent_Basic(int classLabel, double ruleWeight) {
		super(new ClassLabel_Basic(classLabel), new RuleWeight_Basic(ruleWeight));
	}

	@Override
	public Consequent_Basic copy() {
		return new Consequent_Basic(this.classLabel.copy(), this.ruleWeight.copy());
	}

	@Override
	public String toString() {
		String str = this.getClass().getSimpleName();
		str += String.format("class:%s, ", this.classLabel.toString()); //ClassLabel
		str += String.format("weight:%s", this.ruleWeight.toString()); //RuleWeight
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
