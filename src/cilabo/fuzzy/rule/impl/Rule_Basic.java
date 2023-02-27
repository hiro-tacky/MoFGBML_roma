package cilabo.fuzzy.rule.impl;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.fuzzy.rule.AbstractRule;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.impl.Consequent_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import xml.XML_TagName;
import xml.XML_manager;

public final class Rule_Basic extends AbstractRule <Antecedent_Basic, Consequent_Basic,
	ClassLabel_Basic, Integer,
	RuleWeight_Basic, Double>{

	public Rule_Basic(Antecedent_Basic antecedent, Consequent_Basic consequent) {
		super(antecedent, consequent);
	}

	@Override
	public Rule_Basic copy() {
		return new Rule_Basic(this.getAntecedent().copy(), this.getConsequent().copy());
	}

	@Override
	public double getFitnessValue(int[] antecedentIndex, AttributeVector attributeVector) {
		double membership = this.getAntecedent().getCompatibleGradeValue(antecedentIndex, attributeVector);
		double CF = (double) this.getRuleWeight().getRuleWeightValue();
		return membership*CF;
	}

	@Override
	public void setClassLabelValue(Integer classLabelValue) {
		this.consequent.setClassLabelValue(classLabelValue);
	}

	public final static class RuleBuilder_Basic
		extends RuleBuilderCore<Rule_Basic, Antecedent_Basic, Consequent_Basic>{

		public RuleBuilder_Basic(AntecedentIndexFactory antecedentFactory,
				ConsequentFactory<Consequent_Basic> consequentFactory) {
			super(antecedentFactory, consequentFactory);
		}

		@Override
		public Rule_Basic createConsequent(int[] antecedentIndex) {
			Antecedent_Basic antecedent = new Antecedent_Basic();
			Consequent_Basic consequent = this.learning(antecedent, antecedentIndex);
			return new Rule_Basic(antecedent, consequent);
		}

		@Override
		public Rule_Basic createConsequent(Element michiganSolution) {
			Antecedent_Basic antecedent = new Antecedent_Basic();
			Element rule_node = (Element) michiganSolution.getElementsByTagName(XML_TagName.rule.toString()).item(0);
			Element consequent_node = (Element) rule_node.getElementsByTagName(XML_TagName.consequent.toString()).item(0);
			int classLabel = Integer.valueOf(consequent_node.getElementsByTagName(XML_TagName.classLabel.toString()).item(0).getTextContent());
			double ruleWeight = Double.valueOf(consequent_node.getElementsByTagName(XML_TagName.ruleWeight.toString()).item(0).getTextContent());

			Consequent_Basic consequent = new Consequent_Basic(classLabel, ruleWeight);
			return new Rule_Basic(antecedent, consequent);
		}

		@Override
		public RuleBuilder_Basic copy() {
			return new RuleBuilder_Basic(this.antecedentFactory.copy(), this.consequentFactory.copy());
		}
	}

	@Override
	public String toString() {
		return "Rule_Basic [antecedent=" + antecedent + ", consequent=" + consequent + "]";
	}

	@Override
	public Element toElement() {
		//ルール
		Element rule = XML_manager.getInstance().createElement(XML_TagName.rule);

		//前件部
		Element antecedent = this.antecedent.toElement();
		XML_manager.getInstance().addElement(rule, antecedent);

		//後件部
		Element consequent = this.consequent.toElement();
		XML_manager.getInstance().addElement(rule, consequent);

		return rule;
	}
}
