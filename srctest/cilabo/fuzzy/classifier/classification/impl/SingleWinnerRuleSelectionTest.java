package cilabo.fuzzy.classifier.classification.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.MakeTestObject;
import cilabo.data.AttributeVector;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.fuzzy.rule.impl.Rule_Basic;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;
import cilabo.gbml.solution.michiganSolution.impl.MichiganSolution_Basic;

class SingleWinnerRuleSelectionTest {

	static MakeTestObject testObject;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		testObject = new MakeTestObject("iris", 0, 0);
	}

	@Test
	void test() {
		List<MichiganSolution_Basic<Rule_Basic>> michiganSolutionList =
			new ArrayList<MichiganSolution_Basic<Rule_Basic>>();
		for(MichiganSolution_Basic<Rule_Basic> solution_i: testObject.makeMichiganSolutionArray(20)) {
			michiganSolutionList.add(solution_i);
		}
		Classification<MichiganSolution_Basic<Rule_Basic>> classification = testObject.getClassification();
		AttributeVector inputVector = testObject.getTrain().getPattern(0).getAttributeVector();
		assertTrue(classification.classify(michiganSolutionList, inputVector) instanceof MichiganSolution_Basic);
	}

	@Test
	void test2() {
		List<MichiganSolution_Basic<Rule_Basic>> michiganSolutionList =
				new ArrayList<MichiganSolution_Basic<Rule_Basic>>();
		MichiganSolutionBuilder<MichiganSolution_Basic<Rule_Basic>> michiganSolutionBuilder = testObject.getMichiganSolutionBuilder();
		michiganSolutionList.add(michiganSolutionBuilder.createMichiganSolution(new int[] {0,1, 2, 3}));
		michiganSolutionList.add(michiganSolutionBuilder.createMichiganSolution(new int[] {0,1, 2, 3}));
		Classification<MichiganSolution_Basic<Rule_Basic>> classification = testObject.getClassification();
		AttributeVector inputVector = testObject.getTrain().getPattern(0).getAttributeVector();
		assertTrue(classification.classify(michiganSolutionList, inputVector) instanceof MichiganSolution_Basic);
	}
}
