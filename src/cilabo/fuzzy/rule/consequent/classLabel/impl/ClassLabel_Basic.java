package cilabo.fuzzy.rule.consequent.classLabel.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.classLabel.AbstractClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * 標準的ラベルクラス．basic label class
 * @author Takigawa Hiroki
 */
public final class ClassLabel_Basic extends AbstractClassLabel <Integer>{

	/**
	 * コンストラクタ．constructor
	 * @param classLabel クラスラベル class label
	 */
	public ClassLabel_Basic(Integer classLabel) {
		this.classLabel = classLabel;
	}

	/**
	 * 入力された ClassLabel インスタンス と このインスタンスを比較する
	 * Comapre this instance and given class label. when given one equals own, return true, otherwise return false.
	 * @param classLabel 比較したいClassLabel class label to be compared
	 * @return 同値である場合(equal):true 同値でない場合(not equal):false */
	public boolean equalsClassLabel(int classLabel) {
		//クラスラベルの値が同値か調べる
		return this.getClassLabelVariable().equals(classLabel);
	}

	@Override
	public boolean equals(ClassLabel<?> classLabel) {
		//同じクラスのオブィエクトか調べる
		if(!(classLabel instanceof ClassLabel_Basic)) {return false;}
		//クラスラベルの値が同値か調べる
		if( !((ClassLabel_Basic)classLabel).getClassLabelVariable().equals(this.getClassLabelVariable())){return false;}
		return true;
	}

	@Override
	public boolean isRejectedClassLabel() {
		return this.classLabel.equals(RejectedClassLabel);
	}

	@Override
	public void setRejectedClassLabel(){
		this.setClassLabelVariable(RejectedClassLabel);
	}

	@Override
	public ClassLabel_Basic copy() {
		return new ClassLabel_Basic(this.classLabel);
	}

	@Override
	public String toString() {
		String str = String.format("%2d", this.classLabel);
		return str;
	}

	@Override
	public Element toElement() {
		Element classLabel = XML_manager.getInstance().createElement(XML_TagName.classLabel, this.classLabel);
		return classLabel;
	}
}
