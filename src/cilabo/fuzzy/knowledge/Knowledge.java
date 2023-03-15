package cilabo.fuzzy.knowledge;

import java.util.Objects;

import org.w3c.dom.Element;

import cilabo.fuzzy.knowledge.FuzzySetBluePrintManager.FuzzySetBluePrint;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * ファジィ集合マネージャークラス．fuzzy set manager class<br>
 * fuzzy set index = 0 は Don't care で予約済み．fuzzy set index = 0 will be used for Don't care<br>
 * singleton デザインパターンを採用．使用時はgetInstanceでプロジェクト上のどこからでも呼び出せます．
 * this class adopts singleton design. when you use this class, you call getInstance methods anywhere.
 * @author Takigawa Hiroki
 */
public class Knowledge {

	/** shape type ID for Don't care*/
	public final static int DnotCare_FuzzyTermID = 99;
	/** fuzzy set index for Don't care */
	public final static int DnotCare_FuzzySetIndex = 0;
	/** 自分自身のインスタンス */
	private static Knowledge instance = new Knowledge();
	/** ファジィ集合格納オブジェクト */
	private FuzzySet[][] fuzzySetContainer;
	/** when fuzzy set is overwrited, notice it. */
	private boolean overwriteFuzzySetAlert = true;
	/** コンストラクタ constructor */
	private Knowledge() {}

	/**
	 * Knowledge のインスタンスを取得
	 * Get Knowledge instance
	 * @return Knowledge instance
	 */
	public static Knowledge getInstance() {
		return instance;
	}

	/**
	 * ファジィ集合の配列を返します。<br>
	 * Returns array of fuzzy set at the specified position
	 * @return ファジィ集合配列．Array of fuzzy set at the specified position
	 */
	public FuzzySet[][] getFuzzySet() {
		this.initialisedCheck();
		return fuzzySetContainer;
	}

	/**
	 * 指定された位置にあるファジィ集合の配列を返します。<br>
	 * Returns array of fuzzy set at the specified position
	 * @param dimension 返されるファジィ集合の次元．dimension of fuzzy set to return
	 * @return 指定された位置にあるファジィ集合配列．Array of fuzzy set at the specified position
	 */
	public FuzzySet[] getFuzzySet(int dimension) {
		this.initialisedCheck(dimension);
		return fuzzySetContainer[dimension];
	}

	/**
	 * 指定された位置にあるファジィ集合を返します。<br>
	 * Returns fuzzy set at the specified position
	 * @param dimension 返されるファジィ集合の次元．dimension of fuzzy set to return
	 * @param fuzzySetID 返されるファジィ集合のID．ID of fuzzy set to return
	 * @return 指定された位置にあるファジィ集合．fuzzy set at the specified position
	 */
	public FuzzySet getFuzzySet(int dimension, int fuzzySetID) {
		this.initialisedCheck(dimension, fuzzySetID);
		return fuzzySetContainer[dimension][fuzzySetID];
	}

	/**
	 * このインスタンスが持つファジィセットを入力されたファジィセットで置き換えます。<br>
	 * Replaces fuzzy set in this instance.
	 * @param fuzzySets このインスタンスにに格納されるファジィセット．fuzzy set to be stored at this instance
	 */
	public void setFuzzySets(FuzzySet[][] fuzzySets) {
		if(!Objects.isNull(this.fuzzySetContainer) && overwriteFuzzySetAlert){
			System.err.println("fuzzySets was overwrited");
		}
		this.fuzzySetContainer = fuzzySets;
	}

	/**
	 * 指定された次元のファジィセットを入力されたファジィセットで置き換えます。<br>
	 * Replaces fuzzy set at the specified position.
	 * @param dimension dimension of fuzzy set to replace
	 * @param fuzzySet このインスタンスにに格納されるファジィセット Array of fuzzy set to be stored
	 */
	public void setFuzzySets(int dimension, FuzzySet[] fuzzySet) {
		this.initialisedCheck();
		if(!Objects.isNull(this.fuzzySetContainer[dimension]) && overwriteFuzzySetAlert){
			System.err.println("fuzzySets was overwrited");}
		this.fuzzySetContainer[dimension] = fuzzySet;
	}

