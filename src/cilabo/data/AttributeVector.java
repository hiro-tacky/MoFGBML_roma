package cilabo.data;

import java.util.Arrays;

import org.uma.jmetal.util.checking.Check;
import org.w3c.dom.Element;

import xml.XML_TagName;
import xml.XML_manager;


/** 属性値のデータコンテナクラス．attribute vector class
 * @author Takigawa Hiroki
 */
public class AttributeVector{

	/** 属性値の配列 attribute vector array*/
	private final double[] attributeVector;

	/**
	 * 入力された属性値の配列を持つインスタンスを生成する
	 * Initialize AttributeVector object by taken parameter
	 * @param attributeVector 属性値の配列 attribute vector array
	 */
	public AttributeVector(double[] attributeVector) {
		Check.isNotNull(attributeVector);
		this.attributeVector = Arrays.copyOf(attributeVector, attributeVector.length);
	}

	/**
	 * このインスタンスが持つ属性値配列を返します。<br>
	 * Returns attribute array that this instance has.
	 * @return 返される属性値配列．Array of attribute vector to return
	 */
	public double[] getAttributeArray() {
		return attributeVector;
	}

	/**
	 * 指定された位置にある属性値を返します。<br>
	 * Returns attribute value at the specified position
	 * @param index 返される属性値のインデックス．index of attribute value to return
	 * @return 指定された位置にある属性値．Attribute value at the specified position
	 */
	public double getAttributeValue(int index) {
		Check.valueIsInRange(index, -1, attributeVector.length);
		return this.attributeVector[index];
	}

	/**
	 * データセットの属性数/次元を返します。
	 * Returns deta set's number of dimension
	 * @return データセットの属性数/次元 deta set's number of dimension
	 */
	public int getNumberOfDimension() {
		return this.attributeVector.length;
	}

	@Override
	public String toString() {
		if(this.attributeVector == null) { return "null"; }

		String str = String.format("%.4f..", this.attributeVector[0]);
		for(int i = 1; i < this.attributeVector.length; i++) {
			str += String.format(", %.4f..", this.attributeVector[i]);
		}
		return str;
	}

	public Element toElement() {
		Element attributeVectorEL = XML_manager.getInstance().createElement(XML_TagName.attributeVector);
		for(int dim_i=0; dim_i<attributeVector.length; dim_i++) {
			XML_manager.getInstance().addElement(attributeVectorEL,
					XML_TagName.attribute, this.getAttributeValue(dim_i),
					XML_TagName.dimension, dim_i);
		}
		return attributeVectorEL;
	}

	public AttributeVector copy() {
		return new AttributeVector(this.attributeVector);

	}

	public boolean equals(AttributeVector attributeVector) {
		return Arrays.equals(this.attributeVector, attributeVector.attributeVector);
	}
}
