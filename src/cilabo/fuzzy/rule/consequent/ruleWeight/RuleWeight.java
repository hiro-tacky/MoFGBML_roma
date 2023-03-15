package cilabo.fuzzy.rule.consequent.ruleWeight;

import org.w3c.dom.Element;

/** ルール重みクラス rule weight class
 * @author Takigawa Hiroki
 *
 * @param <RuleWeightVariable> RuleWeightクラスでルール重みを保持する変数 vaiable of RuleWeight class
 */
public interface RuleWeight <RuleWeightVariable> {

	/**
	 * このインスタンスが持つルール重みを返します。<br>
	 * Returns rule weight that this instance has.
	 * @return 返されるルール重み．rule weight to return
	 */
	public RuleWeightVariable getRuleWeightVariable();

	/**
	 * このインスタンスが持つルール重みを返します。<br>
	 * Returns rule weight value that this instance has.
	 * @return 返されるルール重み．rule weight value to return
	 */
	public Double getRuleWeightValue();

	/**
	 * ルール重みを入力されたルール重みで置き換えます。<br>
	 * Replaces rule weight in this instance.
	 * @param ruleWeightValue このインスタンスに格納されるルール重み．rule weight to be stored at this instance
	 */
	public void setRuleWeightVariable(RuleWeightVariable ruleWeightVariable);

	public RuleWeight<RuleWeightVariable> copy();

	public Element toElement();
}
