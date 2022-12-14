package cilabo.data.pattern.impl;

import cilabo.data.InputVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;

/** 基本的なパターンクラス．basically pattern class<br>
 * 単一の結論部ラベルと各属性値を持つ．this class has only one conclusion label and own pattern's attributes
 * @author Takigawa Hiroki */
public final class Pattern_Basic extends Pattern <ClassLabel_Basic> {

	/** Pattern_Basic コンストラクタ
	 * @param id このインスタンスに与えられたID．ID which is ginve to this instance
	 * @param inputVector 属性値クラス．attribute class
	 * @param trueClass 結論部ラベルクラス．conclusion label class */
	public Pattern_Basic(int id, InputVector inputVector, ClassLabel_Basic trueClass) {
		super(id, inputVector, trueClass);
	}

	@Override
	public String toString() {
		if(this.inputVector == null || this.trueClass == null) { return "null"; }

		String str = String.format("[id:%d, input:{%s}, Class:%s]", this.id, this.inputVector.toString(), this.trueClass.toString());
		return str;
	}
}
