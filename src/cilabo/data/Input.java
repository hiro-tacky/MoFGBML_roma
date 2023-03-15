package cilabo.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.uma.jmetal.util.checking.Check;

import cilabo.data.pattern.impl.Pattern_Basic;
import cilabo.data.pattern.impl.Pattern_MultiClass;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.main.Consts;
import cilabo.main.ExperienceParameter.CLASS_LABEL_TYPE;

/**
 * データセット入力用メソッド群 this class define methods to load data set file
 * @author Takigawa Hiroki
 */
public class Input {

	/**
	 * データセットファイルを読み込む
	 * Read sata set file
	 * @param filePath データセットのパス
	 * @param classLabelType ターゲットクラスのタイプ
	 * @return 読み込み済みDataSet
	 */
	public static DataSet<?> inputDataSet(String filePath, CLASS_LABEL_TYPE classLabelType) {
		switch(classLabelType) {
			case Multi:
				return inputDataSet_MultiLabel(filePath);
			case Single:
			default:
				return inputDataSet_Basic(filePath);
		}
	}

	/**
	 * 単一クラスラベル用データセットファイルを読み込む
	 * Read data set file for single target class
	 * @param filePath データセットのパス
	 * @return 読み込み済みDataSet
	 */
	private static DataSet<Pattern_Basic> inputDataSet_Basic(String filePath) {
		List<double[]> lines = inputDataAsList(filePath);

		int dataSetSize = (int)lines.get(0)[0];
		int numberOfDimension = (int)lines.get(0)[1];
		int numberOfClass = (int)lines.get(0)[2];
		lines.remove(0);

		// Later second row are patterns
		ArrayList<Pattern_Basic> patterns = new ArrayList<Pattern_Basic>();
		for(int n = 0; n < dataSetSize; n++) {
			double[] line = lines.get(n);

			int id = n;
			double[] vector = new double[numberOfDimension];
			Integer C;
			for(int i = 0; i < numberOfDimension; i++) {
				vector[i] = line[i];
			}
			C = (int)line[numberOfDimension];

			AttributeVector inputVector = new AttributeVector(vector);
			ClassLabel_Basic classLabel = new ClassLabel_Basic(C);

			Pattern_Basic pattern = new Pattern_Basic(
					id,
					inputVector,
					classLabel);
			patterns.add(pattern);
		}

		DataSet<Pattern_Basic> dataSet = new DataSet<Pattern_Basic>(
				dataSetSize,
				numberOfDimension,
				numberOfClass,
				patterns);

		return dataSet;
	}

	/**
	 * マルチクラスラベル用データセットファイルを読み込む
	 * Read data set file for multi target classes
	 * @param filePath データセットのパス
	 * @return 読み込み済みDataSet
	 */
	private static DataSet<Pattern_MultiClass> inputDataSet_MultiLabel(String filePath) {
		List<double[]> lines = inputDataAsList(filePath);

		// The first row is parameters of dataset
		int dataSetSize = (int)lines.get(0)[0];
		int numberOfDimension = (int)lines.get(0)[1];
		int numberOfClass = (int)lines.get(0)[2];
		lines.remove(0);

		// Later second row are patterns
		ArrayList<Pattern_MultiClass> patterns = new ArrayList<Pattern_MultiClass>();
		for(int n = 0; n < dataSetSize; n++) {
			double[] line = lines.get(n);

			int id = n;
			double[] vector = new double[numberOfDimension];
			Integer[] cVec = new Integer[numberOfClass];
			for(int i = 0; i < numberOfDimension; i++) {
				vector[i] = line[i];
			}
			for(int i = 0; i < numberOfClass; i++) {
				cVec[i] = (int)line[i + numberOfDimension];
			}

			AttributeVector inputVector = new AttributeVector(vector);
			ClassLabel_Multi classLabel = new ClassLabel_Multi(cVec);

			Pattern_MultiClass pattern = new Pattern_MultiClass(
					id,
					inputVector,
					classLabel);

			patterns.add(pattern);
		}

		DataSet<Pattern_MultiClass> dataSet = new DataSet<Pattern_MultiClass>(
				dataSetSize,
				numberOfDimension,
				numberOfClass,
				patterns);

		return dataSet;
	}

