package cilabo.fuzzy.rule.builder;

import org.w3c.dom.Element;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.Rule;

/**
 * ルールクラスビルダー rule class builder
 * @author Takigawa Hiroki
 *
 * @param <RuleClass> 生成されるルール rule class to be generated
 */
public interface RuleBuilder <RuleClass extends Rule<?, ?>>{

	/** 前件部のファジィセットのインデックス配列を返す<br>
	 * Generate antecedent index
	 * @return 生成されたRuleオブジェクトの配列 Generated antecedent index*/
	public int[] createAntecedentIndex();

	/** 前件部のファジィセットのインデックス配列を複数返す<br>
	 * Generate specified number of antecedent index
	 * @param numberOfGenerateRule 生成する前件部の数 number of antecedent index to generate
	 * @return 生成されたRuleオブジェクトの配列 Generated array of antecedent index*/
	public int[][] createAntecedentIndex(int numberOfGenerateRule);

	/**
	 * 与えられたパターンから前件部のファジィセットのインデックス配列を生成する<br>
	 * Generate antecedent index by pattern
	 * @param pattern 前件部生成の学習に使用するPatternクラス pattern class to be used to generate antecedent index
	 * @return 生成された前件部のファジィセットのインデックス配列  Generated antecedent index */
	public int[] createAntecedentIndex(Pattern<?> pattern);

	/** 入力されたElementから前件部のファジィセットのインデックス配列を読み込む<br>
	 * Load antecedent index from XML files
	 * @param michiganSolutionEL 読み込むXMLファイル XMLfile to load
	 * @return 読み込まれた前件部のファジィセットのインデックス配列 Loaded antecedent index*/
	public int[] createAntecedentIndex(Element michiganSolutionEL);

	/** 入力された遺伝子情報を基にRuleオブジェクトを生成する
	 * Generate Rule from given antecedent index
	 * @param antecedentIndex 生成に用いる遺伝子情報 antecedent index to generate Rule
	 * @return 生成されたRuleオブジェクト Generated Rule*/
	public RuleClass createRule(int[] antecedentIndex);

	/** 入力されたElementからルールを読み込む<br>
	 * Load Rule from XML files
	 * @param michiganSolutionEL 読み込むXMLファイル XMLfile to load
	 * @return 読み込まれたルールクラス Loaded rule*/
	public RuleClass createRule(Element michiganSolutionEL);

	public RuleBuilder<RuleClass> copy();
}
