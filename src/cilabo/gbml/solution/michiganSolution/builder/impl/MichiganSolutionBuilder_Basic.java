package cilabo.gbml.solution.michiganSolution.builder.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Element;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.builder.RuleBuilder;
import cilabo.gbml.solution.michiganSolution.builder.MichiganSolutionBuilderCore;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Basic;

/**
 * 標準的ミシガン型識別器生成器 basic michigan solution builder
 * @author Takigawa Hiroki
 * @param <RuleClass> ミシガン型識別器が持つルールクラス rule class that michigan solution has
 */
public class MichiganSolutionBuilder_Basic<RuleClass extends Rule<?, ?>>
	extends MichiganSolutionBuilderCore<MichiganSolution_Basic<RuleClass>, RuleClass>{

	/** コンストラクタ．constructor
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列 list of upper bounds and lower bounds for each variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder*/
	public MichiganSolutionBuilder_Basic(
			List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder) {
		super(bounds, numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	/** コンストラクタ．constructor
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder*/
	public MichiganSolutionBuilder_Basic(
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder) {
		super(numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	/** コンストラクタ．constructor
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder*/
	public MichiganSolutionBuilder_Basic(MichiganSolutionBuilder_Basic<RuleClass> solutionBuilder) {
		super(solutionBuilder.numberOfObjectives,
				solutionBuilder.numberOfConstraints,
				solutionBuilder.ruleBuilder);
	}

	@Override
	public MichiganSolution_Basic<RuleClass> createMichiganSolution() {
		MichiganSolution_Basic<RuleClass> solution = new MichiganSolution_Basic<RuleClass>(
				bounds,
				this.numberOfObjectives,
				this.numberOfConstraints,
				this.ruleBuilder);
		this.initializeAttribute(solution);
		return solution;
	}

	@Override
	public MichiganSolution_Basic<RuleClass> createMichiganSolution(Element michiganSolution) {
		MichiganSolution_Basic<RuleClass> solution = new MichiganSolution_Basic<RuleClass>(
				bounds,
				this.numberOfObjectives,
				this.numberOfConstraints,
				this.ruleBuilder,
				michiganSolution);
		this.initializeAttribute(solution);
		return solution;
	}

	@Override
	public MichiganSolution_Basic<RuleClass> createMichiganSolution(Pattern<?> pattern) {
		MichiganSolution_Basic<RuleClass> solution = new MichiganSolution_Basic<RuleClass>(
				bounds,
				this.numberOfObjectives,
				this.numberOfConstraints,
				this.ruleBuilder,
				pattern);
		this.initializeAttribute(solution);
		return solution;
	}

	@Override
	public List<MichiganSolution_Basic<RuleClass>> createMichiganSolutions(int numberOfGenerateRule) {
		List<MichiganSolution_Basic<RuleClass>> returnObject = new ArrayList<MichiganSolution_Basic<RuleClass>>(numberOfGenerateRule);
		for(int i=0; i<numberOfGenerateRule; i++) {
			returnObject.add(this.createMichiganSolution());
		}
		return returnObject;
	}

	@Override
	public String toString() {
		return "MichiganSolutionBuilder_Basic [bounds=" + bounds + ", numberOfObjectives=" + numberOfObjectives
				+ ", numberOfConstraints=" + numberOfConstraints + ", ruleBuilder=" + ruleBuilder + "]";
	}

	@Override
	public MichiganSolutionBuilder_Basic<RuleClass> copy() {
		return new MichiganSolutionBuilder_Basic<RuleClass>(this);
	}
}
