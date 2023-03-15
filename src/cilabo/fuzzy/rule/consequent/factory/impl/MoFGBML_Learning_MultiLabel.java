package cilabo.fuzzy.rule.consequent.factory.impl;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import cilabo.data.DataSet;
import cilabo.data.pattern.Pattern;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import cilabo.fuzzy.rule.consequent.classLabel.AbstractClassLabel;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactory;
import cilabo.fuzzy.rule.consequent.factory.ConsequentFactoryCore;
import cilabo.fuzzy.rule.consequent.impl.Consequent_MultiClass;
import cilabo.fuzzy.rule.consequent.ruleWeight.impl.RuleWeight_Multi;
import cilabo.main.impl.multiTasking.MultiTasking;
import cilabo.utility.Parallel;

/** 入力された前件部から後件部クラスConsequent_MultiClassを生成する
 * @author Takigawa Hiroki */
@MultiTasking
public final class MoFGBML_Learning_MultiLabel
		extends ConsequentFactoryCore<ClassLabel_Multi, RuleWeight_Multi, double[][]>
		implements ConsequentFactory <Consequent_MultiClass>{

	/**コンストラクタ
	 * @param train 生成時に用いる学習用データ */
	public MoFGBML_Learning_MultiLabel(DataSet<Pattern<ClassLabel_Multi>> train) {
		this.train = train;
	}

	@Override
	public Consequent_MultiClass learning(Antecedent antecedent, int[] antecedentIndex, double limit) {
		double[][] confidence = this.calculateConfidence(antecedent, antecedentIndex);

		ClassLabel_Multi classLabel = this.calculateClassLabel(confidence);
		RuleWeight_Multi ruleWeight = this.calculateRuleWeight(classLabel, confidence, limit);

		Consequent_MultiClass consequent = new Consequent_MultiClass(classLabel, ruleWeight);
		return consequent;
	}

	@Override
	public Consequent_MultiClass learning(Antecedent antecedent, int[] antecedentIndex) {
		return this.learning(antecedent, antecedentIndex, defaultLimit);
	}

	@Override
	public double[][] calculateConfidence(Antecedent antecedent, int[] antecedentIndex) {
		int Cnum = train.getNumberOfClass();
		double[][] confidence = new double[Cnum][2];

		for(int c = 0; c < Cnum; c++) {
			for(int i = 0; i < 2; i++) {
				final int CLASS = c;
				final int ASSOCIATE = i;

				Optional<Double> partSum = null;
				try {
					partSum = Parallel.getInstance().getLearningForkJoinPool().submit( () ->
						train.getPatterns().parallelStream()
						// 結論部クラスベクトルのCLASS番目の要素がASSOCIATEであるパターンを抽出
						.filter(pattern -> pattern.getTargetClass().equalsClassLabel(CLASS, ASSOCIATE) )
						// 各パターンの入力ベクトルを抽出
						.map(pattern -> pattern.getAttributeVector())
						// 各入力ベクトルとantecedentのcompatible gradeを計算
						.map(attributeVector -> antecedent.getCompatibleGradeValue(antecedentIndex, attributeVector))
						// compatible gradeを総和する
						.reduce((sum, grade) -> sum+grade)
					).get();
				}
				catch (InterruptedException | ExecutionException e) {
					System.out.println(e);
					return null;
				}

				confidence[CLASS][ASSOCIATE] = partSum.orElse(0.0);
			}

			double sumAll = confidence[c][0] + confidence[c][1];
			for(int i = 0; i < 2; i++) {
				if(sumAll != 0) {
					confidence[c][i] /= sumAll;
				}
				else {
					confidence[c][i] = 0.0;
				}
			}
		}
		return confidence;
	}

	@Override
	public ClassLabel_Multi calculateClassLabel(double[][] confidence) {
		Integer[] classLabelBuf = new Integer[confidence.length];
		for(int c = 0; c < confidence.length; c++) {
			if(confidence[c][0] > confidence[c][1]) {
				classLabelBuf[c] = 0;
			}
			else if(confidence[c][0] < confidence[c][1]) {
				classLabelBuf[c] = 1;
			}
			else {
				classLabelBuf[c] = AbstractClassLabel.RejectedClassLabel;
			}
		}
		ClassLabel_Multi classLabel = new ClassLabel_Multi(classLabelBuf);
		return classLabel;
	}

	// 信頼度下限値にによる ルール生成棄却機能未実装につき limit値 不使用
	@Override
	public RuleWeight_Multi calculateRuleWeight(ClassLabel_Multi classLabel, double[][] confidence, double limit) {

		Double[] ruleWeightBuf = new Double[confidence.length];
		for(int c = 0; c < confidence.length; c++) {
			if( classLabel.equalsClassLabel(c, AbstractClassLabel.RejectedClassLabel) ) {
				ruleWeightBuf[c] = 0.0;
			}
			else {
				ruleWeightBuf[c] = Math.abs(confidence[c][0] - confidence[c][1]);
			}
		}
		RuleWeight_Multi ruleWeightMulti = new RuleWeight_Multi(ruleWeightBuf);
		return ruleWeightMulti;
	}

	@Override
	public MoFGBML_Learning_MultiLabel copy() {
		return new MoFGBML_Learning_MultiLabel(this.train);
	}
}
