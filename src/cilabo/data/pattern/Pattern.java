package cilabo.data.pattern;

import cilabo.data.InputVector;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;

/** パターン抽象クラス．abstract pattern class<br>
 * @param <ClassLabelObject> 実装したPatternクラスが持つラベルクラス
 * @author Takigawa Hiroki
 */
public abstract class Pattern <ClassLabelObject extends ClassLabel<?>>{

	/** このインスタンスに与えられたID．ID which is ginve to this instance */
	protected int id;
	/** 属性値クラス．attribute class */
	protected InputVector inputVector;
	/** 結論部ラベルクラス．conclusion label class*/
	protected ClassLabelObject trueClass;

	/** パターン抽象クラス コンストラクタ
	 * @param id このインスタンスに与えられたID．ID which is ginve to this instance
	 * @param inputVector 属性値クラス．attribute class
	 * @param trueClass 結論部ラベルクラス．conclusion label class */
	public Pattern(int id, InputVector inputVector, ClassLabelObject trueClass) {
		this.id = id;
		this.inputVector = inputVector;
		this.trueClass = trueClass;
	}

	/** このインスタンスが持つIDを返します。<br>
	 * Returns ID which is ginve to this instance
	 * @return 返されるこのインスタンスに与えられたID．ID which is ginve to this instance to return
	 */
	public int getID() {
		return this.id;
	}

	/** このインスタンスが持つ属性値クラスを返します。<br>
	 * Returns attribute class that this instance has.
	 * @return 返される属性値クラス．attribute class to return
	 */
	public InputVector getInputVector() {
		return this.inputVector;
	}

	/** 指定された位置にある属性値を返します。<br>
	 * Returns attribute value at the specified position.
	 * @param index 返される属性値のインデックス．index of attribute value to return
	 * @return 返される属性値．attribute value to return
	 */
	public double getInputValue(int index) {
		return this.inputVector.getDimValue(index);
	}

	/**このインスタンスが持つ結論部ラベルクラスを返します。<br>
	 * Returns conclusion label class that this instance has.
	 * @return 返される結論部ラベルクラス．conclusion label class to return
	 */
	public ClassLabelObject getTrueClass() {
		return this.trueClass;
	}

	@Override
	public abstract String toString();
}
