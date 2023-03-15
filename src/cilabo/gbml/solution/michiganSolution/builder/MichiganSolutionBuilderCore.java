package cilabo.gbml.solution.michiganSolution.builder;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.builder.RuleBuilder;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.util.attribute.NumberOfClassifiedPatterns;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;

/**
 * ミシガン型識別器生成器メソッド群 this class defines basic michigan solution builder methods
 * @author Takigawa Hiroki
 *
 * @param <MichiganSolutionClass> 生成されるミシガン型識別器 michigan solution to generate
 * @param <RuleClass> ミシガン型識別器が持つルールクラス rule class that michigan solution has
 */
public abstract class MichiganSolutionBuilderCore <
		MichiganSolutionClass extends MichiganSolution<RuleClass>,
		RuleClass extends Rule<?, ?>>
		implements MichiganSolutionBuilder<MichiganSolutionClass>{

	/** 各遺伝子が取りうる値の上限値と下限値の配列 list of upper bounds and lower bounds for each variables */
	protected List<Pair<Integer, Integer>> bounds;
	/** 目的関数の個数 number of objectives */
	protected int numberOfObjectives;
	/** 制約の個数 number of constraints */
	protected int numberOfConstraints;
	/** ルール生成器．rule builder */
	protected RuleBuilder<RuleClass> ruleBuilder;
	/** 勝利回数属性ID attribute ID for number of winner*/
	protected String attributeIdForNumberOfWinner = new NumberOfWinner<>().getAttributeId();
	/** 識別パターン数属性ID attribute ID for number of classified patterns*/
	protected String attributeIdForNumberOfClassifiedPatterns = new NumberOfClassifiedPatterns<>().getAttributeId();

	/** コンストラクタ．constructor
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列 list of upper bounds and lower bounds for each variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder*/
	public MichiganSolutionBuilderCore(
			List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder) {
		this.bounds = bounds;
		this.numberOfObjectives = numberOfObjectives;
		this.numberOfConstraints = numberOfConstraints;
		this.ruleBuilder = ruleBuilder;
	}

	/** コンストラクタ．constructor
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder*/
	public MichiganSolutionBuilderCore(
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder) {
		this.bounds = AbstractMichiganSolution.makeBounds();
		this.numberOfObjectives = numberOfObjectives;
		this.numberOfConstraints = numberOfConstraints;
		this.ruleBuilder = ruleBuilder;
	}

	@Override
	public void initializeAttribute(MichiganSolution<?> solution) {
		solution.setAttribute(attributeIdForNumberOfWinner, 0);
		solution.setAttribute(attributeIdForNumberOfClassifiedPatterns, 0);
	}
}
