package cilabo.fuzzy.rule.antecedent;

import cilabo.fuzzy.knowledge.Knowledge;
import jfml.term.FuzzyTermType;

public abstract class AbstractAntecedent implements Antecedent{

	/** 入力された遺伝子情報に対応するファジィセットを返す
	 * @param antecedentIndex ファジィセットのインデックス配列
	 * @return ファジィセットの配列 */
	protected FuzzyTermType[] getFuzzySets(int[] antecedentIndex) {
		FuzzyTermType[] fuzzyTermTypeList = new FuzzyTermType[antecedentIndex.length];
		for(int i=0; i<antecedentIndex.length; i++) {
			fuzzyTermTypeList[i] = Knowledge.getInstance().getFuzzySet(i, antecedentIndex[i]);
		}
		return fuzzyTermTypeList;
	}

	/** 入力された遺伝子情報に対応するファジィセットを返す
	 * @param dimensionIndex 遺伝子情報に対応する次元
	 * @param antecedentIndex ファジィセットのインデックス
	 * @return ファジィセット */
	protected FuzzyTermType getFuzzySet(int dimensionIndex, int antecedentIndex) {
		return Knowledge.getInstance().getFuzzySet(dimensionIndex, antecedentIndex);
	}
}
