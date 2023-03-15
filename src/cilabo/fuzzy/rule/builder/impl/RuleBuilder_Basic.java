package cilabo.fuzzy.rule.builder.impl;

import org.uma.jmetal.util.checking.Check;
import org.w3c.dom.Element;

import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.builder.RuleBuilder;
import cilabo.fuzzy.rule.builder.RuleBuilderCore;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.impl.Consequent_Basic;
import cilabo.fuzzy.rule.impl.Rule_Basic;
import xml.XML_TagName;

/**
 * 標準的ルールビルダー basic rule builder
 * @author Takigawa Hiroki
 */
public final class RuleBuilder_Basic
		extends RuleBuilderCore<Rule_Basic>
		implements RuleBuilder<Rule_Basic>{

	/** 結論部クラスとルール重みの算出及び後件部を生成用クラス. <br> generater of conclusion class label, rule weight and consequent class*/
	protected ConsequentFactory<Consequent_Basic> consequentFactory;

	/**
	 * コンストラクタ．constructor
	 * @param antecedentFactory 前件部のファジィ集合のインデックス配列生成用クラス generater of array of fuzzy set index
	 * @param consequentFactory 結論部クラスとルール重みの算出及び後件部を生成用クラス generater of conclusion class label, rule weight and consequent class
	 */
	public RuleBuilder_Basic(AntecedentIndexFactory antecedentFactory,
			ConsequentFactory<Consequent_Basic> consequentFactory) {
		super(antecedentFactory);
		this.consequentFactory = consequentFactory;
	}

	/** 入力された遺伝子情報と前件部オブジェクトを基に後件部オブジェクトを生成する
	 * Set consequent class from antecedent class and antecedent index
	 * @param antecedent 前件部オブジェクト antecedent class
	 * @param antecedentIndex 遺伝子情報 antecedent index
	 * @return 生成された後件部 generated consequent class*/
	public Consequent_Basic learning(Antecedent_Basic antecedent, int[] antecedentIndex) {
		Check.isNotNull(antecedent);
		Check.isNotNull(antecedentIndex);
		return this.consequentFactory.learning(antecedent, antecedentIndex);
	}

	@Override
	public Rule_Basic createRule(int[] antecedentIndex) {
		Antecedent_Basic antecedent = new Antecedent_Basic();
		Consequent_Basic consequent = this.learning(antecedent, antecedentIndex);
		return new Rule_Basic(antecedent, consequent);
	}

	@Override
	public Rule_Basic createRule(Element michiganSolutionEL) {
		Antecedent_Basic antecedent = new Antecedent_Basic();
		Element ruleEL = (Element) michiganSolutionEL.getElementsByTagName(XML_TagName.rule.toString()).item(0);
		Element consequentEl = (Element) ruleEL.getElementsByTagName(XML_TagName.consequent.toString()).item(0);
		int classLabel = Integer.valueOf(consequentEl.getElementsByTagName(XML_TagName.classLabel.toString()).item(0).getTextContent());
		double ruleWeight = Double.valueOf(consequentEl.getElementsByTagName(XML_TagName.ruleWeight.toString()).item(0).getTextContent());

		Consequent_Basic consequent = new Consequent_Basic(classLabel, ruleWeight);
		return new Rule_Basic(antecedent, consequent);
	}

	@Override
	public RuleBuilder_Basic copy() {
		return new RuleBuilder_Basic(this.antecedentFactory.copy(), this.consequentFactory.copy());
	}
}
