package cilabo.fuzzy.classifier.pittsburgh;

import cilabo.fuzzy.classifier.pittsburgh.classification.Classification;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

/** pittsuburgh型識別器のメソッドクラス．識別を行う為のClassificationを持つ．
 * this class defines methods for pittsuburgh classifier.
 * @author Takigawa Hiroki
 * @param <PittsburghSolutionClass> このクラスが扱うpittsuburgh型識別器
 */
public interface Classifier <PittsburghSolutionClass extends PittsburghSolution<?>>
		extends Classification<PittsburghSolutionClass>{

	/**
	 * 与えられたpittsuburgh型識別器のルール長を返します。<br>
	 * Returns rule length of given pittsburgh solution.
	 * @param pittsburghSolution pittsuburgh型識別器. pittsuburgh classifier
	 * @return ルール長．rule length that given pittsuburgh classifier has
	 */
	public int getRuleLength(PittsburghSolutionClass pittsburghSolution);

	 /**
	 * 与えられたpittsuburgh型識別器のルール数を返します。<br>
	 * Returns number of rule that given pittsburgh solution has.
	 * @param pittsburghSolution pittsuburgh型識別器. pittsuburgh classifier
	 * @return ルール数．number of rule that given pittsuburgh classifier has
	 */
	public int getNumberOfRule(PittsburghSolutionClass pittsburghSolution);

	@Override
	public Classifier<PittsburghSolutionClass> copy();

	@Override
	public String toString();
}
