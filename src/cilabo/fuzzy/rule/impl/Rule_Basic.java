package cilabo.fuzzy.rule.impl;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.fuzzy.rule.AbstractRule;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.consequent.impl.Consequent_Basic;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * 標準的ルールクラス basic rule class
 * @author Takigawa Hiroki
 */
public final class Rule_Basic extends AbstractRule <Antecedent_Basic, Consequent_Basic>{

	public Rule_Basic(Antecedent_Basic antecedent, Consequent_Basic consequent) {
		super(antecedent, consequent);
	}

	@Override
	public double getFitnessValue(int[] antecedentIndex, AttributeVector attributeVector) {
		double membership = this.getAntecedent().getCompatibleGradeValue(antecedentIndex, attributeVector);
		double CF = (double) this.getConsequent().getRuleWeight().getRuleWeightValue();
		return membership*CF;
	}

	@Override
	public Rule_Basic copy() {
		return new Rule_Basic(this.getAntecedent().copy(), this.getConsequent().copy());
	}

	@Override
	public String toString() {
		return "Rule_Basic {antecedent:" + antecedent.toString() + ", consequent:" + consequent.toString() + "}";
	}

	@Override
	public Element toElement() {
		//ルール
		Element ruleEL = XML_manager.getInstance().createElement(XML_TagName.rule);

		//前件部
		XML_manager.getInstance().addElement(ruleEL, this.antecedent.toElement());

		//後件部
		XML_manager.getInstance().addElement(ruleEL, this.consequent.toElement());

		return ruleEL;
	}
}
