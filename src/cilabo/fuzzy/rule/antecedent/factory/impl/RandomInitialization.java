package cilabo.fuzzy.rule.antecedent.factory.impl;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.main.Consts;
import random.MersenneTwisterFast;

/**ランダムに選択されたファジィセットの前件部のファジィセットのインデックス配列のFactory
 * @author Takigawa Hiroki
 */
public class RandomInitialization implements AntecedentIndexFactory{
	/**	乱数生成器 */
	MersenneTwisterFast uniqueRnd;

	/** 学習用データ */
	DataSet train;

	private int dimension;

	private int seed;

	public RandomInitialization(int seed, DataSet train) {
		this.seed = seed;
		this.uniqueRnd = new MersenneTwisterFast(seed);
		this.train = train;
		this.dimension = Knowledge.getInstance().getDimension();
	}

	@Override
	public int[] create() {
		double dcRate;
		if(Consts.IS_PROBABILITY_DONT_CARE) {
			// Constant Value
			dcRate = Consts.DONT_CARE_RT;
		}
		else {
			// (dimension - Const) / dimension
			dcRate = (dimension - Consts.ANTECEDENT_NUMBER_DO_NOT_DONT_CARE)/(double)dimension;
		}

		int[] antecedentIndex = new int[dimension];

		Pattern randomPattern = train.getPattern(uniqueRnd.nextInt(train.getDataSize()));

		for(int n = 0; n < dimension; n++) {
			if(uniqueRnd.nextBoolean(dcRate)) {
				// don't care
				antecedentIndex[n] = 0;
			}
			else {
				// Judge which dimension n is categorical or numerical.
				if(randomPattern.getInputValue(n) < 0) {
					// Categorical
					antecedentIndex[n] = (int)randomPattern.getInputValue(n);
				}
				else {
					// Numerical
					antecedentIndex[n] = uniqueRnd.nextInt(Knowledge.getInstance().getFuzzySetNum(n));
				}
			}
		}

		return antecedentIndex;
	}

	@Override
	public int[][] create(int numberOfGenerateRule) {
		int[][] antecedentIndexArray = new int[numberOfGenerateRule][this.dimension];
		for(int i=0; i<numberOfGenerateRule; i++) {
			antecedentIndexArray[i] = this.create();
		}
		return antecedentIndexArray;
	}

	@Override
	public String toString() {
		return "RandomInitialization [uniqueRnd=" + uniqueRnd + "]";
	}

	@Override
	public RandomInitialization copy() {
		return new RandomInitialization(this.seed, this.train);
	}

}
