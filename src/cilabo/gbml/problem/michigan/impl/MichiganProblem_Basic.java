package cilabo.gbml.problem.michigan.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.classifier.michigan.classification.Classification;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.builder.RuleBuilder;
import cilabo.gbml.problem.michigan.AbstractMichiganProblem;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Basic;

/**
 * Michigan型識別器による標準的識別問題クラス basic classification problem class with michigan solution
 * @author Takigawa Hiroki
 *
 * @param <RuleClass> Michigan型識別器が持つルールクラス rule class that michigan solution has
 */
public final class MichiganProblem_Basic <RuleClass extends Rule<?, ?>>
	extends AbstractMichiganProblem <MichiganSolution_Basic<RuleClass>, RuleClass>{

	/** 目的関数の個数 number of objectives */
	private static final int numberOfObjectives = 1;
	/** 制約の個数 number of constraints */
	private static final int numberOfConstraints = 0;
	/** 誤識別パターンに対するペナルティ係数 */
	private double penaltyDegree;

	/** コンストラクタ．constructor
	 * @param train 識別されるデータセット data set to be classified
	 * @param ruleBuilder ルール生成器．rule builder
	 * @param classification 勝者ルール決定クラス mthods to define decide a single winner solution
	 */
	public MichiganProblem_Basic(
			double penaltyDegree,
			DataSet<?> train,
			RuleBuilder<RuleClass> ruleBuilder,
			Classification<MichiganSolution_Basic<RuleClass>> classification) {
		this(penaltyDegree,
				train,
				AbstractMichiganSolution.makeBounds(),
				ruleBuilder,
				classification);
	}

	/** コンストラクタ．constructor
	 * @param train 識別されるデータセット data set to be classified
	 * @param bounds 前件部の遺伝子の下限値と上限値のペアの配列 list of upper bounds and lower bounds for each variables
	 * @param ruleBuilder ルール生成器．rule builder
	 * @param classification 勝者ルール決定クラス mthods to define decide a single winner solution
	 */
	public MichiganProblem_Basic(
			double penaltyDegree,
			DataSet<?> train,
			List<Pair<Integer, Integer>> bounds,
			RuleBuilder<RuleClass> ruleBuilder,
			Classification<MichiganSolution_Basic<RuleClass>> classification) {
		super(MichiganProblem_Basic.numberOfObjectives,
				MichiganProblem_Basic.numberOfConstraints,
				train,
				bounds,
				ruleBuilder,
				classification);
		this.penaltyDegree = penaltyDegree;
		this.setName("MichiganClassificationProblem_Basic");
	}

	@Override
	public void evaluate(MichiganSolution_Basic<RuleClass> solution) {
		List<Integer> classifiedPatternIDList = (List<Integer>) solution.getAttribute(this.selectedWinnerSolutionByPattern.getAttributeId());

		double fitness = 0;
		for(int patternID_i: classifiedPatternIDList) {
			Pattern<?> pattern = this.getTrain().getPattern(patternID_i);
			if(pattern.getTargetClass().equals(solution.getRule().getConsequent().getClassLabel())) {
				fitness += 1;
			}else {
				fitness -= 1*penaltyDegree;
			}
		}
		solution.setObjective(0, fitness);
	}

	@Override
	public MichiganSolution_Basic<RuleClass> createSolution() {
		MichiganSolution_Basic<RuleClass> michiganSolution =
			new MichiganSolution_Basic<RuleClass>(
				this.bounds,
				this.getNumberOfObjectives(),
				this.getNumberOfConstraints(),
				this.ruleBuilder
			);
		michiganSolution.setAttribute(this.selectedWinnerSolutionByPattern.getAttributeId(), new ArrayList<Integer>());
		return michiganSolution;
	}

}
