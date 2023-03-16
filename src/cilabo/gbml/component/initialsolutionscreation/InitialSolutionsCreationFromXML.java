package cilabo.gbml.component.initialsolutionscreation;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.component.initialsolutioncreation.InitialSolutionsCreation;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.gbml.problem.pittsburgh.impl.PittsburghProblem_Basic;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import xml.XML_TagName;

public class InitialSolutionsCreationFromXML <S extends PittsburghSolution<?>>
	implements InitialSolutionsCreation<S> {

	private final Element population;
	private final PittsburghProblem_Basic<?> problem;

	public InitialSolutionsCreationFromXML(PittsburghProblem_Basic<?> problem, Element population) {
		this.problem = problem;
		this.population = population;
	}

	@Override
	public List<S> create() {
		NodeList pittsburghSolutionNodes = population.getElementsByTagName(XML_TagName.pittsburghSolution.toString());
		List<S> solutionList = new ArrayList<S>(pittsburghSolutionNodes.getLength());
		for(int i=0; i<pittsburghSolutionNodes.getLength(); i++) {
			solutionList.add((S) problem.createSolution((Element)pittsburghSolutionNodes.item(i)));
		}
		return solutionList;
	}

}
