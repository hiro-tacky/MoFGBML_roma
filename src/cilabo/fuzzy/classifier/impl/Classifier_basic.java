package cilabo.fuzzy.classifier.impl;

import java.util.List;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/**ルールベース識別器
 * @author hirot
 */
public class Classifier_basic<michiganSolution extends MichiganSolution<?>> implements Classifier<michiganSolution> {

	protected Classification<michiganSolution> classification;

	/** 空のインスタンスを生成します <br> Constructs an empty instance of class */
	public Classifier_basic(Classification<michiganSolution> classification) {
		this.classification = classification;
	}

	@Override
	public michiganSolution classify(List<michiganSolution> michiganSolutionList, InputVector vector) {
		return this.classification.classify(michiganSolutionList, vector);
	}

	@Override
	public Classifier<michiganSolution> copy(){
		return new Classifier_basic<michiganSolution>(this.classification.copy());
	}

	@Override
	public int getRuleLength(List<michiganSolution> michiganSolutionList) {
		int length = 0;
		for(int i = 0; i < michiganSolutionList.size(); i++) {
			length += michiganSolutionList.get(i).getRuleLength();
		}
		return length;
	}

	@Override
	public int getRuleNum(List<michiganSolution> michiganSolutionList) {
		return michiganSolutionList.size();
	}

}
