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
	public boolean isRejectedClassLabel() {
		return this.classLabel.isRejectedClassLabel();
	}
}
