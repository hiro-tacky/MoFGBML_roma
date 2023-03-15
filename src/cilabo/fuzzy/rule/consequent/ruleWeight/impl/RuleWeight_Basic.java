package cilabo.fuzzy.rule.consequent.ruleWeight.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.ruleWeight.AbstractRuleWeight;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * 標準的ルール重みクラス basic rule weight class
 * @author Takigawa Hiroki
 */
public final class RuleWeight_Basic extends AbstractRuleWeight <Double>{

	/**
	 * コンストラクタ．constructor
	 * @param ruleWeight ルール重み格納変数 vaiable of rule weight
	 */
	public RuleWeight_Basic(double ruleWeight) {
		this.ruleWeight = ruleWeight;
	}

	@Override
	public Double getRuleWeightValue() {
		return this.ruleWeight;
	}

	@Override
	public RuleWeight_Basic copy() {
		return new RuleWeight_Basic(this.ruleWeight);
	}

	@Override
	public String toString() {
		return String.format("%.4f..", this.ruleWeight);
	}

	@Override
	public Element toElement() {
		Element ruleWeightEL = XML_manager.getInstance().createElement(XML_TagName.ruleWeight, this.ruleWeight);
		return ruleWeightEL;
	}
}
