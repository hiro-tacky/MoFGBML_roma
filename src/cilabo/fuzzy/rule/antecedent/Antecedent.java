package cilabo.fuzzy.rule.antecedent;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;

/**
 * 前件部のメソッド群定義．但し，遺伝子情報は持たないので注意
 * @author Takigawa Hiroki
 * @see cilabo.gbml.solution.michiganSolution.MichiganSolution
 */
public interface Antecedent {

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対応するメンバシップ値の配列を返す
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値配列 */
	public double[] getCompatibleGrade(int[] antecedentIndex, AttributeVector attributeVector);

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対するメンバシップ値の最終演算結果を返す
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値 */
	public double getCompatibleGradeValue(int[] antecedentIndex, AttributeVector attributeVector);

	/** 遺伝子情報を受け取り，ルール長を返す
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列
	 * @return 算出されたルール長 */
	public int getRuleLength(int[] antecedentIndex);

	/** このインスタンスのコピーを返す
	 * @return このインスタンスのコピー */
	public Antecedent copy();

	public Element toElement();
}
