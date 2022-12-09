package cilabo.fuzzy.rule.consequent.classLabel.impl;

import java.util.Objects;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.classLabel.AbstractClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.main.impl.multiTasking.MultiTasking;
import xml.XML_TagName;
import xml.XML_manager;

@MultiTasking
public final class ClassLabel_Multi extends AbstractClassLabel <Integer[]> {

	/**
	 * 入力されたクラスラベルの配列を持つインスタンスを生成する
	 * @param classLabel クラスラベルの配列
	 */
	public ClassLabel_Multi(Integer[] classLabel) {
		this.classLabel = classLabel;
	}

	/** 指定したインデックスのクラスラベルを取得
	 * @param index クラスラベルの配列のインデックス
	 * @return 指定されたインデックスにあるクラスラベル
	 */
	public Integer getClassLabelValueAt(int index) {
		if( Objects.isNull(this.classLabel) ) {
			throw new NullPointerException();
		}
		return this.classLabel[index];
	}

	/** 指定したインデックスのクラスラベルを置き換え
	 * @param index 置き換えるクラスラベルのインデックス
	 * @param value 指定されたインデックスに格納されるクラスラベル
	 */
	public void setClassLabelValueAt(int index, Integer value) {
		this.classLabel[index] = value;
	}

	/** 結論部クラスの個数を取得する
	 * @return 結論部クラスの個数
	 */
	public int getClassLabelLength() {
		return this.classLabel.length;
	}

	@Override
	public ClassLabel_Multi copy() {
		return new ClassLabel_Multi(this.classLabel);
	}

	@Override
	public boolean equals(ClassLabel<Integer[]> x) {
		//同じクラスのオブィエクトか調べる
		if(!(x instanceof ClassLabel_Multi)) {return false;}
		//配列長の検証
		if( x.getClassLabelValue().length != this.classLabel.length ) {return false;}

		for(int i=0; i<this.classLabel.length; i++) {
			//クラスラベルの値が同値か調べる
			if( !x.getClassLabelValue()[i].equals(this.getClassLabelValue()[i]) ){return false;}
		}
		return true;
	}

	@Override
	public boolean equalsValueOf(Integer[] x) {
		//配列長の検証
		if(x.length != this.classLabel.length) {return false;}
		for(int i=0; i<this.classLabel.length; i++) {
			//クラスラベルの値が同値か調べる
			if(!this.getClassLabelValue()[i].equals(x[i])){return false;}
		}
		return true;
	}

	/** 入力されたインデックスのクラスラベルが同値か返す
	 * @param x 調べたいクラスラベル
	 * @return 同値である場合:true 同値でない場合:false
	 */
	public boolean equalsValueOf(int index, Integer x) {
		//クラスラベルの値が同値か調べる
		if(!this.getClassLabelValue()[index].equals(x)){return false;}
		return true;
	}

	@Override
	public String toString() {
		//null check
		if( Objects.isNull(this.classLabel) ) {
			throw new NullPointerException();
		}

		String str = String.format("%2d", this.classLabel[0]);
		if(this.classLabel.length < 2) {
			return str;
		}else {
			for(int i=1; i<this.classLabel.length; i++) {
				str += String.format(", %2d", this.classLabel[i]);
			}
			return str;
		}
	}

	@Override
	public Element toElement() {
		Element consequentClass = XML_manager.createElement(XML_TagName.ClassLabelList);
		for(int i=0; i<this.classLabel.length; i++) {
			XML_manager.addChildNode(consequentClass, XML_TagName.ClassLabel, String.valueOf(this.classLabel[i]),
					XML_TagName.ClassID, String.valueOf(i));
		}
		return consequentClass;
	}

	@Override
	public boolean isRejectedClassLabel() {
		boolean flag = true;
		for(Integer buf: this.classLabel) {
			if(buf == AbstractClassLabel.RejectedClassLabel) flag = false;
		}
		return flag;
	}
}
