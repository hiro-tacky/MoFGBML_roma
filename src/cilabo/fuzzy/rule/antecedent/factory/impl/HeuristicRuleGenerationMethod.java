package cilabo.fuzzy.rule.antecedent.factory.impl;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.main.Consts;
import cilabo.utility.GeneralFunctions;
import cilabo.utility.Random;

/**ヒューリスティックに基づいた前件部のファジィセットのインデックス配列のFactory
 * @author Takigawa Hiroki
 */
public class HeuristicRuleGenerationMethod implements AntecedentIndexFactory{

	/**  学習用データ*/
	DataSet train;

	private int dimension;

	private int dataSize;

	/**コンストラクタ
	 * @param train 選択時の学習に用いる学習用データ
	 * @param samplingIndex 選択時の学習に用いるパターンのidリスト
	 */
	public HeuristicRuleGenerationMethod(DataSet train) {
		this.train = train;
		this.dimension = Knowledge.getInstance().getDimension();
		this.dataSize = train.getDataSize();
	}

	/** 前件部のファジィ集合のidを決定する
	 * @param head 生成する前件部の番目
	 * @return 決定された前件部のFuzzyTermTypeのidリスト
	 */
	public int[] selectAntecedentPart(int index) {
		Pattern pattern = train.getPattern(index);
		return this.calculateAntecedentPart(pattern);
	}

	public int[] calculateAntecedentPart(Pattern pattern) {
		int dimension = train.getNdim();
		double[] vector = pattern.getInputVector().getVector();

		//Don't careの決定
		double dcRate;
		if(Consts.IS_PROBABILITY_DONT_CARE) {
			dcRate = Consts.DONT_CARE_RT;
		}
		else {
			// (Ndim - const) / Ndim
			dcRate = Math.max((dimension - Consts.ANTECEDENT_NUMBER_DO_NOT_DONT_CARE) / (double)dimension, Consts.DONT_CARE_RT);
		}

		int[] antecedentIndex = new int[dimension];
		for(int n = 0; n < dimension; n++) {
			// don't care
			if(Random.getInstance().getGEN().nextBoolean(dcRate)) {
				antecedentIndex[n] = 0;
				continue;
			}

			// Categorical Judge
			if(vector[n] < 0) {
				antecedentIndex[n] = (int)vector[n];
				continue;
			}

			// Numerical
			int fuzzySetNum = Knowledge.getInstance().getFuzzySetNum(n)-1;
			double[] membershipValueRoulette = new double[fuzzySetNum];
			double sumMembershipValue = 0;
			membershipValueRoulette[0] = 0;
			for(int h = 0; h < fuzzySetNum; h++) {
				sumMembershipValue += Knowledge.getInstance().getMembershipValue(vector[n], n, h+1);
				membershipValueRoulette[h] = sumMembershipValue;
			}

			double arrow = Random.getInstance().getGEN().nextDouble() * sumMembershipValue;
			for(int h = 0; h < fuzzySetNum; h++) {
				if(arrow < membershipValueRoulette[h]) {
					antecedentIndex[n] = h+1;
					break;
				}
			}
		}
		return antecedentIndex;
	}

	@Override
	public int[] create() {
		int patternIndex = Random.getInstance().getGEN().nextInt(this.dataSize);

		int[] antecedentIndex = this.selectAntecedentPart(patternIndex);

		return antecedentIndex;
	}

	@Override
	public int[][] create(int numberOfGenerateRule) {

		int[][] antecedentIndexArray = new int[numberOfGenerateRule][this.dimension];
		Integer[] antecedentIndexArrayIndex;

		//学習に用いるパターンのインデックス配列を生成する
		if(numberOfGenerateRule <= this.dataSize) {
			antecedentIndexArrayIndex = GeneralFunctions.samplingWithout(this.dataSize, numberOfGenerateRule, Random.getInstance().getGEN());
		}else {
			antecedentIndexArrayIndex = new Integer[numberOfGenerateRule];
			for(int i=0; i<numberOfGenerateRule/this.dataSize; i++) {
				for(int j=0; j<this.dataSize; j++) {
					antecedentIndexArrayIndex[i*this.dataSize + j] = j;
				}
			}
			int restArrayIndex = numberOfGenerateRule % this.dataSize;
			Integer[] buf = GeneralFunctions.samplingWithout(this.dataSize,
					restArrayIndex, Random.getInstance().getGEN());
			for(int i=0; i<buf.length; i++) {
				antecedentIndexArrayIndex[numberOfGenerateRule - i -1]= buf[i];
			}
		}

		for(int i=0; i<numberOfGenerateRule; i++) {
			antecedentIndexArray[i] = this.selectAntecedentPart(antecedentIndexArrayIndex[i]);
		}
		return antecedentIndexArray;
	}

	@Override
	public HeuristicRuleGenerationMethod copy() {
		return new HeuristicRuleGenerationMethod(this.train);
	}
}
