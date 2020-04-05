package softwaremetrics;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import softwaremetricshelperclasses.InnerClassOfFile;

public class CouplingBetweenObjectClasses {
	
	public static ArrayList<Entry<InnerClassOfFile, Double>> run(ArrayList<InnerClassOfFile> allClasses) throws FileNotFoundException {
		ArrayList<Entry<InnerClassOfFile, Double>> cboOfAllInnerClasses = new ArrayList<>();
		
		for (InnerClassOfFile currentClass : allClasses) {
			double cboOfClass = currentClass.getCoupledObjectClasses().size();
			for (InnerClassOfFile dependantClass : currentClass.getDependantClasses()) {
				if (!currentClass.getCoupledObjectClasses().contains(dependantClass)) {
					cboOfClass += 1;
				}
			}
			Entry<InnerClassOfFile, Double> currentClassResult = new SimpleEntry<>(currentClass, cboOfClass);
			cboOfAllInnerClasses.add(currentClassResult);
		}
		return cboOfAllInnerClasses;
	}
}
