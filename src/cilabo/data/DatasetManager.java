package cilabo.data;

import java.util.ArrayList;
import java.util.Objects;

import cilabo.data.dataSet.impl.DataSet_Basic;
import cilabo.main.Consts;

/** 学習用データ，評価用データのデータセットを保持するマネージャークラス.<br>
 * this class has one training dataset and one test dataset.<br>
 * singletone デザインパターンを採用．実行ファイル上で唯一のインスタンスを生成．使用時はgetInstance()で呼び出す．
 * @author Takigawa Hiroki
 */
public class DatasetManager {

	/** 自分自身のインスタンス */
	private static DatasetManager instance = new DatasetManager();

	// ** 学習用データセット <br>training dataset*/
	private ArrayList<DataSet_Basic<?>> trains = new ArrayList<>();
	// ** 評価用データセット <br>test dataset*/
	private ArrayList<DataSet_Basic<?>> tests = new ArrayList<>();

	private DatasetManager() {}

	/**
	 * DatasetManager のインスタンスを取得
	 * @return Knowledge
	 */
	public static DatasetManager getInstance() {
		return instance;
	}

	/**
	 * 入力された学習用データセットを追加します。<br>
	 * Appends training data set to this instance
	 * @param train リストに追加される学習用データセット．training data set to be appended to the list
	 */
	protected void addTrains(DataSet_Basic<?> train) {
		this.trains.add(train);
	}

	/**
	 * 入力された評価用データセットを追加します。<br>
	 * Appends test data set to this instance
	 * @param test リストに追加される評価用データセット．test data set to be appended to the list
	 */
	protected void addTests(DataSet_Basic<?> test) {
		this.tests.add(test);
	}

	/**
	 * このインスタンスが持つ学習用データセットリストを返します。<br>
	 * Returns list of training data set that this instance has.
	 * @return 返される学習用データセットリスト．list of training data set to return
	 */
	public ArrayList<DataSet_Basic<?>> getTrains() {
		if(Objects.isNull(trains)) {
			throw new NullPointerException("trains hasn't been initialised@TrainTestDatasetManager.getTrains()");}
		return this.trains;
	}

	/**
	 * このインスタンスが持つ評価用データセットリストを返します。<br>
	 * Returns list of test data set that this instance has.
	 * @return 返される評価用データセットリスト．list of test data set to return
	 */
	public ArrayList<DataSet_Basic<?>> getTests() {
		if(Objects.isNull(tests)) {
			throw new NullPointerException("tests hasn't been initialised@TrainTestDatasetManager.getTests()");}
		return this.tests;
	}

	/** このインスタンスが保持するデータセットを初期化 */
	public void clear() {
		this.tests = new ArrayList<>();
		this.trains = new ArrayList<>();
	}

	/**
	 * ファイル名を指定してデータセットをロード
	 * @param trainFile 学習用データセットのパス
	 * @param testFile 評価用データセットのパス
	 * @return データセットが追加されたDatasetManagerインスタンス
	 */
	public DatasetManager loadTrainTestFiles(String trainFile, String testFile) {
		if(Objects.isNull(trains)) {
			throw new IllegalArgumentException("argument [trainFile] is null @TrainTestDatasetManager.loadTrainTestFiles()");}
		if(Objects.isNull(tests)) {
			throw new IllegalArgumentException("argument [testFile] is null @TrainTestDatasetManager.loadTrainTestFiles()");}

		DataSet_Basic<?> train = Input.inputDataSet(trainFile);
		addTrains(train);
		Consts.DATA_SIZE = train.getDataSize();
		Consts.ATTRIBUTE_NUMBER = train.getNdim();
		Consts.CLASS_LABEL_NUMBER = train.getCnum();

		DataSet_Basic<?> test = Input.inputDataSet(testFile);
		addTests(test);

		if(Objects.isNull(trains)) {
			throw new IllegalArgumentException("failed to initialise trains@TrainTestDatasetManager.loadTrainTestFiles()");}
		else if(Objects.isNull(tests)) {
			throw new IllegalArgumentException("failed to initialise tests@TrainTestDatasetManager.loadTrainTestFiles()");}

		return this;
	}
}
