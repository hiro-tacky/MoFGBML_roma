package cilabo.fuzzy.knowledge;

import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import cilabo.main.impl.mixedKnowledge.MixedKnowledge;
import jfml.term.FuzzyTermType;

/**
 * 多種混合分割条件部集合用FuzzyTermTypeのラッパークラス．
 * @author Takigawa Hiroki
 */
@MixedKnowledge
public class FuzzyTermTypeForMixed extends FuzzyTermType {
	/** 分割方式 */
	public DIVISION_TYPE divisionType;
	/** 分割区間の母数 */
	public int partitionNum;
	/** 分割区間内でのID */
	public int partition_i;

	/**
	 * 入力されたデータ持つインスタンスを生成する
	 * @param name ファジィ集合の名前
	 * @param type FuzzyTermTypeの形状ID
	 * @param param FuzzyTermTypeの形状の定義に用いられるパラメータ
	 * @param divisionType 分割方式
	 * @param partitionNum 分割区間の母数
	 * @param partition_i 分割区間内でのID
	 */
	public FuzzyTermTypeForMixed(String name, int type, float[] param, DIVISION_TYPE divisionType, int partitionNum, int partition_i){
		super(name, type, param);
		this.divisionType = divisionType;
		this.partitionNum = partitionNum;
		this.partition_i = partition_i;
	}

	/**
	 * このインスタンスが持つ分割方式を返します。<br>
	 * @return 返される分割方式
	 */
	public DIVISION_TYPE getDivisionType() {
		return divisionType;
	}

	/**
	 * このインスタンスが持つ分割区間の母数式を返します。<br>
	 * @return 返される分割区間の母数
	 */
	public int getPartitionNum() {
		return partitionNum;
	}

	/**
	 * このインスタンスが持つ分割区間内でのIDを返します。<br>
	 * @return 返される分割区間内でのID
	 */
	public int getPartition_i() {
		return partition_i;
	}
}
