package cilabo.gbml.problem.michiganFGBML_Problem;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.problem.AbstractGenericProblem;
import org.uma.jmetal.problem.BoundedProblem;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.classifier.michigan.classification.Classification;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.builder.RuleBuilder;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.util.attribute.michigan.SelectedWinnerSolutionByPattern;

/**
 * Michigan型識別器による識別問題抽象クラス classification problem class with michigan solution
 * @author Takigawa Hiroki
 *
 * @param <MichiganSolutionClass> Michigan型識別器 michigan solution to classify
 * @param <RuleClass> Michigan型識別器が持つルールクラス rule class that michigan solution has
 */
public abstract class AbstractMichiganProblem <
		MichiganSolutionClass extends MichiganSolution<RuleClass>,
		RuleClass extends Rule<?, ?>>
		extends AbstractGenericProblem <MichiganSolutionClass>
		implements BoundedProblem<Integer, MichiganSolutionClass>{

	/** 識別されるデータセット data set to be classified */
	protected DataSet<?> train;
	/** 前件部の遺伝子が持つインデックスの下限値と上限値のペアの配列 list of upper bounds and lower bounds for each variables */
	protected List<Pair<Integer, Integer>> bounds;
	/** ルールクラスビルダー rule class builder */
	protected RuleBuilder<RuleClass> ruleBuilder;
	/** 勝者ルールを決定する手法を定義するClassificationクラス this class define method to decide a single winner rule */
	protected Classification<MichiganSolutionClass> classification;
	/** 勝利ルールとなった際のパターンを保持するAttribute */
	protected SelectedWinnerSolutionByPattern<MichiganSolutionClass> selectedWinnerSolutionByPattern;

	/** コンストラクタ．constructor
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param train 識別されるデータセット data set to be classified
	 * @param bounds 前件部の遺伝子の下限値と上限値のペアの配列 list of upper bounds and lower bounds for each variables
	 * @param ruleBuilder ルール生成器．rule builder
	 * @param classification 勝者ルール決定クラス mthods to define decide a single winner solution
	 */
	public AbstractMichiganProblem(
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet<?> train,
			List<Pair<Integer, Integer>> bounds,
			RuleBuilder<RuleClass> ruleBuilder,
			Classification<MichiganSolutionClass> classification) {
		setNumberOfVariables(bounds.size());
		setNumberOfObjectives(numberOfObjectives);
		setNumberOfConstraints(numberOfConstraints);

		this.train = train;
		this.bounds = bounds;
		this.ruleBuilder = ruleBuilder;
		this.classification = classification;
		this.selectedWinnerSolutionByPattern = new SelectedWinnerSolutionByPattern<MichiganSolutionClass>();
	}

	/**
	 * 個体群から単一勝利個体を決定する decide single winner solution
	 * @param michiganSolutionList Michigan型識別器個体群 list of michigan solution
	 */
	public void selectWinnerSolution(List<MichiganSolutionClass> michiganSolutionList) {
		SelectedWinnerSolutionByPattern<MichiganSolutionClass> attribute =
				new SelectedWinnerSolutionByPattern<MichiganSolutionClass>();
		for(Pattern<?> pattern: this.getTrain().getPatterns()) {
			MichiganSolutionClass michiganSolution = this.classification.classify(michiganSolutionList, pattern);
			List<Integer> classifiedPatternIDList = (List<Integer>) michiganSolution.getAttribute(attribute.getAttributeId());
			classifiedPatternIDList.add(pattern.getID());
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
	 * このインスタンスが持つ前件部の遺伝子の下限値と上限値のペアの配列を返します。<br>
	 * Returns list of upper bounds and lower bounds for each variables that this instance has.
	 * @param 返される前件部の遺伝子の下限値と上限値のペアの配列．list of upper bounds and lower bounds for each variables to return
	 * @see org.uma.jmetal.problem.BoundedProblem#getBounds()
	 */
	@Override
	public List<Pair<Integer, Integer>> getBounds() {
		return bounds;
	}

	/**
	 * 前件部の遺伝子の下限値と上限値のペアの配列を入力された前件部の遺伝子の下限値と上限値のペアの配列で置き換えます。<br>
	 * Replaces list of upper bounds and lower bounds for each variables in this instance.
	 * @param bounds このインスタンスに格納される前件部の遺伝子の下限値と上限値のペアの配列．list of upper bounds and lower bounds for each variables to be stored at this instance
	 */
	public void setBounds(List<Pair<Integer, Integer>> bounds) {
		this.bounds = bounds;
	}

	@Override
	public Integer getLowerBound(int index) {
		return this.bounds.get(index).getLeft();
	}

	@Override
	public Integer getUpperBound(int index) {
		return this.bounds.get(index).getRight();
	}

	/**
	 * このインスタンスが持つルール生成器を返します。<br>
	 * Returns rule builder that this instance has.
	 * @return 返されるルール生成器．rule builder to return
	 */
	public RuleBuilder<RuleClass> getRuleBuilder() {
		return ruleBuilder;
	}

	/**
	 * ルール生成器を入力されたルール生成器で置き換えます。<br>
	 * Replaces rule builder in this instance.	 *
	 * @param ruleBuilder このインスタンスに格納されるルール生成器．rule builder to be stored at this instance
	 */
	public void setRuleBuilder(RuleBuilder<RuleClass> ruleBuilder) {
		this.ruleBuilder = ruleBuilder;
	}
}
