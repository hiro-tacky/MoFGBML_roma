package cilabo.gbml.solution.michiganSolution;

import java.util.List;

import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/** MichiganSolutionのインターフェイス．
 * @author Takigawa Hiroki
 *
 * @param <RuleObject> このMichiganSolutionが持つルールオブジェクトのクラス
 */
public interface MichiganSolution<RuleObject extends Rule<?, ?, ?, ?, ?, ?>> extends IntegerSolution{

	@Override
	public MichiganSolution<RuleObject> copy();

	/** 前件部のファジィセットのインデックス配列を代入する．
	 * @param variables 代入するファジィセットのインデックス配列 */
	public void setVariables(int[] variables);

	/** 指定された次元の前件部のファジィセットのインデックスを返す．
	 * @param index 取得したい前件部のインデックス
	 * @return 指定された次元のファジィセットのインデックス
	 */
	public Integer getVariable(int index);

	/** 前件部のファジィセットのインデックス配列を返す．
	 * @return 前件部のファジィセットのインデックス配列
	 */
	public int[] getVariablesArray();

	/** RuleBuilderによって前件部を生成．遺伝子としてMichiganSolutionにセット．その後後件部学習を行う．*/
	public void createRule();

	/** XMLのmichiganSolutionノードを読み込んで，michiganSolutionを生成する
	 * @param michiganSolution */
	public void createRule(Element michiganSolution);

	/** パターンを読み込んでヒューリスティックにルールを生成する
	 * @param pattern 学習用パターン */
	public void createRule(Pattern<?> pattern);

	/** このインスタンスが持つ前件部を基に後件部の学習を行う */
	public void learning();

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対するルールの適合度を返す
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return ルールの適合度 */
	public double getFitnessValue(AttributeVector attributeVector);

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対応するメンバシップ値の配列を返す
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値配列 */
	public double[] getCompatibleGrade(AttributeVector attributeVector);

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対するメンバシップ値の最終演算結果を返す
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値 */
	public double getCompatibleGradeValue(AttributeVector attributeVector);

	/** 遺伝子情報を受け取り，ルール長を返す
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列
	 * @return 算出されたルール長 */
	public int getRuleLength();

	/** ルールクラスを返す
	 * @return このインスタンスが持つルールクラス */
	public RuleObject getRule();

	/** ルールジェネレーターを返す
	 * @return このインスタンスが持つルールジェネレーター */
	public RuleBuilder<RuleObject, ?, ?> getRuleBuilder();

	/** 後件部クラスを返す
	 * @return このインスタンスが持つ前件部クラス */
	public Consequent<?, ?, ?, ?> getConsequent();

	/** 前件部クラスを返す
	 * @return このインスタンスが持つ前件部クラス */
	public Antecedent getAntecedent();

	/** 後件部の結論部クラスラベルを返す
	 * @return このインスタンスが持つ後件部の結論部クラスラベル */
	public ClassLabel<?> getClassLabel();

	/** 後件部のルール重みを返す
	 * @return このインスタンスが持つ後件部のルール重み */
	public RuleWeight<?> getRuleWeight();

	public Element toElement();

	/** MichiganSolutionオブジェクトを生成するfactoryのインターフェイス．
	 * @author Takigawa Hiroki
	 *
	 * @param <michiganObject> 生成するMichiganSolutionオブジェクトのクラス
	 */
	public interface MichiganSolutionBuilder<michiganObject extends MichiganSolution<?>>{

		/** 遺伝子情報を含むルールを自動生成し，生成されたルールからMichiganSolutionを生成する．
		 * @return 生成されたMichiganSolution */
		public michiganObject createMichiganSolution();

		/**
		 * 入力されたElementをからMichiganSolutionを生成する．
		 * @param michiganSolution XMLファイル上のmichiganSolutionノード
		 * @return 生成されたmichiganSolution
		 */
		public michiganObject createMichiganSolution(Element michiganSolution);

		/** 入力された遺伝子情報を持つMichiganSolutionを生成する．
		 * @param variables 代入する遺伝子情報
		 * @return MichiganSolutionを生成する．*/
		public michiganObject createMichiganSolution(Pattern<?> pattern);

		/** 遺伝子情報を含むルールを自動生成し，生成されたルールからMichiganSolutionを指定された個数生成する．
		 * @param numberOfGenerateRule 生成したいMichiganSolutionの個数
		 * @return 生成されたMichiganSolutionの配列 */
		public List<michiganObject> createMichiganSolutions(int numberOfGenerateRule);

		public MichiganSolutionBuilder<michiganObject> copy();
	}
}
