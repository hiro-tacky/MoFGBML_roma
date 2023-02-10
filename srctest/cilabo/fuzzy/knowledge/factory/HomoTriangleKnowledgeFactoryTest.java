package cilabo.fuzzy.knowledge.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.MakeTestObject;
import cilabo.data.DataSet;
import cilabo.data.DataSetManager;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_2_3_4_5;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;

class HomoTriangleKnowledgeFactoryTest {
	public static DataSet<?> train;
	public static HomoTriangleKnowledgeFactory knowledgeFactory;


	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		MakeTestObject MTO = new MakeTestObject("iris", 0, 0);
		train = MTO.getTrain();
		Parameters parameters = new Parameters(train);
		knowledgeFactory = new HomoTriangleKnowledgeFactory(parameters);
	}

	@Test
	void test() {
		int dimension = train.getNdim();
		knowledgeFactory.create2_3_4_5();

		int len = 2+3+4+5;
		float[][] expected = HomoTriangle_2_3_4_5.getParams();
		for(int dim_i=0; dim_i<dimension; dim_i++) {
			assertEquals(len+1, Knowledge.getInstance().getFuzzySet(dim_i).length);
			for(int j=1; j<len+1; j++) {
				FuzzyTermType ftm = Knowledge.getInstance().getFuzzySet(dim_i, j);
				String name = Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision,
						FuzzyTermType.TYPE_triangularShape, j);
				assertEquals(name, ftm.getName());
				assertArrayEquals(expected[j-1], ftm.getParam(), 1E-4f);
			}
		}
	}

	@AfterAll
	static void afterClass() throws Exception {
		DataSetManager.getInstance().clear();
		Knowledge.getInstance().clear();
	}
}
