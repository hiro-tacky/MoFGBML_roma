package cilabo.fuzzy.rule.consequent.factory;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/**
 * 入力された前件部から後件部を算出する為に使用される関数群<br>
 * methods to calculate consequent by geiven antecedent
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelClass> 算出される後件部で使用される結論部クラス
 * @param <RuleWeightClass> 算出される後件部で使用されるルール重みクラス
 * @param <ConsequentClass> 算出される後件部のクラス
 * @param <Confidence> 信頼度を記憶する変数
 */
public abstract class ConsequentFactoryCore <
	ClassLabelClass extends ClassLabel<?>,
	RuleWeightClass extends RuleWeight<?>,
	Confidence>{

	/**	生成不可能と判断するルールの重みの下限 */
	protected double defaultLimit = 0;
	/** 学習に用いるデータセット*/
	protected DataSet<Pattern<ClassLabelClass>> train;

	public double getLimit() {
		return this.defaultLimit;
	}

	public void setLimit(double limit) {
		this.defaultLimit = limit;
	}

	/**
	 * 前件部からクラス別の信頼度を算出する.calculate confidences for each class by geiven antecedent
	 * @param antecedent 前件部クラス antecedent class
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列 array of antecedent index
	 * @return クラス別の信頼度 confidences for each class
	 */
	public abstract Confidence calculateConfidence(Antecedent antecedent, int[] antecedentIndex);

	/**
	 * クラス別の信頼度から結論部クラスラベルを決定 decide conclusion class label from confidences for each class
	 * @param confidence クラス別の信頼度 confidences for each class
	 * @return 結論部クラスラベル decided conclusion class label
	 */
	public abstract ClassLabelClass calculateClassLabel (Confidence confidence);

	/**
	 * クラス別の信頼度とクラスラベルからルール重みを算出する<br>
	 * calculate rule weight from conclusion class label and confidences for each class
	 * @param classLabel 結論部クラスラベル decided conclusion class label
	 * @param confidence クラス別の信頼度 confidences for each class
	 * @param limit 生成不可能と判断するルールの重みの下限 limit to judge which a rule is enable to be generated or not
	 * @return ルール重み calculated rule weight
	 */
	public abstract RuleWeightClass calculateRuleWeight(ClassLabelClass classLabel, Confidence confidence, double limit);
}
