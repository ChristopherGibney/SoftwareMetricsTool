package softwaremetricshelperclasses;

import java.util.ArrayList;

public class ExtractDependantClasses {
	//must be called after ExtractClassesCoupledFromCurrentClass so coupled object classes are not empty
	public static void extract(ArrayList<InnerClassOfFile> allClasses) {
		for (InnerClassOfFile currentClass : allClasses) {
			for (InnerClassOfFile objectClass : allClasses) {
				if (objectClass.getCoupledObjectClasses().contains(currentClass)) {
					if (!currentClass.getDependantClasses().contains(objectClass)) {
						currentClass.addDependantClass(objectClass);
					}
				}
			}
		}
	}
}
