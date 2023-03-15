package cilabo.fuzzy.knowledge;

import java.util.ArrayList;

import cilabo.main.ExperienceParameter.DIVISION_TYPE;

/**
 * ファジィ集合設計図管理クラス this class has fuzzy set blue prints
 * @author Takigawa Hiroki
 *
 */
public class FuzzySetBluePrintManager {

	/** fuzzySetクラスの設計図格納用リスト fuzzy set blue print container*/
	private ArrayList<ArrayList<FuzzySetBluePrint>> fuzzySetBluePrintContainer;
	/** データセットの次元数 number of dimension */
	private int numberOfDimension;

	/**
	 * コンストラクタ．Initialize FuzzySetBluePrintManager
	 * @param dimension データセットの次元数 number of dimension
	 */
	public FuzzySetBluePrintManager(int dimension) {
		this.numberOfDimension = dimension;
		this.fuzzySetBluePrintContainer = new ArrayList<ArrayList<FuzzySetBluePrint>>(dimension);
		for(int i=0; i<dimension; i++) {
			fuzzySetBluePrintContainer.add(new ArrayList<FuzzySetBluePrint>());
		}
	}

	/**
	 * 指定された次元のfuzzySet設計図リストを返します。<br>
	 * Returns list of fuzzy set blue print at the specified dimension
	 * @param dimension 返されるfuzzySet設計図の次元．dimension of fuzzy Set blue print list to return
	 * @return 指定された次元にあるfuzzySet設計図リスト．fuzzy Set blue print list at the specified dimension
	 */
	public ArrayList<FuzzySetBluePrint> getFuzzySetBluePrint(int dimension) {
		return fuzzySetBluePrintContainer.get(dimension);
	}

	/**
	 * 指定された次元・位置にあるfuzzySet設計図を返します。<br>
	 * Returns fuzzy set blue print at the specified dimension and index
	 * @param dimension 返されるfuzzySet設計図の次元．dimension of fuzzy Set blue print to return
	 * @param index 返されるfuzzySet設計図のインデックス．index of fuzzy Set blue print to return
	 * @return 指定された次元・位置にあるfuzzySet設計図．fuzzy Set blue print at the specified position and dimension
	 */
	public FuzzySetBluePrint getFuzzySetBluePrint(int dimension, int index) {
		return this.getFuzzySetBluePrint(dimension).get(index);
	}

	/**
	 * 入力されたfuzzySet設計図を追加します。<br>
	 * add fuzzy set blue print to this instance
	 * @param dimension 次元 dimension of fuzzy Set blue print to be added
	 * @param divisionType 区間分割方式 division type of fuzzy Set blue print to be added
	 * @param shapeTypeID ファジィセット形状id shape type ID of fuzzy Set blue print to be added
	 * @param K 区間分割数 number of partition
	 */
	public void addFuzzySetBluePrint(int dimension, DIVISION_TYPE divisionType, int shapeTypeID, int K) {
		fuzzySetBluePrintContainer.get(dimension).add(new FuzzySetBluePrint(dimension, divisionType, shapeTypeID, K));
	}

	/**
	 * fuzzySetクラスの設計図を追加
	 * add fuzzy set blue print to this instance
	 * @param dimension 次元 dimension of fuzzy Set blue print to be added
	 * @param divisionType 区間分割方式 division type of fuzzy Set blue print to be added
	 * @param shapeTypeID ファジィセット形状id shape type ID of fuzzy Set blue print to be added
	 * @param K 区間分割数 number of partition
	 */
	public void addFuzzySetBluePrint(int dimension, DIVISION_TYPE divisionType, int shapeTypeID, int[] K) {
		for(int K_i: K) {
			fuzzySetBluePrintContainer.get(dimension).add(new FuzzySetBluePrint(dimension, divisionType, shapeTypeID, K_i));
		}
	}

	/**
	 * 次元数を返す.
	 * Returns number of dimension
	 * @return 次元数．number of dimension
	 */
	public int getNumberOfDimension() {
		if(numberOfDimension != fuzzySetBluePrintContainer.size()) {
			System.err.println("dimension has been initialised as " + String.valueOf(numberOfDimension) +
					", but fuzzySetBluePrintContainer's length is " + String.valueOf(fuzzySetBluePrintContainer.size()
						+ " @"  + this.getClass().getSimpleName()));
		}
		return this.numberOfDimension;
	}

	/**
	 * 指定された次元で生成されるfuzzySetの数を返します。<br>
	 * Returns number of fuzzy set to be generated at the specified dimension
	 * @param dimension 次元 dimension
	 * @return 指定された次元で生成されるfuzzySetの数．number of fuzzy set to be generated at the specified dimension
	 */
	public int getNumberOfFuzzySets(int dimension) {
		int cnt=0;
		for(FuzzySetBluePrint fuzzyTermBP: this.getFuzzySetBluePrint(dimension)) {
			cnt += fuzzyTermBP.getK();
		}
		return cnt;
	}

	/**
	 * fuzzySetの設計図用クラス fuzzy set blue print class
	 * @author Takigawa Hiroki
	 */
	public class FuzzySetBluePrint implements Comparable<FuzzySetBluePrint>{
		public DIVISION_TYPE divisionType;
		public int dimension;
		public int shapeTypeID;
		public int K;

		/**
		 * コンストラクタ．Initialize FuzzySetBluePrint object by taken parameter
		 * @param dimension 次元 dimension of fuzzy Set blue print to be added
		 * @param divisionType 区間分割方式 division type of fuzzy Set blue print to be added
		 * @param shapeTypeID ファジィセット形状id shape type ID of fuzzy Set blue print to be added
		 * @param K 区間分割数 number of partition
		 */
		public FuzzySetBluePrint(int dimension, DIVISION_TYPE divisionType, int shapeTypeID, int K) {
			this.divisionType = divisionType;
			this.dimension = dimension;
			this.shapeTypeID = shapeTypeID;
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
		 * このインスタンスが持つ次元を返します。<br>
		 * Returns dimension that this instance has.
		 * @return 返される次元．dimension to return
		 */
		public int getDimension() {
			return dimension;
		}

		/**
		 * このインスタンスが持つファジィセット形状idを返します。<br>
		 * Returns shape type ID that this instance has.
		 * @return 返されるファジィセット形状id．shape type ID to return
		 */
		public int getShapeTypeID() {
			return shapeTypeID;
		}

		/**
		 * このインスタンスが持つ区間分割数を返します。<br>
		 * Returns number of partition that this instance has.
		 * @return 返される区間分割数．number of partition to return
		 */
		public int getK() {
			return K;
		}

		@Override
		public int compareTo(FuzzySetBluePrint o) {
			if(this.divisionType == DIVISION_TYPE.equalDivision && o.divisionType == DIVISION_TYPE.entropyDivision) return 1;
			else if(this.divisionType == DIVISION_TYPE.entropyDivision && o.divisionType == DIVISION_TYPE.equalDivision) return -1;
			else {
				if(this.shapeTypeID > o.shapeTypeID) return 1;
				else if(this.shapeTypeID < o.shapeTypeID) return -1;
				else {
					if(this.K > o.K) return 1;
					else if(this.K < o.K) return -1;
					else return 0;
				}
			}
		}
	}
}

