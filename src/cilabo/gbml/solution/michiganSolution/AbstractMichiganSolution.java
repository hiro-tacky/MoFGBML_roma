package cilabo.gbml.solution.michiganSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.AbstractSolution;

import cilabo.data.InputVector;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

public abstract class AbstractMichiganSolution<RuleObject extends Rule> extends AbstractSolution<Integer> implements MichiganSolution<RuleObject>{

	/** 前件部の遺伝子が持つインデックスの下限値と上限値のペアの配列 */
	protected List<Pair<Integer, Integer>> bounds;
	/**	ルールオブジェクト */
	protected RuleObject rule;

	/** */
	protected RuleBuilder<RuleObject> ruleBuilder;

	/** コンストラクタ
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列
	 * @param numberOfObjectives 目的関数の個数
	 * @param numberOfConstraints 制約の個数
	 * @param ruleBuilder ルール生成器．*/
	protected AbstractMichiganSolution(List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleObject> ruleBuilder) {
		super(bounds.size(), numberOfObjectives, numberOfConstraints);
		this.bounds = bounds;
		this.ruleBuilder = ruleBuilder;
	}

	/** コンストラクタ
	 * @param numberOfObjectives 目的関数の個数
	 * @param numberOfConstraints 制約の個数
	 * @param ruleBuilder ルール生成器．*/
	protected AbstractMichiganSolution(int numberOfObjectives, int numberOfConstraints, RuleBuilder<RuleObject> ruleBuilder) {
		this(AbstractMichiganSolution.makeBounds(), numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	/** MichiganSolutionでの境界値をKnowledgeから自動生成する
	 * @return 生成された境界値．*/
	public static List<Pair<Integer, Integer>> makeBounds(){
		int dimNum = Knowledge.getInstance().getDimension();

		List<Pair<Integer, Integer>> bounds = new ArrayList<Pair<Integer, Integer>>(dimNum);
	    for (int dim_i = 0; dim_i < dimNum; dim_i++) {
	    	bounds.add(new ImmutablePair<Integer, Integer>(0, Knowledge.getInstance().getFuzzySet(dim_i).length-1));
	    }

	    return bounds;
	}

	@Override
	public Integer getLowerBound(int index) {
		return this.bounds.get(index).getLeft();
	}

	@Override
	public Integer getUpperBound(int index) {
		return this.bounds.get(index).getRight();
	}

	@Override
	public void setVariables(int[] variables) {
		for(int i=0; i<variables.length; i++) {
			this.setVariable(i, variables[i]);
		}
	}

	@Override
	public Integer getVariables(int index) {
		return this.getVariable(index);
	}

	@Override
	public int[] getVariablesAsIntArray() {
		int[] variablesAsIntArray = new int[this.getNumberOfVariables()];
		for(int i=0; i<variablesAsIntArray.length; i++) {
			variablesAsIntArray[i] = this.getVariable(i);
		}
		return variablesAsIntArray;
	}

	@Override
	public void learning() {
		if(Objects.isNull(this.variables)) { throw new IllegalArgumentException("variables Array is null.");}
		this.rule = this.ruleBuilder.createRule(this.getVariablesAsIntArray());
	}

	@Override
	public double getFitnessValue(InputVector inputVector) {
		return this.rule.getFitnessValue(this.getVariablesAsIntArray(), inputVector);
	}

	@Override
	public int getRuleLength() {
		return this.rule.getRuleLength(this.getVariablesAsIntArray());
	}

	@Override
	public boolean equalsClassLabel(ClassLabel classLabel) {
		return this.rule.equalsClassLabel(classLabel);
	}

	@Override
	public ClassLabel getClassLabel() {
		return this.rule.getClassLabel();
	}

	public RuleWeight getRuleWeight() {
		return this.rule.getRuleWeight();
	}

	public abstract static class AbstractMichiganSolutionBuilder
		<michiganObject extends MichiganSolution<RuleObject>, RuleObject extends Rule>
		implements MichiganSolutionBuilder<michiganObject>{

		protected List<Pair<Integer, Integer>> bounds;
		protected int numberOfObjectives;
		protected int numberOfConstraints;
		protected RuleBuilder<RuleObject> ruleBuilder;

		public AbstractMichiganSolutionBuilder(
				List<Pair<Integer, Integer>> bounds,
				int numberOfObjectives,
				int numberOfConstraints,
				RuleBuilder<RuleObject> ruleBuilder) {
			this.bounds = bounds;
			this.numberOfObjectives = numberOfObjectives;
			this.numberOfConstraints = numberOfConstraints;
			this.ruleBuilder = ruleBuilder;
		}
	}
}
