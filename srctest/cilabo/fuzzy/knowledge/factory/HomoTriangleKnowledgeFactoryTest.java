package cilabo.fuzzy.knowledge.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.data.DataSet;
import cilabo.data.TrainTestDatasetManager;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.HomoTriangle_2_3_4_5;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;

class HomoTriangleKnowledgeFactoryTest {
	public static DataSet train;
	public static int dimension;
	public static HomoTriangleKnowledgeFactory knowledgeFactory;


	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		TrainTestDatasetManager.getInstance().loadTrainTestFiles("dataset/iris/a0_0_iris-10tra.dat", "dataset/iris/a0_0_iris-10tst.dat");
		train = TrainTestDatasetManager.getInstance().getTrains().get(0);
		dimension = train.getNdim();
		Parameters parameters = new Parameters(train, dimension);
		knowledgeFactory = new HomoTriangleKnowledgeFactory(dimension, parameters);
	}

	@Test
	void test() {
		int dimension = train.getNdim();
		knowledgeFactory.create();

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
		TrainTestDatasetManager.getInstance().clear();
		Knowledge.getInstance().clear();
	}
}
