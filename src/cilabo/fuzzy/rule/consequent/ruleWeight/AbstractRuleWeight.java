package cilabo.fuzzy.rule.consequent.ruleWeight;

/**RuleWeightクラスの抽象クラス<br>
 * 実装クラスではルール重みを記憶するオブジェクトとしてRuleWeightValueクラスの変数の宣言の実装
 * @author hirot
 *
 * @param <RuleWeightValue> RuleWeight実装クラスのフィールドでルール重みを記憶する変数
 */
public abstract class AbstractRuleWeight <RuleWeightValue> implements RuleWeight <RuleWeightValue>{

	protected RuleWeightValue ruleWeight;

	@Override
	public RuleWeightValue getRuleWeightValue() {
		return ruleWeight;
	}

@	Override
	public void setRuleWeightValue(RuleWeightValue ruleWeight) {
		this.ruleWeight = ruleWeight;
	}
}
