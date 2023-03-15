package cilabo.fuzzy.rule.consequent;

import org.w3c.dom.Element;

import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.fuzzy.rule.consequent.ruleWeight.RuleWeight;

/** 後件部クラス．consequent class
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelClass> 後件部クラスが持つ結論部ラベルクラス conclusion label class that this class has
 * @param <RuleWeightClass> 後件部クラスが持つルール重みクラス rule weight class that this class has
 */
public interface Consequent<ClassLabelClass extends ClassLabel<?>,  RuleWeightClass extends RuleWeight<?>>{

	/**
	 * このインスタンスが持つ結論部ラベルクラスを返します。<br>
	 * Returns class label that this instance has.
	 * @return 返される結論部ラベルクラス．class label to return
	 */
	public ClassLabelClass getClassLabel();

	/**
	 * このインスタンスが持つルール重みを返します。<br>
	 * Returns rule weight that this instance has.
	 * @return 返されるルール重み．rule weight to return
	 */
	public RuleWeightClass getRuleWeight();

	public Consequent<ClassLabelClass, RuleWeightClass> copy();

	public Element toElement();
}
