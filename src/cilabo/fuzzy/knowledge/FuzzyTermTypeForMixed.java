package cilabo.fuzzy.knowledge;

import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;

public class FuzzyTermTypeForMixed extends FuzzyTermType {
	public DIVISION_TYPE divisionType;
	public int partitionNum;
	public int partition_i;

	public FuzzyTermTypeForMixed(String name, int type, float[] param, DIVISION_TYPE divisionType, int partitionNum, int partition_i){
		super(name, type, param);
		this.divisionType = divisionType;
		this.partitionNum = partitionNum;
		this.partition_i = partition_i;
	}

	public DIVISION_TYPE getDivisionType() {
		return divisionType;
	}

	public int getPartitionNum() {
		return partitionNum;
	}

	public int getPartition_i() {
		return partition_i;
	}
}
