package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import cilabo.main.Consts;

public class XML_manager {
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private DOMImplementation domImpl;
	protected Document document;
	private TransformerFactory transFactory;
	private Transformer transformer;
	private String xmlFileName;

	//xmlファイル用パラメータ

	private static XML_manager instance = new XML_manager();

	public static XML_manager getInstance() {
		return instance;
	}

	private XML_manager(){
		xmlFileName = Consts.XML_FILE_NAME;
		if(!(xmlFileName.endsWith(".xml"))) {
			xmlFileName = xmlFileName +".xml";
		}
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		domImpl = builder.getDOMImplementation();
		document = domImpl.createDocument("", xmlFileName, null);
		transFactory = TransformerFactory.newInstance();
		try {
			transformer = transFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 書き込まれたdocファイルをxml形式に書き出す
	 *
	 * @throws TransformerException
	 * @throws IOException
	 */
	public void output(String savePath) throws TransformerException, IOException {
		DOMSource source = new DOMSource(document);
		File newXML = new File(savePath + File.separator + xmlFileName);
		Path path = Paths.get(savePath);
		Files.createDirectories(path);
		FileOutputStream os = new FileOutputStream(newXML);
		StreamResult result = new StreamResult(os);
		transformer.transform(source, result);
	}

	/**
	 * 親ノードに子ノードを追加する．子ノードに値を追加 生成された子ノードを返す
	 * @param parent 追加先の親ノード
	 * @param nodeName 生成したい子ノードの名
	 * @param nodeValue 要素の持つ値
	 * @return 生成された子ノード(Element型)
	 */
	public Element addElement(Element parent, XML_TagName nodeName, String nodeValue) {
		Element v = document.createElement(nodeName.toString());
		Text textContents = document.createTextNode(nodeValue);
		v.appendChild(textContents);
		parent.appendChild(v);
		return v;
	}

	/**
	 * 親ノードに子ノードを追加する．子ノードに値を追加 生成された子ノードを返す
	 * @param parent 追加先の親ノード
	 * @param nodeName 生成したい子ノードの名
	 * @param nodeValue 要素の持つ値
	 * @return 生成された子ノード(Element型)
	 */
	public Element addElement(Element parent, XML_TagName nodeName, Number nodeValue) {
		return this.addElement(parent, nodeName, String.valueOf(nodeValue));
	}

	/**
	 * 親ノードに子ノードを追加する. 値を追加．属性値を追加  生成された子ノードを返す
	 * @param parent 追加先の親ノード
	 * @param nodeName 生成したい子ノードの名
	 * @param nodeValue 要素の持つ値
	 * @param attributeName 属性値名
	 * @param attributeValue 属性の持つ値
	 * @return 生成された子ノード(Element型)
	 */
	public Element addElement(Element parent, XML_TagName nodeName, String nodeValue, XML_TagName attributeName, String attributeValue) {
		Element v = document.createElement(nodeName.toString());
		Text textContents = document.createTextNode(nodeValue);
		v.appendChild(textContents);
		v.setAttribute(attributeName.toString(), attributeValue);
		parent.appendChild(v);
		return v;
	}

	/**
	 * 親ノードに子ノードを追加する. 値を追加．属性値を追加  生成された子ノードを返す
	 * @param parent 追加先の親ノード
	 * @param nodeName 生成したい子ノードの名
	 * @param nodeValue 要素の持つ値
	 * @param attributeName 属性値名
	 * @param attributeValue 属性の持つ値
	 * @return 生成された子ノード(Element型)
	 */
	public Element addElement(Element parent, XML_TagName nodeName, Number nodeValue, XML_TagName attributeName, String attributeValue) {
		return this.addElement(parent, nodeName, String.valueOf(nodeValue), attributeName, attributeValue);
	}

	/**
	 * 親ノードに子ノードを追加する. 値を追加．属性値を追加  生成された子ノードを返す
	 * @param parent 追加先の親ノード
	 * @param nodeName 生成したい子ノードの名
	 * @param nodeValue 要素の持つ値
	 * @param attributeName 属性値名
	 * @param attributeValue 属性の持つ値
	 * @return 生成された子ノード(Element型)
	 */
	public Element addElement(Element parent, XML_TagName nodeName, String nodeValue, XML_TagName attributeName, Number attributeValue) {
		return this.addElement(parent, nodeName, nodeValue, attributeName, String.valueOf(attributeValue));
	}

	/**
	 * 親ノードに子ノードを追加する. 値を追加．属性値を追加  生成された子ノードを返す
	 * @param parent 追加先の親ノード
	 * @param nodeName 生成したい子ノードの名
	 * @param nodeValue 要素の持つ値
	 * @param attributeName 属性値名
	 * @param attributeValue 属性の持つ値
	 * @return 生成された子ノード(Element型)
	 */
	public Element addElement(Element parent, XML_TagName nodeName, Number nodeValue, XML_TagName attributeName, Number attributeValue) {
		return this.addElement(parent, nodeName, String.valueOf(nodeValue), attributeName, String.valueOf(attributeValue));
	}

	/**
	 *ツリーの根を返す
	 */
	public Element getRoot() {
		return document.getDocumentElement();
	}

	//新規のElementを追加する
	public Element createElement(XML_TagName nodeName) {
		return document.createElement(nodeName.toString());
	}

	//新規のElementを追加する
	public Element createElement(XML_TagName nodeName, String nodeValue) {
		Element v = document.createElement(nodeName.toString());
		Text textContents = document.createTextNode(nodeValue);
		v.appendChild(textContents);
		return v;
	}

	//新規のElementを追加する
	public Element createElement(XML_TagName nodeName, Number nodeValue) {
		return this.createElement(nodeName, String.valueOf(nodeValue));
	}

	//新規のElementを追加する.Consts専用．その他非推奨
	public Element createElement(String nodeName, String nodeValue) {
		Element v = document.createElement(nodeName);
		Text textContents = document.createTextNode(nodeValue);
		v.appendChild(textContents);
		return v;
	}

	//Elementを追加する
	public Element createElement(XML_TagName nodeName, XML_TagName attributeName, String attributeValue) {
		Element v = document.createElement(nodeName.toString());
		v.setAttribute(attributeName.toString(), attributeValue);
		return v;
	}

	//Elementを追加する
	public Element createElement(XML_TagName nodeName, XML_TagName attributeName, Number attributeValue) {
		return this.createElement(nodeName, attributeName, String.valueOf(attributeValue));
	}

	//Elementを追加する
	public Element createElement(XML_TagName nodeName, String nodeValue, XML_TagName attributeName, String attributeValue) {
		Element v = document.createElement(nodeName.toString());
		Text textContents = document.createTextNode(nodeValue);
		v.setAttribute(attributeName.toString(), attributeValue);
		v.appendChild(textContents);
		return v;
	}

	//Elementを追加する
	public Element createElement(XML_TagName nodeName, String nodeValue, XML_TagName attributeName, Number attributeValue) {
		return this.createElement(nodeName, nodeValue, attributeName, String.valueOf(attributeValue));
	}

	//Elementを追加する
	public Element createElement(XML_TagName nodeName, Number nodeValue, XML_TagName attributeName, String attributeValue) {
		return this.createElement(nodeName, String.valueOf(nodeValue), attributeName, attributeValue);
	}

	//Elementを追加する
	public Element createElement(XML_TagName nodeName, Number nodeValue, XML_TagName attributeName, Number attributeValue) {
		return this.createElement(nodeName, String.valueOf(nodeValue), attributeName, String.valueOf(attributeValue));
	}

	//Elementを追加する
	public Element addElement(Element parent, Element child) {
		parent.appendChild(child);
		return parent;
	}

	//Elementを追加する
	public Element addElement(Element parent, Element child, XML_TagName attributeName, String attributeValue) {
		child.setAttribute(attributeName.toString(), attributeValue);
		parent.appendChild(child);
		return parent;
	}

	//Elementを追加する
	public Element addElement(Element parent, Element child, XML_TagName attributeName, Number attributeValue) {
		child.setAttribute(attributeName.toString(), String.valueOf(attributeValue));
		parent.appendChild(child);
		return parent;
	}

	/**
	 * 子ノードのリストを返す
	 * @param parent
	 * @return 子ノードのリスト(NodeList型)
	 */
	public NodeList getChildNodeList(Element parent) {
		return parent.getChildNodes();
	}

	/**
	 * 子ノードのリストを返す
	 * @param parent
	 * @return 子ノードのリスト(Element[]型)
	 */
	public Element[] getChildElementList(Element parent) {
		NodeList nodelist = parent.getChildNodes();
		int len = nodelist.getLength();
		Element[] elementList = new Element[len];
		for(int i=0; i<nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				elementList[i] = (Element)node;
			}
		}
		return elementList;
	}

	/**
	 * node名が一致する子ノードを返す
	 * @param parent
	 * @return 子ノード(Element型)
	 */
	public Element findChildElement(Element parent, XML_TagName nodeName) {
		NodeList nodelist = parent.getChildNodes();
		for(int i=0; i<nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if(element.getNodeName().equals(nodeName.toString())) {
					return element;
				}
			}
		}
		return null;
	}

	/**
	 * node名とAttributeが一致する子ノードを返す
	 * @param parent
	 * @return 子ノード(Element型)
	 */
	public Element findChildElement(Element parent, XML_TagName nodeName, XML_TagName attributeName, String attributeValue) {
		NodeList nodelist = parent.getChildNodes();
		for(int i=0; i<nodelist.getLength(); i++) {
			Node node = nodelist.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element)node;
				if(element.getNodeName().equals(nodeName.toString()) && element.getAttribute(attributeName.toString()).equals(attributeValue)) {
					return element;
				}
			}
		}
		return null;
	}
}
