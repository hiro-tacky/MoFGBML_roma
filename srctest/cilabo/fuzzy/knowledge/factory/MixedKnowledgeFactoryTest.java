package cilabo.fuzzy.knowledge.factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.data.DataSet;
import cilabo.data.TrainTestDatasetManager;
import cilabo.fuzzy.knowledge.FuzzyTermBluePrintManager;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;

class MixedKnowledgeFactoryTest {
	public static DataSet train;
	public static int dimension;
	public static Parameters parameters;
	public static MixedKnowledgeFactory knowledgeFactory;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		TrainTestDatasetManager.getInstance().loadTrainTestFiles("dataset/iris/a0_0_iris-10tra.dat", "dataset/iris/a0_0_iris-10tst.dat");
		train = TrainTestDatasetManager.getInstance().getTrains().get(0);
		dimension = train.getNdim();
		parameters = new Parameters(train, dimension);
		knowledgeFactory = new MixedKnowledgeFactory(dimension, parameters);
	}

	@Test
	void test() {
		FuzzyTermBluePrintManager FuzzyTermBMP = new FuzzyTermBluePrintManager(train, dimension);
		for(int dim_i=0; dim_i<dimension; dim_i++) {
			int[] K = new int[] {2, 3, 4, 5};
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.equalDivision, dim_i, K, FuzzyTermType.TYPE_triangularShape);
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.equalDivision, dim_i, K, FuzzyTermType.TYPE_trapezoidShape);
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.equalDivision, dim_i, K, FuzzyTermType.TYPE_rectangularShape);
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.equalDivision, dim_i, K, FuzzyTermType.TYPE_gaussianShape);
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.entropyDivision, dim_i, K, FuzzyTermType.TYPE_triangularShape);
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.entropyDivision, dim_i, K, FuzzyTermType.TYPE_trapezoidShape);
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.entropyDivision, dim_i, K, FuzzyTermType.TYPE_rectangularShape);
			FuzzyTermBMP.addFuzyyTermsBluePrint(DIVISION_TYPE.entropyDivision, dim_i, K, FuzzyTermType.TYPE_gaussianShape);
		}

		knowledgeFactory.create(FuzzyTermBMP);
		int len = (2+3+4+5)*8;
		for(int dim_i=0; dim_i<dimension; dim_i++) {
			assertEquals(len+1, Knowledge.getInstance().getFuzzySet(dim_i).length);
			for(int j=1; j<len+1; j++) {
				FuzzyTermType ftm = Knowledge.getInstance().getFuzzySet(dim_i, j);

				String name = Knowledge.makeFuzzyTermName(getExpected_DIVISION_TYPE(j),
						getExpected_fuzzyTermType(j), j);
				assertEquals(name, ftm.getName());

				float[][] parameters_expected = MixedKnowledgeFactoryTest.parameters.getParameter(getExpected_DIVISION_TYPE(j), dim_i,
						getK(j), getExpected_fuzzyTermType(j));
				assertArrayEquals(parameters_expected[getK_(j)], ftm.getParam(), 1E-5f, ftm.getName());
			}
		}
	}

	@AfterAll
	static void afterClass() throws Exception {
		TrainTestDatasetManager.getInstance().clear();
		Knowledge.getInstance().clear();
	}

	private int getK(int x) {
		x = (x-1)%(2+3+4+5);
		if(x < 2) {
			return 2;
		}else if(x < 5){
			return 3;
		}else if(x < 9) {
			return 4;
		}else {
			return 5;
		}
	}

	private int getK_(int x) {
		x = (x-1)%(2+3+4+5);
		if(x < 2) {
			return x;
		}else if(x < 5){
			return x-2;
		}else if(x < 9) {
			return x-5;
		}else {
			return x-9;
		}
	}

	private DIVISION_TYPE getExpected_DIVISION_TYPE(int x) {
		switch ( (x-1) / ((2+3+4+5)*4) ) {
		case 0:
		default:
			return DIVISION_TYPE.equalDivision;
		case 1:
			return DIVISION_TYPE.entropyDivision;
		}
	}

	private int getExpected_fuzzyTermType(int x) {
		switch ((x-1)/(2+3+4+5)) {
		case 0:
		case 4:
		default:
			return FuzzyTermType.TYPE_triangularShape;
		case 1:
		case 5:
			return FuzzyTermType.TYPE_trapezoidShape;
		case 2:
		case 6:
			return FuzzyTermType.TYPE_rectangularShape;
		case 3:
		case 7:
			return FuzzyTermType.TYPE_gaussianShape;
		}
	}

}
