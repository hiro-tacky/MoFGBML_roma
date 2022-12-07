package cilabo.fuzzy.rule.antecedent.impl;

import java.util.Arrays;

import cilabo.fuzzy.rule.antecedent.AbstractAntecedent;

public final class Antecedent_Basic extends AbstractAntecedent{

	public Antecedent_Basic() {	}

	@Override
	public double[] getCompatibleGrade(int[] antecedentIndex, double[] x) {
		if(antecedentIndex.length != x.length) {
			throw new IllegalArgumentException("antecedentIndex and pattern must be same length");
		}

		double[] grade = new double[antecedentIndex.length];
		for(int i = 0; i < x.length; i++) {
			if(antecedentIndex[i] <= 0 && x[i] <= 0) {
				// categorical
				if(antecedentIndex[i] == (int)x[i]) grade[i] = 1.0;
				else grade[i] = 0.0;
			}else if(antecedentIndex[i] >= 0 && x[i] >= 0){
				// numerical
				grade[i] = this.getFuzzySet(i, antecedentIndex[i]).getMembershipValue((float)x[i]);
			}else if(antecedentIndex[i] == 0) {
				//don't care
				grade[i] = 1.0;
			}else {
				throw new IllegalArgumentException();
			}
		}
		return grade;
	}

	@Override
	public double getCompatibleGradeValue(int[] antecedentIndex, double[] x) {
		if(antecedentIndex.length != x.length) {
			throw new IllegalArgumentException("antecedentIndex and pattern must be same length");
		}

		double[] buf = this.getCompatibleGrade(antecedentIndex, x);
		double grade = Arrays.stream(buf).reduce(1, (multi, i) -> multi*i);
		return grade;
	}

	@Override
	public int getRuleLength(int[] antecedentIndex) {
		int length = 0;
		for(int i = 0; i < antecedentIndex.length; i++) {
			if(antecedentIndex[i] != 0) {length++; }
		}
		return length;
	}

	@Override
	public Antecedent_Basic copy() {
		return new Antecedent_Basic();
	}
}
