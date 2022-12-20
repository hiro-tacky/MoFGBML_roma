package cilabo.gbml.solution.pittsburghSolution.impl;

import java.util.HashMap;

import org.w3c.dom.Element;

import cilabo.data.InputVector;
import cilabo.data.TrainTestDatasetManager;
import cilabo.fuzzy.classifier.Classifier;
import cilabo.gbml.objectivefunction.pittsburgh.ErrorRate;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.pittsburghSolution.AbstractPittsburghSolution;
import cilabo.main.ExperienceParameter.ObjectivesForPittsburgh;
import xml.XML_TagName;
import xml.XML_manager;

public final class PittsburghSolution_Basic <michiganSolution extends MichiganSolution<?>>
		extends AbstractPittsburghSolution<michiganSolution>{

	public PittsburghSolution_Basic(int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			MichiganSolutionBuilder<michiganSolution> michiganSolutionBuilder,
			Classifier<michiganSolution> classifier) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints,
				michiganSolutionBuilder, classifier);
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			michiganSolution michiganSolution = michiganSolutionBuilder.createMichiganSolution();
			this.setVariable(i, michiganSolution);
		}
	}

	private PittsburghSolution_Basic(PittsburghSolution_Basic<michiganSolution> solution) {
	    super(solution.getNumberOfVariables(), solution.getNumberOfObjectives(), solution.getNumberOfConstraints(),
	    		solution.michiganSolutionBuilder, solution.classifier.copy());

	    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
	      setVariable(i, (michiganSolution) solution.getVariable(i).copy());
	    }

	    for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
	      setObjective(i, solution.getObjective(i));
	    }

	    for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
	      setConstraint(i, solution.getConstraint(i));
	    }

	    this.attributes = new HashMap<>(solution.attributes);
	}

	@Override
	public PittsburghSolution_Basic<michiganSolution> copy() {
		return new PittsburghSolution_Basic<michiganSolution>(this);
	}

	@Override
	public MichiganSolution classify(InputVector x) {
		return this.classifier.classify(this.getVariables(), x);
	}

	@Override
	public String toString() {
		String ln = System.getProperty("line.separator");
		String str = "PittsburghSolution_Basic";

		str += ", {" + String.format("Objectives[%d]=%.4f..", 0, this.getObjective(0));
		for(int i=1; i<this.getNumberOfObjectives(); i++) {
			str += String.format(", Objectives[%d]=%.4f..", i, this.getObjective(i));
		}
		str += "}" + ln;

		for(michiganSolution tmp: this.variables) {
			str += " ->" + tmp + ln;
		}
		str += "[classifier=" + classifier + ", attributes=" + attributes + "]" + ln + ln;
		return str;
	}

	@Override
	public Element toElement() {
		//新規のElementを追加する
		Element pittsburghSolution = XML_manager.createElement(XML_TagName.pittsburghSolution);

		for(int i=0; i<this.getNumberOfVariables(); i++) {
			Element michiganSolution = this.getVariable(i).toElement();
			XML_manager.addElement(pittsburghSolution, michiganSolution,
					XML_TagName.id, String.valueOf(i));
		}

		return pittsburghSolution;
	}

	/**
	 * population を構成するElementを返す
	 *
	 * @param xml_manager
	 * @param solutionList
	 * @return population(Element型)
	 */
	public Element printSolutionsToElement() {
		Element pittsburghSolution = XML_manager.createElement(XML_TagName.pittsburghSolution);
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			Element michiganSolution = this.getVariable(i).toElement();
			XML_manager.addElement(pittsburghSolution, michiganSolution,
					XML_TagName.id, String.valueOf(i));
		}

		//各目的関数の結果
		Element objectives = XML_manager.addElement(pittsburghSolution, XML_TagName.objectivesSet);

			double f1 = this.getObjective(ObjectivesForPittsburgh.ErrorRateDtra.toInt());
			Element f1_ = XML_manager.createElement(XML_TagName.objective, String.valueOf(f1));
			f1_.setAttribute(XML_TagName.id.toString(), String.valueOf(ObjectivesForPittsburgh.ErrorRateDtra.toInt()));
			f1_.setAttribute(XML_TagName.objectiveName.toString(), ObjectivesForPittsburgh.ErrorRateDtra.toString());
			XML_manager.addElement(objectives, f1_);

			double f2 = this.getObjective(ObjectivesForPittsburgh.NumberOfRule.toInt());
			Element f2_ = XML_manager.createElement(XML_TagName.objective, String.valueOf(f2));
			f2_.setAttribute(XML_TagName.id.toString(), String.valueOf(ObjectivesForPittsburgh.NumberOfRule.toInt()));
			f2_.setAttribute(XML_TagName.objectiveName.toString(), ObjectivesForPittsburgh.NumberOfRule.toString());
			XML_manager.addElement(objectives, f2_);

			ErrorRate<PittsburghSolution_Basic<michiganSolution>> errorRate = new ErrorRate<>();
			double f3 = errorRate.function(this, TrainTestDatasetManager.getInstance().getTests().get(0));
			Element f3_ = XML_manager.createElement(XML_TagName.objective, String.valueOf(f3));
			f3_.setAttribute(XML_TagName.id.toString(), String.valueOf(ObjectivesForPittsburgh.ErrorRateDtst.toInt()));
			f3_.setAttribute(XML_TagName.objectiveName.toString(), ObjectivesForPittsburgh.ErrorRateDtst.toString());
			XML_manager.addElement(objectives, f3_);

		XML_manager.addElement(pittsburghSolution, objectives);
		return pittsburghSolution;
	}

}
