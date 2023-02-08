package cilabo.fuzzy.knowledge.factory;

import java.util.ArrayList;
import java.util.Objects;

import cilabo.fuzzy.knowledge.FuzzyTermBluePrintManager;
import cilabo.fuzzy.knowledge.FuzzyTermBluePrintManager.FuzzyTermsBluePrint;
import cilabo.fuzzy.knowledge.FuzzyTermTypeForMixed;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;

/**
 * 多種混合分割条件部によるによるKnowledgeBaseを生成する．
 * @author Takigawa Hiroki
 */
public class MixedKnowledgeFactory {

	/** Number of features */
	int dimension;

	/** Parameters of membership functions */
	Parameters parameters;

	/**
	 * インスタンスを生成
	 * @param parameters 分割区間のパラメータ
	 */
	public MixedKnowledgeFactory(Parameters parameters) {
		if(Objects.isNull(parameters)) {
			throw new IllegalArgumentException("argument [parameters] is null @MixedKnowledgeFactory.MixedKnowledgeFactory");
		}
		this.parameters = parameters;
		this.dimension = parameters.getNumberOfDimension();
	}

	/**
	 * 設計図を基にKnowledgeBaseを生成する
	 * @param FuzzyTermBPM 生成するFuzzyTermType設計図
	 */
	public void create(FuzzyTermBluePrintManager FuzzyTermBPM) {
		if(Objects.isNull(parameters)) {
			throw new IllegalArgumentException("argument [FuzzyTermBPM] is null @" + this.getClass().getSimpleName());
		}
		// make fuzzy sets
		FuzzyTermTypeForMixed[][] fuzzySets = new FuzzyTermTypeForMixed[this.dimension][];

		for(int dim_i=0; dim_i<this.dimension; dim_i++) {
			fuzzySets[dim_i] = new FuzzyTermTypeForMixed[FuzzyTermBPM.getfuzyyTermsNum(dim_i)+1];
			//Don't care をセット
			fuzzySets[dim_i][0] = Knowledge.getInstance().makeDontCare();

			//設計図読み込み
			ArrayList<FuzzyTermsBluePrint> FuzzyTermBP = FuzzyTermBPM.getFuzzyTermsBluePrint(dim_i);
			if(Objects.isNull(FuzzyTermBP)) {
				throw new IllegalArgumentException("FuzzyTermsBluePrint of FuzzyTermBPM is null@" + this.getClass().getSimpleName());
			}

			int cnt = 1;
			//設計図を基にFuzzyTermTypeを生成
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
