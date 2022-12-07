package cilabo.data.pattern;

import cilabo.data.InputVector;
import cilabo.fuzzy.rule.consequent.classLabel.AbstractClassLabel;

/**Patternクラスの抽象クラス<br>
 * 基本的なgetter機能を使用できます
 * @param <ClassLabelObject> 実装したPatternクラスが持つラベルクラス
 * @author Takigawa Hiroki
 */
public abstract class Pattern <ClassLabelObject extends AbstractClassLabel>{

	/** パターンの固有id */
	final protected int id;
	/** 属性値 */
	final protected InputVector inputVector;
	/** 結論部クラス*/
	final protected ClassLabelObject trueClass;

	/** コンストラクタ <br> Constructs an instance of class
	 * @param id パターンに与えられたID
	 * @param inputVector 属性値クラス
	 * @param trueClass 結論部クラス
	 */
	public Pattern(int id, InputVector inputVector, ClassLabelObject trueClass) {
		super();
		this.id = id;
		this.inputVector = inputVector;
		this.trueClass = trueClass;
	}

	public int getID() {
		return this.id;
	}

	public InputVector getInputVector() {
		return this.inputVector;
	}

	public double getInputValueAt(int index) {
		return this.inputVector.getDimValue(index);
	}

	public ClassLabelObject getTrueClass() {
		return this.trueClass;
	}

	@Override
	public abstract String toString();
}
