package xml;

//必須
public enum XML_TagName {
	id,//汎用
	dimension,//汎用

	generations, evaluation,
		population,
			pittsburghSolution,
				michiganSolution,
					fuzzySetList,
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

				attributes,
					attribute, attributeID,

		knowledgeBase,
			fuzzySets,
				fuzzyTerm,
					fuzzyTermID,
					fuzzyTermName,
					ShapeTypeID,
					ShapeTypeName,
					parameterSet,
						parameter,

					divisionType,
					partitionNum,
					partition_i,

	dataSet,
		pattern,
			attributeVector,

	consts,
}
