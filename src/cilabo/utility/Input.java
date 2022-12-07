package cilabo.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cilabo.data.DataSet;
import cilabo.data.InputVector;
import cilabo.data.pattern.impl.Pattern_Basic;
import cilabo.data.pattern.impl.Pattern_MultiClass;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Basic;
import cilabo.fuzzy.rule.consequent.classLabel.impl.ClassLabel_Multi;
import cilabo.main.ExperienceMethods;

public class Input {

	/**
	 * <h1>Input File for Single-Label Classification Dataset</h1>
	 * @param data : DataSet
	 * @param fileName : String
	 */

	public static DataSet inputDataSet(String fileName) {
		switch(ExperienceMethods.classlabel) {
			case Multi:
				return inputMultiLabelDataSet(fileName);
			case Single:
			default:
				return inputSingleLabelDataSet(fileName);
		}
	}

	private static DataSet inputSingleLabelDataSet(String fileName) {
		List<double[]> lines = inputDataAsList(fileName);

		// The first row is parameters of dataset
		DataSet data = new DataSet(
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

			InputVector inputVector = new InputVector(vector);
			ClassLabel_Basic classLabel = new ClassLabel_Basic(C);

			Pattern_Basic pattern = new Pattern_Basic(
					id,
					inputVector,
					classLabel);
			data.addPattern(pattern);
		}
		return data;
	}

	/**
	 * <h1>Input File for Multi-Label Classification Dataset</h1>
	 * @param data : DataSet
	 * @param fileName : String
	 */
	private static DataSet inputMultiLabelDataSet(String fileName) {
		List<double[]> lines = inputDataAsList(fileName);

		// The first row is parameters of dataset
		DataSet data = new DataSet(
				(int)lines.get(0)[0],
				(int)lines.get(0)[1],
				(int)lines.get(0)[2]);
		lines.remove(0);

		// Later second row are patterns
		for(int n = 0; n < data.getDataSize(); n++) {
			double[] line = lines.get(n);

			int id = n;
			double[] vector = new double[data.getNdim()];
			Integer[] cVec = new Integer[data.getCnum()];
			for(int i = 0; i < vector.length; i++) {
				vector[i] = line[i];
			}
			for(int i = 0; i < data.getCnum(); i++) {
				cVec[i] = (int)line[i + data.getNdim()];
			}

			InputVector inputVector = new InputVector(vector);
			ClassLabel_Multi classLabel = new ClassLabel_Multi(cVec);

			Pattern_MultiClass pattern = new Pattern_MultiClass(
					id,
					inputVector,
					classLabel);
			data.addPattern(pattern);
		}
		return data;
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
	private static List<double[]> inputDataAsList(String fileName) {
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
