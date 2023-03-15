package cilabo.utility;

import java.util.ArrayList;
import java.util.List;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;
import random.MersenneTwisterFast;


public class GeneralFunctions {


	public static boolean checkRule(List<MichiganSolution<?>> offspring) {
		for(MichiganSolution<?> michiganSolution_tmp : offspring) {
			if(michiganSolution_tmp.getRule().getConsequent().getClassLabel().isRejectedClassLabel()) return false;
		}
		return true;
	}

	public static boolean checkRule(PittsburghSolution<?> offspring) {
		if(offspring.getNumberOfVariables() < 1) {return false;}
		for(MichiganSolution<?> michiganSolution_tmp : offspring.getVariables()) {
			if(michiganSolution_tmp.getRule().getConsequent().getClassLabel().isRejectedClassLabel()) return false;
		}
		return true;
	}

	/** 配列の全要素の総和を返す．
	 * @param array
	 * @return 総和 */
	public static int sumOfArray(int[] array) {
		int sum = 0;
		for( int i=0; i<array.length; i++ ) {
			sum += array[i];
		}
		return sum;
	}

	/** 配列の全要素の総和を返す．
	 * @param array
	 * @return 総和 */
	public static double sumOfArray(double[] array) {
		double sum = 0;
		for( int i=0; i<array.length; i++ ) {
			sum += array[i];
		}
		return sum;
	}

	/**
	 * <h1> Uniform variant line separator</h1>
	 * @param original : String
	 * @return String
	 */
	public static String uniformLineSeparator(String original) {
		String windows = "\r\n";
		String mac = "\n\r";
		String unix = "\n";
		String ln = System.lineSeparator();
		if(original.contains(windows)) {
			 return original.replace(windows, ln);
		}
		else if(original.contains(mac)) {
			return original.replace(mac, ln);
		}
		else {
			return original.replace(unix, ln);
		}
	}

	/**
	 * <h1>Distance between 2 vectors.</h1>
	 * @param vector1 : double[] :
	 * @param vector2 : double[] :
	 * @return double : distance between vector1 and vector2.
	 */
	public static double distanceVectors(Double[] vector1, Double[] vector2) {
		if(vector1.length != vector2.length) {
			return -1;
		}
		double sum = 0.0;
		for(int n = 0; n < vector1.length; n++) {
			sum += (vector1[n] - vector2[n]) * (vector1[n] - vector2[n]);
		}
		return Math.sqrt(sum);
	}

	/**
	 * <h1>Calculation norm</h1>
	 * @param vector : double[]
	 * @return double : norm of vector
	 */
	public static double vectorNorm(Double[] vector) {
		double norm = 0.0;
		double sum = 0.0;
		for(int i = 0; i < vector.length; i++) {
			sum += vector[i] * vector[i];
		}
		norm = Math.sqrt(sum);
		return norm;
	}

	/**
	 * <h1>Calculate Inner Product of a and b.</h1>
	 * @param a : double[]
	 * @param b : double[]
	 * @return double : Inner Product
	 */
	public static double innerProduct(Double[] a, Double[] b) {
		if(a.length != b.length) {
			return -1;
		}
		Double[] ab = new Double[a.length];
		for(int i = 0; i < a.length; i++) {
			ab[i] = a[i] * b[i];
		}
		return GeneralFunctions.vectorNorm(ab);
	}

	/**
	 * <h1>Hamming Distance</h1>
	 * @param a : Integer[] :
	 * @param b : Integer[] :
	 * @return double :
	 */
	public static double hammingDistance(Integer[] a, Integer[] b) {
		double distance = 0.0;
		for(int i = 0; i < a.length; i++) {
			if(a[i] != b[i]) {
				distance++;
			}
		}
		return distance;
	}

