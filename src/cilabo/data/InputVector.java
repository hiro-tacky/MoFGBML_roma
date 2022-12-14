package cilabo.data;

import java.util.Arrays;


/** 属性値クラス．attribute class
 * @author Takigawa Hiroki
 */
public class InputVector {

	final double[] inputVector; // 属性値の配列

	/**  入力された属性値の配列を持つインスタンスを生成する
	 * @param inputVector 属性値の配列 */
	public InputVector(double[] inputVector) {
		this.inputVector = Arrays.copyOf(inputVector, inputVector.length);
	}

	/** 指定したインデックスの属性値の配列を取得
	 * @param index 属性値の配列のインデックス
	 * @return 指定されたインデックスにある属性値の配列 */
	public double getDimValue(int index) {
		return this.inputVector[index];
	}

	/** 属性値配列を取得
	 * @return 属性値の配列 */
	public double[] getVector() {
		return this.inputVector.clone();
	}

	@Override
	public String toString() {
		if(this.inputVector == null) {
			return null;
		}
		String str = String.format("%.4f..", this.inputVector[0]);
		for(int i = 1; i < this.inputVector.length; i++) {
			str += String.format(", %.4f..", this.inputVector[i]);
		}
		return str;
	}

}
