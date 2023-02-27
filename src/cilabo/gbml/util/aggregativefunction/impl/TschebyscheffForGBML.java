package cilabo.gbml.util.aggregativefunction.impl;

import org.uma.jmetal.util.aggregativefunction.AggregativeFunction;
import org.uma.jmetal.util.point.impl.IdealPoint;

public class TschebyscheffForGBML implements AggregativeFunction {
  private IdealPoint idealPoint ;
  private int numberOfRule;
  private int numberOfPattern;

  public TschebyscheffForGBML(int numberOfRule, int numberOfPattern) {
	  idealPoint = null;
	  this.numberOfRule = numberOfRule;
	  this.numberOfPattern = numberOfPattern;
  }

  public double compute(double[] vector, double[] weightVector) {
	if(vector.length != 2) {
		System.err.println("Number of objectives must be 2");
	}
    double maxFun = 1.0e+30;
    for(int ruleNum_i=0; ruleNum_i<numberOfRule; ruleNum_i++) {
    	double P = 100 * Math.max(vector[1] - ruleNum_i, 0);
    	double feval = vector[0]*weightVector[0] + P + vector[1]/numberOfPattern;
    	if(maxFun > feval) {
    		maxFun = feval;
    	}
    }
    return maxFun;
  }

	@Override
	public void update(double[] vector) {
	    if (idealPoint == null) {
	        idealPoint = new IdealPoint(vector.length) ;
	      }
	      idealPoint.update(vector);
	}

}
