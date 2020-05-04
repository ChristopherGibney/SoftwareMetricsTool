package softwaremetrics;

import java.util.ArrayList;

import softwaremetricshelperclasses.InnerClassOfFile;

public class AfferentCoupling {
	public static double run(ArrayList<InnerClassOfFile> packageClasses) {
		ArrayList<InnerClassOfFile> afferentClasses = new ArrayList<>();
		
		for (InnerClassOfFile currentClass : packageClasses) {
			for (InnerClassOfFile dependantClass : currentClass.getDependantClasses()) {
				if (!dependantClass.getPackageName().equals(currentClass.getPackageName())) {
					if (!afferentClasses.contains(dependantClass)) {
						afferentClasses.add(dependantClass);
					}
				}
			}
		}
		
		double afferentClassCount = afferentClasses.size();
		return afferentClassCount;
	}
}
