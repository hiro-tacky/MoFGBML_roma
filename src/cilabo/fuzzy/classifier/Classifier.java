package cilabo.fuzzy.classifier;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/** michigan型識別器集合へのメソッド定義クラス
 * @author Takigawa Hiroki
 *
 * @param <michiganSolution> 対象となるMichiganSolution実装クラス
 */
public interface Classifier <michiganSolution extends MichiganSolution<?>> {

	/** 未知パターンに対する勝利ルールを返す
	 * @param michiganSolutionList ルールセット
	 * @param vector 未知パターンの属性値クラス
	 * @return 勝利ルール */
	public michiganSolution classify(List<michiganSolution> michiganSolutionList, InputVector vector);

	/**  このインスタンスのディープコピーを取得する
	 * @return  このインスタンスのディープコピー */
	public Classifier<michiganSolution> copy();

	/**	ルール長を取得する
	 * @param michiganSolutionList ルールセット
	 * @return ルール長
	 */
	public int getRuleLength(List<michiganSolution> michiganSolutionList);

	 /** ルール数を取得する
	 * @param michiganSolutionList ルールセット
	 * @return ルール数
	 */
	public int getRuleNum(List<michiganSolution> michiganSolutionList);

}
