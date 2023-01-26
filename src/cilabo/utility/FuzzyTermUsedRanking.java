package cilabo.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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

					if(ID != 0) UsedFuzzySetNum[dim][ID]++;
				}
			}
		}

		return UsedFuzzySetNum;
	}

	public static ArrayList<HashMap<FuzzyStyle, Integer>> getUsedFuzzyStyleNum(Element population, int dimension){
		NodeList pittsburghSolutionList = population.getElementsByTagName(XML_TagName.pittsburghSolution.toString());
		ArrayList<HashMap<FuzzyStyle, Integer>> UsedFuzzySetNum = new ArrayList<HashMap<FuzzyStyle, Integer>>();

		for(int dim_i=0; dim_i<dimension; dim_i++) {
			UsedFuzzySetNum.add(new HashMap<FuzzyStyle, Integer>());
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

					if(ID != 0) {
						FuzzyTermTypeForMixed fuzzyTremBuf = Knowledge.getInstance().getFuzzySet(dim, ID);
						if(UsedFuzzySetNum.get(dim).containsKey( new FuzzyStyle(fuzzyTremBuf.getDivisionType(), fuzzyTremBuf.getType()) )) {
							int tmp = UsedFuzzySetNum.get(dim).get( new FuzzyStyle(fuzzyTremBuf.getDivisionType(), fuzzyTremBuf.getType()) );
							UsedFuzzySetNum.get(dim).put(new FuzzyStyle(fuzzyTremBuf.getDivisionType(), fuzzyTremBuf.getType()), tmp+1);
						}else {
							UsedFuzzySetNum.get(dim).put(new FuzzyStyle(fuzzyTremBuf.getDivisionType(), fuzzyTremBuf.getType()), 1);
						}
					}
				}
			}
		}

		return UsedFuzzySetNum;
	}

	public static class FuzzyStyle{
		private DIVISION_TYPE division_type;
		private int shapeTypeID;

		public FuzzyStyle(DIVISION_TYPE division_type, int shapeTypeID) {
			this.division_type = division_type;
			this.shapeTypeID = shapeTypeID;
		}

		@Override
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null) return false;
	        if (getClass() != obj.getClass()) return false;

	        FuzzyStyle other = (FuzzyStyle) obj;
	        if (division_type == null || other.division_type == null) {
	        	return false;
	        }else if(!division_type.equals(other.division_type)) {
	        	return false;
	        }

	        if (shapeTypeID != other.shapeTypeID) return false;
	        return true;
	    }

	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((division_type == null) ? 0 : division_type.hashCode());
	        result = prime * result + Integer.valueOf(shapeTypeID).hashCode();
	        return result;
	    }
	}


	public static FuzzyTermTypeForMixed[][] getUsedFuzzySetID(int[][] fuzzySetUsedRank, int[] fuzzyTermNum){

		FuzzyTermTypeForMixed[][] fuzzyTerms = new FuzzyTermTypeForMixed[fuzzySetUsedRank.length][];

		for(int dim_i=0; dim_i<fuzzySetUsedRank.length; dim_i++) {
			List<FuzzyTermTypeForMixed> buf = new ArrayList<FuzzyTermTypeForMixed>();
			buf.add( new FuzzyTermTypeForMixed(
					Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, Knowledge.DnotCare_FuzzyTermID),
					FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f}, DIVISION_TYPE.equalDivision, 0, 0) );

			for(int j=1; j<fuzzyTermNum[dim_i]+1; j++) {
				int tmp = 0;
				List<Integer> pool = new ArrayList<Integer>();
				for(int k=0; k<fuzzySetUsedRank[dim_i].length; k++) {
					if(tmp<fuzzySetUsedRank[dim_i][k] && 0<fuzzySetUsedRank[dim_i][k]) {tmp=fuzzySetUsedRank[dim_i][k]; pool.clear(); pool.add(k);}
					else if(tmp==fuzzySetUsedRank[dim_i][k] && 0<fuzzySetUsedRank[dim_i][k]) {pool.add(k);}
				}

				if(pool.size() == 1) {
					buf.add(Knowledge.getInstance().getFuzzySet(dim_i, pool.get(0)));
					fuzzySetUsedRank[dim_i][pool.get(0)] = 0;
				}else if(pool.size() > 1){
					int index = Random.getInstance().getGEN().nextInt(pool.size());
					int id = pool.get(index);
					buf.add(Knowledge.getInstance().getFuzzySet(dim_i, id));
					fuzzySetUsedRank[dim_i][id] = 0;
				}
			}
			fuzzyTerms[dim_i] = new FuzzyTermTypeForMixed[buf.size()];
			for(int x=0; x<buf.size(); x++) {
				fuzzyTerms[dim_i][x] = buf.get(x);
			}
		}
		return fuzzyTerms;
	}

	public static FuzzyTermTypeForMixed[][] getUsedFuzzySetID(Element population, int dimension, int[] fuzzyTermNum){
		int[][] tmp = FuzzyTermUsedRanking.getUsedFuzzySetNum(population, dimension);
		return FuzzyTermUsedRanking.getUsedFuzzySetID(tmp, fuzzyTermNum);
	}

	public static FuzzyTermTypeForMixed[][] getUsedFuzzyStyle(ArrayList<HashMap<FuzzyStyle, Integer>> fuzzySetUsedRank){

		FuzzyTermTypeForMixed[][] fuzzyTerms = new FuzzyTermTypeForMixed[fuzzySetUsedRank.size()][];

		for(int dim_i=0; dim_i<fuzzySetUsedRank.size(); dim_i++) {
			List<FuzzyTermTypeForMixed> buf = new ArrayList<FuzzyTermTypeForMixed>();
			buf.add( new FuzzyTermTypeForMixed(
					Knowledge.makeFuzzyTermName(DIVISION_TYPE.equalDivision, FuzzyTermType.TYPE_rectangularShape, Knowledge.DnotCare_FuzzyTermID),
					FuzzyTermType.TYPE_rectangularShape, new float[] {0f, 1f}, DIVISION_TYPE.equalDivision, 0, 0) );

			int tmp = 0;
			List<FuzzyStyle> pool = new ArrayList<FuzzyStyle>();
			for(Entry<FuzzyStyle, Integer> fuzzyStyleEntry : fuzzySetUsedRank.get(dim_i).entrySet()) {
				if(tmp<fuzzyStyleEntry.getValue() && 0<fuzzyStyleEntry.getValue()) {
					tmp=fuzzyStyleEntry.getValue(); pool.clear(); pool.add(fuzzyStyleEntry.getKey());
				}
				else if(tmp==fuzzyStyleEntry.getValue() && 0<fuzzyStyleEntry.getValue()) {pool.add(fuzzyStyleEntry.getKey());}
			}

			FuzzyStyle fuzzyStyle = null;
			if(pool.size() == 1) {
				fuzzyStyle = pool.get(0);
			}else if(pool.size() > 1){
				int index = Random.getInstance().getGEN().nextInt(pool.size());
				fuzzyStyle = pool.get(index);
			}

			for(FuzzyTermTypeForMixed fuzzyTerm: Knowledge.getInstance().getFuzzySet(dim_i)) {
				if(fuzzyTerm.divisionType == fuzzyStyle.division_type && fuzzyTerm.getType() == fuzzyStyle.shapeTypeID) {
					buf.add(fuzzyTerm);
				}
			}

			fuzzyTerms[dim_i] = new FuzzyTermTypeForMixed[buf.size()];
			for(int x=0; x<buf.size(); x++) {
				fuzzyTerms[dim_i][x] = buf.get(x);
			}
		}
		return fuzzyTerms;
	}

	public static FuzzyTermTypeForMixed[][] getUsedFuzzyStyle(Element population, int dimension){
		ArrayList<HashMap<FuzzyStyle, Integer>> tmp = FuzzyTermUsedRanking.getUsedFuzzyStyleNum(population, dimension);
		return FuzzyTermUsedRanking.getUsedFuzzyStyle(tmp);
	}
}
