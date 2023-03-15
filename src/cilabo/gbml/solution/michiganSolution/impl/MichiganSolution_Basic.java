package cilabo.gbml.solution.michiganSolution.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Element;

import cilabo.data.DataSet;
import cilabo.data.DataSetManager;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.builder.RuleBuilder;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.utility.Random;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * 標準的機能を持つMichigan型識別器 basic michigan solution
 * @author Takigawa Hiroki
 *
 * @param <RuleClass> このMichiganSolutionが持つルールクラス rule class that this class has
 */
public final class MichiganSolution_Basic<RuleClass extends Rule<?, ?>>
	extends AbstractMichiganSolution<RuleClass>{

	/** コンストラクタ．constructor
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列 list of upper bounds and lower bounds for each variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder
	 */
	public MichiganSolution_Basic(List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder) {
		super(bounds, numberOfObjectives, numberOfConstraints, ruleBuilder);
		//生成不可ルールの場合は再生成
		int cnt = 0;
		do {
			cnt++;
			this.createRule();
			if(cnt > 1000) {System.err.print("number of trials to generaete rule is more than 1000");}
		}while(this.rule.getConsequent().getClassLabel().isRejectedClassLabel());
	}

	/** コンストラクタ．constructor
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder
	 */
	public MichiganSolution_Basic(int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder) {
		super(numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	/** コンストラクタ．constructor
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列 list of upper bounds and lower bounds for each variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder
	 * @param michiganSolutionEL 読み込むXMLファイル XMLfile to load
	 */
	public MichiganSolution_Basic(List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder,
			Element michiganSolutionEL) {
		super(bounds, numberOfObjectives, numberOfConstraints, ruleBuilder);

		this.createRule(michiganSolutionEL);

		//生成不可ルールの場合は再生成
		while(this.rule.getConsequent().getClassLabel().isRejectedClassLabel()) {
			this.createRule();
		}
	}

	/** コンストラクタ．constructor
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder
	 * @param michiganSolutionEL 読み込むXMLファイル XMLfile to load
	 */
	public MichiganSolution_Basic(int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder,
			Element michiganSolutionEL) {
		this(AbstractMichiganSolution.makeBounds(), numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	/** コンストラクタ．constructor
	 * @param bounds 各遺伝子が取りうる値の上限値と下限値の配列 list of upper bounds and lower bounds for each variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder
	 * @param 前件部生成の学習に使用するPatternクラス pattern class to be used to generate Rule
	 */
	public MichiganSolution_Basic(List<Pair<Integer, Integer>> bounds,
			int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder,
			Pattern<?> pattern) {
		super(bounds, numberOfObjectives, numberOfConstraints, ruleBuilder);

		this.createRule(pattern);

		//生成不可ルールの場合はランダムなパターンをから再生成
		DataSet<?> train = DataSetManager.getInstance().getTrains().get(0);
		while(this.rule.getConsequent().getClassLabel().isRejectedClassLabel()) {
			int index = Random.getInstance().getGEN().nextInt(train.getDataSetSize());
			this.createRule(train.getPattern(index));
		}
	}

	/** コンストラクタ．constructor
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param ruleBuilder ルール生成器．rule builder
	 * @param 前件部生成の学習に使用するPatternクラス pattern class to be used to generate Rule
	 */
	public MichiganSolution_Basic(int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<RuleClass> ruleBuilder,
			Pattern<?> pattern) {
		this(AbstractMichiganSolution.makeBounds(), numberOfObjectives, numberOfConstraints, ruleBuilder, pattern);
	}

	/** コピーコンストラクタ．copy constructor
	 * @param solution コピー元となるインスタンス copy instance*/
	public MichiganSolution_Basic(MichiganSolution_Basic<RuleClass> solution) {
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
	    this.rule = (RuleClass) solution.getRule().copy();
	}

	@Override
	public MichiganSolution_Basic<RuleClass> copy() {
		return new MichiganSolution_Basic<RuleClass>(this);
	}

	@Override
	public String toString() {
		String str = "MichiganSolution_Basic,variables=,";
		str += String.format("%3d", this.getVariable(0));
		for(int i=1; i<this.getNumberOfVariables(); i++) {str += String.format(", %3d", this.getVariable(i));}

		str += ",RuleWeight=," + this.rule.getConsequent().getRuleWeight().getRuleWeightVariable().toString() + ",ClassLabel=," + this.rule.getConsequent().getClassLabel().toString();

		str += "," + String.format("Objectives[%d]=,%.4f..", 0, this.getObjective(0));
		for(int i=1; i<this.getNumberOfObjectives(); i++) {
			str += String.format(",Objectives[%d]=,%.4f..", i, this.getObjective(i));
		}

		str += ",attributes={,";
		for (Entry<Object, Object> entry : attributes.entrySet()) {
			String[] str2 = ((String)entry.getKey()).split("\\.");
		    str += String.format("%s,%s,", str2[str2.length-1], entry.getValue().toString());
		}
		str += "}";

		return str;
	}

	@Override
	public Element toElement() {
		//新規のElementを追加する
		Element michiganSolution = XML_manager.getInstance().createElement(XML_TagName.michiganSolution);

		Element rule = this.rule.toElement();
		XML_manager.getInstance().addElement(michiganSolution, rule);

		//新規のElementを追加する
		Element fuzzySets = XML_manager.getInstance().createElement(XML_TagName.fuzzySetList);
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			XML_manager.getInstance().addElement(fuzzySets, XML_TagName.fuzzySetID, this.getVariable(i),
					XML_TagName.dimension, i);
		}
		XML_manager.getInstance().addElement(michiganSolution, fuzzySets);

		Element attribute_Element = XML_manager.getInstance().createElement(XML_TagName.attributes);
		for (Entry<Object, Object> entry : attributes.entrySet()) {
			String[] str2 = ((String)entry.getKey()).split("\\.");
			XML_manager.getInstance().addElement(attribute_Element, XML_TagName.attribute, entry.getValue().toString(),
					XML_TagName.attributeID, str2[str2.length-1]);
		}
		XML_manager.getInstance().addElement(michiganSolution, attribute_Element);

		return michiganSolution;
	}

}
