package cilabo.fuzzy.rule.consequent.classLabel;

import org.w3c.dom.Element;

/**
 * ラベルクラス．label class
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelVariable> 実装したクラスラベルが結論部クラスとして持つ変数の型(Integer, Integer[], ..., etc.) ルール重み格納変数 vaiable of label
 */
public interface ClassLabel<ClassLabelVariable> {

	/** 拒否ルール用クラスラベル*/
	public final static Integer RejectedClassLabel = -1;

	/**
	 * このインスタンスが持つクラスラベル変数を返します。<br>
	 * Returns vaiable of class label that this instance has.
	 * @return 返されるクラスラベル変数．vaiable of class label to return */
	public ClassLabelVariable getClassLabelVariable();

	/**
	 * クラスラベル変数を入力されたクラスラベル変数で置き換えます。<br>
	 * Replaces vaiable of class label in this instance.
	 * @param classLabelVariable このインスタンスに格納されるクラスラベル変数．vaiable of class label to be stored at this instance */
	public void setClassLabelVariable(ClassLabelVariable classLabelVariable);

	/**
	 * 入力された ClassLabel インスタンス と このインスタンスを比較する
	 * Comapre this instance and given instance. when given one equals own, return true, otherwise return false.
	 * @param classLabel 比較したいClassLabelインスタンス class label instance to be compared
	 * @return 同値である場合(equal):true 同値でない場合(not equal):false */
	public boolean equals(ClassLabel<?> classLabel);

	/**
	 * このインスタンスが拒否クラスラベルかを判断する
	 * Return this instance was set RejectedClassLabel or not.
	 * @return 拒否クラスラベルである場合(RejectedClassLabel):true <br>
	 * 拒否クラスラベルでない場合(not RejectedClassLabel):false*/
	public boolean isRejectedClassLabel();

	/**
	 * このインスタンスを拒否クラスラベルとして設定する
	 * Set this instance as a RejectedClassLabel
	 */
	public void setRejectedClassLabel();

	public ClassLabel<ClassLabelVariable> copy();

	public Element toElement();
}
