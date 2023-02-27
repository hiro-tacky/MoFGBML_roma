package cilabo.gbml.solution.michiganSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.AbstractSolution;
import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

public abstract class AbstractMichiganSolution<RuleObject extends Rule<?, ?, ?, ?, ?, ?>>
	extends AbstractSolution<Integer> implements MichiganSolution<RuleObject>{

	/** 前件部の遺伝子が持つインデックスの下限値と上限値のペアの配列 */
	protected List<Pair<Integer, Integer>> bounds;
	/**	ルールオブジェクト */
	protected RuleObject rule;
	/** ルールジェネレーター */
	protected RuleBuilder<RuleObject, ?, ?> ruleBuilder;

	/** コンストラクタ
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列
	 * @param numberOfObjectives 目的関数の個数
	 * @param numberOfConstraints 制約の個数
	 * @param ruleBuilder ルール生成器．*/
	protected AbstractMichiganSolution(List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleObject, ?, ?> ruleBuilder) {
		super(bounds.size(), numberOfObjectives, numberOfConstraints);
		this.bounds = bounds;
		this.ruleBuilder = ruleBuilder;
	}

	/** コンストラクタ
	 * @param numberOfObjectives 目的関数の個数
	 * @param numberOfConstraints 制約の個数
	 * @param ruleBuilder ルール生成器．*/
	protected AbstractMichiganSolution(int numberOfObjectives, int numberOfConstraints, RuleBuilder<RuleObject, ?, ?> ruleBuilder) {
		this(AbstractMichiganSolution.makeBounds(), numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	/** MichiganSolutionでの境界値をKnowledgeから自動生成する
	 * @return 生成された境界値．*/
	public static List<Pair<Integer, Integer>> makeBounds(){
		int dimNum = Knowledge.getInstance().getNumberOfDimension();

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
	public Integer getVariable(int index) {
		return this.variables.get(index);
	}

	@Override
	public void createRule() {
		int[] antecedentIndex = ruleBuilder.createAntecedentIndex();
		this.setVariables(antecedentIndex);
		this.learning();
	}

	@Override
	public void createRule(Element michiganSolution) {
		this.setVariables(this.ruleBuilder.createAntecedentIndex(michiganSolution));
		this.learning();
	}

	@Override
	public void createRule(Pattern<?> pattern) {
		this.setVariables(this.ruleBuilder.createAntecedentIndex(pattern));
		this.learning();
	}

	@Override
	public void learning() {
		if(Objects.isNull(this.variables)) { throw new IllegalArgumentException("variables Array is null.");}
		this.rule = this.ruleBuilder.createConsequent(this.getVariablesArray());
	}

	@Override
	public double getFitnessValue(AttributeVector inputVector) {
		return this.rule.getFitnessValue(this.getVariablesArray(), inputVector);
	}

	@Override
	public int getRuleLength() {
		return this.rule.getRuleLength(this.getVariablesArray());
	}

	@Override
	public ClassLabel<?> getClassLabel() {
		return this.rule.getClassLabel();
	}

	public RuleWeight<?> getRuleWeight() {
		return this.rule.getRuleWeight();
	}

	@Override
	public int[] getVariablesArray() {
		int[] buf = new int[this.getNumberOfVariables()];
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			buf[i] = this.getVariable(i);
		}
		return buf;
	}

	@Override
	public RuleObject getRule() {
		return this.rule;
	}

	@Override
	public RuleBuilder<RuleObject, ?, ?> getRuleBuilder() {
		return this.ruleBuilder;
	}

	@Override
	public Consequent<?, ?, ?, ?> getConsequent() {
		return this.rule.getConsequent();
	}

	@Override
	public Antecedent getAntecedent() {
		return this.rule.getAntecedent();
	}

	@Override
	public double[] getCompatibleGrade(AttributeVector attributeVector) {
		return this.rule.getCompatibleGrade(this.getVariablesArray(), attributeVector);
	}

	@Override
	public double getCompatibleGradeValue(AttributeVector attributeVector) {
		return this.rule.getCompatibleGradeValue(this.getVariablesArray(), attributeVector);
	}

	public static abstract class MichiganSolutionBuilderCore<michiganObject extends MichiganSolution<RuleObject>,
		RuleObject extends Rule<?, ?, ?, ?, ?, ?>>
		implements MichiganSolutionBuilder<michiganObject>{

		protected List<Pair<Integer, Integer>> bounds;
		protected int numberOfObjectives;
		protected int numberOfConstraints;
		protected RuleBuilder<RuleObject, ?, ?> ruleBuilder;

		public MichiganSolutionBuilderCore(
				List<Pair<Integer, Integer>> bounds,
				int numberOfObjectives,
				int numberOfConstraints,
				RuleBuilder<RuleObject, ?, ?> ruleBuilder) {
			this.bounds = bounds;
			this.numberOfObjectives = numberOfObjectives;
			this.numberOfConstraints = numberOfConstraints;
			this.ruleBuilder = ruleBuilder;
		}
	}
}
