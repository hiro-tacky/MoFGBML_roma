package cilabo.fuzzy.classifier.pittsburgh.classification.impl;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.classifier.pittsburgh.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

/**
 * 未実装
 * (実装完了時は上のコメントを削除してください)
 * @author hirot
 *
 */
@Deprecated
public final class CFmeanClassification <pittsburghSolution extends PittsburghSolution<?>> implements Classification<pittsburghSolution>{

	@Override
	public MichiganSolution<?> classify(pittsburghSolution michiganSolutionList, Pattern<?> pattern) {
		return null;
	}

	@Override
	public Classification<pittsburghSolution> copy() {
		return new CFmeanClassification<pittsburghSolution>();
	}
}
