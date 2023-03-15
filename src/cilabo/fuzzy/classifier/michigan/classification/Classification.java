package cilabo.fuzzy.classifier.michigan.classification;

import java.util.List;

import cilabo.data.pattern.Pattern;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/**
 * 勝者ルールを決定する手法を定義するClassificationクラス
 * this class define method to decide a single winner rule
 * @author Takigawa Hiroki
 *
 * @param <MichiganSolutionClass> このクラスが扱うpittsuburgh型識別器．PittsburghSolution class
 */
public interface Classification <MichiganSolutionClass extends MichiganSolution<?>>{

	/**
	 * pittsuburgh型識別器と識別対象パターンの属性値クラスを受け取り，識別を行った結果勝利となったMichiganSolutionを返す．識別不能の場合はnullを返す．
	 * decide a single winner rule with given pittsburgh solution and pattern. But when winner rule doesn't exist, return null.
	 * @param pittsburghSolution 識別を行うpittsuburgh型識別器 pittsburgh solution to classify
	 * @param pattern 識別対象となるパターンクラス pattern to be classified
	 * @return 勝者となったミシガン型識別器 識別不能時はnullを返す the winner michigan solution. When winner rule doesn't exist, return null.
	 */
	public MichiganSolutionClass classify(List<MichiganSolutionClass> michiganSolutionList, Pattern<?> pattern);

	public Classification<MichiganSolutionClass> copy();

	@Override
	public String toString();
}
