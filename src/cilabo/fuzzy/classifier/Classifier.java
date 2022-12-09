package cilabo.fuzzy.classifier;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public interface Classifier <michiganSolution extends MichiganSolution> {

	/** 未知パターンに対する勝利ルールを返す
	 * @param vector 未知パターンの属性値クラス
	 * @return 勝利ルール */
	public MichiganSolution classify(List<michiganSolution> michiganSolutionList, InputVector vector);

	/**  このインスタンスのディープコピーを取得する
	 * @return  このインスタンスのディープコピー */
	public Classifier<michiganSolution> copy();

	/**	ルール長を取得する */
	public int getRuleLength(List<michiganSolution> michiganSolutionList);

	/** ルール数を取得する */
	public int getRuleNum(List<michiganSolution> michiganSolutionList);

}
