package cilabo.gbml.solution.michiganSolution;

import org.uma.jmetal.solution.integersolution.IntegerSolution;

import cilabo.data.InputVector;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;

/** MichiganSolutionのインターフェイス．
 * @author Takigawa Hiroki
 *
 * @param <RuleObject> このMichiganSolutionが持つルールオブジェクトのクラス
 */
public interface MichiganSolution<RuleObject extends Rule> extends IntegerSolution{

	@Override
	public MichiganSolution<RuleObject> copy();

	/** 前件部のファジィセットのインデックス配列を代入する．
	 * @param variables 代入するファジィセットのインデックス配列 */
	public void setVariables(int[] variables);

	/** このインスタンス持つVariablesをintの配列として返す
	 * @return インスタンス持つVariables */
	public int[] getVariablesAsIntArray();

	/** このインスタンスが持つ前件部を基に後件部の学習を行う */
	public void learning();

	/** 入力された未知パターンに対して適応度を返す．
	 * @param inputVector 未知パターンの属性値クラス
	 * @return 算出された適応度 */
	public double getFitnessValue(InputVector inputVector);

	/** 前件部のルール長を返す
	 * @return このインスタンスが持つ前件部のルール長 */
	public int getRuleLength();

	/** 後件部の結論部クラスラベルを返す
	 * @return このインスタンスが持つ後件部の結論部クラスラベル */
	public ClassLabel getClassLabel();

	/** 入力された後件部クラスラベルとこのインスタンスが持つ後件部のクラスラベルを比較する
	 * @param classLabel 比較対象となる結論部クラスラベル
	 * @return true:同値であった．false: 異なる結論部クラスラベルであった．*/
	public boolean equalsClassLabel(ClassLabel classLabel);

	/** MichiganSolutionオブジェクトを生成するfactoryのインターフェイス．
	 * @author Takigawa Hiroki
	 *
	 * @param <michiganObject> 生成するMichiganSolutionオブジェクトのクラス
	 */
	public interface MichiganSolutionBuilder<michiganObject extends MichiganSolution>{

		/** 遺伝子情報を含むルールを自動生成し，生成されたルールからMichiganSolutionを生成する．
		 * @return 生成されたMichiganSolution */
		public michiganObject createMichiganSolution();

		/** 入力された遺伝子情報を持つMichiganSolutionを生成する．
		 * @param variables 代入する遺伝子情報
		 * @return MichiganSolutionを生成する．*/
		public michiganObject createMichiganSolution(int[] variables);

		/** 遺伝子情報を含むルールを自動生成し，生成されたルールからMichiganSolutionを指定された個数生成する．
		 * @param numberOfGenerateRule 生成したいMichiganSolutionの個数
		 * @return 生成されたMichiganSolutionの配列 */
		public michiganObject[] createMichiganSolutions(int numberOfGenerateRule);

		/** 入力された遺伝子情報を持つMichiganSolutionを指定された個数生成する．
		 * @param numberOfGenerateRule 生成したいMichiganSolutionの個数
		 * @param variables 生成したいMichiganSolutionの遺伝子情報の配列
		 * @return 生成されたMichiganSolutionの配列 */
		public michiganObject[] createMichiganSolutions(int numberOfGenerateRule, int[][] variables);

		public MichiganSolutionBuilder<michiganObject> copy();
	}
}
