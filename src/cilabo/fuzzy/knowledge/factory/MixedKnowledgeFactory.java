package cilabo.fuzzy.knowledge.factory;

import java.util.ArrayList;

import org.uma.jmetal.util.checking.Check;

import cilabo.fuzzy.knowledge.FuzzySet;
import cilabo.fuzzy.knowledge.FuzzySetBluePrintManager;
import cilabo.fuzzy.knowledge.FuzzySetBluePrintManager.FuzzySetBluePrint;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;

/**
 * 様々な形状のファジィ集合によるKnowledgeBaseを生成する．<br>
 * Generate various-division amd various-shape fuzzy sets
 * @author Takigawa Hiroki
 */
public class MixedKnowledgeFactory {

	/** Number of features */
	int numberOfDimension;
	/** Parameters of membership functions */
	Parameters parameters;

	/**
	 * コンストラクタ．Initialize MixedKnowledgeFactory by Parameters class
	 * @param parameters ファジィ集合パラメータクラス Parameters of membership functions
	 */
	public MixedKnowledgeFactory(Parameters parameters) {
		Check.isNotNull(parameters);
		this.parameters = parameters;
		this.numberOfDimension = parameters.getNumberOfDimension();
	}

	/**
	 * ファジィ集合設計図を基にKnowledgeBaseを生成する<br>
	 * Create Knowledge Base by given fuzzy set blue print
	 * @param fuzzySetBluePrintManager ファジィ集合設計図管理クラス this class has fuzzy set blue prints
	 */
	public void create(FuzzySetBluePrintManager fuzzySetBluePrintManager) {
		// make fuzzy sets
		FuzzySet[][] fuzzySets = new FuzzySet[this.numberOfDimension][];

		for(int dim_i=0; dim_i<this.numberOfDimension; dim_i++) {
			fuzzySets[dim_i] = new FuzzySet[fuzzySetBluePrintManager.getNumberOfFuzzySets(dim_i)+1];
			//Don't care をセット
			fuzzySets[dim_i][Knowledge.DnotCare_FuzzySetIndex] = Knowledge.getInstance().makeDontCare();

			//設計図読み込み
			ArrayList<FuzzySetBluePrint> fuzzyTermBP = fuzzySetBluePrintManager.getFuzzySetBluePrint(dim_i);
			Check.isNotNull(fuzzyTermBP);

			int cnt = 1;
			//設計図を基にFuzzyTermTypeを生成
			for(FuzzySetBluePrint FuzzyTermBP_i : fuzzyTermBP) {
				float[][] param = parameters.getParameter(FuzzyTermBP_i);
				for(int k_i=0; k_i<FuzzyTermBP_i.getK(); k_i++) {
					String name = Knowledge.makeFuzzyTermName(FuzzyTermBP_i, cnt);
					fuzzySets[dim_i][cnt] = new FuzzySet(name, FuzzyTermBP_i, param[k_i], k_i);
					cnt++;
				}
			}
		}

		// Create
		Knowledge knowledge = Knowledge.getInstance();
		knowledge.setFuzzySets(fuzzySets);

		return;
	}
}
