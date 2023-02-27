package cilabo.data;

import java.util.ArrayList;
import java.util.Objects;

/** 学習用データ，評価用データのデータセットを保持するマネージャークラス.<br>
 * this class has one training dataset and one test dataset.<br>
 * singleton デザインパターンを採用．使用時はgetInstanceでプロジェクト上のどこからでも呼び出せます．
 * @author Takigawa Hiroki
 */
public class DataSetManager {

	/** 自分自身のインスタンス */
	private static DataSetManager instance = new DataSetManager();

	// ** 学習用データセット <br>training dataset*/
	private ArrayList<DataSet<?>> trains = new ArrayList<>();
	// ** 評価用データセット <br>test dataset*/
	private ArrayList<DataSet<?>> tests = new ArrayList<>();

	private DataSetManager() {}

	/**
	 * DataSetManager のインスタンスを取得
	 * @return DataSetManager インスタンス
	 */
	public static DataSetManager getInstance() {
		return instance;
	}

	/**
	 * 入力された学習用データセットを追加します。<br>
	 * Appends training data set to this instance
	 * @param train リストに追加される学習用データセット．training data set to be appended to the list
	 */
	public void addTrains(DataSet<?> train) {
		this.trains.add(train);
	}

	/**
	 * 入力された評価用データセットを追加します。<br>
	 * Appends test data set to this instance
	 * @param test リストに追加される評価用データセット．test data set to be appended to the list
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

	/** このインスタンスが保持するデータセットを初期化 */
	public void clear() {
		this.tests = new ArrayList<>();
		this.trains = new ArrayList<>();
	}

}
