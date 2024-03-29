package cilabo.fuzzy.knowledge;

import java.util.Objects;

import org.w3c.dom.Element;

import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import xml.XML_TagName;
import xml.XML_manager;


/**
 * singletoneに変更，アプリケーション内で唯一のインスタンスを持ちます．≒グローバル変数<br>
 * Knowledge.getInstace()でインスタンスを呼出し使用．
 * @author hirot
 *
 */
public class Knowledge {
	public final static int DnotCare_FuzzyTermID = 99;
	private static Knowledge instance = new Knowledge();
	private FuzzyTermTypeForMixed[][] fuzzySets;

	private Knowledge() {}

	/**
	 * Knowledge のインスタンスを取得
	 * @return Knowledge
	 */
	public static Knowledge getInstance() {
		return instance;
	}

	/** 指定したファジィセットを取得
	 * @param dimension 次元
	 * @param H ファジィセットのid
	 * @return dim次元H番目のファジィセット
	 */
	public FuzzyTermTypeForMixed getFuzzySet(int dimension, int H) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets[dimension][H];
	}

	/** 指定したファジィセットを取得
	 * @param dimension 次元
	 * @return dim次元目のファジィセット配列
	 */
	public FuzzyTermTypeForMixed[] getFuzzySet(int dimension) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets[dimension];
	}

	/** 指定したファジィセットを取得
	 * @param x 入力パターンの属性値
	 * @param dimension 次元
	 * @param H ファジィセットのid
	 * @return dim次元H番目のファジィセット
	 */
	public double getMembershipValue(double x, int dimension, int H) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return (double)fuzzySets[dimension][H].getMembershipValue((float)x);
	}

	/** 次元数を返す
	 * @return 次元数 */
	public int getDimension() {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets.length;
	}

	/** 指定した次元のファジィセットの個数を返す
	 * @param dimension 指定する次元数
	 * @return ファジィセットの個数
	 */
	public int getFuzzySetNum(int dimension) {
		if(Objects.isNull(fuzzySets)) {System.err.println("Knowledge hasn't been initialised");}
		return fuzzySets[dimension].length;
	}

	public void setFuzzySets(FuzzyTermTypeForMixed[][] fuzzySets) {
		this.fuzzySets = fuzzySets;
	}

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
		Element knowledge = XML_manager.createElement(XML_TagName.knowledgeBase);
		for(int dim_i=0; dim_i<this.getDimension(); dim_i++) {
			FuzzyTermTypeForMixed[] fuzzyTermTypeAtDim = this.fuzzySets[dim_i];
			Element fuzzySets = XML_manager.createElement(XML_TagName.fuzzySets,
					XML_TagName.dimension, String.valueOf(dim_i));
			for(int j=0; j<fuzzyTermTypeAtDim.length; j++) {
				FuzzyTermTypeForMixed fuzzyTerm = fuzzyTermTypeAtDim[j];
				Element fuzzyTermElement = XML_manager.createElement(XML_TagName.fuzzyTerm);
					XML_manager.addElement(fuzzyTermElement, XML_TagName.fuzzyTermID, String.valueOf(j));
					XML_manager.addElement(fuzzyTermElement, XML_TagName.fuzzyTermName, fuzzyTerm.getName());
					XML_manager.addElement(fuzzyTermElement, XML_TagName.ShapeTypeID, String.valueOf(fuzzyTerm.getType()));
					XML_manager.addElement(fuzzyTermElement, XML_TagName.ShapeTypeName, Knowledge.getShapeTypeNameFromID(fuzzyTerm.getType()));
					XML_manager.addElement(fuzzyTermElement, XML_TagName.divisionType, fuzzyTerm.getDivisionType().toString());
					XML_manager.addElement(fuzzyTermElement, XML_TagName.partitionNum, String.valueOf(fuzzyTerm.getPartitionNum()));
					XML_manager.addElement(fuzzyTermElement, XML_TagName.partition_i, String.valueOf(fuzzyTerm.getPartition_i()));
				XML_manager.addElement(fuzzySets, fuzzyTermElement);

				Element parameters = XML_manager.createElement(XML_TagName.parameterSet);
				float[] parametersList = fuzzyTerm.getParam();
				for(int k=0; k<parametersList.length; k++) {
					XML_manager.addElement(parameters, XML_TagName.parameter, String.valueOf(parametersList[k]),
							XML_TagName.id, String.valueOf(k));
				}
				XML_manager.addElement(fuzzyTermElement, parameters);

				XML_manager.addElement(fuzzySets, fuzzyTermElement);
			}
			XML_manager.addElement(knowledge, fuzzySets);
		}
		return knowledge;
	}

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

	public static String getDdivisionTypeNameFromID(DIVISION_TYPE divisionType) {
		String TypeName =null;
		switch(divisionType) {
			case equalDivision: TypeName = "equalDdivision"; break;
			case entropyDivision: TypeName = "entropyDdivision"; break;
			default: TypeName = "equalDdivision"; break;
		}
		return TypeName;
	}

	public static String makeFuzzyTermName(DIVISION_TYPE divisionType, int ShapeTypeID, int FuzzyTermID) {
		if(ShapeTypeID == Knowledge.DnotCare_FuzzyTermID) {
			return "DontCare";
		}else {
			return String.format("%s_%s_%02d", Knowledge.getShapeTypeNameFromID(ShapeTypeID),
			Knowledge.getDdivisionTypeNameFromID(divisionType), FuzzyTermID);
		}
	}
}
