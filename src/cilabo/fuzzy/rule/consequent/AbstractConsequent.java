package cilabo.fuzzy.rule.consequent;

import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/**
 * 後件部クラスの抽象クラス abstract consequent class
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelClass> 後件部クラスが持つ結論部ラベルクラス conclusion label class that this class has
 * @param <RuleWeightClass> 後件部クラスが持つルール重みクラス rule weight class that this class has
 */
public abstract class AbstractConsequent <ClassLabelClass extends ClassLabel<?>, RuleWeightClass extends RuleWeight<?>>
	implements Consequent<ClassLabelClass, RuleWeightClass>{

	/** 結論部ラベルクラス conclusion label class */
	protected ClassLabelClass classLabel;
	/** ルール重みクラス rule weight class */
	protected RuleWeightClass ruleWeight;

	/** コンストラクタ．constructor
	 * @param classLabel 結論部ラベルクラス conclusion label class
	 * @param ruleWeight ルール重みクラス rule weight class
	 */
	public AbstractConsequent(ClassLabelClass classLabel, RuleWeightClass ruleWeight) {
		this.classLabel = classLabel;
		this.ruleWeight = ruleWeight;
	}

	@Override
	public ClassLabelClass getClassLabel() {
		return this.classLabel;
	}

	@Override
	public RuleWeightClass getRuleWeight() {
		return this.ruleWeight;
	}
}
