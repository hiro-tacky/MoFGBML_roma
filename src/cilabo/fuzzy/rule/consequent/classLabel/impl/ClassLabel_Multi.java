package cilabo.fuzzy.rule.consequent.classLabel.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.classLabel.AbstractClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * マルチラベル用ラベルクラス．label class for multi-label
 * @author Takigawa Hiroki
 */
public final class ClassLabel_Multi extends AbstractClassLabel <Integer[]> {

	/**
	 * コンストラクタ．constructor
	 * @param classLabel クラスラベル配列 array of class label
	 */
	public ClassLabel_Multi(Integer[] classLabel) {
		this.classLabel = classLabel;
	}

	/**
	 * 指定された位置にあるクラスラベルを返します。<br>
	 * Returns class label at the specified position
	 * @param index 返されるクラスラベルのインデックス．index of class label to return
	 * @return 指定された位置にあるクラスラベル．class label at the specified position in the list
	 */
	public Integer getClassLabel(int index) {
		return this.classLabel[index];
	}

	/**
	 * 指定された位置にあるクラスラベルを、入力されたクラスラベルで置き換えます。<br>
	 * Replaces class label at the specified position in the list with the specified class label.
	 * @param index 置換されるクラスラベルのインデックス．index of class label to replace
	 * @param classLabel 指定された位置に格納されるクラスラベル．class labelt to be stored at the specified position
	 */
	public void setClassLabel(int index, int classLabel) {
		this.classLabel[index] = classLabel;
	}

	/**
	 * このインスタンスが持つクラスラベル配列長を返します。<br>
	 * Returns length of class label array that this instance has.
	 * @return 返されるクラスラベル配列長．length of class label array to return
	 */
	public int getClassLabelLength() {
		return this.classLabel.length;
	}

	@Override
	public boolean equals(ClassLabel<?> classLabel) {
		//同じクラスのオブィエクトか調べる
		if(!(classLabel instanceof ClassLabel_Multi)) {return false;}
		else {
			ClassLabel_Multi classLabel_tmp = (ClassLabel_Multi)classLabel;
			//配列長の検証
			if( classLabel_tmp.getClassLabelVariable().length != this.classLabel.length ) {return false;}

			for(int i=0; i<this.classLabel.length; i++) {
				//クラスラベルの値が同値か調べる
				if( !classLabel_tmp.getClassLabelVariable()[i].equals(this.getClassLabelVariable()[i]) ){return false;}
			}
			return true;
		}
	}

	/**
	 * 入力された ClassLabel インスタンス と このインスタンスを比較する
	 * Comapre this instance and given instance. when given one equals own, return true, otherwise return false.
	 * @param index 比較するクラスラベルのインデックス index of class label to be compared
	 * @param classLabel 比較したいClassLabel class label to be compared
	 * @return 同値である場合(equal):true 同値でない場合(not equal):false */
	public boolean equalsClassLabel(int index, int classLabel) {
		//クラスラベルの値が同値か調べる
		return this.getClassLabelVariable()[index].equals(classLabel);
	}

	@Override
	public boolean isRejectedClassLabel() {
		boolean flag = false;
		for(Integer buf: this.classLabel) {
			if(buf == RejectedClassLabel) flag = true;
		}
		return flag;
	}

	@Override
	public void setRejectedClassLabel(){
		this.classLabel[0] = RejectedClassLabel;
	}

	@Override
	public ClassLabel_Multi copy() {
		return new ClassLabel_Multi(this.classLabel);
	}

	@Override
	public String toString() {
		String str = String.format("%2d", this.classLabel[0]);
		for(int i=1; i<this.classLabel.length; i++) {
			str += String.format(", %2d", this.classLabel[i]);
		}
		return str;
	}

	@Override
	public Element toElement() {
		Element classLabel = XML_manager.getInstance().createElement(XML_TagName.classLabelMulti);
		for(int i=0; i<this.classLabel.length; i++) {
			XML_manager.getInstance().addElement(classLabel, XML_TagName.classLabel, this.classLabel[i],
					XML_TagName.id, String.valueOf(i));
		}
		return classLabel;
	}
}
