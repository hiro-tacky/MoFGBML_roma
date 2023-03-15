package cilabo.data.pattern.impl;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import xml.XML_TagName;
import xml.XML_manager;

/** 単一のターゲットクラスを持つパターンクラス．pattern class that has only one target class and own pattern's attribute vector
 * @author Takigawa Hiroki */
public final class Pattern_Basic extends Pattern <ClassLabel_Basic> {

	/** コンストラクタ constructor
	 * @param id このインスタンスに与えられたID．ID that is ginve to this instance
	 * @param attributeVector 属性値クラス．attribute class
	 * @param targetClass ターゲットクラス．target class */
	public Pattern_Basic(int id, AttributeVector attributeVector, ClassLabel_Basic targetClass) {
		super(id, attributeVector, targetClass);
	}

	@Override
	public String toString() {
		if(this.attributeVector == null || this.targetClass == null) { return "null"; }

		String str = String.format("[id:%d, input:{%s}, Class:%s]", this.id, this.attributeVector.toString(), this.targetClass.toString());
		return str;
	}

	@Override
	public Element toElement() {
		Element patternEL = XML_manager.getInstance().createElement(XML_TagName.pattern);
		XML_manager.getInstance().addElement(patternEL, this.attributeVector.toElement());
		XML_manager.getInstance().addElement(patternEL, this.targetClass.toElement());
		return patternEL;
	}

	@Override
	public Pattern_Basic copy() {
		return new Pattern_Basic(this.id, this.attributeVector.copy(), this.targetClass.copy());

	}

	@Override
	public boolean equals(Pattern<?> pattern) {
		if(!(pattern instanceof Pattern_Basic) ||
			!this.attributeVector.equals(pattern.getAttributeVector()) ||
			!this.targetClass.equals(pattern.getTargetClass())) return false;
		return true;
	}
}
