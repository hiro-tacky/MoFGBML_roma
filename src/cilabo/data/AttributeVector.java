package cilabo.data;

import java.util.Arrays;
import java.util.Objects;


/** 属性値のデータコンテナクラス．
 * @author Takigawa Hiroki
 */
public class AttributeVector {

	/** 属性値の配列 */
	private final double[] attributeVector;

	/**
	 * 入力された属性値の配列を持つインスタンスを生成する
	 * @param attributeVector 属性値の配列
	 */
	public AttributeVector(double[] attributeVector) {
		if(Objects.isNull(attributeVector)) {
			throw new IllegalArgumentException("argument [inputVector] is null @InputVector.InputVector()");}
		this.attributeVector = Arrays.copyOf(attributeVector, attributeVector.length);
	}

	/**
	 * 指定された位置にある属性値を返します。<br>
	 * Returns Attribute value at the specified position
	 * @param index 返される属性値のインデックス．index of Attribute value to return
	 * @return 指定された位置にある属性値．Attribute value at the specified position in the list
	 */
	public double getAttributeValue(int index) {
		return this.attributeVector[index];
	}

	/**
	 * このインスタンスが持つ属性値配列を返します。<br>
	 * Returns list of Attribute value that this instance has.
	 * @return 返される属性値配列．list of Attribute value to return
	 */
	public double[] getAttributeValue() {
		return Arrays.copyOf(attributeVector, attributeVector.length);
	}

	@Override
	public String toString() {
		if(this.attributeVector == null) {
			return null;
		}
		String str = String.format("%.4f..", this.attributeVector[0]);
		for(int i = 1; i < this.attributeVector.length; i++) {
			str += String.format(", %.4f..", this.attributeVector[i]);
		}
		return str;
	}

}
