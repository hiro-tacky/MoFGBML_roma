package cilabo.fuzzy.rule.antecedent.factory.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;

import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.utility.GeneralFunctions;
import cilabo.utility.Random;

/** 全パターンの組み合わせを持つ前件部のファジィ集合のインデックス配列生成クラス
 * this class define methods to generate all combination array of fuzzy set index
 * @author Takigawa Hiroki
 */
public final class AllCombinationAntecedentFactory implements AntecedentIndexFactory{

	/**	前件部のファジィセットのインデックスを格納 */
	private int[][] antecedents;
	/** 次元数 */
	private int numberOfDimension;

	/** コンストラクタ．constructor */
	public AllCombinationAntecedentFactory() {
		generateAntecedents();
		numberOfDimension = Knowledge.getInstance().getNumberOfDimension();
	}

	/** 全てのファジィ集合の組わせを生成する．*/
	private void generateAntecedents() {

		Queue<ArrayList<Integer>> que = new ArrayDeque<>();
		ArrayList<ArrayList<Integer>> ids = new ArrayList<>();

		que.add(new ArrayList<Integer>());

		while(!que.isEmpty()){
			ArrayList<Integer> buf = que.poll();
			int dim_i = buf.size();
			if(dim_i < numberOfDimension) {
				for(int i=0; i < Knowledge.getInstance().getNumberOfFuzzySet(dim_i); i++) {
					ArrayList<Integer> tmp = (ArrayList<Integer>) buf.clone();
					tmp.add(i);
					que.add(tmp);
				}
			}else {
				ids.add(buf);
			}
		}

		// Antecedent Part
		antecedents = new int[ids.size()][numberOfDimension];
		for(int i=0; i<ids.size(); i++) {
			for(int dim_i=0; dim_i<numberOfDimension; dim_i++) {
				antecedents[i][dim_i] = ids.get(i).get(dim_i);
			}
		}
	}

	@Override
	public int[] create() {
		if(Objects.isNull(this.antecedents)) {System.err.println("AllCombinationAntecedentFactory hasn't been initialised");}
		int antecedentIndexArrayIndex = Random.getInstance().getGEN().nextInt(this.antecedents.length);
		int[] antecedentIndex = this.antecedents[antecedentIndexArrayIndex];

		return antecedentIndex;
	}

	@Override
	public int[][] create(int numberOfGenerateRule) {
		if(Objects.isNull(this.antecedents)) {System.err.println("AllCombinationAntecedentFactory hasn't been initialised");}
		int[][] antecedentIndexArray = new int[numberOfGenerateRule][this.numberOfDimension];
		Integer[] antecedentIndexArrayIndex = GeneralFunctions.samplingWithout(this.antecedents.length, numberOfGenerateRule, Random.getInstance().getGEN());
		for(int i=0; i<numberOfGenerateRule; i++) {
			antecedentIndexArray[i] = this.antecedents[antecedentIndexArrayIndex[i]];
		}
		return antecedentIndexArray;
	}

	@Override
	public String toString() {
		return "AllCombinationAntecedentFactory [antecedents=" + Arrays.toString(antecedents) + ", dimension="
				+ numberOfDimension + "]";
	}

	@Override
	public AllCombinationAntecedentFactory copy() {
		return new AllCombinationAntecedentFactory();
	}
}
