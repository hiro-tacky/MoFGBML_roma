package xml;

import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XML_reader {

	DocumentBuilderFactory factory;
	DocumentBuilder builder;
	Document document;
	Element root;

	public XML_reader(String XMLFilePath) throws Exception{
		// 1. DocumentBuilderFactoryのインスタンスを取得する
		factory = DocumentBuilderFactory.newInstance();
		// 2. DocumentBuilderのインスタンスを取得する
		builder = factory.newDocumentBuilder();
		// 3. DocumentBuilderにXMLを読み込ませ、Documentを作る
		document = builder.parse(Paths.get(XMLFilePath).toFile());
		// 4. Documentから、ルート要素を取得する
		root = document.getDocumentElement();

	}

	public Element getKnowledgeElement(int evaluation) {
		NodeList generations = root.getElementsByTagName(XML_TagName.generations.toString());
		for(int i=0; i<generations.getLength(); i++) {
			Element generation_i = (Element) generations.item(i);
			String evaluation_attr = generation_i.getAttributes().getNamedItem(XML_TagName.evaluation.toString()).getTextContent();
			if(Integer.valueOf(evaluation_attr) == evaluation) {
				return (Element) generation_i.getElementsByTagName(XML_TagName.knowledgeBase.toString()).item(0);
			}
		}
		throw new NullPointerException("XML hasn't been read");
	}

	public Element getPopulation(int evaluation) {
		NodeList generations = root.getElementsByTagName(XML_TagName.generations.toString());
		for(int i=0; i<generations.getLength(); i++) {
			Element generation_i = (Element) generations.item(i);
			String evaluation_attr = generation_i.getAttributes().getNamedItem(XML_TagName.evaluation.toString()).getTextContent();
			if(Integer.valueOf(evaluation_attr) == evaluation) {
				return (Element) generation_i.getElementsByTagName(XML_TagName.population.toString()).item(0);
			}
		}
		throw new NullPointerException("XML hasn't been read");
	}
}
