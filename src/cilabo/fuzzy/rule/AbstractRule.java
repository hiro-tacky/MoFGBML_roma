package cilabo.fuzzy.rule;

import java.util.Objects;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.data.AttributeVector;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.antecedent.factory.impl.HeuristicRuleGenerationMethod;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;
import xml.XML_TagName;

/** if-thenルールを表現する抽象クラス
 * @author hirot
 */
public abstract class AbstractRule <AntecedentObject extends Antecedent,
	ConsequentObject extends Consequent<classLabel, T1, ruleWeight, T2>,
	classLabel extends ClassLabel<T1>, T1,
	ruleWeight extends RuleWeight<T2>, T2>
	implements Rule<AntecedentObject, ConsequentObject, classLabel, T1, ruleWeight, T2>{

	/** 前件部クラス */
	protected AntecedentObject antecedent;
	/** 後件部クラス */
	protected ConsequentObject consequent;

	public AbstractRule(AntecedentObject antecedent, ConsequentObject consequent) {
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	@Override
	public AntecedentObject getAntecedent() {
		return this.antecedent;
	}

	@Override
	public ConsequentObject getConsequent() {
		return this.consequent;
	}

	@Override
	public double[] getCompatibleGrade(int[] antecedentIndex, AttributeVector attributeVector) {
		return this.antecedent.getCompatibleGrade(antecedentIndex, attributeVector);
	}

	@Override
	public double getCompatibleGradeValue(int[] antecedentIndex, AttributeVector attributeVector) {
		return this.antecedent.getCompatibleGradeValue(antecedentIndex, attributeVector);
	}

	@Override
	public int getRuleLength(int[] antecedentIndex) {
		return this.getAntecedent().getRuleLength(antecedentIndex);
	}

	@Override
	public classLabel getClassLabel() {
		return this.consequent.getClassLabel();
	}

	@Override
	public T1 getClassLabelValue() {
		return this.consequent.getClassLabelValue();
	}

	@Override
	public boolean equalsClassLabel(ClassLabel<?> classLabel) {
		return this.consequent.getClassLabel().equalsClassLabel(classLabel);
	}

	@Override
	public boolean equalsClassLabel(int classLabel) {
		return this.consequent.equalsClassLabel(classLabel);
	}

	@Override
	public boolean isRejectedClassLabel() {
		return this.consequent.getClassLabel().isRejectedClassLabel();
	}

	@Override
	public void setRejectedClassLabel() {
		this.consequent.getClassLabel().setRejectedClassLabel();
	}

	@Override
	public ruleWeight getRuleWeight() {
		return this.consequent.getRuleWeight();
	}

	@Override
	public T2 getRuleWeightValue() {
		return this.consequent.getRuleWeightValue();
	}

	@Override
	public Double getRuleWeightDouble() {
		return this.consequent.getRuleWeightDouble();
	}

	@Override
	public void setRuleWeightValue(T2 ruleWeightValue) {
		this.consequent.setRuleWeightValue(ruleWeightValue);
	}

	public abstract static class RuleBuilderCore<AntecedentObject extends Antecedent,
		ConsequentObject extends Consequent<?, ?, ?, ?>>{

		protected AntecedentIndexFactory antecedentFactory;
		protected ConsequentFactory<ConsequentObject> consequentFactory;

		public RuleBuilderCore(
				AntecedentIndexFactory antecedentFactory,
				ConsequentFactory<ConsequentObject> consequentFactory
		) {
			this.antecedentFactory = antecedentFactory;
			this.consequentFactory = consequentFactory;
		}

		/** 前件部のファジィセットのインデックス配列を返す
		 * @return 生成されたRuleオブジェクトの配列*/
		public int[] createAntecedentIndex() {
			return this.antecedentFactory.create();
		}

		/** 前件部のファジィセットのインデックス配列を複数返す
		 * @param numberOfGenerateRule 生成する前件部の数
		 * @return 生成されたRuleオブジェクトの配列*/
		public int[][] createAntecedentIndex(int numberOfGenerateRule){
			int[][] return_buf = new int[numberOfGenerateRule][];
			for(int i=0; i<numberOfGenerateRule; i++) {
				return_buf[i] = this.createAntecedentIndex();
			}
			return return_buf;
		}

		/** 前件部のファジィセットのインデックス配列を返す
		 * @param pattern 前件部生成の学習に使用するPatternクラス
		 * @return 生成された前件部のファジィセットのインデックス配列
		 */
		public int[] createAntecedentIndex(Pattern<?> pattern) {
			int[] antecedentIndex = null;
			if(this.antecedentFactory instanceof HeuristicRuleGenerationMethod) {
				antecedentIndex = ((HeuristicRuleGenerationMethod)this.antecedentFactory).calculateAntecedentPart(pattern);
			}else {
				throw new ClassCastException("antecedentFactory is not HeuristicRuleGenerationMethod@RuleBuilder_Basic.createRule");
			}
			return antecedentIndex;
		}

		public int[] createAntecedentIndex(Element michiganSolution) {
			Element fuzzySetList_node = (Element) michiganSolution.getElementsByTagName(XML_TagName.fuzzySetList.toString()).item(0);
			NodeList fuzzySetIDs = fuzzySetList_node.getElementsByTagName(XML_TagName.fuzzySetID.toString());
			int[] return_buf = new int[fuzzySetIDs.getLength()];
			for(int i=0; i<fuzzySetIDs.getLength(); i++) {
				return_buf[i] = Integer.valueOf(fuzzySetIDs.item(i).getTextContent());
			}
			return return_buf;
		}

		/** 入力された遺伝子情報と前件部オブジェクトを基に後件部オブジェクトを生成する
		 * @param antecedent 前件部オブジェクト
		 * @param antecedentIndex 遺伝子情報
		 * @return 生成された後件部 */
		public ConsequentObject learning(AntecedentObject antecedent, int[] antecedentIndex) {
			if(Objects.isNull(consequentFactory)) {throw new NullPointerException("consequentFactory hasn't been initialised@RuleBuilderCore.learning");}
			if(Objects.isNull(antecedent)) {throw new IllegalArgumentException("antecedent is null@RuleBuilderCore.learning");}
			return this.consequentFactory.learning(antecedent, antecedentIndex);
		}
	}
}
