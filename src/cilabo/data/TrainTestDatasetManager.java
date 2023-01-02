package cilabo.data;

import java.util.ArrayList;
import java.util.Objects;

import cilabo.main.Consts;
import cilabo.main.ExperienceParameter;
import cilabo.utility.Input;

/** 学習用データ1つ，評価用データ1つのデータセットを保持するクラス.<br>
 * this class has one training dataset and one test dataset.<br>
 * singletone デザインパターンを採用．実行ファイル上で唯一のインスタンスを生成．使用時はgetInstance()で呼び出す．
 * @author Takigawa Hiroki
 */
public class TrainTestDatasetManager {
	private static TrainTestDatasetManager instance = new TrainTestDatasetManager();
	// ** 学習用データセット <br>training dataset*/
	final ArrayList<DataSet> trains = new ArrayList<>();
	// ** 評価用データセット <br>test dataset*/
	final ArrayList<DataSet> tests = new ArrayList<>();

	private TrainTestDatasetManager() {}

	/** 空のインスタンスを生成 <br> Constructs an empty instance of class
	 @return 空のインスタンス
	 */
	public static TrainTestDatasetManager getInstance() {
		return instance;
	}

	/** 学習用データセットを追加
	 * @param train 学習用データセット
	 */
	protected void addTrains(DataSet train) {
		this.trains.add(train);
	}

	/** 評価用データセットを追加
	 * @param test 評価用データセット
	 */
	protected void addTests(DataSet test) {
		this.tests.add(test);
	}

	/** 学習用データセットを取得
	 * @return 学習用データセット
	 */
	public ArrayList<DataSet> getTrains() {
		if(Objects.isNull(this.trains)) {System.err.println("TrainTestDatasetManager is null");}
		return this.trains;
	}

	/** 評価用データセットを取得
	 * @return 評価用データセット
	 */
	public ArrayList<DataSet> getTests() {
		if(Objects.isNull(this.tests)) {System.err.println("TrainTestDatasetManager is null");}
		return this.tests;
	}

	/**
	 * ファイル名を指定してデータセットをロードする関数
	 * @param trainFile 学習用データセットのパス
	 * @param testFile 評価用データセットのパス
	 * @return 生成されたTrainTestDatasetManagerインスタンス
	 */
	public TrainTestDatasetManager loadTrainTestFiles(String trainFile, String testFile) {
		if(Objects.isNull(trainFile) || Objects.isNull(testFile)) { throw new NullPointerException("fileNameString is null");}

		ExperienceParameter.classlabel = ExperienceParameter.ClassLabel.Single;

		DataSet train = Input.inputDataSet(trainFile);
		addTrains(train);
		Consts.DATA_SIZE = train.getDataSize();
		Consts.ATTRIBUTE_NUMBER = train.getNdim();
		Consts.CLASS_LABEL_NUMBER = train.getCnum();

		DataSet test = Input.inputDataSet(testFile);
		addTests(test);

		return this;
	}
}
