package cilabo.data.pattern.impl;

import cilabo.data.InputVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.main.impl.multiTasking.MultiTasking;

/** 基本的な機能を持つPatternクラス．
 * 複数の結論部クラスと各属性値を持つ
 * @author Takigawa Hiroki
 */
@MultiTasking
public final class Pattern_MultiClass extends Pattern <ClassLabel_Multi>{

	/** コンストラクタ <br> Constructs an instance of class
	 * @param id パターンに与えられたID
	 * @param inputVector 属性値クラス
	 * @param trueClass 結論部クラス
	 */
	public Pattern_MultiClass(int id, InputVector inputVector, ClassLabel_Multi trueClass) {
		super(id, inputVector, trueClass);
	}

	@Override
	public String toString() {
		if(this.inputVector == null || this.trueClass == null) {
			return null;
		}

		String str = String.format("id:%d,input:[%s],Class:[%s]", this.id, this.inputVector.toString(), this.trueClass.toString());
		return str;
	}
}
