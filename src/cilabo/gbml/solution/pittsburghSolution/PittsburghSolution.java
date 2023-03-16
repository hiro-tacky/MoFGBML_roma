package cilabo.gbml.solution.pittsburghSolution;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.w3c.dom.Element;

import cilabo.data.pattern.Pattern;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.builder.MichiganSolutionBuilder;

/**
 * Pittsburgh型識別器クラス pittsburgh solution class
 * @author Takigawa Hiroki
 *
 * @param <MichiganSolutionClass> このPittsburgh型識別器が持つMichigan型識別器クラス michigan solution class that this class has
 */
public interface PittsburghSolution <MichiganSolutionClass extends MichiganSolution<?>>
	extends Solution<MichiganSolutionClass> {

	/**
	 * 指定された位置にあるMichigan型識別器を返します。<br>
	 * Returns index of michigan solution at the specified position
	 * @param index 返されるMichigan型識別器のインデックス．index of michigan solution to return
	 * @return 指定された位置にあるMichigan型識別器．michigan solution at the specified position in the list
	 */
	@Override
	public MichiganSolutionClass getVariable(int index);

	/**
	 * 指定された位置にあるMichigan型識別器を、入力されたMichigan型識別器で置き換えます。<br>
	 * Replaces michigan solution at the specified position in the list with the given michigan solution.
	 * @param index 置換されるMichigan型識別器．index of michigan solution to replace
	 * @param michiganSolution 指定された位置に格納されるMichigan型識別器．michigan solution to be stored at the specified position
	 */
	@Override
	public void setVariable(int index, MichiganSolutionClass michiganSolution);

	/**
	 * 指定された位置にあるMichigan型識別器リストを返します。<br>
	 * Returns list of michigan solution
	 * @return 返されるMichigan型識別器リスト．list of michigan solution to return
	 */
	@Override
	public List<MichiganSolutionClass> getVariables();

	/**
	 * 指定された位置にあるMichigan型識別器を削除します。<br>
	 * Remove michigan solution at the specified position in the list.
	 * @param index 削除されるMichigan型識別器のインデックス．index of michigan solution to remove
	 */
	public void removeVariable(int index);

	/**
	 * 入力されたMichigan型識別器を追加します。<br>
	 * Appends michigan solution to this instance
	 * @param michiganSolution 追リストに追加されるMichigan型識別器．michigan solution to be appended to the list
	 */
	public void addVariable(MichiganSolutionClass michiganSolution);

	/** このインスタンスが保持するMichigan型識別器リストを初期化 Clear list of michigan solution that this instance has*/
	public void clearVariables();

	/** 入力されたミシガン型識別器の属性情報を初期化する Initialize attribute of this instance*/
	public void clearAttributes();

	/**
	 * このインスタンスが持つミシガン型識別器ジェネレーターを返します。<br>
	 * Returns michigan solution builder that this instance has.
	 * @return 返されるミシガン型識別器ジェネレーター．michigan solution builder to return
	 */
	public MichiganSolutionBuilder<MichiganSolutionClass> getMichiganSolutionBuilder();

	/** このインスタンスが持つ全てのMichiganSolutionを対象に前件部を基に後件部の学習を行う<br>
	 * Learning consequent for each michigan solutions that this instance has*/
	public void learning();

	/**
	 * 与えられたパターンに対して識別を行い，勝者となったミシガン型識別器を返す<br>
	 * Classify to given pattern. and return the winner michigan solution. When winner rule doesn't exist, return null.
	 * @param pattern 識別対象となるパターンクラス pattern to be classified
	 * @return 勝者となったミシガン型識別器 識別不能時はnullを返す the winner michigan solution. When winner rule doesn't exist, return null.
	 */
	public MichiganSolutionClass classify(Pattern<?> pattern);

	/**
	 * pittsuburgh型識別器のルール長を返します。<br>
	 * Returns rule length of given pittsburgh solution.
	 * @return ルール長．rule length that given pittsuburgh classifier has
	 */
	public int getRuleLength();

	 /**
	 * pittsuburgh型識別器のルール数を返します。<br>
	 * Returns number of rule that given pittsburgh solution has.
	 * @return ルール数．number of rule that given pittsuburgh classifier has
	 */
	public int getNumberOfRule();

	@Override
	public PittsburghSolution<MichiganSolutionClass> copy();

	public Element toElement();

	@Override
	public String toString();
}
