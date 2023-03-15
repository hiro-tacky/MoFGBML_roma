package cilabo.fuzzy.rule.consequent.factory;

import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;

/**
 * 結論部クラスとルール重みを算出し後件部を生成する.Calculate conclusion class label and rule weight and ganerate consequent
 * @author Takigawa Hiroki
 *
 * @param <ConsequentClass> 出力用される後件部クラス
 */
public interface ConsequentFactory <ConsequentClass extends Consequent<?, ?>>{

	/**
	 * このインスタンスが持つ生成不可能と判断するルールの重みの下限を返します。<br>
	 * Returns limit to judge which a rule is enable to be generated or not that this instance has.
	 * @return 返される生成不可能と判断するルールの重みの下限．limit to judge which a rule is enable to be generated or not to return
	 */
	public double getLimit();

	/**
	 * 生成不可能と判断するルールの重みの下限を入力された生成不可能と判断するルールの重みの下限で置き換えます。<br>
	 * Replaces limit to judge which a rule is enable to be generated or not in this instance.
	 * @param limit このインスタンスに格納される生成不可能と判断するルールの重みの下限．limit to judge which a rule is enable to be generated or not to be stored at this instance
	 */
	public void setLimit(double limit);

	/**
	 * 前件部から後件部を生成する Set consequent by geiven antecedent<br>
	 * 但し，生成不可能と判断する際に用いるルールの信頼度の下限はデフォルトの値を使用する．
	 * @param antecedent 前件部クラス antecedent class
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列 array of antecedent index
	 * @return 生成された後件部 generated consequent
	 */
	public ConsequentClass learning(Antecedent antecedent, int[] antecedentIndex);

	/**前件部から後件部を生成する Set consequent by geiven antecedent
	 * @param antecedent 前件部クラス antecedent class
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列 array of antecedent index
	 * @param limit 生成不可能と判断する際に用いるルールの信頼度の下限 limit to judge which a rule is enable to be generated or not
	 * @return 生成された後件部 generated consequent
	 */
	public ConsequentClass learning(Antecedent antecedent, int[] antecedentIndex, double limit);

	public ConsequentFactory <ConsequentClass> copy();
}
