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
	public Integer getClassLabel(int index) {
		if( Objects.isNull(this.classLabel) ) {
			throw new NullPointerException();
		}
		return this.classLabel[index];
	}

	/** 指定したインデックスのクラスラベルを置き換える
	 * @param index 置き換えるクラスラベルのインデックス
	 * @param classLabel 指定されたインデックスに格納されるクラスラベル
	 */
	public void setClassLabel(int index, int classLabel) {
		this.classLabel[index] = classLabel;
	}

	/** 結論部クラスの個数を取得する
	 * @return 結論部クラスの個数
	 */
	public int getClassLabelLength() {
		return this.classLabel.length;
	}

	@Override
	public boolean equalsClassLabel(ClassLabel<?> classLabel) {
		//同じクラスのオブィエクトか調べる
		if(!(classLabel instanceof ClassLabel_Multi)) {return false;}
		else {
			ClassLabel_Multi classLabel_tmp = (ClassLabel_Multi)classLabel;
			//配列長の検証
			if( classLabel_tmp.getClassLabelValue().length != this.classLabel.length ) {return false;}

			for(int i=0; i<this.classLabel.length; i++) {
				//クラスラベルの値が同値か調べる
				if( !classLabel_tmp.getClassLabelValue()[i].equals(this.getClassLabelValue()[i]) ){return false;}
			}
			return true;
		}
	}

	/**
	 * 入力されたインデックスに対応するのクラスラベルを比較する
	 * @param index 比較するクラスラベルのインデックス
	 * @param classLabel 比較対象となるクラスラベル
	 * @return 同値である場合:true 同値でない場合:false
	 */
	public boolean equalsClassLabel(int index, int classLabel) {
		//クラスラベルの値が同値か調べる
		return this.getClassLabelValue()[index].equals(classLabel);
	}

	@Override
	public boolean equalsClassLabel(int classLabel) {
		return this.classLabel[0].equals(classLabel);
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
		Element classLabel = XML_manager.getInstance().createElement(XML_TagName.classLabelMulti);
		for(int i=0; i<this.classLabel.length; i++) {
			XML_manager.getInstance().addElement(classLabel, XML_TagName.classLabel, String.valueOf(this.classLabel[i]),
					XML_TagName.id, String.valueOf(i));
		}
		return classLabel;
	}
}
