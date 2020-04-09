package softwaremetrics;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

import softwaremetricshelperclasses.InnerClassOfFile;

public class CouplingBetweenObjectClasses {
	
	public static double run(InnerClassOfFile currentClass) throws FileNotFoundException {
		double cboOfClass = currentClass.getCoupledObjectClasses().size();
		for (InnerClassOfFile dependantClass : currentClass.getDependantClasses()) {
			if (!currentClass.getCoupledObjectClasses().contains(dependantClass)) {
				cboOfClass += 1;
			}
		}
		return cboOfClass;
	}
}
