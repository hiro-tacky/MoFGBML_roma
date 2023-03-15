package cilabo.fuzzy.knowledge;

import org.w3c.dom.Element;

import cilabo.fuzzy.knowledge.FuzzySetBluePrintManager.FuzzySetBluePrint;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import cilabo.main.impl.mixedKnowledge.MixedKnowledge;
import jfml.term.FuzzyTermType;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * ファジィ集合クラス fuzzy set class
 * @author Takigawa Hiroki
 */
@MixedKnowledge
public class FuzzySet extends FuzzyTermType {
	/** 分割方式 division type */
	public DIVISION_TYPE divisionType;
	/** 分割区間の母数 number of partition*/
	public int numberOfPartition;
	/** 分割区間内でのID ID in partition set*/
	public int partition_i;

	/**
	 * 入力されたデータ持つインスタンスを生成する
	 * Initialize FuzzySet object by taken parameter
	 * @param fuzzyTermName ファジィセットの名前．name of fuzzy set
	 * @param shapeTypeID ファジィセット形状id shape type ID of fuzzy set
	 * @param parameter FuzzyTermTypeの形状の定義に用いられるパラメータ parameter for fuzzy set
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param numberOfPartition 分割区間の母数 number of partition
	 * @param partition_i 分割区間内でのID ID in partition set
	 */
	public FuzzySet(String fuzzyTermName, int shapeTypeID, float[] parameter, DIVISION_TYPE divisionType, int numberOfPartition, int partition_i){
		super(fuzzyTermName, shapeTypeID, parameter);
		this.divisionType = divisionType;
		this.numberOfPartition = numberOfPartition;
		this.partition_i = partition_i;
	}

	/**
	 * 入力されたデータ持つインスタンスを生成する
	 * Initialize FuzzySet object by taken parameter
	 * @param fuzzyTermName ファジィセットの名前．name of fuzzy set
	 * @param FuzzyTermBP fuzzySetクラスの設計図 fuzzy set blue print
	 * @param parameter FuzzyTermTypeの形状の定義に用いられるパラメータ parameter for fuzzy set
	 * @param partition_i 分割区間内でのID ID in partition set
	 */
	public FuzzySet(String fuzzyTermName, FuzzySetBluePrint FuzzyTermBP, float[] parameter, int partition_i){
		super(fuzzyTermName, FuzzyTermBP.getShapeTypeID(), parameter);
		this.divisionType = FuzzyTermBP.getDivisionType();
		this.numberOfPartition = FuzzyTermBP.getK();
		this.partition_i = partition_i;
	}

	/**
	 * このファジィ集合の分割方式を返します。<br>
	 * Returns division type that this instance has.
	 * @return 返される分割方式．division type to return
	 */
	public DIVISION_TYPE getDivisionType() {
		return divisionType;
	}

	/**
	 * このファジィ集合の分割区間数を返します。<br>
	 * Returns number of partition that this instance has.
	 * @return 返される分割区間数．number of partition to return
	 */
	public int getNumberOfPartition() {
		return numberOfPartition;
	}

	/**
	 * このファジィ集合の分割区間内でのIDを返します。<br>
	 * Returns ID in partition set that this instance has.
	 * @return 返される分割区間内でのID．ID in partition set to return
	 */
	public int getPartition_i() {
		return partition_i;
	}

	public Element toElement() {
		Element fuzzyTermEL = XML_manager.getInstance().createElement(XML_TagName.fuzzyTerm);
			XML_manager.getInstance().addElement(fuzzyTermEL, XML_TagName.fuzzyTermName, this.getName());
			XML_manager.getInstance().addElement(fuzzyTermEL, XML_TagName.ShapeTypeID, this.getType());
			XML_manager.getInstance().addElement(fuzzyTermEL, XML_TagName.ShapeTypeName, Knowledge.getShapeTypeNameFromID(this.getType()));
			XML_manager.getInstance().addElement(fuzzyTermEL, XML_TagName.divisionType, this.getDivisionType().toString());
			XML_manager.getInstance().addElement(fuzzyTermEL, XML_TagName.partitionNum, this.getNumberOfPartition());
			XML_manager.getInstance().addElement(fuzzyTermEL, XML_TagName.partition_i, this.getPartition_i());

		Element parameterSetEL = XML_manager.getInstance().createElement(XML_TagName.parameterSet);
		float[] parametersList = this.getParam();
		for(int i=0; i<parametersList.length; i++) {
			XML_manager.getInstance().addElement(parameterSetEL, XML_TagName.parameter, parametersList[i],
					XML_TagName.id, i);
		}
		XML_manager.getInstance().addElement(fuzzyTermEL, parameterSetEL);
		return fuzzyTermEL;
	}

	@Override
	public String toString() {
		String str = String.format("fuzzy set name:%s, shape type ID:%d, division type:%s, number of partition:%d, ID in partition set:%d, parameter:",
				this.name, this.type, this.divisionType.toString(), this.numberOfPartition, this.partition_i);
		for(int parameter_i=0; parameter_i<this.getParam().length; parameter_i++) {
			str += String.format("{id:%d, point:%f}", parameter_i, this.getParam()[parameter_i]);
		}
		return str;
	}
}
