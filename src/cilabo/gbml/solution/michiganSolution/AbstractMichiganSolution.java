package cilabo.gbml.solution.michiganSolution;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.AbstractSolution;
import org.uma.jmetal.util.checking.Check;
import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.builder.RuleBuilder;

/**
 * Michigan型識別器抽象クラス
 * @author Takigawa Hiroki
 *
 * @param <RuleClass> このMichiganSolutionが持つルールクラス rule class that this class has
 */
public abstract class AbstractMichiganSolution <RuleClass extends Rule<?, ?>>
		extends AbstractSolution<Integer>
		implements MichiganSolution<RuleClass>{

	/** 前件部の遺伝子が持つインデックスの下限値と上限値のペアの配列 list of upper bounds and lower bounds for each variables */
	protected List<Pair<Integer, Integer>> bounds;
	/**	ルールオブジェクト rule instance */
	protected RuleClass rule;
	/** ルールジェネレーター rule builder*/
	protected RuleBuilder<RuleClass> ruleBuilder;

	/** コンストラクタ
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列 list of upper bounds and lower bounds for each variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder*/
	protected AbstractMichiganSolution(List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder) {
		super(bounds.size(), numberOfObjectives, numberOfConstraints);
		this.bounds = bounds;
		this.ruleBuilder = ruleBuilder;
	}

	/** コンストラクタ
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder*/
	protected AbstractMichiganSolution(int numberOfObjectives, int numberOfConstraints, RuleBuilder<RuleClass> ruleBuilder) {
		this(AbstractMichiganSolution.makeBounds(), numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	/** MichiganSolutionでの境界値をKnowledgeから自動生成する<br>
	 * Generate bounds from Knowledge.
	 * @return 生成された境界値．Generated bounds */
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
	public Integer getVariable(int index) {
		return this.variables.get(index);
	}

	@Override
	public void setVariable(int index, Integer variable) {
		this.variables.set(index, variable);
	}

	@Override
	public List<Integer> getVariables(){
		return this.variables;
	}

	@Override
	public void setVariables(int[] variables) {
		for(int i=0; i<variables.length; i++) {
			this.setVariable(i, variables[i]);
		}
	}

	@Override
	public void setVariables(List<Integer> variables) {
		this.variables = variables;
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
	public void createRule() {
		this.setVariables(ruleBuilder.createAntecedentIndex());
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
		Check.isNotNull(this.variables);
		this.rule = this.ruleBuilder.createRule(this.getVariablesArray());
	}

	@Override
	public double getFitnessValue(AttributeVector inputVector) {
		return this.rule.getFitnessValue(this.getVariablesArray(), inputVector);
	}

	@Override
	public int getRuleLength() {
		return this.rule.getAntecedent().getRuleLength(this.getVariablesArray());
	}

	@Override
	public double[] getCompatibleGrade(AttributeVector attributeVector) {
		return this.rule.getAntecedent().getCompatibleGrade(this.getVariablesArray(), attributeVector);
	}

	@Override
	public double getCompatibleGradeValue(AttributeVector attributeVector) {
		return this.rule.getAntecedent().getCompatibleGradeValue(this.getVariablesArray(), attributeVector);
	}

	@Override
	public RuleClass getRule() {
		return this.rule;
	}

	@Override
	public RuleBuilder<RuleClass> getRuleBuilder() {
		return this.ruleBuilder;
	}
}
