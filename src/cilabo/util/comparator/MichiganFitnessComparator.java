package cilabo.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

import cilabo.data.pattern.Pattern;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public class MichiganFitnessComparator<S extends MichiganSolution<?>> implements Comparator<S>, Serializable {

	public enum Ordering {ASCENDING, DESCENDING} ;
	private Ordering order;
	private Pattern<?> pattern;

	/**
	 * Constructor.
	 *
	 * @param pattern pattern to compare
	 */
	public MichiganFitnessComparator(Pattern<?> pattern) {
		  order = Ordering.ASCENDING;
		  this.pattern = pattern;
	}

	/**
	 * Comparator.
	 * @param pattern pattern to compare
	 * @param order Ascending or descending order
	 */
	public MichiganFitnessComparator(Pattern<?> pattern, Ordering order) {
		this.order = order;
		this.pattern = pattern;
	}

	@Override
	public int compare(S solution1, S solution2) {
		int result ;
		if (solution1 == null) {
			if (solution2 == null) {
				result = 0;
			} else {
				result = 1;
			}
		} else if (solution2 == null) {
			result =  -1;
		} else {
			Double fitness1 = solution1.getFitnessValue(this.pattern.getAttributeVector());
			Double fitness2 = solution2.getFitnessValue(this.pattern.getAttributeVector());
			if (order == Ordering.ASCENDING) {
				result = Double.compare(fitness1, fitness2);
			} else {
				result = Double.compare(fitness2, fitness1);
			}
		}
		return result;
	}
}
