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
	public boolean equals(ClassLabel<Integer> classLabel) {
		//同じクラスのオブィエクトか調べる
		if(!(classLabel instanceof ClassLabel_Basic)) {return false;}
		//クラスラベルの値が同値か調べる
		if( !classLabel.getClassLabelValue().equals(this.getClassLabelValue()) ){return false;}
		return true;
	}

	@Override
	public boolean equalsValueOf(Integer x) {
		//クラスラベルの値が同値か調べる
		if(!(this.getClassLabelValue().equals(x))){return false;}
		return true;
	}

	@Override
	public Integer getClassLabelInteger() {
		return this.classLabel;
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
		Element classLabel = XML_manager.createElement(XML_TagName.classLabel, String.valueOf(this.classLabel));
		return classLabel;
	}

	@Override
	public boolean isRejectedClassLabel() {
		return this.classLabel == AbstractClassLabel.RejectedClassLabel;
	}

}
