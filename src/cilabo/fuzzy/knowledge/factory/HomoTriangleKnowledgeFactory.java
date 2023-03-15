package cilabo.fuzzy.knowledge.factory;

import org.uma.jmetal.util.checking.Check;

import cilabo.fuzzy.knowledge.FuzzySet;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import cilabo.utility.GeneralFunctions;
import jfml.term.FuzzyTermType;

/**
 * 三角形型のファジィ集合によるKnowledgeBaseを生成する．<br>
 * Crate equal-division triangle fuzzy sets
 * @author Takigawa Hiroki
 */
public class HomoTriangleKnowledgeFactory{

	/** Number of features */
	int numberOfDimension;
	/** Parameters of membership functions */
	Parameters parameters;

	/**
	 * コンストラクタ．Initialize HomoTriangleKnowledgeFactory by Parameters class
	 * @param parameters ファジィ集合パラメータクラス Parameters of membership functions
	 */
	public HomoTriangleKnowledgeFactory(Parameters parameters) {
		Check.isNotNull(parameters);
		this.parameters = parameters;
		this.numberOfDimension = parameters.getNumberOfDimension();
	}

	/**
	 * 入力された分割数の三角形型のファジィ集合から構成されるKnowledgeBaseを生成<br>
	 * Crate equal-division triangle fuzzy sets by given number of partition
	 * @param K 分割数配列[次元][分割数] number of partition [dimension][number of dimension]
	 */
	public void create(int[][] K) {

		// make fuzzy sets
		FuzzySet[][] fuzzySets = new FuzzySet[numberOfDimension][];
		int shapeType = FuzzyTermType.TYPE_triangularShape;
		for(int dim_i = 0; dim_i < numberOfDimension; dim_i++) {
			int len = GeneralFunctions.sumOfArray(K[dim_i]);
			fuzzySets[dim_i] = new FuzzySet[len + 1];

			//Don't care を追加
			fuzzySets[dim_i][Knowledge.DnotCare_FuzzySetIndex] = Knowledge.getInstance().makeDontCare();

			for(int j=0, cnt=1; j < K[dim_i].length; j++) {
				float[][] param = parameters.triangle(dim_i, DIVISION_TYPE.equalDivision, K[dim_i][j]);
				for(int k_i=0; k_i<K[dim_i][j]; k_i++) {
					String name = Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, shapeType, cnt);
					fuzzySets[dim_i][cnt] = new FuzzySet(name, shapeType, param[k_i], DIVISION_TYPE.equalDivision, K[dim_i][j], k_i);
					cnt++;
				}
			}
		}

		// Create
		Knowledge knowledge = Knowledge.getInstance();
		knowledge.setFuzzySets(fuzzySets);

		return;
	}


	/** 2-5分割分割三角形型ファジィのKBを生成<br>
	 Create 2-5 divided triangle fuzzy sets */
	public void create2_3_4_5() {
		int[][] tmp = new int[this.numberOfDimension][];
		for(int i=0; i<this.numberOfDimension; i++) {
			tmp[i] = new int[]{2, 3, 4, 5};
		}
		this.create(tmp);
		return;
	}
}
