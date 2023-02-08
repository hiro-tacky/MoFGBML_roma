package cilabo.gbml.solution.pittsburghSolution;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.w3c.dom.Element;

import cilabo.data.AttributeVector;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.MichiganSolution.MichiganSolutionBuilder;

/**
 * @author Takigawa Hiroki
 *
 * @param <michiganSolution>
 */
public interface PittsburghSolution <michiganSolution extends MichiganSolution<?>>
	extends Solution<michiganSolution> {

	@Override
	public void setVariable(int index, michiganSolution value);

	@Override
	public michiganSolution getVariable(int index);

	@Override
	public List<michiganSolution> getVariables();

	/** 指定したインデックスのMichiganSolutionを削除する
	 * @param index 削除したいMichiganSolutionのインデックス
	 */
	public void removeVariable(int index);

	/** MichiganSolutionを追加する
	 * @param value 追加したいMichiganSolution
	 */
	public void addVariable(michiganSolution value);

	/** 指定したAttributesをフォーマットする
	 * @param attributeID Attributesのキー
	 */
	public void clearAttributes(String attributeID);

	/**
	 * 遺伝情報をフォーマットする
	 */
	public void clearVariable();

	/** 遺伝情報をフォーマットし，空の配列を生成する
	 * @param numberOfVariables 新たに生成する配列の配列長
	 */
	public void clearVariable(int numberOfVariables);

	/** 識別を行い勝者となったMichiganSolutionを返す
	 * @param x 識別対象となるパターンの属性値クラス
	 * @return 勝者となったMichiganSolution
	 */
	public MichiganSolution classify(AttributeVector x);

	public Element toElement();

	@Override
	public PittsburghSolution<michiganSolution> copy();

	/**
	 * このインスタンスが持つ全てのMichiganSolutionを対象に前件部を基に後件部の学習を行う
	 */
	public void learning();

	public MichiganSolutionBuilder<michiganSolution> getMichiganSolutionBuilder();
}
