package cilabo.fuzzy.knowledge;

import java.util.Objects;

import org.w3c.dom.Element;

import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;
import xml.XML_TagName;
import xml.XML_manager;


/**
 * ファジィ集合マネージャークラス
 * singletoneデザインパターンを採用，アプリケーション内で唯一のインスタンスを持ちます．≒グローバル変数<br>
 * Knowledge.getInstace()でインスタンスを呼出し使用．
 * @author Takigawa Hiroki
 */
public class Knowledge {

	/** Don't care 用FuzzyTermID*/
	public final static int DnotCare_FuzzyTermID = 99;
	/** 自分自身のインスタンス */
	private static Knowledge instance = new Knowledge();
	/** ファジィ集合格納オブジェクト */
	private FuzzyTermTypeForMixed[][] fuzzySets;

	private Knowledge() {}

	/**
	 * Knowledge のインスタンスを取得
	 * @return Knowledge
	 */
	public static Knowledge getInstance() {
		return instance;
	}

	/**
	 * 指定された位置にあるファジィセットを返します。<br>
	 * Returns Fuzzy Set at the specified position
	 * @param dimension 返されるファジィセットの次元．dimension of Fuzzy Set to return
	 * @param fuzzySet_id 返されるファジィセットのID．ID of Fuzzy Set to return
	 * @return 指定された位置にあるファジィセット．Fuzzy Set at the specified position
	 */
	public FuzzyTermTypeForMixed getFuzzySet(int dimension, int fuzzySet_id) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets[dimension][fuzzySet_id];
	}

	/**
	 * 指定された位置にあるファジィセットの配列を返します。<br>
	 * Returns Array of Fuzzy Set at the specified position
	 * @param dimension 返されるファジィセットの次元．dimension of Fuzzy Set to return
	 * @return 指定された位置にあるファジィセット配列．Array of Fuzzy Set at the specified position
	 */
	public FuzzyTermTypeForMixed[] getFuzzySet(int dimension) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets[dimension];
	}

	/**
	 * 指定された次元のファジィセットの個数を返します。<br>
	 * Returns number of fuzzy Set that this instance has.
	 * @param dimension ファジィセットの次元
	 * @return 返されるファジィセットの個数．number of fuzzy Set to return
	 */
	public int getFuzzySetNum(int dimension) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets[dimension].length;
	}

	/**
	 * ファジィセットを入力されたファジィセットで置き換えます。<br>
	 * Replaces Fuzzy Set in this instance.
	 * @param fuzzySets このインスタンスにに格納されるファジィセット．Fuzzy Set to be stored at this instance
	 */
	public void setFuzzySets(FuzzyTermTypeForMixed[][] fuzzySets) {
		if(!Objects.isNull(this.fuzzySets)) {System.err.println("fuzzySets was overwrited");}
		this.fuzzySets = fuzzySets;
	}

	/**
	 * 指定されたファジィセットの入力された属性値に対するメンバシップ値を返す．
	 * @param attributeValue 属性値
	 * @param dimension  ファジィセットの次元．dimension of Fuzzy Set
	 * @param fuzzySet_id ファジィセットのID．ID of Fuzzy Set
	 * @return 属性値に対するメンバシップ値
	 */
	public double getMembershipValue(double attributeValue, int dimension, int fuzzySet_id) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return (double)fuzzySets[dimension][fuzzySet_id].getMembershipValue((float)attributeValue);
	}

	/**
	 * don't Careファジィ集合を返す
	 * @return don't Careファジィ集合
	 */
	public FuzzyTermTypeForMixed makeDontCare(){
		return new FuzzyTermTypeForMixed(
			Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, Knowledge.DnotCare_FuzzyTermID),
			FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f}, DIVISION_TYPE.equalDivision, 0, 0);
	}

	/**
	 * 次元数を返します。<br>
	 * Returns number of dimension
	 * @return 次元数．number of dimension
	 */
	public int getNumberOfDimension() {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets.length;
	}

	/** ファジィセットを初期化します */
	public void clear() {
		this.fuzzySets = null;
	}

	@Override
	public String toString() {
		String ln = System.lineSeparator();
		String str = "";

		for(int i = 0; i < fuzzySets.length; i++) {
			for(int j = 0; j < fuzzySets[i].length; j++) {
				str += fuzzySets[i][j].toString() + ln;
			}
		}

		return str;
	}

	public Element toElement() {
		Element knowledge = XML_manager.getInstance().createElement(XML_TagName.knowledgeBase);
		for(int dim_i=0; dim_i<this.getNumberOfDimension(); dim_i++) {
			FuzzyTermTypeForMixed[] fuzzyTermTypeAtDim = this.fuzzySets[dim_i];
			Element fuzzySets = XML_manager.getInstance().createElement(XML_TagName.fuzzySets,
					XML_TagName.dimension, String.valueOf(dim_i));
			for(int j=0; j<fuzzyTermTypeAtDim.length; j++) {
				FuzzyTermTypeForMixed fuzzyTerm = fuzzyTermTypeAtDim[j];
				Element fuzzyTermElement = XML_manager.getInstance().createElement(XML_TagName.fuzzyTerm);
					XML_manager.getInstance().addElement(fuzzyTermElement, XML_TagName.fuzzyTermID, String.valueOf(j));
					XML_manager.getInstance().addElement(fuzzyTermElement, XML_TagName.fuzzyTermName, fuzzyTerm.getName());
					XML_manager.getInstance().addElement(fuzzyTermElement, XML_TagName.ShapeTypeID, String.valueOf(fuzzyTerm.getType()));
					XML_manager.getInstance().addElement(fuzzyTermElement, XML_TagName.ShapeTypeName, Knowledge.getShapeTypeNameFromID(fuzzyTerm.getType()));
					XML_manager.getInstance().addElement(fuzzyTermElement, XML_TagName.divisionType, fuzzyTerm.getDivisionType().toString());
					XML_manager.getInstance().addElement(fuzzyTermElement, XML_TagName.partitionNum, String.valueOf(fuzzyTerm.getPartitionNum()));
					XML_manager.getInstance().addElement(fuzzyTermElement, XML_TagName.partition_i, String.valueOf(fuzzyTerm.getPartition_i()));
				XML_manager.getInstance().addElement(fuzzySets, fuzzyTermElement);

				Element parameters = XML_manager.getInstance().createElement(XML_TagName.parameterSet);
				float[] parametersList = fuzzyTerm.getParam();
				for(int k=0; k<parametersList.length; k++) {
					XML_manager.getInstance().addElement(parameters, XML_TagName.parameter, String.valueOf(parametersList[k]),
							XML_TagName.id, String.valueOf(k));
				}
				XML_manager.getInstance().addElement(fuzzyTermElement, parameters);

				XML_manager.getInstance().addElement(fuzzySets, fuzzyTermElement);
			}
			XML_manager.getInstance().addElement(knowledge, fuzzySets);
		}
		return knowledge;
	}

	/** ShapeTypeに対応するファジィ集合の形状名を返します
	 * @param id ShapeTypeID
	 * @return ファジィ集合の形状名
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
			case 99: ShapeName = "DontCare"; break;
		}
		return ShapeName;
	}

	/** 入力された情報を基にファジィ集合名を生成します．
	 * @param divisionType 分割方式
	 * @param ShapeTypeID ShapeTypeID
	 * @param FuzzyTermID 各ファジィに与えられる固有のID
	 * @return 生成されたファジィ集合名
	 */
	public static String makeFuzzyTermName(DIVISION_TYPE divisionType, int ShapeTypeID, int FuzzyTermID) {
		if(ShapeTypeID == Knowledge.DnotCare_FuzzyTermID) {
			return "DontCare";
		}else {
			return String.format("%s_%s_%02d", Knowledge.getShapeTypeNameFromID(ShapeTypeID),
					divisionType.toString(), FuzzyTermID);
		}
	}
}
