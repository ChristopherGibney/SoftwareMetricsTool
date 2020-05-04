package softwaremetrics;

import java.util.ArrayList;

import softwaremetricshelperclasses.InnerClassOfFile;

public class EfferentCoupling {
	public static double run(ArrayList<InnerClassOfFile> packageClasses) {
		ArrayList<InnerClassOfFile> efferentClasses = new ArrayList<>();
		
		for (InnerClassOfFile currentClass : packageClasses) {
			for (InnerClassOfFile classUsedByCurrentClasses : currentClass.getCoupledObjectClasses()) {
				if (!classUsedByCurrentClasses.getPackageName().equals(currentClass.getPackageName())) {
					if (!efferentClasses.contains(classUsedByCurrentClasses)) {
						efferentClasses.add(classUsedByCurrentClasses);
					}
				}
			}
		}
		
		double efferentClassCount = efferentClasses.size();
		return efferentClassCount;
	}
}
