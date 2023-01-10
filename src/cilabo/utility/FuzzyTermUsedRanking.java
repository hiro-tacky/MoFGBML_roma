package cilabo.utility;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import cilabo.fuzzy.knowledge.FuzzyTermTypeForMixed;
import cilabo.fuzzy.knowledge.Knowledge;
import cilabo.main.ExperienceParameter.DIVISION_TYPE;
import jfml.term.FuzzyTermType;
import xml.XML_TagName;

public class FuzzyTermUsedRanking {

	public static int[][] getUsedFuzzySetNum(Element population, int dimension){
		NodeList pittsburghSolutionList = population.getElementsByTagName(XML_TagName.pittsburghSolution.toString());
		int[][] UsedFuzzySetNum = new int[dimension][];

		for(int dim_i=0; dim_i<dimension; dim_i++) {
			int tmp = Knowledge.getInstance().getFuzzySetNum(dim_i);
			UsedFuzzySetNum[dim_i] = new int[tmp];
		}

		for(int i = 0; i < pittsburghSolutionList.getLength(); i++) {

			Element pittsburghSolution = (Element) pittsburghSolutionList.item(i);
			NodeList michiganSolutionList = pittsburghSolution.getElementsByTagName(XML_TagName.michiganSolution.toString());
			for(int j = 0; j < michiganSolutionList.getLength(); j++) {
				Element michiganSolution = (Element) michiganSolutionList.item(j);

				NodeList fuzzySetList = michiganSolution.getElementsByTagName(XML_TagName.fuzzySetID.toString());
				for (int k = 0; k < fuzzySetList.getLength(); k++) {
					Element fuzzySetID = (Element) fuzzySetList.item(k);

					int dim = Integer.valueOf(fuzzySetID.getAttribute(XML_TagName.dimension.toString()));
					int ID = Integer.valueOf(fuzzySetID.getTextContent());

					UsedFuzzySetNum[dim][ID]++;
				}
			}
		}

		return UsedFuzzySetNum;
	}

	public static FuzzyTermTypeForMixed[][] getUsedFuzzySetID(int[][] fuzzySetUsedRank, int[] fuzzyTermNum){

		FuzzyTermTypeForMixed[][] fuzzyTerms = new FuzzyTermTypeForMixed[fuzzySetUsedRank.length][];

		for(int dim_i=0; dim_i<fuzzySetUsedRank.length; dim_i++) {
			fuzzyTerms[dim_i] = new FuzzyTermTypeForMixed[fuzzyTermNum[dim_i]+1];
			fuzzyTerms[dim_i][0] = new FuzzyTermTypeForMixed(
					Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, Knowledge.DnotCare_FuzzyTermID),
					FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f}, DIVISION_TYPE.equalDivision, 0, 0);

			for(int j=1; j<fuzzyTermNum[dim_i]+1; j++) {
				int tmp = -1, id = -1;
				for(int k=0; k<fuzzySetUsedRank[dim_i].length; k++) {
					if(tmp<fuzzySetUsedRank[dim_i][k]) {tmp=fuzzySetUsedRank[dim_i][k]; id=k;}
				}

				if(id>-1) {
					fuzzyTerms[dim_i][j] = Knowledge.getInstance().getFuzzySet(dim_i, id);
					fuzzySetUsedRank[dim_i][id] = 0;
				}else {
					fuzzyTerms[dim_i][j] = new FuzzyTermTypeForMixed(
							Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, Knowledge.DnotCare_FuzzyTermID),
							FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f}, DIVISION_TYPE.equalDivision, 0, 0);
				}
			}
		}
		return fuzzyTerms;
	}

	public static FuzzyTermTypeForMixed[][] getUsedFuzzySetID(Element population, int dimension, int[] fuzzyTermNum){
		int[][] tmp = FuzzyTermUsedRanking.getUsedFuzzySetNum(population, dimension);
		return FuzzyTermUsedRanking.getUsedFuzzySetID(tmp, fuzzyTermNum);
	}
}
