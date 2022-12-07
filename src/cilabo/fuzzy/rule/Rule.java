package cilabo.fuzzy.rule;

import cilabo.data.InputVector;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;

/** ルールのインターフェイス．但し，このクラスは遺伝子情報は持たない．
 * @author Takigawa Hiroki
 *
 * @param <AntecedentObject> ルールオブジェクトが持つ前件部のクラス
 * @param <ConsequentObject> ルールオブジェクトが持つ後件部のクラス
 * @see cilabo.gbml.solution.michiganSolution.MichiganSolution
 */
public interface Rule<AntecedentObject extends Antecedent, ConsequentObject extends Consequent>
	extends Antecedent, Consequent{

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対するルールの適合度を返す
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param inputVector 識別対象となるパターンの属性値クラス
	 * @return ルールの適合度 */
	public double getFitnessValue(int[] antecedentIndex, InputVector inputVector);

	/** 遺伝子情報を受け取り，ルール長を返す
	 * @param 前件部のファジィセットのインデックス配列
	 * @return 算出された持つルール長 */
	public int getRuleLength(int[] antecedentIndex);

	/** 入力された後件部クラスラベルとこのインスタンスが持つ後件部のクラスラベルを比較する
	 * @param classLabel 比較対象となる結論部クラスラベル
	 * @return true:同値であった．false: 異なる結論部クラスラベルであった．*/
	boolean equalsClassLabel(ClassLabel classLabel);

	/** Antecedentオブジェクトを取得
	 * @return Antecedentオブジェクト */
	public AntecedentObject getAntecedent();

	/** Consequentオブジェクトを取得
	 * @return Consequentオブジェクト */
	public ConsequentObject getConsequent();

	@Override
	public Rule<AntecedentObject, ConsequentObject> copy();

	/** Ruleオブジェクトを生成するfactoryのインターフェイス．
	 * @author Takigawa Hiroki
	 * @param <RuleObject> 生成するRuleオブジェクトのクラス
	 */
	public interface RuleBuilder<RuleObject extends Rule>{

		/** 前件部のファジィセットのインデックス配列を返す
		 * @return 生成された前件部のファジィセットのインデックス配列 */
		public int[] createAntecedentIndex();

		/** 前件部のファジィセットのインデックス配列を指定された個数返す
		 * @param numberOfGenerateRule 生成したいインデックス配列の個数
		 * @return 生成された前件部のファジィセットのインデックス配列 */
		public int[][] createAntecedentIndex(int numberOfGenerateRule);

		/** 入力された遺伝子情報を基にRuleオブジェクトを生成する
		 * @param antecedentIndex 生成に用いる遺伝子情報
		 * @return 生成されたRuleオブジェクト */
		public RuleObject createRule(int[] antecedentIndex);

		/** 入力された遺伝子情報を基にRuleオブジェクトを複数生成する
		 * @param antecedentIndex 生成に用いる遺伝子情報
		 * @return 生成されたRuleオブジェクトの配列*/
		public RuleObject[] createRule(int[][] antecedentIndex);

		public RuleBuilder<RuleObject> copy();
	}
}
