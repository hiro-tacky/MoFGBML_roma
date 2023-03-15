package cilabo.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.util.checking.Check;
import org.w3c.dom.Element;

import cilabo.data.pattern.Pattern;
import xml.XML_TagName;
import xml.XML_manager;

/**
 * データセット用のデータコンテナクラス．Patternクラスのリストとデータセットに関する情報を持つ．<br>
 * data set class. this class has pattern class ArrayList and data set's information
 * @param <PatternClass> データセットが扱うパターンクラスの型
 * @author Takigawa Hiroki
 */
public class DataSet<PatternClass extends Pattern<?>> {
	/** Number of Pattern*/
	private int dataSetSize;
	/** Number of Feature*/
	private int numberOfDimension;
	/** Number of Class*/
	private int numberOfClass;

	/**	Patternクラスのリスト. pattern class ArrayList*/
	private ArrayList<PatternClass> patterns;

	/** コンストラクタ
	 * @param dataSetSize データセットのパターン数 Number of pattern
	 * @param numberOfDimension 属性数/次元数 Number of feature
	 * @param numberOfClass ターゲットクラスのラベル種類数 Number of target class
	 */
	public DataSet(int dataSetSize, int numberOfDimension, int numberOfClass, ArrayList<PatternClass> patterns) {
		Check.isNotNull(patterns);
		if(dataSetSize != patterns.size()) {
			throw new IllegalArgumentException(String.format("Ginve dataSetSize is " + String.valueOf(dataSetSize)
				+ ", but pattern ArrayList size is " + String.valueOf(patterns.size())));
		}
		this.dataSetSize = dataSetSize;
		this.numberOfDimension = numberOfDimension;
		this.numberOfClass = numberOfClass;
		this.patterns = new ArrayList<>(patterns);
	}

	/** このインスタンスが持つパターンクラスのリストを返す。<br>
	 * Returns list of pattern classes that this instance has.
	 * @return 返されるパターンクラスのリスト．list of pattern classes to return
	 */
	public ArrayList<PatternClass> getPatterns(){
		return this.patterns;
	}

	/**
	 * 指定された位置にあるパターンクラスを返す。<br>
	 * Returns pattern class at the specified position.
	 * @param index 返されるパターンクラスのインデックス．index of pattern class to return
	 * @return 指定された位置にあるパターンクラス．pattern class at the specified position
	 */
	public PatternClass getPattern(int index) {
		return this.patterns.get(index);
	}

	/**
	 * 指定された位置にあるパターンクラスを返す。(並列処理用)<br>
	 * Returns pattern class at the specified position.(for parallel processing)
	 * @param index 返されるパターンクラスのインデックス．index of pattern class to return
	 * @return 指定された位置にあるパターンクラス．pattern class at the specified position
	 */
	public PatternClass getPatternWithID(int index) {
		List<PatternClass> list = this.patterns.stream()
										.filter(p -> p.getID() == index)
										.collect( Collectors.toList() );
		return list.get(0);
	}

	/**
	 * データセットのパターン数を返します。<br>
	 * Returns data set's number of pattern
	 * @return データセットのパターン数．data set's number of pattern
	 */
	public int getDataSetSize() {
		return this.dataSetSize;
	}

	/**
	 * データセットの属性数/次元を返します。
	 * Returns deta set's number of dimension
	 * @return データセットの属性数/次元 deta set's number of dimension
	 */
	public int getNumberOfDimension() {
		return this.numberOfDimension;
	}

	/**
	 * データセットのクラスラベル数を返します。<br>
	 * Returns data set's number of target class
	 * @return データセットのクラスラベル数．data set's number of target class
	 */
	public int getNumberOfClass() {
		return this.numberOfClass;
	}

	@Override
	public String toString() {
		if(this.patterns.size() == 0) { return "null"; }

		String ln = System.lineSeparator();
		// Header
		String str = String.format("dataSetSize:%d, numberOfDimension:%d, numberOfClass:%d" + ln, this.dataSetSize, this.numberOfDimension, this.numberOfClass);
		// Patterns
		for(int n = 0; n < this.patterns.size(); n++) {
			str += this.patterns.get(n).toString() + ln;
		}
		return str;
	}

	public Element toElement() {
		Element dataSetEL = XML_manager.getInstance().createElement(XML_TagName.dataSet);
		for(int i=0; i<this.getDataSetSize(); i++) {
			Pattern<?> pattern = this.getPattern(i);
			XML_manager.getInstance().addElement(dataSetEL, pattern.toElement(),
					XML_TagName.id, pattern.getID());
		}
		return dataSetEL;
	}

	public DataSet<PatternClass> copy() {
		return new DataSet<PatternClass>(this.dataSetSize, this.numberOfDimension, this.numberOfClass, new ArrayList<>(this.patterns));

	}

	public boolean equals(DataSet<PatternClass> dataSet) {
		if(dataSet.dataSetSize != this.dataSetSize ||
				dataSet.numberOfDimension != this.numberOfDimension ||
				dataSet.numberOfClass != this.numberOfClass) return false;

		if(dataSet.patterns.size() != this.patterns.size()) return false;
		for(int i=0; i<dataSet.patterns.size(); i++) {
			if(!dataSet.getPattern(i).equals(this.getPattern(i))) return false;
		}
		return true;
	}
	//並列分散実装用 (ver. 21以下)
	// ************************************************************
	// Fields

//		int setting = 0;
//		InetSocketAddress[] serverList = null;

	// ************************************************************
	// Constructor

	//並列分散実装用 (ver. 21以下)
//		public DataSetInfo(int Datasize, int Ndim, int Cnum, int setting, InetSocketAddress[] serverList){
//			this.DataSize = Datasize;
//			this.Ndim = Ndim;
//
//			this.setting = setting;
//			this.serverList = serverList;
//		}
//
//		public DataSetInfo(int Ndim, int Cnum, int DataSize, ArrayList<Pattern> patterns) {
//			this.Ndim = Ndim;
//			this.DataSize = DataSize;
//			this.patterns = patterns;
//		}

	// ************************************************************
	// Methods

//		public int getSetting() {
//			return this.setting;
//		}
//
//		public void setSetting(int setting) {
//			this.setting = setting;
//		}
//
//		public InetSocketAddress[] getServerList() {
//			return this.serverList;
//		}
//
//		public void setServerList(InetSocketAddress[] serverList) {
//			this.serverList = serverList;
//		}

}