	/**
	 * 学習用及び評価用データセットファイルを読み込む
	 * Load training and test data set file for
	 * @param trainFile 学習用データセットのパス training data set file's path
	 * @param testFile 評価用データセットのパス test data set file's path
	 */
	public static void loadTrainTestFiles(String trainFile, String testFile, CLASS_LABEL_TYPE classLabelType) {

		/* Load Dataset ======================== */
		Check.isNotNull(trainFile);
		Check.isNotNull(testFile);

		DataSet<?> train = Input.inputDataSet(trainFile, classLabelType);
		DataSetManager.getInstance().addTrains(train);
		Consts.DATA_SIZE = train.getDataSetSize();
		Consts.ATTRIBUTE_NUMBER = train.getNumberOfDimension();
		Consts.CLASS_LABEL_NUMBER = train.getNumberOfClass();

		DataSet<?> test = Input.inputDataSet(trainFile, classLabelType);
		DataSetManager.getInstance().addTests(test);

		Check.isNotNull(DataSetManager.getInstance().getTrains());
		Check.isNotNull(DataSetManager.getInstance().getTests());
		return;
	}

//	public static DataSet inputSubdata(DataSet origin, DataSet divided, String fileName) {
//		List<double[]> lines = inputDataAsList(fileName);
//
//		// The first row is parameters of dataset
//		divided.setDataSize( (int)lines.get(0)[0] );
//		divided.setNdim( (int)lines.get(0)[1] );
//		divided.setCnum( (int)lines.get(0)[2] );
//		lines.remove(0);
//
//		// Later second row are patterns
//		for(int n = 0; n < divided.getDataSize(); n++) {
//			int id = (int)lines.get(n)[0];
//			Pattern pattern = origin.getPatternWithID(id);
//			divided.addPattern(pattern);
//		}
//		return divided;
//	}

//	/**
//	 * 引数に与えられた試行回数に応じたファイル名を作成するメソッド
//	 * @param dataName
//	 * @param cv_i
//	 * @param rep_i
//	 * @param isTra
//	 * @return String : a"rep_i"_"cv_i"_"dataName"-10"isTra"に応じたファイル名
//	 */
//	public static String makeFileNameOne(String dataName, int cv_i, int rep_i, boolean isTra) {
//		String sep = File.separator;
//		String fileName = "";
//		if(isTra) {
//			fileName = System.getProperty("user.dir") + sep + "dataset" + sep + dataName + sep + "a" + rep_i + "_" + cv_i + "_" + dataName + "-10tra.dat";
//		} else {
//			fileName = System.getProperty("user.dir") + sep + "dataset" + sep + dataName + sep + "a" + rep_i + "_" + cv_i + "_" + dataName + "-10tst.dat";
//		}
//		return fileName;
//	}

//	private static List<String> inputDataAsListString(String fileName) {
//		List<String> lines = new ArrayList<>();
//		try ( Stream<String> line = Files.lines(Paths.get(fileName)) ) {
//		    lines = line.collect( Collectors.toList() );
//		} catch (IOException e) {
//		    e.printStackTrace();
//		}
//		return lines;
//	}

	/**
	 *
	 * @param fileName
	 * @return : List{@literal <double[]>}
	 */
	public static List<double[]> inputDataAsList(String fileName) {
		List<double[]> lines = new ArrayList<double[]>();
		try ( Stream<String> line = Files.lines(Paths.get(fileName)) ) {
		    lines =
		    	line.map(s ->{
		    	String[] numbers = s.split(",");
		    	double[] nums = new double[numbers.length];

		    	//値が無い場合の例外処理
		    	for (int i = 0; i < nums.length; i++) {
		    			nums[i] = Double.parseDouble(numbers[i]);
				}
		    	return nums;
		    })
		    .collect( Collectors.toList() );
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return lines;
	}
}
