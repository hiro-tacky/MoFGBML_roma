package cilabo.fuzzy.rule.consequent;

import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

public abstract class AbstractConsequent <ClassLabelObject extends ClassLabel<T1>, T1, RuleWeightObject extends RuleWeight<T2>, T2>
		implements Consequent<ClassLabelObject, RuleWeightObject>, ClassLabel<T1>, RuleWeight<T2>{

	protected ClassLabelObject classLabel;

	protected RuleWeightObject ruleWeight;

	public AbstractConsequent(ClassLabelObject classLabel, RuleWeightObject ruleWeight) {
		this.classLabel = classLabel;
		this.ruleWeight = ruleWeight;
	}

	@Override
	public abstract AbstractConsequent<ClassLabelObject, T1, RuleWeightObject, T2> copy();

	@Override
	public ClassLabelObject getClassLabel() {
		return this.classLabel;
	}

	@Override
	public RuleWeightObject getRuleWeight() {
		return this.ruleWeight;
	}

	@Override
	public T1 getClassLabelValue() {
		return this.classLabel.getClassLabelValue();
	}

	@Override
	public void setClassLabelValue(T1 classLabelValue) {
		this.classLabel.setClassLabelValue(classLabelValue);
	}

	@Override
	public boolean equalsClassLabel(ClassLabel<?> classLabel) {
		return this.classLabel.equalsClassLabel(classLabel);
	}

	@Override
	public boolean equalsClassLabel(int classLabelValue) {
		return this.classLabel.equalsClassLabel(classLabelValue);
	}

	@Override
	public boolean isRejectedClassLabel() {
		return this.classLabel.isRejectedClassLabel();
	}

	@Override
	public void setRejectedClassLabel() {
		this.classLabel.setRejectedClassLabel();
	}

	@Override
	public T2 getRuleWeightValue() {
		return this.ruleWeight.getRuleWeightValue();
	}

	@Override
	public void setRuleWeightValue(T2 ruleWeightValue) {
		this.ruleWeight.setRuleWeightValue(ruleWeightValue);
	}

	@Override
	public Double getRuleWeightDouble() {
		return this.ruleWeight.getRuleWeightDouble();
	}
}
