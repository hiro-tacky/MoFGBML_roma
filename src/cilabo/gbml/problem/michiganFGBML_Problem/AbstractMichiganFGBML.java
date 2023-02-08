package cilabo.gbml.problem.michiganFGBML_Problem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.problem.AbstractGenericProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.checking.Check;

import cilabo.data.dataSet.impl.DataSet_Basic;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/** MichiganGBML_Problemで扱うのはMichiganSolutionの配列への評価及び個体生成．
 * @author hirot
 *
 * @param <michiganSolutionObject>
 * @param <antecedentObject>
 * @param <consequentObject>
 */
/** MichiganFGBML_Problemの抽象クラス
 * @author Takigawa Hiroki
 *
 * @param <michiganSolutionObject> 実装MichiganFGBML_Problemクラスが扱うMichiganSolutionクラス
 * @param <RuleObject> MichiganSolutionクラスが持つRuleクラス
 */
public abstract class AbstractMichiganFGBML <michiganSolutionObject extends MichiganSolution<RuleObject>, RuleObject extends Rule>
		extends AbstractGenericProblem <michiganSolutionObject> implements Problem<michiganSolutionObject>{

	protected DataSet_Basic train;

	protected List<Pair<Integer, Integer>> bounds;

	protected RuleBuilder<RuleObject> ruleBuilder;

	/** Constructor */
	public AbstractMichiganFGBML(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet_Basic train,
			RuleBuilder<RuleObject> ruleBuilder) {
		this(numberOfVariables, numberOfObjectives, numberOfConstraints,
				train, AbstractMichiganSolution.makeBounds(), ruleBuilder);
	}

	/** Constructor */
	public AbstractMichiganFGBML(
			int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			DataSet_Basic train,
			List<Pair<Integer, Integer>> bounds,
			RuleBuilder<RuleObject> ruleBuilder) {
		setNumberOfVariables(bounds.size());
		setNumberOfObjectives(numberOfObjectives);
		setNumberOfConstraints(numberOfConstraints);

		this.train = train;
		this.bounds = bounds;
		this.ruleBuilder = ruleBuilder;
	}

	public DataSet_Basic getTrain() {
		return train;
	}

	public List<Pair<Integer, Integer>> getVariableBounds() {
		return bounds;
	}

	public void setVariableBounds(List<Integer> lowerBounds, List<Integer> upperBounds) {
		Check.isNotNull(lowerBounds);
		Check.isNotNull(upperBounds);
		Check.that(
		lowerBounds.size() == upperBounds.size(),
		"The size of the lower bound list is not equal to the size of the upper bound list");

		bounds =
			IntStream.range(0, lowerBounds.size())
				.mapToObj(i -> new ImmutablePair<>(lowerBounds.get(i), upperBounds.get(i)))
				.collect(Collectors.toList());
	}
}
