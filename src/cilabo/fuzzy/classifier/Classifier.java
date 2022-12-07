package cilabo.fuzzy.classifier;

import java.util.ArrayList;

import org.w3c.dom.Element;

import cilabo.data.InputVector;
import cilabo.fuzzy.classifier.classification.Classification;
import cilabo.fuzzy.rule.Rule;

public abstract class Classifier<RuleObject extends Rule>{

	/** 識別方式
	 * @see cilabo.fuzzy.classifier.classification */
	protected Classification<RuleObject> classification;


	public Classifier(Classification<RuleObject> classification) {
		this.classification = classification;
		ruleSet = new ArrayList<Rule>();
	}

	/** 未知パターンに対する勝利ルールを返す
	 * @param vector 未知パターンの属性値クラス
	 * @return 勝利ルール */
	public abstract Rule classify(InputVector vector);

	/**  このインスタンスのディープコピーを取得する
	 * @return  このインスタンスのディープコピー */
	public abstract Classifier<RuleObject> copy();

	public abstract Classifier<RuleObject> build();

	public void clear() {
		this.ruleSet.clear();
	}

	/**	ルール長を取得する */
	public int getRuleLength() {
		int length = 0;
		for(int i = 0; i < ruleSet.size(); i++) {
			length += ruleSet.get(i).getRuleLength();
		}
		return length;
	}

	/** ルール数を取得する */
	public int getRuleNum() {
		return this.ruleSet.size();
	}

	/**  idを指定してルールを取得<br>Returns the element at the specified position in this list.
	 * @param index id
	 * @return ルール  */
	public Rule getRule(int index) { return this.ruleSet.get(index); }

	/**  id指定でルールを取得<br>Returns the element at the specified position in this list.
	 * @param index id
	 * @return ルール */
	public void setRule(int index, RuleObject rule) {
		while(this.ruleSet.size() <= index) {
			this.ruleSet.add(null);
		}
		this.ruleSet.set(index, rule);
	}

	/** ルールを追加する
	 * @param rule 追加ルール */
	public void addRule(RuleObject rule) { this.ruleSet.add(rule); }

	public void setVariable(int rule_i, int index, int value) {
		this.ruleSet.get(rule_i).setAntecedentIndexAt(index, value);
	}

	public abstract Element toElemnt();

	public void removeRule(int index) {
		this.ruleSet.remove(index);
	}
}
