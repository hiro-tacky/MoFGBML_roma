package cilabo.fuzzy.rule.consequent.ruleWeight.impl;

import java.util.Objects;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.ruleWeight.AbstractRuleWeight;
import cilabo.main.impl.multiTasking.MultiTasking;
import xml.XML_TagName;
import xml.XML_manager;

@MultiTasking
public final class RuleWeight_Multi extends AbstractRuleWeight <Double[]>{

	/**
	 * 入力されたルール重みを持つインスタンスを生成する
	 * @param ruleWeight ルール重み
	 */
	public RuleWeight_Multi(Double[] ruleWeight) {
		this.ruleWeight = ruleWeight;
	}

	/**指定したインデックスのルール重みを取得
	 * @param index ルール重みの配列のインデックス
	 * @return 指定されたインデックスにあるルール重み
	 */
	public Double getRuleWeightAt(int index) {
		return this.ruleWeight[index];
	}

	/**指定したインデックスのルール重みを置き換え
	 * @param index 置き換えるルール重みのインデックス
	 * @param ruleWeight 指定されたインデックスに格納されるルール重み
	 */
	public void setRuleWeightAt(int index, Double ruleWeight) {
		this.ruleWeight[index] = ruleWeight;
	}

	/** ルール重み配列の長さを返す
	 * @return ルール重み配列の長さ
	 */
	public int getRuleWeightLength() {
		return this.ruleWeight.length;
	}

	@Override
	public RuleWeight_Multi copy() {
		return new RuleWeight_Multi(this.ruleWeight);
	}

	@Override
	public String toString() {
		if( Objects.isNull(this.ruleWeight) ) {
			throw new NullPointerException();
		}

		String str = String.format("%.4f..", this.ruleWeight[0]);
		if(this.ruleWeight.length < 2) {
			return str;
		}else {
			for(int i=1; i<this.ruleWeight.length; i++) {
				str += String.format(", %.4f..", this.ruleWeight[i]);
			}
			return str;
		}
	}

	@Override
	public Element toElement() {
		Element ruleWeight = XML_manager.createElement(XML_TagName.ruleWeightMulti);
		for(int i=0; i<this.ruleWeight.length; i++) {
			XML_manager.addElement(ruleWeight, XML_TagName.ruleWeight, String.valueOf(this.ruleWeight[i]),
					XML_TagName.id, String.valueOf(i));
		}
		return ruleWeight;
	}

}
