package cilabo.fuzzy.rule;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/** ルールのインターフェイス．但し，このクラスは遺伝子情報は持たない．
 * @author Takigawa Hiroki
 *
 * @param <AntecedentObject> ルールオブジェクトが持つ前件部のクラス
 * @param <ConsequentObject> ルールオブジェクトが持つ後件部のクラス
 * @see cilabo.gbml.solution.michiganSolution.MichiganSolution
 */
public interface Rule<
	AntecedentObject extends Antecedent,
	ConsequentObject extends Consequent<classLabel, T1, ruleWeight, T2>,
	classLabel extends ClassLabel<T1>, T1,
	ruleWeight extends RuleWeight<T2>, T2>
	extends Antecedent, Consequent<classLabel, T1, ruleWeight, T2>{

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対するルールの適合度を返す
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param inputVector 識別対象となるパターンの属性値クラス
	 * @return ルールの適合度 */
	public double getFitnessValue(int[] antecedentIndex, AttributeVector inputVector);

	/** Antecedentオブジェクトを取得
	 * @return Antecedentオブジェクト */
	public AntecedentObject getAntecedent();

	/** Consequentオブジェクトを取得
	 * @return Consequentオブジェクト */
	public ConsequentObject getConsequent();

	@Override
	public Element toElement();

	@Override
	public Rule<AntecedentObject, ConsequentObject, classLabel, T1, ruleWeight, T2> copy();

	/** Ruleオブジェクトを生成するfactoryのインターフェイス．
	 * @author Takigawa Hiroki
	 * @param <RuleObject> 生成するRuleオブジェクトのクラス
	 */
	public interface RuleBuilder
		<RuleObject extends Rule<AntecedentObject, ConsequentObject, ?, ?, ?, ?>,
		AntecedentObject extends Antecedent,
		ConsequentObject extends Consequent<?, ?, ?, ?>>{
		/** 前件部のファジィセットのインデックス配列を返す
		 * @return 生成されたRuleオブジェクトの配列*/
		public int[] createAntecedentIndex();

		/** 前件部のファジィセットのインデックス配列を複数返す
		 * @param numberOfGenerateRule 生成する前件部の数
		 * @return 生成されたRuleオブジェクトの配列*/
		public int[][] createAntecedentIndex(int numberOfGenerateRule);

		/** 前件部のファジィセットのインデックス配列を返す
		 * @param pattern 前件部生成の学習に使用するPatternクラス
		 * @return 生成された前件部のファジィセットのインデックス配列
		 */
		public int[] createAntecedentIndex(Pattern<?> pattern);

		/** 入力されたElementを基に前件部のファジィセットのインデックス配列を返す
		 * @param antecedentIndex 生成に用いる遺伝子情報
		 * @return 生成されたRuleオブジェクトの配列*/
		public int[] createAntecedentIndex(Element michiganSolution);

		/** 入力された遺伝子情報を基にRuleオブジェクトを生成する
		 * @param antecedentIndex 生成に用いる遺伝子情報
		 * @return 生成されたRuleオブジェクト */
		public RuleObject createConsequent(int[] antecedentIndex);

		/** 入力されたElementを基にRuleオブジェクトを複数生成する
		 * @param michiganSolution 生成に用いるElement
		 * @return 生成されたRuleオブジェクトの配列*/
		public RuleObject createConsequent(Element michiganSolution);

		/** 入力された遺伝子情報と前件部オブジェクトを基に後件部オブジェクトを生成する
		 * @param antecedent 前件部オブジェクト
		 * @param antecedentIndex 遺伝子情報
		 * @return 生成された後件部 */
		public ConsequentObject learning(AntecedentObject antecedent, int[] antecedentIndex);

		public RuleBuilder<RuleObject,
			AntecedentObject,
			ConsequentObject> copy();
	}
}
