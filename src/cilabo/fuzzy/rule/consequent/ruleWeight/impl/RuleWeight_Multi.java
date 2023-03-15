package cilabo.fuzzy.rule.consequent.ruleWeight.impl;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.ruleWeight.AbstractRuleWeight;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * マルチラベル用ルール重みクラス rule weight class for multi-label
 * @author Takigawa Hiroki
 */
public final class RuleWeight_Multi extends AbstractRuleWeight <Double[]>{

	/**
	 * コンストラクタ．constructor
	 * @param ruleWeight ルール重み格納変数 vaiable of rule weight
	 */
	public RuleWeight_Multi(Double[] ruleWeight) {
		this.ruleWeight = ruleWeight;
	}

	/**
	 * 指定された位置にあるルール重みを返します。<br>
	 * Returns rule weight at the specified position
	 * @param index 返されるルール重みのインデックス．index of rule weight to return
	 * @return 指定された位置にあるルール重み．rule weight at the specified position in the list
	 */
	public Double getRuleWeightAt(int index) {
		return this.ruleWeight[index];
	}

	@Override
	public Double getRuleWeightValue() {
		return this.getRuleWeightAt(0);
	}

	/**
	 * 指定された位置にあるルール重みを、入力されたルール重みで置き換えます。<br>
	 * Replaces rule weight at the specified position in the list with the specified rule weight.
	 * @param index 置換されるルール重みのインデックス．index of rule weight to replace
	 * @param ruleWeight 指定された位置に格納されるルール重み．rule weightt to be stored at the specified position
	 */
	public void setRuleWeightAt(int index, Double ruleWeight) {
		this.ruleWeight[index] = ruleWeight;
	}

	/**
	 * このインスタンスが持つルール重み配列長を返します。<br>
	 * Returns length of rule weight array that this instance has.
	 * @return 返されるルール重み配列長．length of rule weight array to return
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
		String str = String.format("%.4f..", this.ruleWeight[0]);
		for(int i=1; i<this.ruleWeight.length; i++) {
			str += String.format(", %.4f..", this.ruleWeight[i]);
		}
		return str;
	}

	@Override
	public Element toElement() {
		Element ruleWeight = XML_manager.getInstance().createElement(XML_TagName.ruleWeightMulti);
		for(int i=0; i<this.ruleWeight.length; i++) {
			XML_manager.getInstance().addElement(ruleWeight, XML_TagName.ruleWeight, String.valueOf(this.ruleWeight[i]),
					XML_TagName.id, String.valueOf(i));
		}
		return ruleWeight;
	}
}
