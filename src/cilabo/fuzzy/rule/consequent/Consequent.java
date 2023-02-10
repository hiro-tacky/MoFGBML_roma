package cilabo.fuzzy.rule.consequent;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/** 後件部クラスのインターフェイス．基本的なsetter及びgetter機能を持つ
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelObject> 実装した後件部クラスが持つ結論部クラス
 * @param <RuleWeightObject> 実装した後件部クラスが持つルール重みクラス
 */
public interface Consequent<ClassLabelObject extends ClassLabel<T1>, T1,  RuleWeightObject extends RuleWeight<T2>, T2>
	extends ClassLabel<T1>, RuleWeight<T2>{

	/** クラスラベルのコピーインスタンスを取得
	 * @return このインスタンスが持つクラスラベルのコピーインスタンス
	 */
	public ClassLabelObject getClassLabel();

	/** ルール重みのコピーインスタンスを取得
	 * @return このインスタンスが持つルール重みのコピーインスタンス
	 */
	public RuleWeightObject getRuleWeight();

	public Consequent<ClassLabelObject, T1, RuleWeightObject, T2> copy();

	public Element toElement();
}
