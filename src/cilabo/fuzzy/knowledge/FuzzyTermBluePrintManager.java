package cilabo.fuzzy.knowledge;

import java.util.ArrayList;

import cilabo.main.ExperienceParameter.DIVISION_TYPE;

/**
 * fuzzyTermクラスの設計図用マネージャー
 * @author Takigawa Hiroki
 */
public class FuzzyTermBluePrintManager {

	/** fuzzyTermクラスの設計図格納用リスト */
	private ArrayList<ArrayList<FuzzyTermsBluePrint>> fuzyyTermsBluePrintList;
	/** データセットの次元数 */
	private int numberOfdimension;

	/**
	 * インスタンスを生成
	 * @param dimension データセットの次元数
	 */
	public FuzzyTermBluePrintManager(int dimension) {
		this.numberOfdimension = dimension;
		this.fuzyyTermsBluePrintList = new ArrayList<ArrayList<FuzzyTermsBluePrint>>(dimension);
		for(int i=0; i<dimension; i++) {
			fuzyyTermsBluePrintList.add(new ArrayList<FuzzyTermsBluePrint>());
		}
	}

	/**
	 * 指定された次元にあるfuzzyTermクラスの設計図リストを返します。<br>
	 * @param dimension 返されるfuzzyTermクラスの設計図リストの次元．
	 * @return 指定された次元のfuzzyTermクラスの設計図リスト．
	 */
	public ArrayList<FuzzyTermsBluePrint> getFuzzyTermsBluePrint(int dimension) {
		if(dimension >= this.getNdim()) {
			throw new IllegalArgumentException("dimension has been initialised as " + String.valueOf(this.getNdim()) +
					", but argument [dimension] is " + String.valueOf(dimension));
		}
		return fuzyyTermsBluePrintList.get(dimension);
	}

	/**
	 * 指定された次元・次元にあるfuzzyTermクラスの設計図を返します。<br>
	 * @param dimension 返されるfuzzyTermクラスの設計図の次元．
	 * @param index 返されるfuzzyTermクラスの設計図のインデックス
	 * @return 指定された次元・インデックスのfuzzyTermクラスの設計図．
	 */
	public FuzzyTermsBluePrint getFuzzyTermsBluePrint(int dimension, int index) {
		if(index >= this.fuzyyTermsBluePrintList.get(dimension).size()) {
			throw new IllegalArgumentException("fuzyyTermsBluePrintList[" + String.valueOf(dimension) + "][" + String.valueOf(index) + "] hasn't been initialised");
		}
		return this.getFuzzyTermsBluePrint(dimension).get(index);
	}

	/**
	 * fuzzyTermクラスの設計図を追加
	 * @param divisionType 区間分割方式
	 * @param dimension 次元
	 * @param K 区間分割数
	 * @param fuzzyTermType ファジィセット形状id
	 */
	public void addFuzyyTermsBluePrint(DIVISION_TYPE divisionType, int dimension, int K, int fuzzyTermType) {
		if(dimension >= this.getNdim()) {
			throw new IllegalArgumentException("dimension has been initialised as " + String.valueOf(this.getNdim()) +
					", but argument [dimension] is " + String.valueOf(dimension));
		}
		fuzyyTermsBluePrintList.get(dimension).add(new FuzzyTermsBluePrint(divisionType, dimension, K, fuzzyTermType));
	}

	/**
	 * fuzzyTermクラスの設計図を追加
	 * @param divisionType 区間分割方式
	 * @param dimension 次元
	 * @param K 区間分割数配列
	 * @param fuzzyTermType ファジィセット形状id
	 */
	public void addFuzyyTermsBluePrint(DIVISION_TYPE divisionType, int dimension, int[] K, int fuzzyTermType) {
		for(int K_i: K) {
			fuzyyTermsBluePrintList.get(dimension).add(new FuzzyTermsBluePrint(divisionType, dimension, K_i, fuzzyTermType));
		}
	}

	/**
	 * 次元数を返します。<br>
	 * Returns number of dimension
	 * @return 次元数．number of dimension
	 */
	public int getNdim() {
		if(numberOfdimension != fuzyyTermsBluePrintList.size()) {
			System.err.println("dimension has been initialised as " + String.valueOf(numberOfdimension) +
					", but fuzyyTermsBluePrintList's length is " + String.valueOf(fuzyyTermsBluePrintList.size() 
						+ " @"  + this.getClass().getSimpleName()));
		}
		return this.numberOfdimension;
	}

	/**
	 * このインスタンスが持つ設計図の数を返します。<br>
	 * Returns number of fuzyy term blue print that this instance has.
	 * @param dimension 次元
	 * @return 設計図の数
	 */
	public int getBluePrintNum(int dimension) {
		return this.getFuzzyTermsBluePrint(dimension).size();
	}


	/**
	 * 指定された次元にある生成されるfuzzyTermの数を返します。<br>
	 * Returns number of fuzyy term to be generated at the specified dimension
	 * @param dimension 次元
	 * @return 指定された次元で生成されるfuzzyTermの数．number of fuzyy term to be generated at the specified dimension
	 */
	public int getfuzyyTermsNum(int dimension) {
		int cnt=0;
		for(FuzzyTermsBluePrint FuzzyTermBP: this.getFuzzyTermsBluePrint(dimension)) {
			cnt += FuzzyTermBP.getK();
		}
		return cnt;
	}

	/**
	 * fuzzyTermの設計図用クラス
	 * @author Takigawa Hiroki
	 */
	public class FuzzyTermsBluePrint{
		public DIVISION_TYPE divisionType;
		public int dimension;
		public int fuzzyTermType;
		public int K;

		/**
		 * インスタンスを生成
		 * @param divisionType 区間分割方式
		 * @param dimension 次元
		 * @param K 区間分割数
		 * @param fuzzyTermType ファジィセット形状id
		 */
		public FuzzyTermsBluePrint(DIVISION_TYPE divisionType, int dimension, int K, int fuzzyTermType) {
			this.divisionType = divisionType;
			this.dimension = dimension;
			this.fuzzyTermType = fuzzyTermType;
			this.K = K;
		}

		/**
		 * このインスタンスが持つ区間分割方式を返します。<br>
		 * Returns divisionType that this instance has.
		 * @return 返される区間分割方式．divisionType to return
		 */
		public DIVISION_TYPE getDivisionType() {
			return divisionType;
		}

		/**
		 * このインスタンスが持つ次元数を返します。<br>
		 * Returns number of dimension that this instance has.
		 * @return 返される次元数．number of dimension to return
		 */
		public int getDim() {
			return dimension;
		}

		/**
		 * このインスタンスが持つファジィセット形状idを返します。<br>
		 * Returns fuzzyTermType that this instance has.
		 * @return 返されるファジィセット形状id．fuzzyTermType to return
		 */
		public int getFuzzyTermType() {
			return fuzzyTermType;
		}

		/**
		 * このインスタンスが持つ区間分割数を返します。<br>
		 * Returns number of partition that this instance has.
		 * @return 返される区間分割数．number of partition to return
		 */
		public int getK() {
			return K;
		}
	}
}

