package cilabo.fuzzy.knowledge.factory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.fuzzy.knowledge.FuzzyTermTypeForMixed;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import xml.XML_TagName;

public class KnowledgeFactoryFromXML {

	/** Number of features */
	public int dimension;
	public Element knowledge;



	public KnowledgeFactoryFromXML(int dimension, Element knowledge) {
		this.dimension = dimension;
		this.knowledge = knowledge;
	}

	public void create(){
		NodeList fuzzySetsList = knowledge.getElementsByTagName(XML_TagName.fuzzySets.toString());
		this.dimension = fuzzySetsList.getLength();

		// make fuzzy sets
		FuzzyTermTypeForMixed[][] fuzzySets = new FuzzyTermTypeForMixed[this.dimension][];

		for(int dim_i=0; dim_i<this.dimension; dim_i++) {
			Element fuzzySet  = (Element) fuzzySetsList.item(dim_i);
			NodeList fuzzyTermList = fuzzySet.getElementsByTagName(XML_TagName.fuzzyTerm.toString());

			fuzzySets[dim_i] = new FuzzyTermTypeForMixed[fuzzyTermList.getLength()];
			for(int fuzzyTerm_i=0; fuzzyTerm_i<fuzzyTermList.getLength(); fuzzyTerm_i++) {
				Element fuzzyTerm = (Element) fuzzyTermList.item(fuzzyTerm_i);

				int fuzzyTermID = 0;
				String fuzzyTermName = null;
				int ShapeTypeID = 0;
				DIVISION_TYPE divisionType = null;
				float[] parameterSet = null;
				int partitionNum = 0;
				int partition_i = 0;

				NodeList fuzzyTermChildNodes = fuzzyTerm.getChildNodes();
				for(int i=0; i<fuzzyTermChildNodes.getLength(); i++) {
					Element fuzzyTermComponent = (Element) fuzzyTermChildNodes.item(i);
					if( fuzzyTermComponent.getNodeName().equals( XML_TagName.fuzzyTermID.toString()) )
						{ fuzzyTermID = Integer.valueOf(fuzzyTermComponent.getTextContent());}

					else if( fuzzyTermComponent.getNodeName().equals( XML_TagName.fuzzyTermName.toString()) )
						{ fuzzyTermName = fuzzyTermComponent.getTextContent();}

					else if( fuzzyTermComponent.getNodeName().equals( XML_TagName.ShapeTypeID.toString()) )
						{ ShapeTypeID = Integer.valueOf(fuzzyTermComponent.getTextContent());}

					else if( fuzzyTermComponent.getNodeName().equals( XML_TagName.divisionType.toString()) )
						{ divisionType = DIVISION_TYPE.valueOf(fuzzyTermComponent.getTextContent());}

					else if( fuzzyTermComponent.getNodeName().equals( XML_TagName.partitionNum.toString()) )
						{ partitionNum = Integer.valueOf(fuzzyTermComponent.getTextContent());}

					else if( fuzzyTermComponent.getNodeName().equals( XML_TagName.partition_i.toString()) )
						{ partition_i = Integer.valueOf(fuzzyTermComponent.getTextContent());}

					else if( fuzzyTermComponent.getNodeName().equals( XML_TagName.parameterSet.toString()) ) {
						NodeList parameterList = fuzzyTermComponent.getElementsByTagName(XML_TagName.parameter.toString());
						parameterSet = new float[parameterList.getLength()];
						for(int j=0; j<parameterList.getLength(); j++) {
							parameterSet[j] = Float.valueOf(parameterList.item(j).getTextContent());
						}
					}
				}

				fuzzySets[dim_i][fuzzyTermID] = new FuzzyTermTypeForMixed(
						fuzzyTermName,
						ShapeTypeID,
						parameterSet,
						divisionType,
						partitionNum,
						partition_i
				);
			}
		}
		// Create
		Knowledge.getInstance().setFuzzySets(fuzzySets);
	}
}
