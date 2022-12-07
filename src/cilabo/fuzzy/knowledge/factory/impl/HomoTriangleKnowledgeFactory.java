package cilabo.fuzzy.knowledge.factory.impl;

import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.factory.KnowledgeFactory;
import jfml.term.FuzzyTermType;

/**等分割三角形型ファジィのKBを生成
 * @author hirot
 *
 */
public class HomoTriangleKnowledgeFactory implements KnowledgeFactory {

	/** Number of features */
	int dimension;

	/** Parameters of membership functions */
	float[][] params;

	/**コンストラクタ
	 *
	 * @param dimension 次元数
	 * @param params 分割区間のパラメータ
	 */
	public HomoTriangleKnowledgeFactory(int dimension, float[][] params) {
		this.dimension = dimension;
		this.params = params;
	}

	@Override
	public void create() {
		int fuzzySetNum = params.length;

		// make fuzzy sets
		final FuzzyTermType[][] fuzzySets = new FuzzyTermType[dimension][fuzzySetNum+1];
		for(int i = 0; i < dimension; i++) {
			//Don't care
			fuzzySets[i][0] = new FuzzyTermType(" 0",
												FuzzyTermType.TYPE_rectangularShape,
												new float[] {0f, 1f});
			for(int j = 1; j < fuzzySetNum+1; j++) {
				int shapeType = FuzzyTermType.TYPE_triangularShape;
                String name = String.format("%2s", String.valueOf(j));
				float[] param = params[j-1];
				fuzzySets[i][j] = new FuzzyTermType(name, shapeType, param);
			}
		}

		// Create
		Knowledge knowledge = Knowledge.getInstance();
		knowledge.setFuzzySets(fuzzySets);

		return;
	}

	/**
	 * 等分割三角形型ファジィのKBの雛形を生成
	 * @return 生成された雛形
	 */
	public static HomoTriangleKnowledgeFactory.KnowledgeBuilder builder() {
		return new KnowledgeBuilder();
	}

	public static class KnowledgeBuilder {
		private int dimension = -1;
		private float[][] params;

		KnowledgeBuilder() {}

		/** 生成するKBの次元数を入力
		 * @param dimension 次元数
		 * @return 記入済みの雛形
		 */
		public HomoTriangleKnowledgeFactory.KnowledgeBuilder dimension(int dimension) {
			this.dimension = dimension;
			return this;
		}

		/** 生成するKBのパラメータを入力
		 * @param params 分割区間集合のパラメータ
		 * @return 記入済みの雛形
		 */
		public HomoTriangleKnowledgeFactory.KnowledgeBuilder params(float[][] params) {
			this.params = params;
			return this;
		}

		/** 雛形を基にKBを生成
		 * @return 生成されたKB
		 */
		public HomoTriangleKnowledgeFactory build() {
			return new HomoTriangleKnowledgeFactory(dimension, params);
		}

	}

}
