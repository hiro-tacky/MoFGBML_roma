package cilabo.data.pattern;

import org.uma.jmetal.util.checking.Check;
import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;

/** パターン用データコンテナクラス．pattern class<br>
 * @param <ClassLabelClass> Patternクラスが扱うラベルクラス
 * @author Takigawa Hiroki
 */
public abstract class Pattern <ClassLabelClass extends ClassLabel<?>>{

	/** このインスタンスに与えられたID．ID that is ginve to this instance */
	protected final int id;
	/** 属性値クラス．attribute vector*/
	protected final AttributeVector attributeVector;
	/** ターゲットクラス．target class*/
	protected final ClassLabelClass targetClass;

	/** 入力されたデータを基にインスタンスを生成
	 * Initialize Pattern object by given parameter
	 * @param id このインスタンスに与えられたID．ID that is ginve to this instance
	 * @param attributeVector 属性値クラス．attribute vector
	 * @param targetClass ターゲットクラス．target class */
	public Pattern(int id, AttributeVector attributeVector, ClassLabelClass targetClass) {
		Check.isNotNull(attributeVector);
		Check.isNotNull(targetClass);
		this.id = id;
		this.attributeVector = attributeVector;
		this.targetClass = targetClass;
	}

	/**
	 * このインスタンスが持つIDを返します。<br>
	 * Returns ID that this instance was given.
	 * @return 返されるこのインスタンスに与えられたID．ID that was ginve to this instance
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * このインスタンスが持つ属性値クラスを返します。<br>
	 * Returns AttributeVector class that this instance has.
	 * @return 返される属性値クラス．AttributeVector class to return
	 */
	public AttributeVector getAttributeVector() {
		return this.attributeVector;
	}

	/**
	 * このインスタンスが持つ属性値配列を返します。<br>
	 * Returns attribute array that this instance has.
	 * @return 返される属性値配列．attribute array to return
	 */
	public double[] getAttributeArray() {
		return this.attributeVector.getAttributeArray();
	}

	/** 指定された位置にある属性値を返します。<br>
	 * Returns attribute value at the specified position.
	 * @param index 返される属性値のインデックス．index of attribute value to return
	 * @return 返される属性値．attribute value to return
	 */
	public double getAttributeValue(int index) {
		return this.attributeVector.getAttributeValue(index);
	}

	/**このインスタンスが持つターゲットクラスを返します。<br>
	 * Returns target class that this instance has.
	 * @return 返されるターゲットクラス．target class to return
	 */
	public ClassLabelClass getTargetClass() {
		return this.targetClass;
	}

	@Override
	public abstract String toString();

	public abstract Element toElement();

	public abstract Pattern<ClassLabelClass> copy();

	public abstract boolean equals(Pattern<?> pattern_Basic);
}
