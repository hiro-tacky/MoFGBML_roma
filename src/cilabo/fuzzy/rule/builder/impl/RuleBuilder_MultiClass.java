package cilabo.fuzzy.rule.builder.impl;

import org.uma.jmetal.util.checking.Check;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.impl.Antecedent_Basic;
import cilabo.fuzzy.rule.builder.RuleBuilder;
import cilabo.fuzzy.rule.builder.RuleBuilderCore;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.impl.Consequent_MultiClass;
import cilabo.fuzzy.rule.impl.Rule_MultiClass;
import xml.XML_TagName;

/**
 * マルチラベル用ルールビルダー rule builder for multi-label
 * @author Takigawa Hiroki
 */
public final class RuleBuilder_MultiClass
		extends RuleBuilderCore<Rule_MultiClass>
		implements RuleBuilder<Rule_MultiClass>{

	/** 結論部クラスとルール重みの算出及び後件部を生成用クラス. <br> generater of conclusion class label, rule weight and consequent class*/
	protected ConsequentFactory<Consequent_MultiClass> consequentFactory;

	/**
	 * コンストラクタ．constructor
	 * @param antecedentFactory 前件部のファジィ集合のインデックス配列生成用クラス generater of array of fuzzy set index
	 * @param consequentFactory 結論部クラスとルール重みの算出及び後件部を生成用クラス generater of conclusion class label, rule weight and consequent class
	 */
	public RuleBuilder_MultiClass(AntecedentIndexFactory antecedentFactory,
			ConsequentFactory<Consequent_MultiClass> consequentFactory) {
		super(antecedentFactory);
		this.consequentFactory = consequentFactory;
	}

	/** 入力された遺伝子情報と前件部オブジェクトを基に後件部オブジェクトを生成する
	 * Set consequent class from antecedent class and antecedent index
	 * @param antecedent 前件部オブジェクト antecedent class
	 * @param antecedentIndex 遺伝子情報 antecedent index
	 * @return 生成された後件部 generated consequent class*/
	public Consequent_MultiClass learning(Antecedent_Basic antecedent, int[] antecedentIndex) {
		Check.isNotNull(antecedent);
		Check.isNotNull(antecedentIndex);
		return this.consequentFactory.learning(antecedent, antecedentIndex);
	}

	@Override
	public Rule_MultiClass createRule(int[] antecedentIndex) {
		Antecedent_Basic antecedent = new Antecedent_Basic();
		Consequent_MultiClass consequent = this.learning(antecedent, antecedentIndex);
		return new Rule_MultiClass(antecedent, consequent);
	}

	@Override
	public Rule_MultiClass createRule(Element michiganSolution) {
		Antecedent_Basic antecedent = new Antecedent_Basic();
		Element ruleEL = (Element) michiganSolution.getElementsByTagName(XML_TagName.rule.toString()).item(0);
		Element consequentEL = (Element) ruleEL.getElementsByTagName(XML_TagName.consequent.toString()).item(0);

		NodeList classLabelNL = consequentEL.getElementsByTagName(XML_TagName.classLabelMulti.toString());
		Integer[] classLabel = new Integer[classLabelNL.getLength()];
		for(int i=0; i<classLabelNL.getLength(); i++) {
			classLabel[i] = Integer.valueOf(classLabelNL.item(i).getTextContent());
		}

		NodeList ruleWeightNL = consequentEL.getElementsByTagName(XML_TagName.ruleWeightMulti.toString());
		Double[] ruleWeight = new Double[ruleWeightNL.getLength()];
		for(int i=0; i<ruleWeightNL.getLength(); i++) {
			ruleWeight[i] = Double.valueOf(ruleWeightNL.item(i).getTextContent());
		}

		Consequent_MultiClass consequent = new Consequent_MultiClass(classLabel, ruleWeight);
		return new Rule_MultiClass(antecedent, consequent);
	}

	@Override
	public RuleBuilder_MultiClass copy() {
		return new RuleBuilder_MultiClass(this.antecedentFactory.copy(), this.consequentFactory.copy());
	}

}
