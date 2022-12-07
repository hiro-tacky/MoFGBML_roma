package cilabo.fuzzy.rule.consequent;

import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/** 後件部クラスのインターフェイス．基本的なsetter及びgetter機能を持つ
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelObject> 実装した後件部クラスが持つ結論部クラス
 * @param <T1> 結論部クラスが結論部クラスを記憶するための変数
 * @param <RuleWeightObject> 実装した後件部クラスが持つルール重みクラス
 * @param <T2> ルール重みクラスがルール重みを記憶するための変数
 */
public interface Consequent<ClassLabelObject extends ClassLabel, RuleWeightObject extends RuleWeight>{

	/** クラスラベルのコピーインスタンスを取得
	 * @return このインスタンスが持つクラスラベルのコピーインスタンス
	 */
	public ClassLabelObject getClassLabel();

	/** ルール重みのコピーインスタンスを取得
	 * @return このインスタンスが持つルール重みのコピーインスタンス
	 */
	public RuleWeightObject getRuleWeight();

	public Consequent<ClassLabelObject, RuleWeightObject> copy();
}
