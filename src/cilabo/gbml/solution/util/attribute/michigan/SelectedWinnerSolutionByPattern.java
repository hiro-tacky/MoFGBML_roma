package cilabo.gbml.solution.util.attribute.michigan;

import java.util.Comparator;

import org.uma.jmetal.solution.util.attribute.Attribute;
import org.uma.jmetal.solution.util.attribute.util.attributecomparator.AttributeComparator;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.util.attribute.util.attributecomparator.impl.ListSumIntegerValueAttributeComparator;

public final class SelectedWinnerSolutionByPattern<S extends MichiganSolution<?>> implements Attribute<S> {

	private String attributeId = this.getClass().getName();
	private Comparator<S> solutionComparator ;

	public SelectedWinnerSolutionByPattern() {
	    this.solutionComparator =
	            new ListSumIntegerValueAttributeComparator<>(attributeId, AttributeComparator.Ordering.ASCENDING);
	}

	@Override
	public String getAttributeId() {
		return attributeId;
	}

	@Override
	public Comparator<S> getSolutionComparator() {
		return solutionComparator;
	}

}
