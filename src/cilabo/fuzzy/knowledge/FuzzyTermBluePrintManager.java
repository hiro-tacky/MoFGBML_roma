package cilabo.fuzzy.knowledge;

import java.util.ArrayList;

import cilabo.data.DataSet;
import cilabo.fuzzy.knowledge.membershipParams.Parameters;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;

public class FuzzyTermBluePrintManager {

	public ArrayList<ArrayList<FuzzyTermsBluePrint>> fuzyyTermsBluePrintList;
	public Parameters parameters;
	public int dim;
	public DataSet dtra;

	public FuzzyTermBluePrintManager(DataSet dataSet, int Ndim) {
		this.fuzyyTermsBluePrintList = new ArrayList<ArrayList<FuzzyTermsBluePrint>>(Ndim);
		this.parameters = new Parameters(dataSet, dim);
		for(int i=0; i<Ndim; i++) {
			fuzyyTermsBluePrintList.add(new ArrayList<FuzzyTermsBluePrint>());
		}
		this.dim = Ndim;
	}

	public void addFuzyyTermsBluePrint(DIVISION_TYPE divisionType, int dim, int K, int fuzzyTermType) {
		fuzyyTermsBluePrintList.get(dim).add(new FuzzyTermsBluePrint(divisionType, dim, fuzzyTermType, K));
	}

	public void addFuzyyTermsBluePrint(DIVISION_TYPE divisionType, int dim, int[] K, int fuzzyTermType) {
		for(int K_i: K) {
			fuzyyTermsBluePrintList.get(dim).add(new FuzzyTermsBluePrint(divisionType, dim, fuzzyTermType, K_i));
		}
	}

	public ArrayList<FuzzyTermsBluePrint> getFuzzyTermsBluePrint(int dim) {
		return fuzyyTermsBluePrintList.get(dim);
	}

	public FuzzyTermsBluePrint getFuzzyTermsBluePrint(int dim, int index) {
		return fuzyyTermsBluePrintList.get(dim).get(index);
	}

	public int getNdim() {
		return this.fuzyyTermsBluePrintList.size();
	}

	public int getBluePrintNum(int dim) {
		return this.fuzyyTermsBluePrintList.get(dim).size();
	}

	public int getfuzyyTermsNum(int dim) {
		int cnt=0;
		for(FuzzyTermsBluePrint FuzzyTermBP: this.fuzyyTermsBluePrintList.get(dim)) {
			cnt += FuzzyTermBP.getK();
		}
		return cnt;
	}

	public class FuzzyTermsBluePrint{
		public DIVISION_TYPE divisionType;
		public int dim;
		public int fuzzyTermType;
		public int K;

		public FuzzyTermsBluePrint(DIVISION_TYPE divisionType, int dim, int fuzzyTermType, int K) {
			this.divisionType = divisionType;
			this.dim = dim;
			this.fuzzyTermType = fuzzyTermType;
			this.K = K;
		}

		public DIVISION_TYPE getDivisionType() {
			return divisionType;
		}

		public int getDim() {
			return dim;
		}

		public int getFuzzyTermType() {
			return fuzzyTermType;
		}

		public int getK() {
			return K;
		}
	}
}

