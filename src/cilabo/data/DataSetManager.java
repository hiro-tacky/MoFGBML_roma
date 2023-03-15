package cilabo.data;

import java.util.ArrayList;
import java.util.Objects;

/** 学習用データ，評価用データのデータセットを保持するマネージャークラス.<br>
 * data set manager class. this class has training data sets and test data sets.<br>
 * singleton デザインパターンを採用．使用時はgetInstanceでプロジェクト上のどこからでも呼び出せます．
 * this class adopts singleton design. when you use this class, you call getInstance methods anywhere.
 * @author Takigawa Hiroki
 */
public class DataSetManager {

	/** 自分自身のインスタンス */
	private static DataSetManager instance = new DataSetManager();
	/** 学習用データセット training dataset*/
	private ArrayList<DataSet<?>> trains = new ArrayList<>();
	/** 評価用データセット test dataset*/
	private ArrayList<DataSet<?>> tests = new ArrayList<>();

	/** コンストラクタ constructor */
	private DataSetManager() {}

	/**
	 * DataSetManager インスタンスを取得。<br>
	 * Gets DataSetManager instance
	 * @return DataSetManager インスタンス．DataSetManager instance
	 */
	public static DataSetManager getInstance() {
		return instance;
	}

	/**
	 * 入力された学習用データセットを追加します。<br>
	 * Add training data set to this instance
	 * @param train リストに追加される学習用データセット．training data set to be added to the list
	 */
	public void addTrains(DataSet<?> train) {
		this.trains.add(train);
	}

	/**
	 * 入力された評価用データセットを追加します。<br>
	 * Add test data set to this instance
	 * @param test リストに追加される評価用データセット．test data set to be added to the list
	 */
	public void addTests(DataSet<?> test) {
		this.tests.add(test);
	}

	/**
	 * このインスタンスが持つ学習用データセットリストを返します。<br>
	 * Returns list of training data set that this instance has.
	 * @return 返される学習用データセットリスト．list of training data set to return
	 */
	public ArrayList<DataSet<?>> getTrains() {
		if(Objects.isNull(trains)) {
			throw new NullPointerException("trains hasn't been initialised @" + this.getClass().getSimpleName());}
		return this.trains;
	}

	/**
	 * このインスタンスが持つ評価用データセットリストを返します。<br>
	 * Returns list of test data set that this instance has.
	 * @return 返される評価用データセットリスト．list of test data set to return
	 */
	public ArrayList<DataSet<?>> getTests() {
		if(Objects.isNull(tests)) {
			throw new NullPointerException("tests hasn't been initialised @" + this.getClass().getSimpleName());}
		return this.tests;
	}

	/** このインスタンスが保持するデータセットを初期化 Clear training and test data sets that this instance has*/
	public void clear() {
		this.tests = new ArrayList<>();
		this.trains = new ArrayList<>();
	}
}
