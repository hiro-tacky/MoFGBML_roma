package xml;

public enum XML_TagName {
	id,//汎用
	dimension,//汎用

	generations, evaluation,
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

				objectives,
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
