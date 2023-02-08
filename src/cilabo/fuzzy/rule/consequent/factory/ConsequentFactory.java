package cilabo.fuzzy.rule.consequent.factory;

import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;

/**
 * 入力された前件部から後件部を算出する
 * @author Takigawa Hiroki
 *
 * @param <ConsequentObject> 出力用される後件部クラス
 */
public interface ConsequentFactory <ConsequentObject extends Consequent<?, ?>>{

	/**前件部から後件部を生成する<br>
	 * 但し，生成不可能と判断する際に用いるルールの信頼度の下限はデフォルトの値を使用する．
	 * @param antecedent 前件部
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列
	 * @return 生成された後件部
	 */
	public ConsequentObject learning(Antecedent antecedent, int[] antecedentIndex);

	/**前件部から後件部を生成する
	 * @param antecedent 前件部
	 * @param antecedentIndex 前件部のファジィセットのインデックス配列
	 * @param limit 生成不可能と判断する際に用いるルールの信頼度の下限
	 * @return 生成された後件部
	 */
	public ConsequentObject learning(Antecedent antecedent, int[] antecedentIndex, double limit);

	public ConsequentFactory <ConsequentObject> copy();
}
