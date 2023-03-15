package cilabo.fuzzy.rule.antecedent.factory;

/** 前件部のファジィ集合のインデックス配列を生成用クラス
 * this class define methods to generate array of fuzzy set index
 * @author Takigawa Hiroki
 */
public interface AntecedentIndexFactory{

	/** 1個体分のファジィ集合インデックス配列を生成する<br>
	 * generate array of fuzzy set index
	 * @return 生成されたファジィ集合インデックス配列[次元数] generated fuzzy set index*/
	public int[] create();

	/** 指定された個体数分のファジィ集合インデックス配列を生成する
	 * generate specified number ofarraies of fuzzy set index
	 * @param numberOfGenerateRule 生成するルールの個体数 number of generate rule
	 * @return 生成されたファジィ集合インデックス配列[ルール数][次元数] generated fuzzy set index[number of rule][number of dimension]*/
	public int[][] create(int numberOfGenerateRule);

	public AntecedentIndexFactory copy();
}
