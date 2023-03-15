package cilabo.fuzzy.rule;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;

/**
 * ルールのメソッドクラス．但し，このクラスは遺伝子情報は持たない．
 * this class defines methods of rule. this class doesn't have antecedentIndex.
 * @author Takigawa Hiroki
 *
 * @param <AntecedentClass> ルールオブジェクトが持つ前件部のクラス antecedent class that this class has
 * @param <ConsequentClass> ルールオブジェクトが持つ後件部のクラス consequent class that this class has
 */
public interface Rule<
	AntecedentClass extends Antecedent,
	ConsequentClass extends Consequent<?, ?>>{

	/** 前件部のファジィセットのインデックス配列と属性値クラスを受け取り，入力パターンの属性値に対するルールの適合度を返す
	 * Return fitness value with given antecedent index and attribute vector
	 * @param antecedentIndex 識別に用いる遺伝子情報．前件部のファジィセットのインデックス配列 array of fuzzy set index
	 * @param inputVector 識別対象となるパターンの属性値クラス pattern class to be classified
	 * @return ルールの適合度 fitness value*/
	public double getFitnessValue(int[] antecedentIndex, AttributeVector inputVector);

	/** Antecedentオブジェクトを取得
	 * @return Antecedentオブジェクト */
	public AntecedentClass getAntecedent();

	/** Consequentオブジェクトを取得
	 * @return Consequentオブジェクト */
	public ConsequentClass getConsequent();

	@Override
	public String toString();

	public Element toElement();

	public Rule<AntecedentClass, ConsequentClass> copy();

}
