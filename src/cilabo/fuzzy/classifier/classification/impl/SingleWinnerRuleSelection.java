package cilabo.fuzzy.classifier.classification.impl;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public final class SingleWinnerRuleSelection <michiganSolution extends MichiganSolution<?>> implements Classification <michiganSolution> {

	/**
	 * 単一勝利ルールに基づいて勝利ルールを出力する．
	 * @param classifier 識別器
	 * @param vector 入力パターン
	 * @return 勝利ルール 識別不能時はMichiganSolution_Rejected
	 * @see cilabo.fuzzy.classifier.classification.Classification#classify(cilabo.fuzzy.classifier.Classifier, cilabo.data.InputVector)
	 */
	@Override
	public michiganSolution classify(List<michiganSolution> michiganSolutionList, InputVector vector) {

		boolean canClassify = true; //識別可能フラグ
		double max = -Double.MAX_VALUE; //適用度最大値保存バッファ
		int winner = 0; //勝利ルールインデックス保存バッファ
		for(int q = 0; q < michiganSolutionList.size(); q++) {
			MichiganSolution<?> michiganSolution = michiganSolutionList.get(q);
			double value = michiganSolution.getFitnessValue(vector); //適合度計算

			//適用度最大値更新ケース
			if(value > max) {
				max = value;
				winner = q;
				canClassify = true;
			}
			//適用度最大値が同値を取る場合
			else if(value == max) {
				MichiganSolution<?> winnerRule = michiganSolutionList.get(winner);
				// "membership*CF"が同値 かつ 結論部クラスが異なる場合識別不能とする
				if(!michiganSolution.equalsClassLabel(winnerRule.getClassLabel())) {
					canClassify = false;
				}
			}
		}

		//識別可能である場合勝利ルールを返す
		if(canClassify && max > 0) {
			return michiganSolutionList.get(winner);
		}
		//識別不可能である場合はダミーであるMichiganSolution_Rejectedを返す．
		else {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public Classification<michiganSolution> copy() {
		return new SingleWinnerRuleSelection<michiganSolution>();
	}
}
