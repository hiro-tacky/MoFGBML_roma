package cilabo.fuzzy.knowledge.factory;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cilabo.data.DataSetManager;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.main.Consts;
import xml.XML_reader;

class KnowledgeFactoryFromXMLTest {
	private static XML_reader XML_reader;
	private static KnowledgeFactoryFromXML KBFactory;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		try {
			String ln = File.separator;
			XML_reader = new XML_reader("results" + ln + "mixedKB" + ln + "pima" + ln + "trial00" + ln + Consts.XML_FILE_NAME + ".xml");
		} catch (Exception e) {
			e.printStackTrace();
		}

		KBFactory = new KnowledgeFactoryFromXML(7, XML_reader.getKnowledgeElement(Consts.TERMINATE_EVALUATION));
	}

	@Test
	void test() {
		KBFactory.create();
		Knowledge KB = Knowledge.getInstance();
	}

	@AfterAll
	static void afterClass() throws Exception {
		DataSetManager.getInstance().clear();
		Knowledge.getInstance().clear();
	}
}
