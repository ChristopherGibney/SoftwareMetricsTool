package softwaremetrics;

import java.util.ArrayList;

import softwaremetricshelperclasses.InnerClassOfFile;

public class PackageCohesion {
	public static double run(ArrayList<InnerClassOfFile> packageClasses) {
		double result = 0.0;
		if (packageClasses.size() == 1) {
			return 1.0;
		}
		else if (packageClasses.isEmpty()) {
			return 0.0;
		}
		for (InnerClassOfFile currentClass : packageClasses) {
			ArrayList<InnerClassOfFile> currClassCohesiveClasses = new ArrayList<>();
			for (InnerClassOfFile classUsedByCurrentClasses : currentClass.getCoupledObjectClasses()) {
				if (packageClasses.contains(classUsedByCurrentClasses)) {
					result++;
					currClassCohesiveClasses.add(classUsedByCurrentClasses);
				}
			}
			for (InnerClassOfFile otherPackageClass : packageClasses) {
				if (!otherPackageClass.equals(currentClass)) {
					if (otherPackageClass.getClassOrInterfaceDeclaration().isAncestorOf(currentClass.getClassOrInterfaceDeclaration())) {
						if (!currClassCohesiveClasses.contains(otherPackageClass)) {
							result++;
							currClassCohesiveClasses.add(otherPackageClass);
						}
					}
				}
			}
		}
		result = result/(packageClasses.size()*(packageClasses.size()-1));
		return result;
	}
}
