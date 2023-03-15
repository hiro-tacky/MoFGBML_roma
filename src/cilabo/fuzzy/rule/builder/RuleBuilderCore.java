package cilabo.fuzzy.rule.builder;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.Rule;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.factory.impl.HeuristicRuleGenerationMethod;
import xml.XML_TagName;

/**
 * ルールクラスビルダー標準的メソッド定義クラス this class defines basic methods of rule builder
 * @author Takigawa Hiroki
 *
 * @param <RuleClass> 生成されるルールクラス rule class to be generated
 */
public abstract class RuleBuilderCore <RuleClass extends Rule<?, ?>> implements RuleBuilder<RuleClass>{

	/** 前件部のファジィ集合のインデックス配列生成用クラス <br> generater of array of fuzzy set index */
	protected AntecedentIndexFactory antecedentFactory;

	/**
	 * コンストラクタ．constructor
	 * @param antecedentFactory 前件部のファジィ集合のインデックス配列生成用クラス generater of array of fuzzy set index
	 */
	public RuleBuilderCore(AntecedentIndexFactory antecedentFactory) {
		this.antecedentFactory = antecedentFactory;
	}

	@Override
	public int[] createAntecedentIndex() {
		return this.antecedentFactory.create();
	}

	@Override
	public int[][] createAntecedentIndex(int numberOfGenerateRule){
		int[][] generatedAntecedentIndex = new int[numberOfGenerateRule][];
		for(int i=0; i<numberOfGenerateRule; i++) {
			generatedAntecedentIndex[i] = this.createAntecedentIndex();
		}
		return generatedAntecedentIndex;
	}

	@Override
	public int[] createAntecedentIndex(Pattern<?> pattern) {
		int[] antecedentIndex = null;
		if(this.antecedentFactory instanceof HeuristicRuleGenerationMethod) {
			antecedentIndex = ((HeuristicRuleGenerationMethod)this.antecedentFactory).calculateAntecedentPart(pattern);
		}else {
			throw new ClassCastException("antecedentFactory is not HeuristicRuleGenerationMethod");
		}
		return antecedentIndex;
	}

	@Override
	public int[] createAntecedentIndex(Element michiganSolutionEL) {
		Element fuzzySetListEL = (Element) michiganSolutionEL.getElementsByTagName(XML_TagName.fuzzySetList.toString()).item(0);
		NodeList fuzzySetNL = fuzzySetListEL.getElementsByTagName(XML_TagName.fuzzySetID.toString());
		int[] generatedAntecedentIndex = new int[fuzzySetNL.getLength()];
		for(int i=0; i<fuzzySetNL.getLength(); i++) {
			generatedAntecedentIndex[i] = Integer.valueOf(fuzzySetNL.item(i).getTextContent());
		}
		return generatedAntecedentIndex;
	}
}
