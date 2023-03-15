package cilabo.fuzzy.rule.consequent.classLabel;

/**
 * 抽象ラベルクラス．abstract label class
 * @author Takigawa Hiroki
 *
 * @param <ClassLabelValue> クラスラベル変数．vaiable of class label
 */
public abstract class AbstractClassLabel <ClassLabelValue> implements ClassLabel<ClassLabelValue>{
	/** クラスラベル変数．vaiable of class label */
	protected ClassLabelValue classLabel;

	@Override
	public ClassLabelValue getClassLabelVariable() {
		return this.classLabel;
	}

	@Override
	public void setClassLabelVariable(ClassLabelValue classLabel) {
		this.classLabel = classLabel;
	}
}
