package cilabo.fuzzy.rule.consequent.classLabel.impl;

import java.util.Objects;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.classLabel.AbstractClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import xml.XML_TagName;
import xml.XML_manager;

public final class ClassLabel_Basic extends AbstractClassLabel <Integer>{

	/**
	 * 入力されたクラスラベルを持つインスタンスを生成する
	 * @param classLabel クラスラベル
	 */
	public ClassLabel_Basic(Integer classLabel) {
		this.classLabel = classLabel;
	}

	@Override
	public boolean equalsClassLabel(ClassLabel<?> classLabel) {
		//同じクラスのオブィエクトか調べる
		if(!(classLabel instanceof ClassLabel_Basic)) {return false;}
		//クラスラベルの値が同値か調べる
		if( !((ClassLabel_Basic)classLabel).getClassLabelValue().equals(this.getClassLabelValue())){return false;}
		return true;
	}

	/** 入力されたクラスラベルIDとこのインスタンスを比較する
	 * @param classLabel 比較対象クラスラベルID
	 * @return 同値である場合:true 同値でない場合:false
	 */
	public boolean equalsClassLabel(int classLabel) {
		return this.classLabel.equals(classLabel);
	}

	@Override
	public boolean isRejectedClassLabel() {
		return this.classLabel.equals(RejectedClassLabel);
	}

	@Override
	public void setRejectedClassLabel(){
		this.setClassLabelValue(RejectedClassLabel);
	}

	@Override
	public ClassLabel_Basic copy() {
		return new ClassLabel_Basic(this.classLabel);
	}

	@Override
	public String toString() {
		if( Objects.isNull(this.classLabel) ) {
			throw new NullPointerException();
		}
		String str = String.format("%2d", this.classLabel);
		return str;
	}

	@Override
	public Element toElement() {
		Element classLabel = XML_manager.getInstance().createElement(XML_TagName.classLabel, String.valueOf(this.classLabel));
		return classLabel;
	}
}
