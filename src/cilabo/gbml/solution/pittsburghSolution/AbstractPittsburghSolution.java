package cilabo.gbml.solution.pittsburghSolution;

import org.uma.jmetal.solution.AbstractSolution;
import org.w3c.dom.Element;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.michiganSolution.builder.MichiganSolutionBuilder;
import xml.XML_TagName;

/**
 * Pittsburgh型識別器抽象クラス abstract pittsburgh solution class
 * @author Takigawa Hiroki
 *
 * @param <MichiganSolutionClass> このPittsburgh型識別器が持つMichigan型識別器クラス michigan solution class that this class has
 */
public abstract class AbstractPittsburghSolution <MichiganSolutionClass extends MichiganSolution<?>>
		extends AbstractSolution<MichiganSolutionClass>
		implements PittsburghSolution<MichiganSolutionClass>{

	/** ミシガン型識別器生成器 michigan solution builder */
	public MichiganSolutionBuilder<MichiganSolutionClass> michiganSolutionBuilder;

	/** コンストラクタ constructor
	 * @param numberOfVariables Michigan型識別器の個数 number of variables
	 * @param numberOfObjectives 目的関数の個数 number of objectives
	 * @param numberOfConstraints 制約の個数 number of constraints
	 * @param michiganSolutionBuilder ミシガン型識別器生成器 michigan solution builder*/
	protected AbstractPittsburghSolution(int numberOfVariables,
			int numberOfObjectives,
			int numberOfConstraints,
			MichiganSolutionBuilder<MichiganSolutionClass> michiganSolutionBuilder) {
		super(numberOfVariables, numberOfObjectives, numberOfConstraints);
		this.michiganSolutionBuilder = michiganSolutionBuilder;
	}

	public static int getNumberOfVariables(Element pittsburghSolutionEL) {
		return pittsburghSolutionEL.getElementsByTagName(XML_TagName.michiganSolution.toString()).getLength();
	}

	@Override
	public MichiganSolutionBuilder<MichiganSolutionClass> getMichiganSolutionBuilder() {
		return this.michiganSolutionBuilder;
	}

	@Override
	public void removeVariable(int index) {
		this.variables.remove(index);
	}

	@Override
	public void addVariable(MichiganSolutionClass value) {
		this.variables.add(value);
	}

	@Override
	public void clearVariables() {
		this.variables.clear();
	}

	@Override
	public void clearAttributes() {
		this.attributes.clear();
	}

	@Override
	public void learning() {
		for(int i=0; i<this.getNumberOfVariables(); i++) {
			this.variables.get(i).learning();
		}
	}
}
