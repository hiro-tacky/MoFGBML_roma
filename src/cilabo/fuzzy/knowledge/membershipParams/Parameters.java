package cilabo.fuzzy.knowledge.membershipParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.uma.jmetal.util.checking.Check;

import cilabo.data.DataSet;
import cilabo.fuzzy.knowledge.FuzzySetBluePrintManager.FuzzySetBluePrint;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.main.Consts;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import cilabo.utility.Parallel;
import jfml.term.FuzzyTerm;

/** メンバシップ関数のパラメータ生成用クラス<br>
 * this class generates partitions for membership function
 * @author Takigawa Hiroki
 */
public class Parameters{
	/** パラメータ保存用バッファ [分割方式][次元数][区間ID] <br>
	 partitions container [dimension][division type][numberOfpartition] */
	private ArrayList<HashMap<DIVISION_TYPE, HashMap<Integer, Double[]>>> partitions = new ArrayList<HashMap<DIVISION_TYPE, HashMap<Integer, Double[]>>>();//partitions[DIVISION_TYPE][dim][分割数]=[パラメータ配列]
	/** エントロピー計算用データセット data set to calculate entropy*/
	private DataSet<?> dataSet;
	/** 次元数 number of dimension*/
	private int numberOfDimension;

	/**
	 * コンストラクタ．Initialize Parameters
	 * @param dataSet エントロピー計算用データセット data set to calculate entropy
	 */
	public Parameters(DataSet<?> dataSet) {
		Check.isNotNull(dataSet);
		this.dataSet = dataSet;
		this.numberOfDimension = dataSet.getNumberOfDimension();
		for(int dim_i=0; dim_i<numberOfDimension; dim_i++) {
			partitions.add(new HashMap<DIVISION_TYPE, HashMap<Integer, Double[]>>());
			for(DIVISION_TYPE divisionType: DIVISION_TYPE.values()) {
				partitions.get(dim_i).put(divisionType, new HashMap<Integer, Double[]>());
			}
		}
	}

	/**
	 * 指定された分割方式での分割区間リストを返します。<br>
	 * Returns partitions container that at the specified dimension
	 * @param dimension 次元 dimension
	 * @return 返される分割区間．partitions container to return
	 */
	public HashMap<DIVISION_TYPE, HashMap<Integer, Double[]>> getPartition(int dimension) {
		return this.partitions.get(dimension);
	}

