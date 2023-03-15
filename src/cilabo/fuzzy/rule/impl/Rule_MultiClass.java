package cilabo.fuzzy.rule.impl;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.fuzzy.rule.AbstractRule;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.consequent.impl.Consequent_MultiClass;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * マルチラベル用ルールクラス．rule class for multi-label
 * @author Takigawa Hiroki
 *
 */
public final class Rule_MultiClass extends AbstractRule <Antecedent_Basic, Consequent_MultiClass> {

	public Rule_MultiClass(Antecedent_Basic antecedent, Consequent_MultiClass consequent) {
		super(antecedent, consequent);
	}

	@Override
	public double getFitnessValue(int[] antecedentIndex, AttributeVector inputVector) {
		double membership = this.getAntecedent().getCompatibleGradeValue(antecedentIndex, inputVector);

		double CFmean = 0;
		Double[] ruleWeightList = this.getConsequent().getRuleWeight().getRuleWeightVariable();
		for(int i=0; i<ruleWeightList.length; i++) {
			CFmean += ruleWeightList[i];
		}
		CFmean /= (double)ruleWeightList.length;
		return membership*CFmean;
	}

	@Override
	public Rule_MultiClass copy() {
		return new Rule_MultiClass(this.getAntecedent(), this.getConsequent());
	}

	@Override
	public String toString() {
		return "Rule_MultiClass {antecedent:" + antecedent.toString() + ", consequent:" + consequent.toString() + "}";
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
