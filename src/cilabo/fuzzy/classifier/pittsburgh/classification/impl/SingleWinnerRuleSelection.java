package cilabo.fuzzy.classifier.pittsburgh.classification.impl;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.classifier.pittsburgh.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

/** 単一勝利ルールの実装クラス
 * Single winner rule method
 * @author Takigawa Hiroki
 *
 * @param <PittsburghSolutionClass> 識別器が扱うpittsuburgh型識別器
 */
public final class SingleWinnerRuleSelection
		<PittsburghSolutionClass extends PittsburghSolution<MichiganSolutionClass>,
		MichiganSolutionClass extends MichiganSolution<?>>
		implements Classification <PittsburghSolutionClass, MichiganSolutionClass> {

	/**
	 * 単一勝利ルールに基づいて適合度が最大値となるMichiganSolutionを返す．<br>
	 * 適合度が更新されない場合or適合度が最大値となるMichiganSolutionが複数複数存在し，
	 * それらの結論部クラスが異なる場合は識別不能とし，nullを返す．
	 * @param pittsburghSolution 識別を行うpittsuburgh型識別器
	 * @param pattern 識別対象となる入力パターン
	 * @return 勝利となったMichiganSolution 但し，識別不能となった場合はnullを返す
	 */
	@Override
	public MichiganSolutionClass classify(PittsburghSolutionClass pittsburghSolution, Pattern<?> pattern) {
		if(pittsburghSolution.getNumberOfVariables() < 1) {
			throw new IllegalArgumentException("argument [pittsburghSolution] has no michiganSolution @" + this.getClass().getSimpleName());}

		boolean canClassify = false; //識別可能フラグ
		double max = -Double.MAX_VALUE; //適用度最大値保存バッファ
		int winner = 0; //勝利ルールインデックス保存バッファ
		for(int q = 0; q < pittsburghSolution.getNumberOfVariables(); q++) {
			MichiganSolution<?> michiganSolution = pittsburghSolution.getVariable(q);
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
				MichiganSolution<?> winnerRule = pittsburghSolution.getVariable(winner);
				// "membership*CF"が同値 かつ 結論部クラスが異なる場合識別不能とする
				if(!michiganSolution.getRule().getConsequent().getClassLabel().equals(winnerRule.getRule().getConsequent().getClassLabel())) {
					canClassify = false;
				}
			}
		}

		//識別可能である場合勝利ルールを返す
		MichiganSolution<?> winnerRule = pittsburghSolution.getVariable(winner);
		if(canClassify && max >= 0 && !winnerRule.getRule().getConsequent().getClassLabel().isRejectedClassLabel()) {
			return pittsburghSolution.getVariable(winner);
		}
		//識別不可能である場合はnullを返す．
		else {
			return null;
		}
	}

	@Override
	public Classification<PittsburghSolutionClass, MichiganSolutionClass> copy() {
		return new SingleWinnerRuleSelection<PittsburghSolutionClass, MichiganSolutionClass>();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