	/**
	 * 非復元抽出<br>
	 * Sampling without replacement<br>
	 * @param box : int : 元のデータサイズ
	 * @param want : int : 抽出したいindexの数
	 * @param rnd 乱数生成器
	 * @return Integer[] : 非復元抽出したwant個のindex
	 */
	public static Integer[] samplingWithout(int box, int want, MersenneTwisterFast rnd) {
		MersenneTwisterFast uniqueRnd = new MersenneTwisterFast(rnd.nextInt());
		Integer[] answer = new Integer[want];
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < box; i++) {
			list.add(i);
		}
		for(int i = 0; i < want; i++) {
			if(list.size() == 0) {
				break;
			}
			int index = uniqueRnd.nextInt(list.size());
			answer[i] = list.get(index);
			list.remove(index);
		}
		return answer;
	}

	/**
	 * 非復元抽出<br>
	 * Sampling without replacement<br>
	 * @param box : int : 元のデータサイズ
	 * @param want : int : 抽出したいindexの数
	 * @param rnd 乱数生成器
	 * @return Integer[] : 非復元抽出したwant個のindex
	 */
	public static Integer[][] samplingWithoutForOption2(int box, int want, MersenneTwisterFast rnd) {
		MersenneTwisterFast uniqueRnd = new MersenneTwisterFast(rnd.nextInt());
		Integer[][] answer = new Integer[2][want];
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < box; i++) {
			list.add(i);
		}
		for(int i = 0; i < want; i++) {
			if(list.size() == 0) {
				break;
			}
			int index = uniqueRnd.nextInt(list.size());
			answer[0][i] = list.get(index);
			list.remove(index);
		}
		answer[1] = list.toArray(new Integer[list.size()]);
		return answer;
	}

	/**
	 * log関数 底の変換公式
	 * @param a : double : 引数
	 * @param b : double : 底
	 * @return double : log_b (a)
	 */
	public static double log(double a, double b) {
		return (Math.log(a) / Math.log(b));
	}

	/**
	 * <h1>組合せの総数, nCr</h1>
	 * @param n n
	 * @param r r
	 * @return int : nCr
	 */
	public static int combination(int n, int r) {
		int ans = 1;
		for(int i = 1; i <= r; i++) {
			ans = ans * (n-i + 1) / i;
		}
		return ans;
	}


	public static String toString(int[] tmp) {
		String str = "[" + String.valueOf(tmp[0]);
		for(int i=1; i<tmp.length; i++) {str += ", " + String.valueOf(tmp[i]);}
		str += "]";
		return str;
	}

/** 挙動テスト用関数*/
//	public static <S> void test(List<S> tmp, String str) {
//		for(S gg: tmp) {
//			List<IntegerSolution> buf = ((PittsburghSolution) gg).getMichiganPopulation();
//			for(IntegerSolution buf_i_: buf) {
//				MichiganSolution buf_i = (MichiganSolution) buf_i_;
//
//				if(!buf_i.getVariables().toString().equals(GeneralFunctions.toString(buf_i.getRule().getAntecedent()))) {
//					System.out.print(str + " ");
//					System.out.print(GeneralFunctions.toString(buf_i.getRule().getAntecedent()) + " ");
//					System.out.print(buf_i.getVariables().toString() + " ");
//				}
//			}
//		}
//		return;
//	}
//
//	public static <S> void test2(PittsburghSolution solution, String str) {
//		List<IntegerSolution> buf = solution.getMichiganPopulation();
//		for(IntegerSolution buf_i_: buf) {
//			MichiganSolution buf_i = (MichiganSolution) buf_i_;
//
//			if(!buf_i.getVariables().toString().equals(GeneralFunctions.toString(buf_i.getRule().getAntecedent()))) {
//				System.out.print(str + " ");
//				System.out.print(GeneralFunctions.toString(buf_i.getRule().getAntecedent()) + " ");
//				System.out.print(buf_i.getVariables().toString() + " ");
//				System.out.println();
//			}
//		}
//		return;
//	}
//
//	private void test3(MichiganSolution solution, String str) {
//		if(!solution.getVariables().toString().equals(GeneralFunctions.toString(solution.getRule().getAntecedent()))) {
//			System.out.print(str + " ");
//			System.out.print(GeneralFunctions.toString(solution.getRule().getAntecedent()) + " ");
//			System.out.print(solution.getVariables().toString() + " ");
//			System.out.println();
//		}
//	}

}
