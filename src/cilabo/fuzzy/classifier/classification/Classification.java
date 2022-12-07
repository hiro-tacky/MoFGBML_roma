package cilabo.fuzzy.classifier.classification;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/**識別方式のインターフェイス
 * @author hirot
 *
 * @param <ClassifierObject> 識別を行う識別器
 */
public interface Classification <michiganSolution extends MichiganSolution>{

	/**
	 * 未知パターンに対する識別方式
	 *
	 * @param classifier 識別器
	 * @param vector 入力パターン
	 * @return 勝利ルール */
	public MichiganSolution classify(List<michiganSolution> michiganSolutionList, InputVector vector);

	public Classification<michiganSolution> copy();
}
