package cilabo.fuzzy.rule.consequent.classLabel;

import org.w3c.dom.Element;

/** クラスラベルのインターフェイス<br>
 * 基本的なsetterやgetterや，クラスラベルインスタンス同士を比較する機能を持つ
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelValue> 実装したクラスラベルが結論部クラスとして持つ変数のクラス
 */
public interface ClassLabel<ClassLabelValue> {

	/** 結論部クラス変数のコピーを取得<br>
	 * @return このインスタンスが持つクラスラベルのコピー	 */
	public ClassLabelValue getClassLabelValue();

	/** クラスラベルの結論部クラス変数を代入<br>
	 * @param classLabel 代入されるクラスラベル */
	public void setClassLabelValue(ClassLabelValue classLabelValue);

	/**
	 * 入力された ClassLabel インスタンス と このインスタンスを比較する
	 * @param classLabel 比較したい ClassLabel インスタンス
	 * @return 同値である場合:true 同値でない場合:false */
	public boolean equals(ClassLabel<ClassLabelValue> classLabel);

	/**
	 * 入力された クラスラベルの変数 と このインスタンスの持つ結論部クラス変数を比較する
	 * @param classLabel 比較したい クラスラベルの値
	 * @return 同値である場合:true <br>同値でない場合:false
	 */
	public boolean equalsValueOf(ClassLabelValue classLabelValue);

	/** このインスタンスが拒否クラスラベルかを判断する
	 * @return 拒否クラスラベルである場合:true <br>拒否クラスラベルでない場合:false*/
	public boolean isRejectedClassLabel();

	/**	このインスタンスのディープコピーを返す
	 * @return ディープコピーされたこのインスタンス */
	public ClassLabel<ClassLabelValue> copy();

	/**xmlファイル出力用のElementを書き出す
	 * @return クラス内部の情報が書き込まれた Element インスタンス */
	public Element toElement();
}
