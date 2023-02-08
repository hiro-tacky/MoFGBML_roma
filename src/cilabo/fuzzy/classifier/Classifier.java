package cilabo.fuzzy.classifier;

import java.util.List;

import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/** pittsuburgh型識別器のメソッド定義クラス．識別を行う為のClassificationを親として持つ．
 * @author Takigawa Hiroki
 *
 * @param <michiganSolution> pittsuburgh型識別器が持つMichiganSolutionクラスの型
 */
public interface Classifier <michiganSolution extends MichiganSolution<?>> extends Classification<michiganSolution>{

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

	/**  このインスタンスのディープコピーを取得する
	 * @return  このインスタンスのディープコピー */
	public Classifier<michiganSolution> copy();
}
