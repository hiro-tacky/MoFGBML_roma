package cilabo.fuzzy.classifier.classification.impl;

import java.util.List;

import cilabo.data.AttributeVector;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/**
 * 未実装
 * (実装完了時は上のコメントを削除してください)
 * @author hirot
 *
 */
@Deprecated
public final class CFmeanClassification<michiganSolution extends MichiganSolution<?>> implements Classification<michiganSolution>{

	@Override
	public michiganSolution classify(List<michiganSolution> michiganSolutionList, AttributeVector vector) {
		return null;
	}

	@Override
	public Classification<michiganSolution> copy() {
		return new CFmeanClassification<michiganSolution>();
	}
}