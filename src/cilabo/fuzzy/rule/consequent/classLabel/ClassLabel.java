package cilabo.fuzzy.rule.consequent.classLabel;

import org.w3c.dom.Element;

/** 結論部ラベルクラス．conclusion label class<br>
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelValue> 実装したクラスラベルが結論部クラスとして持つ変数の型
 */
public interface ClassLabel<ClassLabelValue> {

	/** クラスラベルの結論部クラス変数を代入<br>
	 * @param classLabelValue 代入されるクラスラベル */
	public void setClassLabelValue(ClassLabelValue classLabelValue);

	/** クラスラベルの結論部クラス変数を取得<br>
	 * @return このインスタンスが持つクラスラベル */
	public ClassLabelValue getClassLabelValue();

	/**
	 * 入力された ClassLabel インスタンス と このインスタンスを比較する
	 * @param classLabel 比較したい ClassLabel インスタンス
	 * @return 同値である場合:true 同値でない場合:false */
	public boolean equalsClassLabel(ClassLabel<?> classLabel);

	/**
	 * 入力された ClassLabel インスタンス と このインスタンスを比較する
	 * @param classLabel 比較したい ClassLabel インスタンス
	 * @return 同値である場合:true 同値でない場合:false */
	public boolean equalsClassLabel(int classLabel);

	/** このインスタンスが拒否クラスラベルかを判断する
	 * @return 拒否クラスラベルである場合:true <br>拒否クラスラベルでない場合:false*/
	public boolean isRejectedClassLabel();

	/** このインスタンスを拒否クラスラベルとして設定する */
	public void setRejectedClassLabel();

	/**	このインスタンスのディープコピーを返す
	 * @return ディープコピーされたこのインスタンス */
	public ClassLabel<ClassLabelValue> copy();

	/**xmlファイル出力用のElementを書き出す
	 * @return クラス内部の情報が書き込まれた Element インスタンス */
	public Element toElement();
}
