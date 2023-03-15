package cilabo.gbml.solution.michiganSolution.builder;

import java.util.List;

import org.w3c.dom.Element;

import cilabo.data.pattern.Pattern;
import cilabo.gbml.solution.michiganSolution.MichiganSolution;

/** ミシガン型識別器生成器 michigan solution builder
 * @author Takigawa Hiroki
 *
 * @param <MichiganSolutionClass> 生成するミシガン型識別器クラス michigan solution class to be generated
 */
public interface MichiganSolutionBuilder<MichiganSolutionClass extends MichiganSolution<?>>{

	/** 遺伝子情報を含むルールを自動生成し，生成されたルールからミシガン型識別器を生成する<br>
	 * Generate antecedent index and consequent. and create michigan solution
	 * @return 生成されたミシガン型識別器 generated michigan solution*/
	public MichiganSolutionClass createMichiganSolution();

	/** 入力されたElementから遺伝子情報を含むルールを読み込み，生成されたルールからミシガン型識別器を生成する．<br>
	 * Load rule that includes antecedent index from XML files
	 * @param michiganSolutionEL 読み込むXMLファイル XMLfile to load
	 * @return 読み込まれたミシガン型識別器 loaded michigan solution*/
	public MichiganSolutionClass createMichiganSolution(Element michiganSolution);

	/** 与えられたパターンから遺伝子情報を含むルールを生成し，生成されたルールからミシガン型識別器を生成する．<br>
	 * Generate antecedent index and consequent. and create michigan solution with given pattern
	 * @param pattern ミシガン型識別器の生成に使用するPatternクラス pattern class to be used to generate michigan solution
	 * @return 生成されたミシガン型識別器 generated michigan solution*/
	public MichiganSolutionClass createMichiganSolution(Pattern<?> pattern);

	/** 遺伝子情報を含むミシガン型識別器を複数生成する．<br>
	 * Generate specified number of michigan solution
	 * @param numberOfGenerateRule 生成するミシガン型識別器の数 number of michigan solution to generate
	 * @return 生成されたミシガン型識別器リスト generated list of michigan solution*/
	public List<MichiganSolutionClass> createMichiganSolutions(int numberOfGenerateRule);

	/**
	 * 入力されたミシガン型識別器の属性情報を初期化する<br>
	 * initialize attribute of given michigan solution
	 * @param solution 初期化を行うミシガン型識別器 michigan solution to be initialized
	 */
	public void initializeAttribute(MichiganSolution<?> solution);

	public MichiganSolutionBuilder<MichiganSolutionClass> copy();

	@Override
	public String toString();
}
