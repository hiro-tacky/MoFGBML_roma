package cilabo.fuzzy.classifier.classification;

import java.util.List;

import cilabo.data.AttributeVector;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/**
 * 勝者ルールを決定する手法を定義するClassificationクラス
 * @param <michiganSolution> 識別を行う識別器が扱うMichiganSolutionの型
 */
public interface Classification <michiganSolution extends MichiganSolution<?>>{

	/**
	 * MichiganSolutionのリストと識別対象パターンの属性値クラスを受け取り，識別を行った結果勝利となったMichiganSolutionを返す．識別不能の場合はnullを返す．
	 * @param michiganSolutionList 識別器
	 * @param attributeVector 識別対象となる入力パターン
	 * @return 勝利となったMichiganSolution 但し，識別不能となった場合はnullを返す
	 */
	public michiganSolution classify(List<michiganSolution> michiganSolutionList, AttributeVector attributeVector);

	public Classification<michiganSolution> copy();
}
