package cilabo.gbml.solution.michiganSolution.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.Rule.RuleBuilder;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import cilabo.gbml.solution.michiganSolution.AbstractMichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import xml.XML_TagName;
import xml.XML_manager;

public class MichiganSolution_Rejected extends AbstractMichiganSolution implements MichiganSolution {

	protected MichiganSolution_Rejected(int numberOfObjectives,
			int numberOfConstraints,
			RuleBuilder<?> ruleBuilder) {
		super(numberOfObjectives, numberOfConstraints, ruleBuilder);
	}

	private static MichiganSolution_Rejected instance = new MichiganSolution_Rejected(0, 0, null);

	public static MichiganSolution_Rejected getInstance() {
		if(instance == null) {
			instance = new MichiganSolution_Rejected(0, 0, null);
		}
		return instance;
	}

	@Override
	public Integer getVariable(int index) {
		return null;
	}

	@Override
	public MichiganSolution copy() {
		return this.getInstance();
	}

	@Override
	public ClassLabel<?> getClassLabel() {
		return new ClassLabel_Basic(-1);
	}

	@Override
	public RuleWeight<?> getRuleWeight() {
		return new RuleWeight_Basic(-1);
	}

	@Override
	public Element toElement() {
		//新規のElementを追加する
		Element michiganSolution = XML_manager.createElement(XML_TagName.michiganSolution);

		Element rule = XML_manager.addElement(michiganSolution, XML_TagName.rule);

		XML_manager.addElement(rule, XML_TagName.antecedent);

		Element consequent = XML_manager.addElement(rule, XML_TagName.consequent);

		XML_manager.addElement(consequent, XML_TagName.classLabel, String.valueOf(-1));

		XML_manager.addElement(consequent, XML_TagName.ruleWeight, String.valueOf(-1));

		XML_manager.addElement(rule, consequent);

		XML_manager.addElement(michiganSolution, rule);

		//新規のElementを追加する
		Element fuzzySets = XML_manager.createElement(XML_TagName.fuzzySets);

		for(int i=0; i<this.getNumberOfVariables(); i++) {
			XML_manager.addElement(fuzzySets, XML_TagName.fuzzySetID, String.valueOf(this.getVariable(-1)),
					XML_TagName.dimension, String.valueOf(i));
		}

		XML_manager.addElement(michiganSolution, fuzzySets);

		return michiganSolution;
	}
}
