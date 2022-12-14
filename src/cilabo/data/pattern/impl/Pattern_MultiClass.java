package cilabo.data.pattern.impl;

import cilabo.data.InputVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.main.impl.multiTasking.MultiTasking;

/** マルチラベル用のなパターンクラス．pattern class for multi label<br>
 * 複数の結論部ラベルと各属性値を持つ．this class has multi conclusion labels and own pattern's attributes
 * @author Takigawa Hiroki */
@MultiTasking
public final class Pattern_MultiClass extends Pattern <ClassLabel_Multi>{

	/** Pattern_MultiClass コンストラクタ
	 * @param id このインスタンスに与えられたID．ID which is ginve to this instance
	 * @param inputVector 属性値クラス．attribute class
	 * @param trueClass 結論部ラベルクラス．conclusion label class */
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
