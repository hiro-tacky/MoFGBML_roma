package cilabo.fuzzy.rule.consequent.classLabel;

import java.util.Objects;

public abstract class AbstractClassLabel <ClassLabelValue> implements ClassLabel<ClassLabelValue>{
	/** クラスラベル変数 */
	protected ClassLabelValue classLabel;

	@Override
	public ClassLabelValue getClassLabelValue() {
		if( Objects.isNull(this.classLabel) ) {
			throw new NullPointerException();
		}
		return this.classLabel;
	}

	@Override
	public void setClassLabelValue(ClassLabelValue classLabel) {
		this.classLabel = classLabel;
	}
}
