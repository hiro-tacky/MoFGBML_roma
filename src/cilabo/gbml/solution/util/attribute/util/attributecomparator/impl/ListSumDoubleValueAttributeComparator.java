package cilabo.gbml.solution.util.attribute.util.attributecomparator.impl;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.util.attribute.util.attributecomparator.AttributeComparator;
import org.uma.jmetal.util.checking.Check;

public class ListSumDoubleValueAttributeComparator<S extends Solution<?>>  extends AttributeComparator<S> {

	  public ListSumDoubleValueAttributeComparator(String name, Ordering ordering) {
	    super(name, ordering);
	  }

	  public ListSumDoubleValueAttributeComparator(String name) {
	    super(name);
	  }

	  /**
	   * Compare two population.
	   *
	   * @param solution1 Object representing the first <code>Solution</code>.
	   * @param solution2 Object representing the second <code>Solution</code>.
	   * @return -1, or 0, or 1 if solution1 is has greater, equal, or less attribute value than
	   *     solution2, respectively.
	   */
	  @Override
	  public int compare(S solution1, S solution2) {
	    Check.isNotNull(solution1);
	    Check.isNotNull(solution2);

	    //Check.isNotNull(solution1.getAttribute(attributeName));
	    //Check.isNotNull(solution2.getAttribute(attributeName));

	    int result ;

	    List<Double> list1 = null ;
	    List<Double> list2 = null ;

	    if (solution1.getAttribute(attributeName) != null) {
	      list1 = (List<Double>) solution1.getAttribute(attributeName);
	    }
	    if (solution2.getAttribute(attributeName) != null) {
	      list2 = (List<Double>) solution2.getAttribute(attributeName);
	    }

	    double sum1 = list1.stream().mapToDouble(Double::doubleValue).sum(), sum2 = list2.stream().mapToDouble(Double::doubleValue).sum();
	    if (ordering.equals(Ordering.DESCENDING)) {
	      result = Double.compare(sum2, sum1);
	    } else {
	      result = Double.compare(sum1, sum2);
	    }

	    return result;
	  }
}
