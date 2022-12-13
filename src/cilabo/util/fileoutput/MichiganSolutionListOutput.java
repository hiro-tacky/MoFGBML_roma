package cilabo.util.fileoutput;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileoutput.FileOutputContext;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;

public class MichiganSolutionListOutput extends SolutionListOutput {

	private FileOutputContext michiganFileContext;
	private FileOutputContext solutionFileContext;
	private List<MichiganSolution> solutionList;

	public MichiganSolutionListOutput(List<MichiganSolution> solutionList) {
		super(solutionList);
		this.solutionList = solutionList;
	}

	public MichiganSolutionListOutput setMichiganFileOutputContext(FileOutputContext fileContext) {
		michiganFileContext = fileContext;
		return this;
	}

	public MichiganSolutionListOutput setSolutionFileOutputContext(FileOutputContext fileContext) {
		solutionFileContext = fileContext;
		return this;
	}

	@Override
	public void print() {
//		super.print();
		this.printMichiganSolutionFormatsToFile(michiganFileContext, solutionList);
		this.printSolutionsToFile(solutionFileContext, solutionList);
	}

	public void printSolutionsToFile(FileOutputContext context, List<MichiganSolution> solutionList) {
		BufferedWriter bufferedWriter = context.getFileWriter();
		try {
			if(solutionList.size() > 0) {
				for(int i = 0; i < solutionList.size(); i++) {
					bufferedWriter.write(solutionList.get(i).toString());
				}
				bufferedWriter.close();
			}
		}
		catch (IOException e) {
			throw new JMetalException("Error writing data ", e);
		}
	}

	public void printMichiganSolutionFormatsToFile(FileOutputContext context, List<MichiganSolution> solutionList) {
		BufferedWriter bufferedWriter = context.getFileWriter();
		try {
			if(solutionList.size() > 0) {
				for(int i = 0; i < solutionList.size(); i++) {
					bufferedWriter.write(solutionList.get(i).toString());
					bufferedWriter.newLine();
				}
				bufferedWriter.close();
			}
		}
		catch (IOException e) {
			throw new JMetalException("Error writing data ", e);
		}
	}


}
