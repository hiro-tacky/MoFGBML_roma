package cilabo.fuzzy.rule.consequent.factory.impl;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactoryCore;
import cilabo.fuzzy.rule.consequent.impl.Consequent_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import cilabo.utility.Parallel;

/** 入力された前件部から後件部クラスConsequent_Basicを生成する
 * @author Takigawa Hiroki */
public final class MoFGBML_Learning
		extends ConsequentFactoryCore<ClassLabel_Basic, RuleWeight_Basic, double[]>
		implements ConsequentFactory <Consequent_Basic>{

	/**コンストラクタ
	 * @param train 生成時に用いる学習用データ */
	public MoFGBML_Learning(DataSet<Pattern<ClassLabel_Basic>> train) {
		this.train = train;
	}

	@Override
	public Consequent_Basic learning(Antecedent antecedent, int[] antecedentIndex, double limit) {
		double[] confidence = this.calculateConfidence(antecedent, antecedentIndex);
		ClassLabel_Basic classLabel = this.calculateClassLabel(confidence);
		RuleWeight_Basic ruleWeight = this.calculateRuleWeight(classLabel, confidence, limit);

		Consequent_Basic consequent = new Consequent_Basic(classLabel, ruleWeight);
		return consequent;
	}

	@Override
	public Consequent_Basic learning(Antecedent antecedent, int[] antecedentIndex) {
		return this.learning(antecedent, antecedentIndex, defaultLimit);
	}

	@Override
	public double[] calculateConfidence(Antecedent antecedent, int[] antecedentIndex) {
		if(Objects.isNull(antecedentIndex)){
			System.out.print("antecedentIndex i null@" + this.getClass().getSimpleName());
		}
		int Cnum = train.getNumberOfClass();
		double[] confidence = new double[Cnum];

		// 各クラスのパターンに対する適合度の総和
		double[] sumCompatibleGradeForEachClass = new double[Cnum];

		for(int c = 0; c < Cnum; c++) {
			final Integer CLASSNUM = c;
			Optional<Double> partSum = null;
			try {
				partSum = Parallel.getInstance().getLearningForkJoinPool().submit( () ->
					train.getPatterns().parallelStream()
						// 正解クラスが「CLASS == c」のパターンを抽出
						.filter(pattern -> pattern.getTargetClass().equalsClassLabel(CLASSNUM))
						// 各パターンの入力ベクトルを抽出
						.map(pattern -> pattern.getAttributeVector())
						// 各入力ベクトルとantecedentのcompatible gradeを計算
						.map(attributeVector -> antecedent.getCompatibleGradeValue(antecedentIndex, attributeVector))
						// compatible gradeを総和する
						.reduce( (sum, grade) -> sum+grade)
				).get();
			}catch (InterruptedException | ExecutionException e) {
				System.err.print(e);
				throw new IllegalArgumentException(e + " @" + this.getClass().getSimpleName());
			}
			sumCompatibleGradeForEachClass[c] = partSum.orElse(0.0);
		}

		// 全パターンに対する適合度の総和
		double allSum = Arrays.stream(sumCompatibleGradeForEachClass).sum();
		if(allSum != 0) {
			for(int c = 0; c < Cnum; c++) {
				confidence[c] = sumCompatibleGradeForEachClass[c] / allSum;
			}
		}
		return confidence;
	}

	/**
	 * 信頼度から結論部クラスを決定する<br>
	 * confidence[]が最大となるクラスを結論部クラスとする<br>
	 * もし、同じ値をとるクラスが複数存在する場合は生成不可能なルール(-1)とする．<br>
	 *
	 * @param confidence クラス別の信頼度 confidences for each class
	 * @return 結論部クラスラベル decided conclusion class label
	 */
	public ClassLabel_Basic calculateClassLabel(double[] confidence) {
		double max = -Double.MAX_VALUE;
		int consequentClass = -1;
		ClassLabel_Basic classLabel;

		for(int i = 0; i < confidence.length; i++) {
			if(max < confidence[i]) {
				max = confidence[i];
				consequentClass = i;
			}
			else if(max == confidence[i]) {
				consequentClass = -1;
			}
		}
		if(consequentClass < 0) { classLabel = new ClassLabel_Basic(-1); classLabel.setRejectedClassLabel();}
		else { classLabel = new ClassLabel_Basic(consequentClass); }

		return classLabel;
	}

	@Override
	public RuleWeight_Basic calculateRuleWeight(ClassLabel_Basic classLabel, double[] confidence, double limit) {

		RuleWeight_Basic zeroWeight = new RuleWeight_Basic(0.0);
		// 生成不可能ルール判定
		if(classLabel.isRejectedClassLabel()) {
			return zeroWeight;
		}

		int C = (int) classLabel.getClassLabelVariable();
		double sumConfidence = Arrays.stream(confidence).sum();
		double CF = confidence[C] - (sumConfidence - confidence[C]);
		if(CF <= limit) {
			classLabel.setRejectedClassLabel();
			return zeroWeight;
		}
		RuleWeight_Basic ruleWeight = new RuleWeight_Basic(CF);
		return ruleWeight;
	}

	@Override
	public String toString() {
		return "MoFGBML_Learning {defaultLimit:" + defaultLimit + "}";
	}

	@Override
	public MoFGBML_Learning copy() {
		return new MoFGBML_Learning(this.train.copy());
	}
}