package cilabo.fuzzy.knowledge.factory;

import java.util.Objects;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.fuzzy.knowledge.FuzzySet;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import xml.XML_TagName;

/**
 * XMLファイルからKnowledgeBaseを読み込む．<br>
 * Load fuzzy sets XML file
 * @author Takigawa Hiroki
 */
public class KnowledgeFactoryFromXML {

	/** Number of features */
	public int numberOfDimension;
	/** KnowledgeBase保有Element */
	public Element knowledge;

	/**
	 * コンストラクタ．Initialize HomoTriangleKnowledgeFactory by Parameters class
	 * @param numberOfDimension 次元数 number of dimension
	 * @param knowledge KnowledgeBase保有Element Element of Knowledge Base
	 */
	public KnowledgeFactoryFromXML(int numberOfDimension, Element knowledge) {
		this.numberOfDimension = numberOfDimension;
		this.knowledge = knowledge;
	}

	/**
	 * KnowledgeBaseを読み込み
	 * Load Knowledge Base
	 */
	public void create(){
		NodeList fuzzySetsNL = knowledge.getElementsByTagName(XML_TagName.fuzzySets.toString());
		this.numberOfDimension = fuzzySetsNL.getLength();

		// make fuzzy sets
		FuzzySet[][] fuzzySets = new FuzzySet[this.numberOfDimension][];

		for(int dim_i=0; dim_i<this.numberOfDimension; dim_i++) {
			Element fuzzySetEL  = (Element) fuzzySetsNL.item(dim_i);
			NodeList fuzzyTermNL = fuzzySetEL.getElementsByTagName(XML_TagName.fuzzyTerm.toString());

			fuzzySets[dim_i] = new FuzzySet[fuzzyTermNL.getLength()];
			for(int fuzzyTerm_i=0; fuzzyTerm_i<fuzzyTermNL.getLength(); fuzzyTerm_i++) {
				Element fuzzyTermEL = (Element) fuzzyTermNL.item(fuzzyTerm_i);

				//FuzzyTermType 必須データ
				int fuzzyTermID = -1;
				String fuzzyTermName = null;
				int ShapeTypeID = -1;
				float[] parameterSet = null;
				//FuzzyTermTypeForMixed用 オプションデータ
				DIVISION_TYPE divisionType = DIVISION_TYPE.equalDivision;
				int partitionNum = 0;
				int partition_i = 0;

				NodeList fuzzyTermChildNodes = fuzzyTermEL.getChildNodes();
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
						NodeList parameterNL = fuzzyTermComponent.getElementsByTagName(XML_TagName.parameter.toString());
						parameterSet = new float[parameterNL.getLength()];
						for(int j=0; j<parameterNL.getLength(); j++) {
							parameterSet[j] = Float.valueOf(parameterNL.item(j).getTextContent());
						}
					}
				}

				if(Objects.isNull(fuzzyTermName) || ShapeTypeID<0 || Objects.isNull(parameterSet) || Objects.isNull(divisionType)) {
					throw new NullPointerException("Failed to read FuzzyTerm information @" + this.getClass().getSimpleName());}

				fuzzySets[dim_i][fuzzyTermID] = new FuzzySet(
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