	/**
	 * 指定された次元・IDのファジィセットを入力されたファジィセットで置き換えます。<br>
	 * Replaces fuzzy set at the specified position.
	 * @param dimension dimension of fuzzy set to replace
	 * @param fuzzySetID ID of fuzzy set to replace
	 * @param fuzzySet このインスタンスにに格納されるファジィセット fuzzy set to be stored
	 */
	public void setFuzzySets(int dimension, int fuzzySetID, FuzzySet fuzzySet) {
		this.initialisedCheck(dimension);
		if(!Objects.isNull(this.fuzzySetContainer[dimension][fuzzySetID]) && overwriteFuzzySetAlert) {
			System.err.println("fuzzySets was overwrited");
		}
		this.fuzzySetContainer[dimension][fuzzySetID] = fuzzySet;
	}

	/**
	 * 次元数を返します。<br>
	 * Returns number of dimension
	 * @return 次元数．number of dimension
	 */
	public int getNumberOfDimension() {
		this.initialisedCheck();
		return fuzzySetContainer.length;
	}

	/**
	 * 指定された次元のファジィセットの個数を返します。<br>
	 * Returns number of fuzzy set at the specified dimension.
	 * @param dimension ファジィセットの次元 dimension
	 * @return 返されるファジィセットの個数．number of fuzzy set
	 */
	public int getNumberOfFuzzySet(int dimension) {
		this.initialisedCheck(dimension);
		return fuzzySetContainer[dimension].length;
	}

	/**
	 * 指定されたファジィセットの入力された属性値に対するメンバシップ値を返す．
	 * Retrun membership value to given attribute value at the specified fuzzy set.
	 * @param attributeValue 属性値.attribute value
	 * @param dimension  ファジィセットの次元．dimension of fuzzy set
	 * @param fuzzySetID ファジィセットのID．ID of fuzzy set
	 * @return 属性値に対するメンバシップ値 membership value
	 */
	public double getMembershipValue(double attributeValue, int dimension, int fuzzySetID) {
		this.initialisedCheck(dimension, fuzzySetID);
		return (double)fuzzySetContainer[dimension][fuzzySetID].getMembershipValue((float)attributeValue);
	}

