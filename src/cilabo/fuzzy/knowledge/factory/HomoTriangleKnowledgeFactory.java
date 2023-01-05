package cilabo.fuzzy.knowledge.factory;

import cilabo.fuzzy.knowledge.FuzzyTermTypeForMixed;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;

/**等分割三角形型ファジィのKBを生成
 * @author hirot
 *
 */
public class HomoTriangleKnowledgeFactory{

	/** Number of features */
	int dimension;

	/** Parameters of membership functions */
	Parameters parameters;

	/**コンストラクタ
	 *
	 * @param dimension 次元数
	 * @param params 分割区間のパラメータ
	 */
	public HomoTriangleKnowledgeFactory(int dimension, Parameters parameters) {
		this.dimension = dimension;
		this.parameters = parameters;
	}

	public void create(int[][] K) {

		// make fuzzy sets
		FuzzyTermTypeForMixed[][] fuzzySets = new FuzzyTermTypeForMixed[dimension][];
		int shapeType = FuzzyTermType.TYPE_triangularShape;
		for(int dim_i = 0; dim_i < dimension; dim_i++) {
			int len=0; for(int k_i: K[dim_i]) {len += k_i;}
			fuzzySets[dim_i] = new FuzzyTermTypeForMixed[len + 1];

			//Don't care
			fuzzySets[dim_i][0] = new FuzzyTermTypeForMixed(Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, 99),
												FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f},
												DIVISION_TYPE.equalDivision, 0, 0);
			for(int j=1, cnt=1; j < K[dim_i].length + 1; j++) {
				float[][] param = parameters.triangle(DIVISION_TYPE.equalDivision, dim_i, K[dim_i][j-1]);
				for(int k_i=0; k_i<K[dim_i][j-1]; k_i++) {
					String name = Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, shapeType, cnt);
					fuzzySets[dim_i][cnt] = new FuzzyTermTypeForMixed(name, shapeType, param[k_i], DIVISION_TYPE.equalDivision, K[dim_i][j-1], k_i);
					cnt++;
				}
			}
		}

		// Create
		Knowledge knowledge = Knowledge.getInstance();
		knowledge.setFuzzySets(fuzzySets);

		return;
	}

	public void create() {
		int[][] tmp = new int[this.dimension][];
		for(int i=0; i<this.dimension; i++) {
			tmp[i] = new int[]{2, 3, 4, 5};
		}
		this.create(tmp);
		return;
	}
}
