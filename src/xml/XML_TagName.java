package xml;

public enum XML_TagName {
	id,//汎用
	dimension,//汎用

	evaluations, evaluation,
		population,
			pittsburghSolution,
				michiganSolution,
					fuzzySets,
						fuzzySetID,

					rule,
						antecedent,
						consequent,
							classLabel,
							ruleWeight,

							classLabelMulti,
							ruleWeightMulti,

				objectivesSet,
					objective, objectiveName,

	knowledgeBase,
		//fuzzySets,
			fuzzyTerm,
				fuzzyTermID,
				fuzzyTermName,
				ShapeTypeID,
				ShapeTypeName,
				parameterSet,
					parameter,
	consts,
}