	/**
	 * 指定された分割方式・次元での分割区間を返します。<br>
	 * Returns partitions container at the specified divisionType and dimension
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @return 指定された分割方式・次元にある分割区間．Partition at the specified division type and dimension
	 */
	public HashMap<Integer, Double[]> getPartition(int dimension, DIVISION_TYPE divisionType) {
		return this.partitions.get(dimension).get(divisionType);
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間を返します。<br>
	 * Returns partitions at the specified divisionType, dimension and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 * @return 指定された次元・分割方式・分割数にある分割区間．Partitions at the specified divisionType, dimension and number of partition
	 */
	public Double[] getPartition(int dimension, DIVISION_TYPE divisionType, int K) {
		if(!this.getPartition(dimension, divisionType).containsKey(K)) {
			this.makePartition(dimension, divisionType, K);
		}
		return this.partitions.get(dimension).get(divisionType).get(K);
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間を生成します。<br>
	 * Generate partitions at the specified division type, dimension and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 */
	public void makePartition( int dimension, DIVISION_TYPE divisionType, int K) {
		switch(divisionType) {
			case equalDivision:
			default:
				this.makeHomePartition(dimension, K);
				break;
			case entropyDivision:
				this.makeEntropyPartition(dimension, K);
				break;
		}
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間を生成します。<br>
	 * Generate partitions at the specified division type, dimension and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 */
	public void makePartition(int dimension, DIVISION_TYPE divisionType, int[] K) {
		for(int K_i: K) {
			this.makePartition(dimension, divisionType, K_i);
		}
	}

	/**
	 * 指定された次元・分割数での均等分割区間を生成します。<br>
	 * Generate equal distribution partitions at the specified division type and dimension
	 * @param dimension 次元 dimension
	 * @param K 分割数 number of partition
	 */
	public void makeHomePartition(int dimension, int K) {
		if (this.getPartition(dimension, DIVISION_TYPE.equalDivision).containsKey(K)) {
			return;
		}else {
			Double[] partition = new Double[K+1];
			partition[0] = 0d;
			for(int i=1; i<K; i++) {
				partition[i] = (double)(2*i-1)/((K-1)*2);
			}
			partition[K] = 1d;
			this.partitions.get(dimension).get(DIVISION_TYPE.equalDivision).put(K, partition);
		}
	}

	/**
	 * 指定された次元・分割数での均等分割区間を生成します。<br>
	 * Generate equal distribution partitions at the specified division type and dimension
	 * @param dimension 次元 dimension
	 * @param K 分割数 number of partition
	 */
	public void makeHomePartition(int dimension, int[] K) {
		for(int K_i: K) {
			this.makeHomePartition(dimension, K_i);
		}
	}

	/**
	 * 指定された次元・分割数での均等分割区間を生成します。<br>
	 * Generate entropy-based unequal distribution partitions at the specified divisionType and dimension
	 * @param dimension 次元 dimension
	 * @param K 分割数 number of partition
	 */
	public void makeEntropyPartition(int dimension, int K) {
		if (this.getPartition(dimension, DIVISION_TYPE.entropyDivision).containsKey(K)) {
			return;
		}else {
			//Step 0. Judge Categoric.
			if(dataSet.getPattern(0).getAttributeValue(dimension) < 0) {
				//If it's categoric, do NOT partitinon.
				return;
			}

			//Step 1. Sort patterns by attribute "dim_i"
			ArrayList<ForSortPattern> patterns = new ArrayList<ForSortPattern>();
			for(int p = 0; p < dataSet.getDataSetSize(); p++) {
				patterns.add( new ForSortPattern(dataSet.getPattern(p).getAttributeValue(dimension),
						dataSet.getPattern(p).getTargetClass()));
			}
			Collections.sort(patterns, new Comparator<ForSortPattern>() {
				@Override
				//Ascending Order
				public int compare(ForSortPattern o1, ForSortPattern o2) {
					if(o1.getX() > o2.getX()) {return 1;}
					else if(o1.getX() < o2.getX()) {return -1;}
					else {return 0;}
				}
			});

			//Step 3. add boundaries
			// Optimal Splitting.
			ArrayList<Double> boundaries = optimalSplitting(patterns, K, dataSet.getNumberOfClass());
			Double[]  buf = boundaries.toArray(new Double[boundaries.size()]);
			this.partitions.get(dimension).get(DIVISION_TYPE.entropyDivision).put(K, buf);
		}
	}

	/**
	 * 指定された次元・分割数での均等分割区間を生成します。<br>
	 * Generate entropy-based unequal distribution partitions at the specified divisionType and dimension
	 * @param dimension 次元 dimension
	 * @param K 分割数 number of partition
	 */
	public void makeEntropyPartition(int dimension, int[] K) {
		for(int K_i: K) {
			this.makeEntropyPartition(dimension, K_i);
		}
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間をから三角型ファジィ集合のパラメータを生成します。<br>
	 * Return triangle-shape fuzzy set parameter at the specified dimension, divisionType and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 * @return 生成された三角型ファジィ集合のパラメータ generated triangle-shape fuzzy set parameter
	 */
	public float[][] triangle(int dimension, DIVISION_TYPE divisionType, int K){
		Double[] buf = this.getPartition(dimension, divisionType, K);
		float[][] params = new float[K][3];
		for(int i=0; i<K; i++) {
			if(i == 0) {
				params[i] = new float[] {0f, 0f, 2*(float)(double)buf[i+1]};
			}else if(i == buf.length-2) {
				params[i] = new float[] {2*(float)(double)buf[i] - 1f, 1f, 1f};
			}else if(0 < i && i < buf.length-2){
				float left = (float)(double)buf[i], right = (float)(double)buf[i+1];
				params[i] = new float[] {left*3f/2f - right/2f, (left+right)/2f, right*3f/2f - left/2f};
			}
		}
		return params;
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間をから線形型ファジィ集合のパラメータを生成します。<br>
	 * Return liner-shape fuzzy set parameter at the specified dimension, divisionType and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 * @param F ファジィ度 degree of fuzzify
	 * @return 生成された線形型ファジィ集合のパラメータ generated liner-shape fuzzy set parameter
	 */
	public float[][] linerShape(int dimension, DIVISION_TYPE divisionType, int K, double F){
		Double[] buf = this.getPartition(dimension, divisionType, K);
		float[][] params = new float[K][4];
		ArrayList<Float> newPoints = new ArrayList<>();
		//領域左端の点を追加
		newPoints.add(0f);
		newPoints.add(0f);

		//Step 1. Fuzzify each partition without edge of domain.
		for(int i = 1; i < buf.length - 1; i++) {
			double left = buf[i-1];
			double point = buf[i];
			double right = buf[i+1];
			newPoints.addAll(fuzzify(left, point, right, F));
		}

		//Step 2. Take 4 points as trapezoids in order from head of newPoints.
		//領域右端の点を追加
		newPoints.add(1f);
		newPoints.add(1f);

		for(int i = 0; i < (newPoints.size() - 2) / 2; i++) {
			float[] trapezoid = new float[4];
			for(int j = 0; j < 4; j++) {
				trapezoid[j] = newPoints.get(i*2 + j);
			}
			params[i] = trapezoid;
		}
		return params;
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間をからガウシアン型ファジィ集合のパラメータを生成します。<br>
	 * Return gaussian-shape fuzzy set parameter at the specified dimension, divisionType and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 * @return 生成されたガウシアン型ファジィ集合のパラメータ generated gaussian-shape fuzzy set parameter
	 */
	public float[][] gaussian(int dimension, DIVISION_TYPE divisionType, int K){
		Double[] buf = this.getPartition(dimension, divisionType, K);
		float[][] params = new float[K][2];
		for(int i=0; i<K; i++) {
			//最初と最後だけ頂点が区間端になるようにする．
			if(i == 0){
				params[i] = calcGaussParam(0, (float)(double)buf[i+1], 0.5f);
			}else if(i == buf.length-2) {
				params[i] = calcGaussParam(1, (float)(double)buf[i], 0.5f);
			}else  if(0 < i && i < buf.length-2){
				double left = buf[i], right = buf[i+1];
				params[i] = calcGaussParam((float)(left + right)/2, (float)(double)buf[i], 0.5f);
			}
		}
		return params;
	}

	/**
	 * 平均 mean の正規分布(係数なし，x=meanのときvalue=1)について，
	 * 引数に与えられた，(x, value)を通る平均meanの正規分布の標準偏差を計算するメソッド
	 * Calculate gaussian-shape fuzzy set parameter
	 * @param mean 中央値 mean value
	 * @param x 隣接メンバシップ関数との閾値 threshold to next membership function
	 * @param value 閾値でとるメンバシップ値 membership value at the parameter 'x'
	 * @return [中央値, 分散] [mean value, deviation]
	 */
	private static float[] calcGaussParam(float mean, float x, float value) {
		float[] param;

		float variance;		//分散
		float deviation;	//標準偏差
		float numerator;	//分子
		float denominator;	//分母

		numerator = -((x - mean) * (x - mean));
		denominator = 2f * (float)Math.log(value);

		variance = numerator / denominator;
		deviation = (float)Math.sqrt(variance);

		param = new float[] {mean, deviation};

		return param;
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間から区間型ファジィ集合のパラメータを生成します。<br>
	 * Return rectangle-shape fuzzy set parameter at the specified dimension, divisionType and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 * @return 生成された区間型ファジィ集合のパラメータ generated rectangle-shape fuzzy set parameter
	 */
	public float[][] rectangle(int dimension, DIVISION_TYPE divisionType, int K){
		Double[] buf = this.getPartition(dimension, divisionType, K);
		float[][] params = new float[K][2];
		for(int i=0; i<K; i++) {
			params[i] = new float[] {(float)(double)buf[i], (float)(double)buf[i+1]};
		}
		return params;
	}

	/**
	 * 指定された次元・分割方式・分割数での分割区間から台形型ファジィ集合のパラメータを生成します。<br>
	 * Return trapezoid-shape fuzzy set parameter at the specified dimension, divisionType and number of partition
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 * @return 生成された台形型ファジィ集合のパラメータ generated trapezoid-shape fuzzy set parameter
	 */
	public float[][] trapezoid(int dimension, DIVISION_TYPE divisionType, int K){
		Double[] buf = this.getPartition(dimension, divisionType, K);
		float[][] params = new float[K][4];
		for(int i=0; i<K; i++) {
			if(i == 0) {
				params[i] = new float[] {0f, 0f, (float)0.5*(float)(double)buf[i+1], (float)1.5*(float)(double)buf[i+1]};
			}else if(i == buf.length-2) {
				params[i] = new float[] {(float)1.5*(float)(double)buf[i] - 0.5f, (float)0.5*(float)(double)buf[i+1] + 0.5f, 1f, 1f};
			}else {
				float left = (float)(double)buf[i], right = (float)(double)buf[i+1];
				params[i] = new float[] {left*5f/4f - right/4f, left*3f/4f + right/4f, right*3f/4f + left/4f, right*5f/4f - left/4f};
			}
		}
		return params;
	}

	/**
	 * 指定された次元・分割方式・分割数・ファジィ集合形状IDのパラメータを返します。<br>
	 * Returns partitions at the specified dimension, divisionType, number of partition and shape type ID
	 * @param dimension 次元 dimension
	 * @param divisionType 区間分割方式 division type of fuzzy set
	 * @param K 分割数 number of partition
	 * @param shapeTypeID ファジィセット形状ID shape type ID of fuzzy set
	 * @return 生成されたファジィ集合のパラメータ．generated fuzzy set parameter
	 */
	public float[][] getParameter(int dimension, DIVISION_TYPE divisionType, int K, int fuzzyTermShapeID){
		switch(fuzzyTermShapeID) {
		case FuzzyTerm.TYPE_gaussianShape:
			return this.gaussian(dimension, divisionType, K);

		case FuzzyTerm.TYPE_trapezoidShape:
			if(divisionType == DIVISION_TYPE.equalDivision) {
				return this.trapezoid(dimension, divisionType, K);
			}else if(divisionType == DIVISION_TYPE.entropyDivision){
				return this.linerShape(dimension, divisionType, K, Consts.FUZZY_GRADE);
			}

		case FuzzyTerm.TYPE_rectangularShape:
			return this.rectangle(dimension, divisionType, K);

		case FuzzyTerm.TYPE_triangularShape:
		default:
			return this.triangle(dimension, divisionType, K);
		}
	}

	/**
	 * 与えられたfuzzySetの設計図用クラスからパラメータを返す。<br>
	 * Returns partitions by fuzzy set blue print class
	 * @param FuzzyTermBP fuzzySetの設計図用クラス fuzzy set blue print class
	 * @return 生成されたファジィ集合のパラメータ．generated fuzzy set parameter
	 */
	public float[][] getParameter(FuzzySetBluePrint FuzzyTermBP){
		return this.getParameter(FuzzyTermBP.getDimension(), FuzzyTermBP.getDivisionType(), FuzzyTermBP.getK(), FuzzyTermBP.getShapeTypeID());
	}

	/**
	 * エントロピーに基づいた分割区間を返す．
	 * Class-entropy based searching optimal-partitionings
	 * @param patterns パターンのリスト list of patterns
	 * @param K 分割数 number of partitions
	 * @param Cnum ターゲットクラス数 numer of target class
	 * @return エントロピーに基づいた分割区間 entropy-based unequal distribution partitions
	 */
	public static ArrayList<Double> optimalSplitting(ArrayList<ForSortPattern> patterns, int K, int Cnum) {
		double D = patterns.size();

		ArrayList<Double> partitions = new ArrayList<>();
		partitions.add(0.0);
		partitions.add(1.0);

		//Step 1. Collect class changing point.
		ArrayList<Double> candidate = new ArrayList<>();
		double point = 0;
		Set<ClassLabel<?>> classLabelSet = new HashSet<ClassLabel<?>>();
		for(int p = 1; p < patterns.size(); p++) {
			classLabelSet.add(patterns.get(p).getConClass());
			if( !patterns.get(p-1).getConClass().equals(patterns.get(p).getConClass()) ) {
				point = 0.5 * (patterns.get(p-1).getX() + patterns.get(p).getX());
			}

			if(!candidate.contains(point) && point != 0 && point != 1) {
				candidate.add(point);
			}
		}

		//Step 2. Search K partitions which minimize class-entropy.
		for(int k = 2; k <= K; k++) {
			double[] entropy = new double[candidate.size()];

			//Calculate class-entropy for all candidates.
			for(int i = 0; i < candidate.size(); i++) {
				point = candidate.get(i);

				//Step 1. Count #of patterns in each partition.
				//D_jh means #of patterns which is in partition j and whose class is h.
				List<HashMap<ClassLabel<?>, Double>> Djh = new ArrayList<HashMap<ClassLabel<?>, Double>>(k);
				for(int k_i=0; k_i<k; k_i++) {Djh.add(k_i, new HashMap<ClassLabel<?>, Double>());}
				double[] Dj = new double[k];

				ArrayList<Double> range = new ArrayList<>();
				Collections.sort(partitions);	//Ascending Order
				boolean yetContain = true;
				for(int r = 0; r < partitions.size(); r++) {
					if(yetContain && point < partitions.get(r)) {
						range.add(point);
						yetContain = false;
					}
					range.add(partitions.get(r));
				}
				for(int part = 0; part < k; part++) {
					final double LEFT = range.get(part);
					final double RIGHT = range.get(part+1);
					for(ClassLabel<?> classLabel : classLabelSet) {
						try {
							Optional<Double> partSum = Parallel.getInstance().getLearningForkJoinPool().submit( () ->
							patterns.parallelStream()
									.filter(p -> p.getConClass().equals(classLabel))
									.filter(p -> LEFT <= p.getX() && p.getX() <= RIGHT)
									.map(p -> {
										if(p.getX() == 0.0 || p.getX() == 1.0) {return 1.0;}
										else if(p.getX() == LEFT || p.getX() == RIGHT) {return 0.5;}
										else {return 1.0;}
									})
									.reduce( (l,r) -> l+r)
									).get();
							Djh.get(part).put(classLabel, partSum.orElse(0.0));
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
						//Without Classes
						Dj[part] += Djh.get(part).get(classLabel);
					}
				}

				//Step 2. Calculate class-entropy.
				double sum = 0.0;
				for(int part = 0; part < k; part++) {
					double subsum = 0.0;
					for(ClassLabel<?> classLabel : classLabelSet) {
						if(Dj[part] != 0.0 && (Djh.get(part).get(classLabel) / Dj[part]) > 0.0) {
							subsum += (Djh.get(part).get(classLabel) / Dj[part]) * log( (Djh.get(part).get(classLabel) / Dj[part]), 2.0);
						}
					}
					sum += (Dj[part] / D) * subsum;
				}
				entropy[i] = -sum;
			}

			//Find minimize class-entropy.
			if(entropy.length > 0) {
				double min = entropy[0];
				int minIndex = 0;
				for(int i = 1; i < candidate.size(); i++) {
					if(entropy[i] < min) {
						min = entropy[i];
						minIndex = i;
					}
				}
				partitions.add(candidate.get(minIndex));
				candidate.remove(minIndex);
				if(candidate.size() == 0) {
					break;
				}
			}
		}
		if(partitions.size() < K+1) {
			for(int i=2; i<=K; i++) {
				for(int j=1; j<i; j++) {
					double tmp = (double)j/i;
					if(partitions.size() < K+1 && !partitions.contains(tmp)) {
						partitions.add(tmp);
					}
				}
			}
		}
		Collections.sort(partitions);	//Ascending Order
		return partitions;
	}

	/**
	 * 与えられた分割区間・ファジィ度からパラメータを返す．
	 * Fuzzify two partitions [left, point] and [point, right].<br>
	 *
	 * @param left Domain Left
	 * @param point Crisp Point
	 * @param right Domain Right
	 * @param F Grade of overwraping
	 * @return fuzzfied two point
	 */
	public static ArrayList<Float> fuzzify(double left, double point, double right, double F) {
		ArrayList<Float> two = new ArrayList<>();

		//Step 1. Minimize Range (left-point) or (point-right)
		if( (point-left) < (right-point) ) {
			//point is closer to left than right, then right moves.
			right = point + (point-left);
		} else {
			//point is closer to right than left, then left moves.
			left = point - (right-point);
		}

		//Step 2. Make most fuzzified partition and most crisp partition.
		double ac_F0 = point;
		double ac_F1 = 0.5 * (left + point);
		double bd_F0 = point;
		double bd_F1 = 0.5 * (right + point);

		//Step 3. Make F graded fuzzified partition
		double ac_F = ac_F0 + (ac_F1 - ac_F0)*F;
		double bd_F = bd_F0 + (bd_F1 - bd_F0)*F;

		//Step 4. Get Fuzzified two point which has membership value 1.0.
		two.add((float)ac_F);
		two.add((float)bd_F);

		return two;
	}

	/**
	 * <h1>log関数 底の変換公式</h1>
	 * @param a : double : 引数
	 * @param b : double : 底
	 * @return double : log_b (a)
	 */
	public static double log(double a, double b) {
		return (Math.log(a) / Math.log(b));
	}

	/**
	 * データセットの属性数/次元を返します。
	 * Returns deta set's number of dimension
	 * @return データセットの属性数/次元 deta set's number of dimension
	 */
	public int getNumberOfDimension() {
		return numberOfDimension;
	}

}


/**
 * パターンのソート用の一時的クラス
 * @author Takigawa Hiroki
 */
class ForSortPattern{
	/**  */
	double x;
	double index;
	ClassLabel<?> trueClass;

	ForSortPattern(double x, ClassLabel<?> conClass){
		this.x = x;
		this.trueClass = conClass;
	}

	double getX(){
		return x;
	}

	ClassLabel<?> getConClass() {
		return trueClass;
	}

	double getIndex() {
		return index;
	}
}

