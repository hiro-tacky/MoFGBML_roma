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

/**
 * ルールの抽象クラス．基本的なメソッドを実装
 * @author Takigawa Hiroki
 *
 * @param <AntecedentObject> ルールオブジェクトが持つ前件部のクラス
 * @param <ConsequentObject> ルールオブジェクトが持つ後件部のクラス
 * @param <classLabel> 後件部クラスが扱う結論部クラス
 * @param <T1> 結論部クラスが扱うクラスラベル変数
 * @param <ruleWeight> 後件部クラスが扱うルール重みクラス
 * @param <T2> ルール重みクラスが扱うルール重み変数
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

	/**
	 * ルール生成器の抽象クラス．
	 * @author Takigawa Hiroki
	 *
	 * @param <RuleObjects> 扱う(生成する)ルールクラス
	 * @param <AntecedentObject> RuleObjectsが扱う前件部クラス
	 * @param <ConsequentObject> RuleObjectsが扱う後件部クラス
	 */
	public abstract static class RuleBuilderCore<RuleObjects extends Rule<AntecedentObject, ConsequentObject, ?, ?, ?, ?>, AntecedentObject extends Antecedent,
		ConsequentObject extends Consequent<?, ?, ?, ?>>
		implements RuleBuilder<RuleObjects, AntecedentObject, ConsequentObject>{

		/** 前件部の遺伝子(ファジィ集合のインデックス配列)生成器 */
		protected AntecedentIndexFactory antecedentFactory;
		/** 後件部の設定器．後件部クラスとルール重みクラスを設定し，後件部クラスを生成する */
		protected ConsequentFactory<ConsequentObject> consequentFactory;

		public RuleBuilderCore(
				AntecedentIndexFactory antecedentFactory,
				ConsequentFactory<ConsequentObject> consequentFactory
		) {
			this.antecedentFactory = antecedentFactory;
			this.consequentFactory = consequentFactory;
		}

		@Override
		public int[] createAntecedentIndex() {
			return this.antecedentFactory.create();
		}

		@Override
		public int[][] createAntecedentIndex(int numberOfGenerateRule){
			int[][] return_buf = new int[numberOfGenerateRule][];
			for(int i=0; i<numberOfGenerateRule; i++) {
				return_buf[i] = this.createAntecedentIndex();
			}
			return return_buf;
		}

		@Override
		public int[] createAntecedentIndex(Pattern<?> pattern) {
			int[] antecedentIndex = null;
			if(this.antecedentFactory instanceof HeuristicRuleGenerationMethod) {
				antecedentIndex = ((HeuristicRuleGenerationMethod)this.antecedentFactory).calculateAntecedentPart(pattern);
			}else {
				throw new ClassCastException("antecedentFactory is not HeuristicRuleGenerationMethod@RuleBuilder_Basic.createRule");
			}
			return antecedentIndex;
		}

		@Override
		public int[] createAntecedentIndex(Element michiganSolution) {
			Element fuzzySetList_node = (Element) michiganSolution.getElementsByTagName(XML_TagName.fuzzySetList.toString()).item(0);
			NodeList fuzzySetIDs = fuzzySetList_node.getElementsByTagName(XML_TagName.fuzzySetID.toString());
			int[] return_buf = new int[fuzzySetIDs.getLength()];
			for(int i=0; i<fuzzySetIDs.getLength(); i++) {
				return_buf[i] = Integer.valueOf(fuzzySetIDs.item(i).getTextContent());
			}
			return return_buf;
		}

		@Override
		public ConsequentObject learning(AntecedentObject antecedent, int[] antecedentIndex) {
			if(Objects.isNull(consequentFactory)) {throw new NullPointerException("consequentFactory hasn't been initialised@RuleBuilderCore.learning");}
			if(Objects.isNull(antecedent)) {throw new IllegalArgumentException("antecedent is null@RuleBuilderCore.learning");}
			return this.consequentFactory.learning(antecedent, antecedentIndex);
		}
	}
}