	/**
	 * don't Careファジィ集合を返す
	 * Generate don't care fuzzy set
	 * @return Don't Careファジィ集合 don't care fuzzy set
	 */
	public FuzzySet makeDontCare(){
		return new FuzzySet(
			Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, Knowledge.DnotCare_FuzzyTermID),
			FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f}, DIVISION_TYPE.equalDivision, 0, 0);
	}

	/** このインスタンスが保持するファジィ集合を初期化 Clear fuzzy set that this instance has*/
	public void clear() {
		this.fuzzySetContainer = null;
	}

	@Override
	public String toString() {
		String ln = System.lineSeparator();
		String str = "Knowladge Base" + ln;
		for(int i = 0; i < fuzzySetContainer.length; i++) {
			str += "dimension:" + String.valueOf(i) + " ";
			for(int j = 0; j < fuzzySetContainer[i].length; j++) {
				str += "{" + fuzzySetContainer[i][j].toString() + "}";
			}
			str += ln;
		}
		return str;
	}

	public Element toElement() {
		Element knowledgeEL = XML_manager.getInstance().createElement(XML_TagName.knowledgeBase);
		for(int dim_i=0; dim_i<this.getNumberOfDimension(); dim_i++) {
			FuzzySet[] fuzzySetAtDim = this.fuzzySetContainer[dim_i];
			Element fuzzySetsEL = XML_manager.getInstance().createElement(XML_TagName.fuzzySets,
					XML_TagName.dimension, String.valueOf(dim_i));
			for(int j=0; j<fuzzySetAtDim.length; j++) {
				FuzzySet fuzzySet = fuzzySetAtDim[j];

				Element fuzzyTermEL = fuzzySet.toElement();
				XML_manager.getInstance().addElement(fuzzyTermEL, XML_TagName.fuzzyTermID, j);
				XML_manager.getInstance().addElement(fuzzySetsEL, fuzzyTermEL);
			}
			XML_manager.getInstance().addElement(knowledgeEL, fuzzySetsEL);
		}
		return knowledgeEL;
	}

	/** shape type ID に対応するファジィ集合の形状名を返します
	 * Return fuzzy set's shape name by shape type ID
	 * @param id ファジィ集合の形状ID shape type ID of fuzzy set
	 * @return ファジィ集合の形状名 name of fuzzy set
	 */
	public static String getShapeTypeNameFromID(int id) {
		String ShapeName =null;
		switch(id) {
			case 0: ShapeName = "rightLinearShape"; break;
			case 1: ShapeName = "leftLinearShape"; break;
			case 2: ShapeName = "piShape"; break;
			case 3: ShapeName = "triangularShape"; break;
			case 4: ShapeName = "gaussianShape"; break;
			case 5: ShapeName = "rightGaussianShape"; break;
			case 6: ShapeName = "leftGaussianShape"; break;
			case 7: ShapeName = "trapezoidShape"; break;
			case 8: ShapeName = "singletonShape"; break;
			case 9: ShapeName = "rectangularShape"; break;
			case 10: ShapeName = "zShape"; break;
			case 11: ShapeName = "sShape"; break;
			case 12: ShapeName = "pointSetShape"; break;
			case 13: ShapeName = "pointSetMonotonicShape"; break;
			case 14: ShapeName = "circularDefinition"; break;
			case 15: ShapeName = "customShape"; break;
			case 16: ShapeName = "customMonotonicShape"; break;
			case Knowledge.DnotCare_FuzzyTermID: ShapeName = "DontCare"; break;
		}
		return ShapeName;
	}

	/** 入力された情報を基にファジィ集合名を生成します．
	 * Generate fuzzy set name by given information
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param shapeTypeID ファジィセット形状id shape type ID of fuzzy set
	 * @param fuzzyTermID 各ファジィに与えられる固有のID. ID that is to be given to this fuzzy term.
	 * @return 生成されたファジィ集合名 generated name of fuzzy set
	 */
	public static String makeFuzzyTermName(DIVISION_TYPE divisionType, int shapeTypeID, int fuzzyTermID) {
		if(shapeTypeID == Knowledge.DnotCare_FuzzyTermID) {
			return "DontCare";
		}else {
			return String.format("%s_%s_%02d", Knowledge.getShapeTypeNameFromID(shapeTypeID),
					divisionType.toString(), fuzzyTermID);
		}
	}

	/** 入力された情報を基にファジィ集合名を生成します．
	 * Generate fuzzy set name by given information
	 * @param FuzzyTermBP fuzzySetの設計図用クラス fuzzy set blue print class
	 * @param fuzzyTermID 各ファジィに与えられる固有のID. ID that is to be given to this fuzzy term.
	 * @return 生成されたファジィ集合名 generated name of fuzzy set
	 */
	public static String makeFuzzyTermName(FuzzySetBluePrint FuzzyTermBP, int fuzzyTermID) {
		return Knowledge.makeFuzzyTermName(FuzzyTermBP.getDivisionType(), FuzzyTermBP.getShapeTypeID(), fuzzyTermID);
	}

	/**
	 * fuzzySetContainerの初期化チェック Check fuzzySetContainer was already initialized
	 * @param dimension  ファジィセットの次元．dimension of fuzzy set
	 * @param fuzzySetID ファジィセットのID．ID of fuzzy set
	 */
	public void initialisedCheck(int dimension, int fuzzySetID) {
		this.initialisedCheck(dimension);
		if(Objects.isNull(fuzzySetContainer[dimension])) {
			throw new NullPointerException("Knowledge hasn't been initialised");
		}
	}

	/**
	 * fuzzySetContainerの初期化チェック Check fuzzySetContainer was already initialized
	 * @param dimension  ファジィセットの次元．dimension of fuzzy set
	 */
	public void initialisedCheck(int dimension) {
		this.initialisedCheck();
		if(Objects.isNull(fuzzySetContainer[dimension])) {
			throw new NullPointerException("Knowledge hasn't been initialised");
		}
	}

	/**
	 * fuzzySetContainerの初期化チェック Check fuzzySetContainer was already initialized
	 */
	public void initialisedCheck() {
		if(Objects.isNull(fuzzySetContainer)) {
			throw new NullPointerException("Knowledge hasn't been initialised");
		}
	}
}
