package cilabo.fuzzy.rule.consequent.factory.impl;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import cilabo.data.DataSet;
import cilabo.data.pattern.impl.Pattern_Basic;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.classLabel.AbstractClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactoryCore;
import cilabo.fuzzy.rule.consequent.impl.Consequent_Basic;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Basic;
import cilabo.utility.Parallel;

/** 入力された前件部から後件部クラスConsequent_Basicを生成する
 * @author Takigawa Hiroki */
public final class MoFGBML_Learning extends ConsequentFactoryCore
	<ClassLabel_Basic, RuleWeight_Basic, Consequent_Basic,double[]>
		implements ConsequentFactory <Consequent_Basic>{

	/**コンストラクタ
	 * @param train 生成時に用いる学習用データ */
	public MoFGBML_Learning(DataSet train) {
		this.train = train;
	}

	@Override
	public Consequent_Basic learning(Antecedent antecedent, int[] antecedentIndex, double limit) {
		double[] confidence = this.calcConfidence(antecedent, antecedentIndex);
		ClassLabel_Basic classLabel = this.calcClassLabel(confidence);
		RuleWeight_Basic ruleWeight = this.calcRuleWeight(classLabel, confidence, limit);

		Consequent_Basic consequent = new Consequent_Basic(classLabel, ruleWeight);
		return consequent;
	}

	@Override
	public Consequent_Basic learning(Antecedent antecedent, int[] antecedentIndex) {
		return this.learning(antecedent, antecedentIndex, defaultLimit);
	}

	@Override
	public double[] calcConfidence(Antecedent antecedent, int[] antecedentIndex) {
		int Cnum = train.getCnum();
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
						.filter(pattern -> ((Pattern_Basic)pattern).getTrueClass().equalsValueOf(CLASSNUM))
						// 各パターンの入力ベクトルを抽出
						.map(pattern -> pattern.getInputVector().getVector())
						// 各入力ベクトルとantecedentのcompatible gradeを計算
						.map(x -> antecedent.getCompatibleGradeValue(antecedentIndex, x))
						// compatible gradeを総和する
						.reduce( (sum, grade) -> sum+grade)
				).get();
			}
			catch (InterruptedException | ExecutionException e) {
				System.out.println(e);
				return null;
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
	 * <h1>結論部クラス</h1></br>
	 * 信頼度から結論部クラスを決定する</br>
	 * confidence[]が最大となるクラスを結論部クラスとする</br>
	 * </br>
	 * もし、同じ値をとるクラスが複数存在する場合は生成不可能なルール(-1)とする．</br>
	 * </br>
	 *
	 * @param confidence クラス別信頼度
	 * @return クラスラベル
	 */
	@Override
	public ClassLabel_Basic calcClassLabel(double[] confidence) {
		double max = Double.MIN_VALUE;
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
		if(consequentClass < 0) { classLabel = new ClassLabel_Basic(AbstractClassLabel.RejectedClassLabel); }
		else { classLabel = new ClassLabel_Basic(consequentClass); }

		return classLabel;
	}

	@Override
	public RuleWeight_Basic calcRuleWeight(ClassLabel_Basic classLabel, double[] confidence, double limit) {

		RuleWeight_Basic zeroWeight = new RuleWeight_Basic(0.0);
		// 生成不可能ルール判定
		if(classLabel.equalsValueOf(AbstractClassLabel.RejectedClassLabel)) {
			return zeroWeight;
		}

		int C = (int) classLabel.getClassLabelValue();
		double sumConfidence = Arrays.stream(confidence).sum();
		double CF = confidence[C] - (sumConfidence - confidence[C]);
		if(CF <= limit) {
			classLabel.setClassLabelValue(AbstractClassLabel.RejectedClassLabel);
			return zeroWeight;
		}
		RuleWeight_Basic ruleWeight = new RuleWeight_Basic(CF);
		return ruleWeight;
	}

	@Override
	public DataSet getTrain() {
		return this.train;
	}

	@Override
	public String toString() {
		return "MoFGBML_Learning [defaultLimit=" + defaultLimit + "]";
	}

	@Override
	public MoFGBML_Learning copy() {
		return new MoFGBML_Learning(this.train);
	}
}