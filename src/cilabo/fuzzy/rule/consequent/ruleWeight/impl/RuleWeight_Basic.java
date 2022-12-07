package cilabo.fuzzy.rule.consequent.ruleWeight.impl;

import java.util.Objects;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.ruleWeight.AbstractRuleWeight;
import xml.XML_TagName;
import xml.XML_manager;

public final class RuleWeight_Basic extends AbstractRuleWeight <Double>{

	/**
	 * 入力されたルール重みを持つインスタンスを生成する
	 * @param ruleWeight ルール重み
	 */
	public RuleWeight_Basic(double ruleWeight) {
		this.ruleWeight = ruleWeight;
	}

	@Override
	public RuleWeight_Basic copy() {
		return new RuleWeight_Basic(this.ruleWeight);
	}

	@Override
	public String toString() {
		if( Objects.isNull(this.ruleWeight) ) {
			throw new NullPointerException();
		}

		return String.format("%.4f..", this.ruleWeight);
	}

	@Override
	public Element toElement() {
		Element ruleWeight = XML_manager.createElement(XML_TagName.ruleWeight, String.valueOf(this.ruleWeight));
		return ruleWeight;
	}
}
