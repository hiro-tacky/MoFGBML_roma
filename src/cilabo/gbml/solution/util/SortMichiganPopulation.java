package cilabo.gbml.solution.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public class SortMichiganPopulation {
	public static void radixSort(List<MichiganSolution<?>> list) {
		Collections.sort(list, new Comparator<MichiganSolution<?>>() {
			@Override
			public int compare(MichiganSolution<?> a, MichiganSolution<?> b) {
				int dimension = a.getNumberOfVariables();
				for(int i = 0; i < dimension; i++) {
					int index_a = a.getVariable(i);
					int index_b = b.getVariable(i);

					if(index_a < index_b) {
						return -1;
					}
					else if(index_a > index_b) {
						return 1;
					}
					else {
						continue;
					}
				}
				return 0;
			}
		});
	}
}
