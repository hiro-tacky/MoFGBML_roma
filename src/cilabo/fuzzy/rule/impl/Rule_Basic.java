package cilabo.fuzzy.rule.impl;

import org.w3c.dom.Element;

import cilabo.data.InputVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.AbstractRule;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.factory.impl.HeuristicRuleGenerationMethod;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.impl.Consequent_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import xml.XML_TagName;
import xml.XML_manager;

public final class Rule_Basic extends AbstractRule <Antecedent_Basic, Consequent_Basic>{

	public Rule_Basic(Antecedent_Basic antecedent, Consequent_Basic consequent) {
		super(antecedent, consequent);
	}

	@Override
	public Rule_Basic copy() {
		return new Rule_Basic(this.getAntecedent().copy(), this.getConsequent().copy());
	}


	@Override
	public double getFitnessValue(int[] antecedentIndex, InputVector inputVector) {
		double membership = this.getAntecedent().getCompatibleGradeValue(antecedentIndex, inputVector.getVector());
		double CF = (double) this.getRuleWeight().getRuleWeightValue();
		return membership*CF;
	}

	public static class RuleBuilder_Basic
	extends RuleBuilderCore<Antecedent_Basic, Consequent_Basic>
	implements RuleBuilder<Rule_Basic>{

		public RuleBuilder_Basic(AntecedentIndexFactory antecedentFactory,
				ConsequentFactory<Consequent_Basic> consequentFactory) {
			super(antecedentFactory, consequentFactory);
		}

		@Override
		public Rule_Basic createRule(int[] antecedentIndex) {
			Antecedent_Basic antecedent = new Antecedent_Basic();
			Consequent_Basic consequent = this.learning(antecedent, antecedentIndex);
			return new Rule_Basic(antecedent, consequent);
		}

		@Override
		public Rule_Basic[] createRule(int[][] antecedentIndex) {
			Rule_Basic[] buf = new Rule_Basic[antecedentIndex.length];
			for(int i=0; i<antecedentIndex.length; i++) {
				buf[i] = this.createRule(antecedentIndex[i]);
			}
			return buf;
		}

		@Override
		public int[] createAntecedentIndex(Pattern pattern) {
			int[] antecedentIndex = null;
			if(this.antecedentFactory instanceof HeuristicRuleGenerationMethod) {
				antecedentIndex = ((HeuristicRuleGenerationMethod)this.antecedentFactory).calculateAntecedentPart(pattern);
			}else {
				System.err.println("antecedentFactory is not HeuristicRuleGenerationMethod");
			}
			return antecedentIndex;
		}

		@Override
		public Rule_Basic createRule(Element michiganSolution) {
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
	public Antecedent_Basic getAntecedent() {
		return this.antecedent;
	}

	@Override
	public Consequent_Basic getConsequent() {
		return this.consequent;
	}

	@Override
	public ClassLabel_Basic getClassLabel() {
		return this.consequent.getClassLabel();
	}

	@Override
	public RuleWeight_Basic getRuleWeight() {
		return this.consequent.getRuleWeight();
	}

	@Override
	public String toString() {
		return "Rule_Basic [antecedent=" + antecedent + ", consequent=" + consequent + "]";
	}

	@Override
	public Element toElement() {
		//ルール
		Element rule = XML_manager.createElement(XML_TagName.rule);

		//前件部
		Element antecedent = this.antecedent.toElement();
		XML_manager.addElement(rule, antecedent);

		//後件部
		Element consequent = this.consequent.toElement();
		XML_manager.addElement(rule, consequent);

		return rule;
	}

}
