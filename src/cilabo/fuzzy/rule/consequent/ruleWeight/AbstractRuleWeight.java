package cilabo.fuzzy.rule.consequent.ruleWeight;

/**
 * ルール重み抽象クラス abstract rule weight class
 * @author Takigawa Hiroki
 *
 * @param <RuleWeightVariable> RuleWeightクラスでルール重みを保持する変数 vaiable of RuleWeight class
 */
public abstract class AbstractRuleWeight <RuleWeightVariable> implements RuleWeight <RuleWeightVariable>{

	/** ルール重み格納変数 vaiable of rule weight */
	protected RuleWeightVariable ruleWeight;

	@Override
	public RuleWeightVariable getRuleWeightVariable() {
		return ruleWeight;
	}

@	Override
	public void setRuleWeightVariable(RuleWeightVariable ruleWeight) {
		this.ruleWeight = ruleWeight;
	}
}
