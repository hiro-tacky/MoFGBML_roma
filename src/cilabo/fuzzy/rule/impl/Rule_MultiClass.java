package cilabo.fuzzy.rule.impl;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.AbstractRule;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.factory.impl.HeuristicRuleGenerationMethod;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.impl.Consequent_MultiClass;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Multi;
import cilabo.main.impl.multiTasking.MultiTasking;
import xml.XML_TagName;
import xml.XML_manager;

@MultiTasking
public final class Rule_MultiClass extends AbstractRule <Antecedent_Basic, Consequent_MultiClass, ClassLabel_Multi, RuleWeight_Multi> {

	public Rule_MultiClass(Antecedent_Basic antecedent, Consequent_MultiClass consequent) {
		super(antecedent, consequent);
	}

	@Override
	public Rule_MultiClass copy() {
		return new Rule_MultiClass(this.getAntecedent(), this.getConsequent());
	}

	@Override
	public double getFitnessValue(int[] antecedentIndex, AttributeVector inputVector) {
		double membership = this.getAntecedent().getCompatibleGradeValue(antecedentIndex, inputVector.getAttributeValue());

		double CFmean = 0;
		Double[] ruleWeightList = this.getConsequent().getRuleWeightValue();
		for(int i=0; i<ruleWeightList.length; i++) {
			CFmean += ruleWeightList[i];
		}
		CFmean /= (double)ruleWeightList.length;
		return membership*CFmean;
	}

	@Override
	public int getRuleLength(int[] antecedentIndex) {
		return this.getAntecedent().getRuleLength(antecedentIndex);
	}

	public static class RuleBuilder_MultiClas
	extends RuleBuilderCore<Antecedent_Basic, Consequent_MultiClass>
	implements RuleBuilder<Rule_MultiClass>{

		public RuleBuilder_MultiClas(AntecedentIndexFactory antecedentFactory,
				ConsequentFactory<Consequent_MultiClass> consequentFactory) {
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
		public Rule_MultiClass createRule(int[] antecedentIndex) {
			Antecedent_Basic antecedent = new Antecedent_Basic();
			Consequent_MultiClass consequent = this.learning(antecedent, antecedentIndex);
			return new Rule_MultiClass(antecedent, consequent);
		}

		@Override
		public Rule_MultiClass[] createRule(int[][] antecedentIndex) {
			Rule_MultiClass[] buf = new Rule_MultiClass[antecedentIndex.length];
			for(int i=0; i<antecedentIndex.length; i++) {
				buf[i] = this.createRule(antecedentIndex[i]);
			}
			return buf;
		}

		@Override
		public Rule_MultiClass createRule(Element michiganSolution) {
			Antecedent_Basic antecedent = new Antecedent_Basic();
			Element rule_node = (Element) michiganSolution.getElementsByTagName(XML_TagName.rule.toString()).item(0);
			Element consequent_node = (Element) rule_node.getElementsByTagName(XML_TagName.consequent.toString()).item(0);

			NodeList classLabelNodes = consequent_node.getElementsByTagName(XML_TagName.classLabelMulti.toString());
			Integer[] classLabel = new Integer[classLabelNodes.getLength()];
			for(int i=0; i<classLabelNodes.getLength(); i++) {
				classLabel[i] = Integer.valueOf(classLabelNodes.item(i).getTextContent());
			}

			NodeList ruleWeightNodes = consequent_node.getElementsByTagName(XML_TagName.ruleWeightMulti.toString());
			Double[] ruleWeight = new Double[ruleWeightNodes.getLength()];
			for(int i=0; i<ruleWeightNodes.getLength(); i++) {
				ruleWeight[i] = Double.valueOf(ruleWeightNodes.item(i).getTextContent());
			}

			Consequent_MultiClass consequent = new Consequent_MultiClass(classLabel, ruleWeight);
			return new Rule_MultiClass(antecedent, consequent);
		}

		@Override
		public RuleBuilder_MultiClas copy() {
			return new RuleBuilder_MultiClas(this.antecedentFactory.copy(), this.consequentFactory.copy());
		}

	}

	@Override
	public Antecedent_Basic getAntecedent() {
		return this.antecedent;
	}

	@Override
	public Consequent_MultiClass getConsequent() {
		return this.consequent;
	}

	@Override
	public ClassLabel_Multi getClassLabel() {
		return this.consequent.getClassLabel();
	}

	@Override
	public RuleWeight_Multi getRuleWeight() {
		return this.consequent.getRuleWeight();
	}

	@Override
	public String toString() {
		return "Rule_MultiClass [antecedent=" + antecedent + ", consequent=" + consequent + "]";
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
