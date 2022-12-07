package cilabo.fuzzy.rule.consequent.factory;

import cilabo.data.DataSet;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/** 入力された前件部から後件部を算出する為に使用される関数群
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelObject> 算出される後件部で使用される結論部クラス
 * @param <RuleWeightObject> 算出される後件部で使用されるルール重みクラス
 * @param <ConsequentObject> 算出される後件部のクラス
 * @param <confidence> 信頼度を記憶する変数
 */
public abstract class ConsequentFactoryCore <
	ClassLabelObject extends ClassLabel,
	RuleWeightObject extends RuleWeight,
	ConsequentObject extends Consequent,
	confidence>{

	/**	生成不可能と判断するルールの重みの下限 */
	protected double defaultLimit = 0;

	/** 学習に用いるデータセット*/
	protected DataSet train;

	public DataSet getTrain() {
		return this.train;
	}

	/**前件部から後件部を生成する<br>
	 * 但し，生成不可能と判断するルールの信頼度の下限はデフォルトの値を使用する．
	 * @param antecedent 前件部
	 * @return 生成された後件部
	 */
	public abstract ConsequentObject learning(Antecedent antecedent, int[] antecedentIndex);

	/**前件部から後件部を生成する
	 * @param antecedent 前件部
	 * @param limit 生成不可能と判断するルールの信頼度の下限
	 * @return 生成された後件部
	 */
	public abstract ConsequentObject learning(Antecedent antecedent, int[] antecedentIndex, double limit);

	/** 前件部からクラス別の信頼度を算出する
	 * @param antecedent 前件部
	 * @return 信頼度
	 */
	public abstract confidence calcConfidence(Antecedent antecedent, int[] antecedentIndex);

	/** クラス別の信頼度からクラスラベルを算出する
	 * @param confidence クラス別の信頼度
	 * @return クラスラベル
	 */
	public abstract ClassLabelObject calcClassLabel (confidence confidence);

	/** クラス別の信頼度とクラスラベルからルール重みを算出する
	 * @param classLabel クラスラベル
	 * @param confidence クラス別の信頼度
	 * @param limit 生成不可能と判断するルール重みの下限
	 * @return ルール重み
	 */
	public abstract RuleWeightObject calcRuleWeight(ClassLabelObject classLabel, confidence confidence, double limit);

}
