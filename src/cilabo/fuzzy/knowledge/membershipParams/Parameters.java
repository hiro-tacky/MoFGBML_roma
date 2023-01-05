package cilabo.fuzzy.knowledge.membershipParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import cilabo.data.DataSet;
import cilabo.fuzzy.rule.consequent.classLabel.ClassLabel;
import cilabo.main.Consts;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import cilabo.main.ExperienceParameter.SHAPE_TYPE_NAME;
import cilabo.utility.Parallel;
import jfml.term.FuzzyTerm;

public class Parameters {
	public HashMap<DIVISION_TYPE, ArrayList<HashMap<Integer, Double[]>>> partitions = new HashMap<DIVISION_TYPE, ArrayList<HashMap<Integer, Double[]>>>();//partitions[DIVISION_TYPE][dim][分割数]=[パラメータ配列]
	public DataSet dataSet;
	public int Ndim;

	public Parameters(DataSet dataSet, int Ndim) {
		this.dataSet = dataSet;
		this.Ndim = Ndim;
		for(DIVISION_TYPE tmp: DIVISION_TYPE.values()) {
			partitions.put(tmp, new ArrayList<HashMap<Integer, Double[]>>(Ndim));
			for(int i=0; i<Ndim; i++) {
				partitions.get(tmp).add(new HashMap<Integer, Double[]>());
			}
		}
	}

	public ArrayList<HashMap<Integer, Double[]>> getPartition(DIVISION_TYPE divisionType) {
		return this.partitions.get(divisionType);
	}

	public HashMap<Integer, Double[]> getPartition(DIVISION_TYPE divisionType, int dim) {
		return this.partitions.get(divisionType).get(dim);
	}

	public Double[] getPartition(DIVISION_TYPE divisionType, int dim, int K) {
		if(!this.partitions.get(divisionType).get(dim).containsKey(K)) {
			this.makePartition(divisionType, dim, K);
		}
		return this.partitions.get(divisionType).get(dim).get(K);
	}

	/** パラメータを生成する
	 * @param dim 次元
	 * @param divisionType 分割方式
	 * @param k 分割数
	 */
	public void makePartition(DIVISION_TYPE divisionType, int dim, int K) {
		switch(divisionType) {
			case equalDivision:
			default:
				this.makeHomePartition(dim, K);
				break;
			case entropyDivision:
				this.makeEntropyPartition(dim, K);
				break;
		}
	}

	/**
	 * 等分割の分割区間を生成する
	 * @param K 分割数のリスト
	 */
	public void makeHomePartition(int dim, int K) {
		if (this.partitions.get(DIVISION_TYPE.equalDivision).get(dim).containsKey(K)) {
			return;
		}else {
			Double[] partition = new Double[K+1];
			partition[0] = 0d;
			for(int i=1; i<K; i++) {
				partition[i] = (double)(2*i-1)/((K-1)*2);
			}
			partition[K] = 1d;
			this.partitions.get(DIVISION_TYPE.equalDivision).get(dim).put(K, partition);
		}
	}

