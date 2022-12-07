package cilabo.fuzzy.rule.impl;

import cilabo.data.InputVector;
import cilabo.fuzzy.rule.AbstractRule;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.impl.Consequent_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;

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

}
