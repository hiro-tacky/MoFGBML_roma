package cilabo.fuzzy.classifier.michigan.classification.impl;

import java.util.List;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.classifier.michigan.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/** 単一勝利ルールの実装クラス
 * Single winner rule method
 * @author Takigawa Hiroki
 *
 * @param <MichiganSolutionClass> 識別を行うMichigan型識別器
 */
public final class SingleWinnerRuleSelection <MichiganSolutionClass extends MichiganSolution<?>>
		implements Classification <MichiganSolutionClass> {

	/**
	 * 単一勝利ルールに基づいて適合度が最大値となるMichigan型識別器を返す．<br>
	 * 適合度が更新されない場合or適合度が最大値となるMichigan型識別器が複数複数存在し，
	 * それらの結論部クラスが異なる場合は識別不能とし，nullを返す．
	 * @param michiganSolutionList 識別を行うpittsuburgh型識別器
	 * @param pattern 識別対象となる入力パターン
	 * @return 勝利となったMichiganSolution 但し，識別不能となった場合はnullを返す
	 */
	@Override
	public MichiganSolutionClass classify(List<MichiganSolutionClass> michiganSolutionList, Pattern<?> pattern) {
		if(michiganSolutionList.size() < 1) {
			throw new IllegalArgumentException("argument [michiganSolutionList] has no michiganSolution @" + this.getClass().getSimpleName());}

		boolean canClassify = false; //識別可能フラグ
		double max = -Double.MAX_VALUE; //適用度最大値保存バッファ
		int winner = 0; //勝利ルールインデックス保存バッファ
		for(int q = 0; q < michiganSolutionList.size(); q++) {
			MichiganSolution<?> michiganSolution = michiganSolutionList.get(q);
			if(michiganSolution.getRule().getConsequent().getClassLabel().isRejectedClassLabel()) { continue; }
			double value = michiganSolution.getFitnessValue(pattern.getAttributeVector()); //適合度計算

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
				if(!michiganSolution.getRule().getConsequent().getClassLabel().equals(winnerRule.getRule().getConsequent().getClassLabel())) {
					canClassify = false;
				}
			}
		}

		//識別可能である場合勝利ルールを返す
		MichiganSolution<?> winnerRule = michiganSolutionList.get(winner);
		if(canClassify && max >= 0 && !winnerRule.getRule().getConsequent().getClassLabel().isRejectedClassLabel()) {
			return michiganSolutionList.get(winner);
		}
		//識別不可能である場合はnullを返す．
		else {
			return null;
		}
	}

	@Override
	public Classification<MichiganSolutionClass> copy() {
		return new SingleWinnerRuleSelection<MichiganSolutionClass>();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
