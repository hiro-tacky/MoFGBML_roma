package cilabo.fuzzy.classifier.impl;

import java.util.List;
import java.util.Objects;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/** 基本的なpittsuburgh型識別器のメソッド定義クラス．
 * @author Takigawa Hiroki
 *
 * @param <michiganSolution> pittsuburgh識別器が持つmichiganSolutionクラスの型
 */
public class Classifier_basic<michiganSolution extends MichiganSolution<?>> implements Classifier<michiganSolution> {

	/** 識別方式定義クラス */
	protected Classification<michiganSolution> classification;

	/**
	 * 空のインスタンスを生成 <br> Constructs an empty instance of class
	 * @param classification 識別方式定義クラス
	 */
	public Classifier_basic(Classification<michiganSolution> classification) {
		if(Objects.isNull(classification)) {
			throw new NullPointerException("classification is null @" + this.getClass().getSimpleName());}
		this.classification = classification;
	}

	@Override
	public michiganSolution classify(List<michiganSolution> michiganSolutionList, Pattern<?> pattern) {
		return this.classification.classify(michiganSolutionList, pattern);
	}

	@Override
	public Classifier<michiganSolution> copy(){
		return new Classifier_basic<michiganSolution>(this.classification.copy());
	}

	@Override
	public int getRuleLength(List<michiganSolution> michiganSolutionList) {
		int length = 0;
		for(int i = 0; i < michiganSolutionList.size(); i++) {
			//michiganSolutionListが持つルールオブジェクトを呼び出し，ルール長を加算する．
			length += michiganSolutionList.get(i).getRuleLength();
		}
		return length;
	}

	@Override
	public int getRuleNum(List<michiganSolution> michiganSolutionList) {
		return michiganSolutionList.size();
	}

}
