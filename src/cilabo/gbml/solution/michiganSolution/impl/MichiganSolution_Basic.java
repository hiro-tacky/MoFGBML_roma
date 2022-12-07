package cilabo.gbml.solution.michiganSolution.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.util.attribute.NumberOfWinner;

/**基本的な機能を持つMichigan型識別器
 * @author Takigawa Hiroki
 *
 * @param <RuleObject> このMichiganSolutionクラスが持つRuleクラス
 */
public final class MichiganSolution_Basic<RuleObject extends Rule> extends AbstractMichiganSolution<RuleObject> implements IntegerSolution {

	/** コンストラクタ
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列
	 * @param numberOfObjectives 目的関数の個数
	 * @param numberOfConstraints 制約の個数
	 * @param ruleBuilder ルール生成器．
	 */
	public MichiganSolution_Basic(List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleObject> ruleBuilder) {
		super(bounds, numberOfObjectives, numberOfConstraints, ruleBuilder);
		int[] variables = ruleBuilder.createAntecedentIndex();
		this.setVariables(variables);
		this.learning();
	}

	/** コンストラクタ
	 * @param numberOfObjectives 目的関数の個数
	 * @param numberOfConstraints 制約の個数
	 * @param ruleBuilder ルール生成器．*/
	public MichiganSolution_Basic(int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleObject> ruleBuilder) {
		this(AbstractMichiganSolution.makeBounds(), numberOfObjectives, numberOfConstraints, ruleBuilder);
	}
	
	/** コピーコンストラクタ
	 * @param solution コピー元となるインスタンス */
	public MichiganSolution_Basic(MichiganSolution_Basic<RuleObject> solution) {
	    super(solution.bounds, solution.getNumberOfObjectives(), solution.getNumberOfConstraints(),
	    		solution.ruleBuilder);

	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	      setVariable(i, solution.getVariable(i));
	    }

	    for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
	      setObjective(i, solution.getObjective(i));
	    }

	    for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
	      setConstraint(i, solution.getConstraint(i));
	    }

	    this.attributes = new HashMap<>(solution.attributes);
	    this.rule = (RuleObject) solution.rule.copy();
	}

	@Override
	public MichiganSolution_Basic<RuleObject> copy() {
		return new MichiganSolution_Basic<RuleObject>(this);
	}

	public static class MichiganSolutionBuilder_Basic<RuleObject extends Rule>
		extends AbstractMichiganSolutionBuilder <MichiganSolution_Basic<RuleObject>, RuleObject>{

		public MichiganSolutionBuilder_Basic(
				List<Pair<Integer, Integer>> bounds,
				int numberOfObjectives,
				int numberOfConstraints,
				RuleBuilder<RuleObject> ruleBuilder) {
			super(bounds, numberOfObjectives, numberOfConstraints, ruleBuilder);
		}

		@Override
		public MichiganSolution_Basic<RuleObject> createMichiganSolution() {
			List<Pair<Integer, Integer>> bounds = this.bounds;
			if(Objects.isNull(bounds)) {
				bounds = AbstractMichiganSolution.makeBounds();
			}
			MichiganSolution_Basic<RuleObject> solution = new MichiganSolution_Basic<RuleObject>(
					bounds,
					this.numberOfObjectives,
					this.numberOfConstraints,
					this.ruleBuilder);
			String attributeId = new NumberOfWinner<>().getAttributeId();
			solution.setAttribute(attributeId, 0);
			return solution;
		}

		@Override
		public MichiganSolution_Basic<RuleObject> createMichiganSolution(int[] variables) {
			MichiganSolution_Basic<RuleObject> michiganSolution = this.createMichiganSolution();
			michiganSolution.setVariables(variables);
			michiganSolution.learning();
			return michiganSolution;
		}

		@Override
		public MichiganSolution_Basic<RuleObject>[] createMichiganSolutions(int numberOfGenerateRule) {
			int[][] variables = this.ruleBuilder.createAntecedentIndex(numberOfGenerateRule);
			return this.createMichiganSolutions(numberOfGenerateRule, variables);
		}

		@Override
		public MichiganSolution_Basic<RuleObject>[] createMichiganSolutions(int numberOfGenerateRule,
				int[][] variables) {
			if(numberOfGenerateRule != variables.length) {
				throw new IllegalArgumentException("numberOfGenerateRule and variables must be same length");
			}
			MichiganSolution_Basic<RuleObject>[] buf = new MichiganSolution_Basic[numberOfGenerateRule];
			for(int i=0; i<numberOfGenerateRule; i++) {
				buf[i] = this.createMichiganSolution(variables[i]);
			}
			return buf;
		}

		@Override
		public String toString() {
			return "MichiganSolutionBuilder_Basic [bounds=" + bounds + ", numberOfObjectives=" + numberOfObjectives
					+ ", numberOfConstraints=" + numberOfConstraints + ", ruleBuilder=" + ruleBuilder + "]";
		}

		@Override
		public MichiganSolutionBuilder_Basic<RuleObject> copy() {
			return new MichiganSolutionBuilder_Basic<RuleObject>(
					bounds,
					this.numberOfObjectives,
					this.numberOfConstraints,
					this.ruleBuilder.copy());
		}
	}

	@Override
	public String toString() {
		String str = "MichiganSolution_Basic [rule=" + this.rule.getClass().getSimpleName() + ", variables= {";
		str += String.format("%3d", this.getVariable(0));
		for(int i=1; i<this.getNumberOfVariables(); i++) {str += String.format(", %3d", this.getVariable(i));}
		str += "}, " + String.format("Objectives[%d]=%.4f..", 0, this.getObjective(0));
		for(int i=1; i<this.getNumberOfObjectives(); i++) {
			str += String.format(", Objectives[%d]=%.4f..", i, this.getObjective(i));
		}
		str += ", RuleWeight=" + this.rule.getRuleWeight().toString() + ", ClassLabel=" + this.rule.getClassLabel().toString();
		str += ", attributes=" + attributes + "]";

		return str;
	}
}
