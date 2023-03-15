package cilabo.fuzzy.rule.antecedent.impl;

import java.util.Arrays;

import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.fuzzy.knowledge.FuzzySet;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.fuzzy.rule.antecedent.Antecedent;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * 標準的前件部クラス basic antecedent class
 * @author Takigawa Hiroki
 */
public final class Antecedent_Basic implements Antecedent{

	@Override
	public double[] getCompatibleGrade(int[] antecedentIndex, AttributeVector attributeVector) {
		double[] grade = new double[antecedentIndex.length];
		if(antecedentIndex.length != attributeVector.getNumberOfDimension()) {
			throw new IllegalArgumentException("antecedentIndex and pattern must be same length");
		}else {
			for(int i = 0; i < attributeVector.getNumberOfDimension(); i++) {
				if(antecedentIndex[i] <= 0 && attributeVector.getAttributeValue(i) <= 0) {
					// categorical
					if(antecedentIndex[i] == (int)attributeVector.getAttributeValue(i)) grade[i] = 1.0;
					else grade[i] = 0.0;
				}else if(antecedentIndex[i] >= 0 && attributeVector.getAttributeValue(i) >= 0){
					// numerical
					FuzzySet fuzzySet = Knowledge.getInstance().getFuzzySet(i, antecedentIndex[i]);
					grade[i] = fuzzySet.getMembershipValue((float)attributeVector.getAttributeValue(i));
				}else if(antecedentIndex[i] == 0) {
					//don't care
					grade[i] = 1.0;
				}else {
					throw new IllegalArgumentException();
				}
			}
		}
		return grade;
	}

	@Override
	public double getCompatibleGradeValue(int[] antecedentIndex, AttributeVector attributeVector) {
		if(antecedentIndex.length != attributeVector.getNumberOfDimension()) {
			throw new IllegalArgumentException("antecedentIndex and pattern must be same length");
		}

		double[] buf = this.getCompatibleGrade(antecedentIndex, attributeVector);
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

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	@Override
	public Element toElement() {
		Element antecedent = XML_manager.getInstance().createElement(XML_TagName.antecedent);
		return antecedent;
	}
}
