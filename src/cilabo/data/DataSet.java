package cilabo.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cilabo.data.pattern.Pattern;

/**データセット用のクラス．
 * 各パターンを配列として持つ．
 * @author Takigawa Hiroki
 */
public class DataSet {

	/** Number of Patterns*/
	private int DataSize;
	/** Number of Features*/
	private int Ndim;
	/** Number of Classes*/
	private int Cnum;


	/**	データセットのパターンの可変長配列 */
	private ArrayList<Pattern> patterns = new ArrayList<>();

	public DataSet(int dataSize, int ndim, int cnum) {
		DataSize = dataSize;
		Ndim = ndim;
		Cnum = cnum;
	}
	/** Patternを追加
	 * @param pattern 追加するパターン
	 */
	public void addPattern(Pattern pattern) {
		this.patterns.add(pattern);
	}

	/**
	 * 指定したインデックスのPatternを取得
	 * @param index Patternのインデックス
	 * @return 指定されたインデックスにあるPattern
	 */
	public Pattern getPattern(int index) {
		return this.patterns.get(index);
	}

	/**
	 * 指定したインデックスのPatternを取得(並列処理用)
	 * @param index Patternのインデックス
	 * @return 指定されたインデックスにあるPattern
	 */
	public Pattern getPatternWithID(int id) {
		List<Pattern> list = this.patterns.stream()
										.filter(p -> p.getID() == id)
										.collect( Collectors.toList() );
		return list.get(0);
	}

	/** Patternの配列を取得
	 * @return Patternの配列
	 */
	public ArrayList<Pattern> getPatterns(){
		return this.patterns;
	}

	@Override
	public String toString() {
		if(this.patterns.size() == 0) {
			return null;
		}
		String ln = System.lineSeparator();
		// Header
		String str = String.format("%d,%d,%d" + ln, this.DataSize, this.Ndim, this.Cnum);
		// Patterns
		for(int n = 0; n < this.patterns.size(); n++) {
			Pattern pattern = this.patterns.get(n);
			str += pattern.toString() + ln;
		}
		return str;
	}

	public int getNdim() {
		return this.Ndim;
	}

	public int getCnum() {
		return this.Cnum;
	}

	public int getDataSize() {
		return this.DataSize;
	}



	//並列分散実装用 (ver. 21以下)
	// ************************************************************
	// Fields

//	int setting = 0;
//	InetSocketAddress[] serverList = null;

	// ************************************************************
	// Constructor

	//並列分散実装用 (ver. 21以下)
//	public DataSetInfo(int Datasize, int Ndim, int Cnum, int setting, InetSocketAddress[] serverList){
//		this.DataSize = Datasize;
//		this.Ndim = Ndim;
//
//		this.setting = setting;
//		this.serverList = serverList;
//	}
//
//	public DataSetInfo(int Ndim, int Cnum, int DataSize, ArrayList<Pattern> patterns) {
//		this.Ndim = Ndim;
//		this.DataSize = DataSize;
//		this.patterns = patterns;
//	}

	// ************************************************************
	// Methods

//	public int getSetting() {
//		return this.setting;
//	}
//
//	public void setSetting(int setting) {
//		this.setting = setting;
//	}
//
//	public InetSocketAddress[] getServerList() {
//		return this.serverList;
//	}
//
//	public void setServerList(InetSocketAddress[] serverList) {
//		this.serverList = serverList;
//	}

}
