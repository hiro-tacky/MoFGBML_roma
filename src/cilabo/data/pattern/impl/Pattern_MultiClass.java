package cilabo.data.pattern.impl;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.main.impl.multiTasking.MultiTasking;

/** マルチラベル用のなパターンクラス．pattern class for multi label<br>
 * 複数のクラスラベルと各属性値を持つ．this class has multi class labels and own pattern's attributes
 * @author Takigawa Hiroki */
@MultiTasking
public final class Pattern_MultiClass extends Pattern <ClassLabel_Multi>{

	/** Pattern_MultiClass コンストラクタ
	 * @param id このインスタンスに与えられたID．ID which is ginve to this instance
	 * @param attributeVector 属性値クラス．attribute class
	 * @param targetClass ラベルクラス．label class */
	public Pattern_MultiClass(int id, AttributeVector attributeVector, ClassLabel_Multi targetClass) {
		super(id, attributeVector, targetClass);
	}

	@Override
	public String toString() {
		if(this.attributeVector == null || this.targetClass == null) {
			return null;
		}

		String str = String.format("id:%d,input:[%s],Class:[%s]", this.id, this.attributeVector.toString(), this.targetClass.toString());
		return str;
	}
}
