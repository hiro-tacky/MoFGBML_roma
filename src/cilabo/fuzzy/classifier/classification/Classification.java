package cilabo.fuzzy.classifier.classification;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/**勝者ルールを決定する手法を定義するClassificationクラスのインターフェイス
 * @param <ClassifierObject> 識別を行う識別器 */
public interface Classification <michiganSolution extends MichiganSolution<?>>{

	/**
	 * 入力されたパターンに対して識別結果を返す
	 * @param classifier 識別器
	 * @param vector 識別対処となる入力パターン
	 * @return 勝利ルール 但し，識別不能な場合はnull */
	public michiganSolution classify(List<michiganSolution> michiganSolutionList, InputVector vector);

	public Classification<michiganSolution> copy();
}
