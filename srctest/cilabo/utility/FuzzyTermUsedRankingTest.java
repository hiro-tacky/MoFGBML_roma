package cilabo.utility;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.fuzzy.knowledge.FuzzyTermTypeForMixed;
import cilabo.fuzzy.knowledge.factory.KnowledgeFactoryFromXML;
import cilabo.main.Consts;
import xml.XML_reader;

class FuzzyTermUsedRankingTest {
	private static XML_reader XML_reader;
	private static KnowledgeFactoryFromXML KBFactory;
	private static int Ndim = 8;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		try {
			String ln = File.separator;
			XML_reader = new XML_reader("results" + ln + "mixedKB" + ln + "pima" + ln + "trial00" + ln + Consts.XML_FILE_NAME + ".xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		KBFactory = new KnowledgeFactoryFromXML(Ndim, XML_reader.getKnowledgeElement(Consts.TERMINATE_EVALUATION));
		KBFactory.create();
	}

	@Test
	void test() {
		int[][] tmp = FuzzyTermUsedRanking.getUsedFuzzySetNum(XML_reader.getPopulation(Consts.TERMINATE_EVALUATION), Ndim);
		int[] buf = new int[] {5, 5, 5, 5, 5, 5, 5, 5};
		FuzzyTermTypeForMixed[][] tmp2 = FuzzyTermUsedRanking.getUsedFuzzySetID(XML_reader.getPopulation(Consts.TERMINATE_EVALUATION), Ndim, buf);
	}

}
