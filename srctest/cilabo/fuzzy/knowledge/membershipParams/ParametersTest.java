package cilabo.fuzzy.knowledge.membershipParams;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.MakeTestObject;
import cilabo.data.DataSet;
import cilabo.data.DataSetManager;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;

class ParametersTest {
	public static DataSet<?> train;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MakeTestObject MTO = new MakeTestObject("iris", 0, 0);
		train = MTO.getTrain();
	}

	@Test
	void test() {
		Parameters parameters = new Parameters(train);
		float[][] expected = HomoTriangle_2_3_4_5.getParams();
		int cnt = 0;
		int[] K = new int[] {2, 3, 4, 5};
		for(int i=0; i<K.length; i++) {
			float[][] actual = parameters.getParameter(DIVISION_TYPE.equalDivision, 0, K[i], FuzzyTermType.TYPE_triangularShape);
			for(int j=0; j<actual.length; j++) {
				assertArrayEquals(expected[cnt], actual[j], 1E-4f);
				cnt++;
			}
		}
	}

	@AfterAll
	static void afterClass() throws Exception {
		DataSetManager.getInstance().clear();
		Knowledge.getInstance().clear();
	}
}
