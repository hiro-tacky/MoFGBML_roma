package cilabo.gbml.problem.pittsburghFGBML_Problem;

import org.uma.jmetal.problem.AbstractGenericProblem;
import org.uma.jmetal.problem.Problem;

import cilabo.data.DataSet;
import cilabo.fuzzy.classifier.pittsburgh.Classifier;
import cilabo.gbml.objectivefunction.pittsburgh.NumberOfRules;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.builder.MichiganSolutionBuilder;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;
import cilabo.main.ExperienceParameter.OBJECTIVES_FOR_PITTSBURGH;

public abstract class AbstractPittsburghProblem
		<PittsburghSolutionClass extends PittsburghSolution<MichiganSolutionClass>,
		MichiganSolutionClass extends MichiganSolution<?>>
		extends AbstractGenericProblem <PittsburghSolutionClass>
		implements Problem<PittsburghSolutionClass>{

	/** 識別されるデータセット data set to be classified */
	protected DataSet<?> train;
	/** Michigan型識別器ビルダー michigan solution builder */
	protected MichiganSolutionBuilder<MichiganSolutionClass> michiganSolutionBuilder;
	/** pittsuburgh型識別器のメソッドクラス．methods for pittsuburgh classifier.
	 * @see cilabo.fuzzy.classifier.classification */
	protected Classifier<PittsburghSolutionClass, MichiganSolutionClass> classifier;

	protected NumberOfWinner<PittsburghSolutionClass> numberOfWinner;

	/** コンストラクタ．constructor
	 * @param numberOfVariables 遺伝子変数の個数 number of variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param train 識別されるデータセット data set to be classified
	 * @param michiganSolutionBuilder Michigan型識別器ビルダー michigan solution builder
	 * @param classifier pittsuburgh型識別器のメソッドクラス．methods for pittsuburgh classifier
	 */
	public AbstractPittsburghProblem(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet<?> train,
			MichiganSolutionBuilder<MichiganSolutionClass> michiganSolutionBuilder,
			Classifier<PittsburghSolutionClass,MichiganSolutionClass> classifier) {
		setNumberOfVariables(numberOfVariables);
		setNumberOfObjectives(numberOfObjectives);
		setNumberOfConstraints(numberOfConstraints);
		this.train = train;
		this.michiganSolutionBuilder = michiganSolutionBuilder;
		this.classifier = classifier;
		this.numberOfWinner = new NumberOfWinner<PittsburghSolutionClass>();
	}

	public void removeNoWinnerMichiganSolution(PittsburghSolutionClass solution) {
		if(solution.getNumberOfVariables() == 0) {throw new ArithmeticException("Given pittsburghSolution has no michigan solution");}

		for(int i=0; i<solution.getNumberOfVariables(); i++) {
			if((int)solution.getVariable(i).getAttribute(numberOfWinner.getAttributeId()) < 1) {
				solution.removeVariable(i); i--;
			}
		}

		NumberOfRules<PittsburghSolutionClass> objective =
				new NumberOfRules<PittsburghSolutionClass>(
						OBJECTIVES_FOR_PITTSBURGH.NumberOfRule.toInt());
		double f2 = objective.calculateObjective(solution);
		solution.setObjective(OBJECTIVES_FOR_PITTSBURGH.NumberOfRule.toInt(), f2);

		if(solution.getNumberOfVariables() == 0) {
			throw new ArithmeticException("Given pittsburghSolution has no winner michigan solution");
		}
	}
	/**
	 * このインスタンスが持つデータセットを返します。<br>
	 * Returns data set that this instance has.	 *
	 * @return 返されるデータセット．data set to return
	 */
	public DataSet<?> getTrain() {
		return train;
	}

	/**
	 * データセットを入力されたデータセットで置き換えます。<br>
	 * Replaces data set in this instance.	 *
	 * @param train このインスタンスに格納されるデータセット．data set to be stored at this instance
	 */
	public void setTrain(DataSet<?> train) {
		this.train = train;
	}

	/**
	 * このインスタンスが持つMichigan型識別器ビルダーを返します。<br>
	 * Returns michigan solution builder that this instance has.
	 * @return 返されるMichigan型識別器ビルダー．michigan solution builder to return
	 */
	public MichiganSolutionBuilder<MichiganSolutionClass> getMichiganSolutionBuilder() {
		return michiganSolutionBuilder;
	}

	/**
	 * Michigan型識別器ビルダーを入力されたMichigan型識別器ビルダーで置き換えます。<br>
	 * Replaces michigan solution builder in this instance.
	 * @param michiganSolutionBuilder このインスタンスに格納されるMichigan型識別器ビルダー．michigan solution builder to be stored at this instance
	 */
	public void setMichiganSolutionBuilder(MichiganSolutionBuilder<MichiganSolutionClass> michiganSolutionBuilder) {
		this.michiganSolutionBuilder = michiganSolutionBuilder;
	}

	/**
	 * このインスタンスが持つpittsuburgh型識別器のメソッドクラスを返します。<br>
	 * Returns methods for pittsuburgh classifier that this instance has.
	 * @return 返されるpittsuburgh型識別器のメソッドクラス．methods for pittsuburgh classifier to return
	 */
	public Classifier<PittsburghSolutionClass, MichiganSolutionClass> getClassifier() {
		return classifier;
	}

	/**
	 * pittsuburgh型識別器のメソッドクラスを入力されたpittsuburgh型識別器のメソッドクラスで置き換えます。<br>
	 * Replaces methods for pittsuburgh classifier in this instance.
	 * @param classifier このインスタンスに格納されるpittsuburgh型識別器のメソッドクラス．methods for pittsuburgh classifier to be stored at this instance
	 */
	public void setClassifier(Classifier<PittsburghSolutionClass, MichiganSolutionClass> classifier) {
		this.classifier = classifier;
	}
}
