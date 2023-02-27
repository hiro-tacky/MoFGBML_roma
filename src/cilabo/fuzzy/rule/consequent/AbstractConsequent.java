package cilabo.fuzzy.rule.consequent;

import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/**
 * 後件部クラスの抽象クラス
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelObject> 実装した後件部クラスが持つ結論部クラス
 * @param <T1> 結論部クラスが扱うクラスラベル変数クラス
 * @param <RuleWeightObject> 実装した後件部クラスが持つルール重みクラス
 * @param <T2> ルール重みクラスが扱うルール重み変数クラス
 */
public abstract class AbstractConsequent <ClassLabelObject extends ClassLabel<T1>, T1, RuleWeightObject extends RuleWeight<T2>, T2>
	implements Consequent<ClassLabelObject, T1, RuleWeightObject, T2>{

	protected ClassLabelObject classLabel;

	protected RuleWeightObject ruleWeight;

	public AbstractConsequent(ClassLabelObject classLabel, RuleWeightObject ruleWeight) {
		this.classLabel = classLabel;
		this.ruleWeight = ruleWeight;
	}

	@Override
	public ClassLabelObject getClassLabel() {
		return this.classLabel;
	}

	@Override
	public void setClassLabelValue(T1 classLabelValue) {
		this.classLabel.setClassLabelValue(classLabelValue);
	}

	@Override
	public T1 getClassLabelValue() {
		return this.classLabel.getClassLabelValue();
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
	public RuleWeightObject getRuleWeight() {
		return this.ruleWeight;
	}

	@Override
	public Double getRuleWeightDouble() {
		return this.ruleWeight.getRuleWeightDouble();
	}

	@Override
	public T2 getRuleWeightValue() {
		return this.ruleWeight.getRuleWeightValue();
	}

	@Override
	public void setRuleWeightValue(T2 ruleWeightValue) {
		this.ruleWeight.setRuleWeightValue(ruleWeightValue);
	}

}
