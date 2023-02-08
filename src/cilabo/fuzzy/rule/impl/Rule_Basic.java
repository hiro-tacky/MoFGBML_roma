package cilabo.fuzzy.rule.impl;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.data.AttributeVector;
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

public final class Rule_Basic extends AbstractRule <Antecedent_Basic, Consequent_Basic, ClassLabel_Basic, RuleWeight_Basic>{

	public Rule_Basic(Antecedent_Basic antecedent, Consequent_Basic consequent) {
		super(antecedent, consequent);
	}

	@Override
	public Rule_Basic copy() {
		return new Rule_Basic(this.getAntecedent().copy(), this.getConsequent().copy());
	}


	@Override
	public double getFitnessValue(int[] antecedentIndex, AttributeVector inputVector) {
		double membership = this.getAntecedent().getCompatibleGradeValue(antecedentIndex, inputVector.getAttributeValue());
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
		public int[] createAntecedentIndex() {
			return this.antecedentFactory.create();
		}

		@Override
		public int[][] createAntecedentIndex(int numberOfGenerateRule) {
			int[][] return_buf = new int[numberOfGenerateRule][];
			for(int i=0; i<numberOfGenerateRule; i++) {
				return_buf[i] = this.createAntecedentIndex();
			}
			return return_buf;
		}

		@Override
		public int[] createAntecedentIndex(Pattern<?> pattern) {
			int[] antecedentIndex = null;
			if(this.antecedentFactory instanceof HeuristicRuleGenerationMethod) {
				antecedentIndex = ((HeuristicRuleGenerationMethod)this.antecedentFactory).calculateAntecedentPart(pattern);
			}else {
				throw new ClassCastException("antecedentFactory is not HeuristicRuleGenerationMethod@RuleBuilder_Basic.createRule");
			}
			return antecedentIndex;
		}

		@Override
		public int[] createAntecedentIndex(Element michiganSolution) {
			Element fuzzySetList_node = (Element) michiganSolution.getElementsByTagName(XML_TagName.fuzzySetList.toString()).item(0);
			NodeList fuzzySetIDs = fuzzySetList_node.getElementsByTagName(XML_TagName.fuzzySetID.toString());
			int[] return_buf = new int[fuzzySetIDs.getLength()];
			for(int i=0; i<fuzzySetIDs.getLength(); i++) {
				return_buf[i] = Integer.valueOf(fuzzySetIDs.item(i).getTextContent());
			}
			return return_buf;
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
