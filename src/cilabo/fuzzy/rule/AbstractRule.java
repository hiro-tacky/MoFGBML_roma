package cilabo.fuzzy.rule;

import java.util.Objects;

import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.antecedent.factory.AntecedentIndexFactory;
import cilabo.fuzzy.rule.consequent.Consequent;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/** if-thenルールを表現する抽象クラス
 * @author hirot
 */
public abstract class AbstractRule <AntecedentObject extends Antecedent, ConsequentObject extends Consequent<classLabel, ruleWeight>,
	classLabel extends ClassLabel<?>, ruleWeight extends RuleWeight<?>>
	implements Rule<AntecedentObject, ConsequentObject, classLabel, ruleWeight>{

	/** 前件部クラス */
	protected AntecedentObject antecedent;
	/** 後件部クラス */
	protected ConsequentObject consequent;

	public AbstractRule(AntecedentObject antecedent, ConsequentObject consequent) {
		this.antecedent = antecedent;
		this.consequent = consequent;
	}

	@Override
	public double[] getCompatibleGrade(int[] antecedentIndex, double[] x) {
		return this.antecedent.getCompatibleGrade(antecedentIndex, x);
	}

	@Override
	public double getCompatibleGradeValue(int[] antecedentIndex, double[] x) {
		return this.antecedent.getCompatibleGradeValue(antecedentIndex, x);
	}

	@Override
	public int getRuleLength(int[] antecedentIndex) {
		return this.getAntecedent().getRuleLength(antecedentIndex);
	}

	@Override
	public boolean equalsClassLabel(ClassLabel<?> classLabel) {
		return this.consequent.getClassLabel().equalsClassLabel(classLabel);
	}

	@Override
	public boolean isRejectedClassLabel() {
		return this.consequent.getClassLabel().isRejectedClassLabel();
	}

	public abstract static class RuleBuilderCore<
		AntecedentObject extends Antecedent,
		ConsequentObject extends Consequent<?, ?>>{

		protected AntecedentIndexFactory antecedentFactory;
		protected ConsequentFactory<ConsequentObject> consequentFactory;

		public RuleBuilderCore(
				AntecedentIndexFactory antecedentFactory,
				ConsequentFactory<ConsequentObject> consequentFactory
		) {
			this.antecedentFactory = antecedentFactory;
			this.consequentFactory = consequentFactory;
		}

		protected int[] createAntecedentIndex() {
			return antecedentFactory.create();
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
