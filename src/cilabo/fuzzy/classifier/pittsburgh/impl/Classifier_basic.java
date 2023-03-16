package cilabo.fuzzy.classifier.pittsburgh.impl;

import org.uma.jmetal.util.checking.Check;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.classifier.pittsburgh.Classifier;
import cilabo.fuzzy.classifier.pittsburgh.classification.Classification;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

/**
 * pittsburgh型識別器のメソッド定義クラス．
 * this calss define basic methods for pittsburgh classifier.
 * @author Takigawa Hiroki
 *
 * @param <PittsburghSolutionClass> このクラスが扱うpittsuburgh型識別器
 */
public final class Classifier_basic
		<PittsburghSolutionClass extends PittsburghSolution<MichiganSolutionClass>,
		MichiganSolutionClass extends MichiganSolution<?>>
		implements Classifier<PittsburghSolutionClass, MichiganSolutionClass> {

	/** 識別方式定義クラス method to decide a single winner rule*/
	protected Classification<PittsburghSolutionClass, MichiganSolutionClass> classification;

	/**
	 * コンストラクタ．Initialize Classifier_basic
	 * @param classification 識別方式定義クラス method to decide a single winner rule
	 */
	public Classifier_basic(Classification<PittsburghSolutionClass, MichiganSolutionClass> classification) {
		Check.isNotNull(classification);
		this.classification = classification;
	}

	@Override
	public MichiganSolutionClass classify(PittsburghSolutionClass pittsburghSolution, Pattern<?> pattern) {
		return this.classification.classify(pittsburghSolution, pattern);
	}

	@Override
	public int getRuleLength(PittsburghSolutionClass pittsburghSolution) {
		int length = 0;
		for(int i = 0; i < pittsburghSolution.getNumberOfVariables(); i++) {
			//pittsburghSolutionが持つルールオブジェクトを呼び出し，ルール長を加算する．
			length += pittsburghSolution.getVariable(i).getRuleLength();
		}
		return length;
	}

	@Override
	public int getNumberOfRule(PittsburghSolutionClass pittsburghSolution) {
		return pittsburghSolution.getNumberOfVariables();
	}

	@Override
	public Classifier<PittsburghSolutionClass, MichiganSolutionClass> copy(){
		return new Classifier_basic<PittsburghSolutionClass, MichiganSolutionClass>(this.classification.copy());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "+" + this.classification.toString();
	}
}
