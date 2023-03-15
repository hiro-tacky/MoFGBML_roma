package cilabo.fuzzy.rule;

import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;

/**
 * ルールの抽象クラス．基本的なメソッドを実装 abstract rule class. this class define basic methods
 * @author Takigawa Hiroki
 *
 * @param <AntecedentClass> ルールオブジェクトが持つ前件部のクラス antecedent class that this class has
 * @param <ConsequentClass> ルールオブジェクトが持つ後件部のクラス consequent class that this class has
 */
public abstract class AbstractRule <
	AntecedentClass extends Antecedent,
	ConsequentClass extends Consequent<?, ?>>
	implements Rule<AntecedentClass, ConsequentClass>{

	/** 前件部クラス antecedent class*/
	protected AntecedentClass antecedent;
	/** 後件部クラス consequent class*/
	protected ConsequentClass consequent;

	/**
	 * コンストラクタ．constructor
	 * @param antecedent 前件部クラス antecedent class
	 * @param consequent 後件部クラス consequent class
	 */
	public AbstractRule(AntecedentClass antecedent, ConsequentClass consequent) {
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	@Override
	public AntecedentClass getAntecedent() {
		return this.antecedent;
	}

	@Override
	public ConsequentClass getConsequent() {
		return this.consequent;
	}
}
