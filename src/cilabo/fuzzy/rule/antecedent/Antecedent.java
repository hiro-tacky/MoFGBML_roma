package cilabo.fuzzy.rule.antecedent;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;

/**
 * 前件部のメソッド群定義．但し，遺伝子情報は持たないので注意
 * this class define antecdent's methods. but this class doesn't have gentic information
 * @author Takigawa Hiroki
 */
public interface Antecedent {

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対応するメンバシップ値の配列を返す
	 * Return list of compatible grade by given antecedent index(fuzzy set index) and attribute vector
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値配列 */
	public double[] getCompatibleGrade(int[] antecedentIndex, AttributeVector attributeVector);

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対するメンバシップ値の最終演算結果を返す
	 * Return compatible grade value by given antecedent index(fuzzy set index) and attribute vector
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列
	 * @param attributeVector 識別対象となるパターンの属性値クラス
	 * @return メンバシップ値 */
	public double getCompatibleGradeValue(int[] antecedentIndex, AttributeVector attributeVector);

	/** 遺伝子情報を受け取り，ルール長を返す
	 * receive antecedent index, and return rule length
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列
	 * @return 算出されたルール長 */
	public int getRuleLength(int[] antecedentIndex);

	public Antecedent copy();

	@Override
	public String toString();

	public Element toElement();
}
