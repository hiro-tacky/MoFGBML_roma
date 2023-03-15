package cilabo.gbml.solution.michiganSolution;

import java.util.List;

import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.builder.RuleBuilder;

/**
 * Michigan型識別器クラス michigan solution class
 * @author Takigawa Hiroki
 *
 * @param <RuleClass> このMichigan型識別器が持つルールクラス rule class that this class has
 */
public interface MichiganSolution<RuleClass extends Rule<?, ?>> extends IntegerSolution{

	/**
	 * 指定された位置にあるファジィセットのインデックスを返します。<br>
	 * Returns index of fuzzy set at the specified position
	 * @param index 返されるファジィセットのインデックスのインデックス．index of index of fuzzy set to return
	 * @return 指定された位置にあるファジィセットのインデックス．index of fuzzy set at the specified position in the list
	 */
	@Override
	public Integer getVariable(int index);

	/**
	 * 指定された位置にあるファジィセットのインデックスを、入力されたファジィセットのインデックスで置き換えます。<br>
	 * Replaces index of fuzzy set at the specified position in the list with the specified index of fuzzy set.
	 * @param index 置換されるファジィセットのインデックスのインデックス．index of index of fuzzy set to replace
	 * @param variable 指定された位置に格納されるファジィセットのインデックス．index of fuzzy sett to be stored at the specified position
	 */
	public void setVariable(int index, Integer variable) ;

	/**
	 * このインスタンスが持つファジィセットのインデックス配列を返します。<br>
	 * Returns array of index of fuzzy set that this instance has.
	 * @return 返されるファジィセットのインデックス配列．array of index of fuzzy set to return
	 */
	@Override
	public List<Integer> getVariables();

	/**
	 * このインスタンスが持つファジィセットのインデックス配列を返します。<br>
	 * Returns array of index of fuzzy set that this instance has.
	 * @param variables 返されるファジィセットのインデックス配列．array of index of fuzzy set to return
	 */
	public void setVariables(int[] variables);

	/**
	 * このインスタンスが持つファジィセットのインデックス配列を返します。<br>
	 * Returns array of index of fuzzy set that this instance has.
	 * @param variables 返されるファジィセットのインデックス配列．array of index of fuzzy set to return
	 */
	public void setVariables(List<Integer> variables);

	/**
	 * このインスタンスが持つファジィセットのインデックス配列を返します。<br>
	 * Returns array of index of fuzzy set that this instance has.
	 * @return 返されるファジィセットのインデックス配列．array of index of fuzzy set to return
	 */
	public int[] getVariablesArray();

	/** 属性値クラスを受け取り，入力パターンの属性値に対するルールの適合度を返す
	 * Return fitness value with given attribute vector
	 * @param attributeVector 識別対象となるパターンの属性値クラス pattern class to be classified
	 * @return ルールの適合度 fitness value*/
	public double getFitnessValue(AttributeVector attributeVector);

	/** 属性値クラスを受け取り，入力パターンの属性値に対応するメンバシップ値の配列を返す
	 * Return list of compatible grade by given attribute vector
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値配列 */
	public double[] getCompatibleGrade(AttributeVector attributeVector);

	/** 属性値クラスを受け取り，入力パターンの属性値に対するメンバシップ値の最終演算結果を返す
	 * Return compatible grade value by given attribute vector
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値 */
	public double getCompatibleGradeValue(AttributeVector attributeVector);

	/** AntecedentIndexFactory class を用いてRuleオブジェクトを生成する
	 * Generate Rule with antecedent index factory class*/
	public void createRule();

	/** 与えられたパターンを基にRuleオブジェクトを生成する<br>
	 * Generate Rule from given pattern
	 * @param pattern 前件部生成の学習に使用するPatternクラス pattern class to be used to generate Rule */
	public void createRule(Pattern<?> pattern);

	/** 入力されたElementからルールを読み込む<br>
	 * Load Rule from XML file
	 * @param michiganSolutionEL 読み込むXMLファイル XMLfile to load */
	public void createRule(Element michiganSolutionEL);

	/** 後件部オブジェクトを生成する
	 * Learning consequent */
	public void learning();

	/** 遺伝子情報を受け取り，ルール長を返す
	 * Return rule length
	 * @return 算出されたルール長 */
	public int getRuleLength();

	/**
	 * このインスタンスが持つルールクラスを返します。<br>
	 * Returns rule class that this instance has.
	 * @return 返されるルールクラス．rule class to return */
	public RuleClass getRule();

	/**
	 * このインスタンスが持つルールクラスビルダーを返します。<br>
	 * Returns rule class builder that this instance has.
	 * @return 返されるルールクラスビルダー．rule class builder to return */
	public RuleBuilder<RuleClass> getRuleBuilder();

	@Override
	public MichiganSolution<RuleClass> copy();

	@Override
	public String toString();

	public Element toElement();
}
