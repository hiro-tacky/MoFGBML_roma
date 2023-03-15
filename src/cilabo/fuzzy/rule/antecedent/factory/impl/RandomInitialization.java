package cilabo.fuzzy.rule.antecedent.factory.impl;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.main.Consts;
import random.MersenneTwisterFast;

/**
 * ランダムに決定される前件部のFactory<br>
 * randomly generate array of fuzzy set index
 * @author Takigawa Hiroki
 */
public final class RandomInitialization implements AntecedentIndexFactory{
	/**	乱数生成器 */
	MersenneTwisterFast uniqueRnd;
	/** 学習用データ */
	private DataSet<?> train;
	/** 次元数 */
	private int numberOfDimension;
	/** シード値 */
	private int seed;

	/**
	 * コンストラクタ．constructor
	 * @param seed シード値 seed for random number generator
	 * @param train データセット data set
	 */
	public RandomInitialization(int seed, DataSet<?> train) {
		this.seed = seed;
		this.uniqueRnd = new MersenneTwisterFast(seed);
		this.train = train;
		this.numberOfDimension = Knowledge.getInstance().getNumberOfDimension();
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
			dcRate = (numberOfDimension - Consts.ANTECEDENT_NUMBER_DO_NOT_DONT_CARE)/(double)numberOfDimension;
		}

		int[] antecedentIndex = new int[numberOfDimension];

		Pattern<?> randomPattern = train.getPattern(uniqueRnd.nextInt(train.getDataSetSize()));

		for(int n = 0; n < numberOfDimension; n++) {
			if(uniqueRnd.nextBoolean(dcRate)) {
				// don't care
				antecedentIndex[n] = 0;
			}
			else {
				// Judge which dimension n is categorical or numerical.
				if(randomPattern.getAttributeValue(n) < 0) {
					// Categorical
					antecedentIndex[n] = (int)randomPattern.getAttributeValue(n);
				}
				else {
					// Numerical
					antecedentIndex[n] = uniqueRnd.nextInt(Knowledge.getInstance().getNumberOfFuzzySet(n));
				}
			}
		}

		return antecedentIndex;
	}

	@Override
	public int[][] create(int numberOfGenerateRule) {
		int[][] antecedentIndexArray = new int[numberOfGenerateRule][this.numberOfDimension];
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
