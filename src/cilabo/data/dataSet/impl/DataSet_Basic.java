package cilabo.data.dataSet.impl;

import java.util.List;

import cilabo.data.AttributeVector;
import cilabo.data.dataSet.DataSet;
import cilabo.data.pattern.impl.Pattern_Basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;

/**データセットのデータコンテナクラス．
 * @author Takigawa Hiroki
 * @param <pattern> DataSetが扱うパターンクラスの型
 */
public class DataSet_Basic extends DataSet <Pattern_Basic>{

	/** 入力されたデータセットの基本情報をインスタンスを生成する
	 * @param dataSize データセットのパターン数
	 * @param ndim 属性数．次元数
	 * @param cnum 結論部クラスのラベル種類数
	 */
	public DataSet_Basic(int dataSize, int ndim, int cnum) {
		super(dataSize, ndim, cnum);
	}

	/**
	 * <h1>Input File for Multi-Label Classification Dataset</h1>
	 * @param fileName : String
	 * @return 入力済みDataSet
	 */
	private static inputSingleLabelDataSet(String fileName) {
		List<double[]> lines = inputDataAsList(fileName);

		// The first row is parameters of dataset
		DataSet_Basic<Pattern_Basic> data = new DataSet_Basic<Pattern_Basic>(
				(int)lines.get(0)[0],
				(int)lines.get(0)[1],
				(int)lines.get(0)[2]);
		lines.remove(0);

		// Later second row are patterns
		for(int n = 0; n < data.getDataSize(); n++) {
			double[] line = lines.get(n);

			int id = n;
			double[] vector = new double[data.getNdim()];
			Integer C;
			for(int i = 0; i < vector.length; i++) {
				vector[i] = line[i];
			}
			C = (int)line[data.getNdim()];

			AttributeVector inputVector = new AttributeVector(vector);
			ClassLabel_Basic classLabel = new ClassLabel_Basic(C);

			Pattern_Basic pattern = new Pattern_Basic(
					id,
					inputVector,
					classLabel);
			data.addPattern(pattern);
		}
		return data;
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