	/**
	 * エントロピーに基づいた分割区間を生成する
	 * @param tra データセット
	 * @param K 分割数のリスト
	 * @param dim 導出する属性の次元を指定
	 */
	public void makeEntropyPartition(int dim, int K) {
		if (this.partitions.get(DIVISION_TYPE.entropyDivision).get(dim).containsKey(K)) {
			return;
		}else {

			//Step 0. Judge Categoric.
			if(dataSet.getPattern(0).getInputValue(dim) < 0) {
				//If it's categoric, do NOT partitinon.
				return;
			}

			//Step 1. Sort patterns by attribute "dim_i"
			ArrayList<ForSortPattern> patterns = new ArrayList<ForSortPattern>();
			for(int p = 0; p < dataSet.getDataSize(); p++) {
				patterns.add( new ForSortPattern(dataSet.getPattern(p).getInputValue(dim),
						dataSet.getPattern(p).getTrueClass()));
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
			ArrayList<Double> boundaries = optimalSplitting(patterns, K, dataSet.getCnum());
			Double[]  buf = boundaries.toArray(new Double[boundaries.size()]);
			this.partitions.get(DIVISION_TYPE.entropyDivision).get(dim).put(K, buf);
		}
	}

	/**
	 * 三角形型のパラメータを生成する．
	 *
	 * @return パラメータ[ファジイセットID][パラメータ]
	 */
	public float[][] triangle(DIVISION_TYPE divisionType, int dim, int K){
		Double[] buf = this.getPartition(divisionType, dim, K);
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
	 * 不均一な線形型のパラメータを生成する．
	 *
	 * @return パラメータ[ファジイセットID][パラメータ](台形型パラメータ)
	 */
	public float[][] linerShape(DIVISION_TYPE divisionType, int dim, int K, double F){
		Double[] buf = this.getPartition(divisionType, dim, K);
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

	public float[][] gaussian(DIVISION_TYPE divisionType, int dim, int K){
		Double[] buf = this.getPartition(divisionType, dim, K);
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
	 * @param mean
	 * @param x
	 * @param value
	 * @return
	 */
	public static float[] calcGaussParam(float mean, float x, float value) {
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
	 * 区間型のパラメータを生成する．
	 *
	 * @return パラメータ[ファジイセットID][パラメータ]
	 */
	public float[][] rectangle(DIVISION_TYPE divisionType, int dim, int K){
		Double[] buf = this.getPartition(divisionType, dim, K);
		float[][] params = new float[K][2];
		for(int i=0; i<K; i++) {
			params[i] = new float[] {(float)(double)buf[i], (float)(double)buf[i+1]};
		}
		return params;
	}

	/**
	 * 台形型のパラメータを生成する．
	 *
	 * @return パラメータ[ファジイセットID][パラメータ]
	 */
	public float[][] trapezoid(DIVISION_TYPE divisionType, int dim, int K){
		Double[] buf = this.getPartition(divisionType, dim, K);
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
	 * <h1>Class-entropy based searching optimal-partitionings</h1>
	 * @param patterns : {@literal ArrayList<ForSortPattern>} :
	 * @param K : int : Given number of partitions
	 * @param Cnum : int : #of classes
	 * @return
	 */
	public static ArrayList<Double> optimalSplitting(ArrayList<ForSortPattern> patterns, int K, int Cnum) {
		double D = patterns.size();

		ArrayList<Double> partitions = new ArrayList<>();
		partitions.add(0.0);
		partitions.add(1.0);

		//Step 1. Collect class changing point.
		ArrayList<Double> candidate = new ArrayList<>();
		double point = 0;
//		candidate.add(point);
		for(int p = 1; p < patterns.size(); p++) {
			if(patterns.get(p-1).getConClass() != patterns.get(p).getConClass()) {
				point = 0.5 * (patterns.get(p-1).getX() + patterns.get(p).getX());
			}

			if(!candidate.contains(point) && point != 0 && point != 1) {
				candidate.add(point);
			}
		}
//		candidate.remove(0);

		//Step 2. Search K partitions which minimize class-entropy.
		for(int k = 2; k <= K; k++) {
			double[] entropy = new double[candidate.size()];

			//Calculate class-entropy for all candidates.
			for(int i = 0; i < candidate.size(); i++) {
				point = candidate.get(i);

				//Step 1. Count #of patterns in each partition.
				//D_jh means #of patterns which is in partition j and whose class is h.
				double[][] Djh = new double[k][Cnum];
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
					for(int c = 0; c < Cnum; c++) {
						final int CLASSNUM = c;
						try {
							Optional<Double> partSum = Parallel.getInstance().getLearningForkJoinPool().submit( () ->
							patterns.parallelStream()
									.filter(p -> p.getConClass().getClassLabelInteger() == CLASSNUM)
									.filter(p -> LEFT <= p.getX() && p.getX() <= RIGHT)
									.map(p -> {
										if(p.getX() == 0.0 || p.getX() == 1.0) {return 1.0;}
										else if(p.getX() == LEFT || p.getX() == RIGHT) {return 0.5;}
										else {return 1.0;}
									})
									.reduce( (l,r) -> l+r)
									).get();
							Djh[part][c] = partSum.orElse(0.0);
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
						//Without Classes
						Dj[part] += Djh[part][c];
					}
				}

				//Step 2. Calculate class-entropy.
				double sum = 0.0;
				for(int j = 0; j < k; j++) {
					double subsum = 0.0;
					for(int h = 0; h < Cnum; h++) {
						if(Dj[j] != 0.0 && (Djh[j][h] / Dj[j]) > 0.0) {
							subsum += (Djh[j][h] / Dj[j]) * log( (Djh[j][h] / Dj[j]), 2.0);
						}
					}
					sum += (Dj[j] / D) * subsum;
				}
				entropy[i] = -sum;
			}

			//Find minimize class-entropy.
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

	public float[][] getParameter(DIVISION_TYPE divisionType, int dim, int K, int fuzzyTermShapeID){
		switch(fuzzyTermShapeID) {
		case FuzzyTerm.TYPE_gaussianShape:
			return this.gaussian(divisionType, dim, K);

		case FuzzyTerm.TYPE_trapezoidShape:
			if(divisionType == DIVISION_TYPE.equalDivision) {
				return this.trapezoid(divisionType, dim, K);
			}else if(divisionType == DIVISION_TYPE.entropyDivision){
				return this.linerShape(divisionType, dim, K, Consts.FUZZY_GRADE);
			}

		case FuzzyTerm.TYPE_rectangularShape:
			return this.rectangle(divisionType, dim, K);

		case FuzzyTerm.TYPE_triangularShape:
		default:
			return this.triangle(divisionType, dim, K);
		}
	}

	public float[][] getParameter(DIVISION_TYPE divisionType, int dim, int K, SHAPE_TYPE_NAME fuzzyTermShapeName){
		switch(fuzzyTermShapeName) {
			case gaussian:
				return this.gaussian(divisionType, dim, K);

			case trapezoid:
				if(divisionType == DIVISION_TYPE.equalDivision) {
					return this.trapezoid(divisionType, dim, K);
				}else if(divisionType == DIVISION_TYPE.entropyDivision){
					return this.linerShape(divisionType, dim, K, Consts.FUZZY_GRADE);
				}

			case interval:
				return this.rectangle(divisionType, dim, K);

			case triangle:
			default:
				return this.triangle(divisionType, dim, K);
		}
	}

	/**
	 * <h1>Fuzzifying Partition</h1>
	 * Fuzzify two partitions [left, point] and [point, right].<br>
	 *
	 * @param left : double : Domain Left
	 * @param point : double : Crisp Point
	 * @param right : double : Domain Right
	 * @param F : double : Grade of overwraping
	 * @return {@literal ArrayList<Double} : fuzzfied two point
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

}


class ForSortPattern{
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

