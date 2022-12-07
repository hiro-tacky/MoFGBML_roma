package cilabo.fuzzy.classifier.classification.impl;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Rejected;

public final class SingleWinnerRuleSelection<michiganSolution extends MichiganSolution> implements Classification <michiganSolution> {

	/**
	 * 単一勝利ルールに基づいて勝利ルールを出力する．
	 * @param classifier 識別器
	 * @param vector 入力パターン
	 * @return 勝利ルール
	 * @see cilabo.fuzzy.classifier.classification.Classification#classify(cilabo.fuzzy.classifier.Classifier, cilabo.data.InputVector)
	 */
	@Override
	public MichiganSolution classify(List<michiganSolution> michiganSolutionList, InputVector vector) {

		boolean canClassify = true; //識別可能か
		double max = Double.MIN_VALUE; //適用度最大値
		int winner = 0;
		for(int q = 0; q < michiganSolutionList.size(); q++) {
			michiganSolution michiganSolution = michiganSolutionList.get(q);
			double value = michiganSolution.getFitnessValue(vector);

			//適用度最大値更新
			if(value > max) {
				max = value;
				winner = q;
				canClassify = true;
			}
			//適用度最大値が同値を取る場合
			else if(value == max) {
				michiganSolution winnerRule = michiganSolutionList.get(winner);
				// "membership*CF"が同値 かつ 結論部クラスが異なる
				if(!michiganSolution.equalsClassLabel(winnerRule.getClassLabel())) {
					canClassify = false;
				}
			}
		}

		if(canClassify && max > 0) {
			return michiganSolutionList.get(winner);
		}
		else {
			return MichiganSolution_Rejected.getInstance();
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
