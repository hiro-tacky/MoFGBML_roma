package cilabo.fuzzy.rule.consequent.ruleWeight;

import org.w3c.dom.Element;

/** ルール重みクラスのインターフェイス
 * @author Takigawa Hiroki
 *
 * @param <RuleWeightValue> RuleWeight実装クラスのフィールドでルール重みを記憶する変数
 */
public interface RuleWeight <RuleWeightValue> {

	/** ルール重みのコピーを取得
	 * 実装時は取得されたインスタンスへの変更が反映されない(深いコピーとなる)ように
	 * @return このインスタンスが持つルール重み
	 */
	public RuleWeightValue getRuleWeightValue();

	/** ルール重みのコピーを取得
	 * 実装時は取得されたインスタンスへの変更が反映されない(深いコピーとなる)ように
	 * @return このインスタンスが持つルール重み
	 */
	public Double getRuleWeightDouble();

	/** ルール重みのコピーインスタンスを代入
	 * 実装時は代入されたインスタンスへの変更が反映されない(深いコピーとなる)ように
	 * @param ruleWeight 代入されるルール重み
	 */
	public void setRuleWeightValue(RuleWeightValue ruleWeightValue);

	/**
	 * このインスタンスのディープコピーを返す
	 * @return ディープコピーされたこのインスタンス
	 */
	public RuleWeight<RuleWeightValue> copy();

	/**xmlファイル出力用のElementを書き出す
	 * @return クラス内部の情報が書き込まれた Element インスタンス
	 */
	public Element toElement();
}
