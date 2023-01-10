package cilabo.fuzzy.knowledge.factory;

import java.util.ArrayList;

import cilabo.fuzzy.knowledge.FuzzyTermBluePrintManager;
import cilabo.fuzzy.knowledge.FuzzyTermBluePrintManager.FuzzyTermsBluePrint;
import cilabo.fuzzy.knowledge.FuzzyTermTypeForMixed;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;

public class MixedKnowledgeFactory {

	/** Number of features */
	int dimension;

	/** Parameters of membership functions */
	Parameters parameters;

	/**コンストラクタ
	 *
	 * @param dimension 次元数
	 * @param params 分割区間のパラメータ
	 */
	public MixedKnowledgeFactory(int dimension, Parameters parameters) {
		this.dimension = dimension;
		this.parameters = parameters;
	}

	public void create(FuzzyTermBluePrintManager FuzzyTermBPM) {
		// make fuzzy sets
		FuzzyTermTypeForMixed[][] fuzzySets = new FuzzyTermTypeForMixed[this.dimension][];

		for(int dim_i=0; dim_i<this.dimension; dim_i++) {
			fuzzySets[dim_i] = new FuzzyTermTypeForMixed[FuzzyTermBPM.getfuzyyTermsNum(dim_i)+1];
			//Don't care
			fuzzySets[dim_i][0] = new FuzzyTermTypeForMixed(
					Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, Knowledge.DnotCare_FuzzyTermID),
					FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f}, DIVISION_TYPE.equalDivision, 0, 0);

			ArrayList<FuzzyTermsBluePrint> FuzzyTermBP = FuzzyTermBPM.getFuzzyTermsBluePrint(dim_i);
			int cnt = 1;
			for(FuzzyTermsBluePrint FuzzyTermBP_i : FuzzyTermBP) {
				float[][] param = parameters.getParameter(FuzzyTermBP_i.getDivisionType(), dim_i, FuzzyTermBP_i.getK(), FuzzyTermBP_i.getFuzzyTermType());
				for(int k_i=0; k_i<FuzzyTermBP_i.getK(); k_i++) {
					String name = Knowledge.makeFuzzyTermName(FuzzyTermBP_i.getDivisionType(), FuzzyTermBP_i.getFuzzyTermType(), cnt);
					fuzzySets[dim_i][cnt] = new FuzzyTermTypeForMixed(name, FuzzyTermBP_i.getFuzzyTermType(), param[k_i], FuzzyTermBP_i.getDivisionType(), FuzzyTermBP_i.getK(), k_i);
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
