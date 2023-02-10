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
			throw new IllegalArgumentException("argument [attributeVector] is null @" + this.getClass().getSimpleName());}
		this.attributeVector = Arrays.copyOf(attributeVector, attributeVector.length);
	}

	/**
	 * このインスタンスが持つ属性値配列を返します。<br>
	 * Returns Attribute array that this instance has.
	 * @return 返される属性値配列．Array of AttributeVector to return
	 */
	public double[] getAttributeArray() {
		if(Objects.isNull(attributeVector)) {
			System.err.println("attributeVector hasn't been initialised @" + this.getClass().getSimpleName());}
		return attributeVector;
	}

	/**
	 * 指定された位置にある属性値を返します。<br>
	 * Returns Attribute value at the specified position
	 * @param index 返される属性値のインデックス．index of Attribute value to return
	 * @return 指定された位置にある属性値．Attribute value at the specified position in the list
	 */
	public double getAttributeValue(int index) {
		if(attributeVector.length <= index) {
			throw new ArrayIndexOutOfBoundsException("attributeVector out of index @" + this.getClass().getSimpleName());}
		return this.attributeVector[index];
	}

	/** 次元数を返す．
	 * @return 属性値の個数．次元数
	 */
	public int getNumberOfDimension() {
		return this.attributeVector.length;
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
