package cilabo.util.fileoutput;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileoutput.FileOutputContext;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;

import cilabo.gbml.solution.michiganSolution.MichiganSolution;
import cilabo.gbml.solution.pittsburghSolution.PittsburghSolution;

public class PittsburghSolutionListOutput extends SolutionListOutput {

	private FileOutputContext pittsburghFileContext;
	private FileOutputContext solutionFileContext;
	private List<PittsburghSolution<MichiganSolution<?>>> solutionList;

	public PittsburghSolutionListOutput(List<PittsburghSolution<MichiganSolution<?>>> solutionList) {
		super(solutionList);
		this.solutionList = solutionList;
	}

	public PittsburghSolutionListOutput setPittsburghFileOutputContext(FileOutputContext fileContext) {
		this.pittsburghFileContext = fileContext;
		return this;
	}

	public PittsburghSolutionListOutput setSolutionFileOutputContext(FileOutputContext fileContext) {
		this.solutionFileContext = fileContext;
		return this;
	}

	@Override
	public void print() {
		this.printPittsburghSolutionFormatsToFile(pittsburghFileContext, solutionList);
		this.printSolutionsToFile(solutionFileContext, solutionList);
	}

	public void printSolutionsToFile(FileOutputContext context, List<PittsburghSolution<MichiganSolution<?>>> solutionList) {
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

	public void printPittsburghSolutionFormatsToFile(FileOutputContext context, List<PittsburghSolution<MichiganSolution<?>>> solutionList) {
		BufferedWriter bufferedWriter = context.getFileWriter();
		try {
			if(solutionList.size() > 0) {
				for(int i = 0; i < solutionList.size(); i++) {
					bufferedWriter.write("----");
					PittsburghSolution solution = solutionList.get(i);
					bufferedWriter.write(solution.toString());
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
