
//A uses attributes from B and C, and is used in B, therefore cbo should be 2
//as A is coupled to two other classes
public class CouplingBetweenObjectClasses_TestResourceA {
	CouplingBetweenObjectClasses_TestResourceB bObject;
	CouplingBetweenObjectClasses_TestResourceC cObject;
	int att = bObject.attributeUsedInA;
	int attFromC = cObject.attributeUsedInA;
	int i = 0;
	
	public void UseMethodInB() {
		bObject.methodUsedInA();
	}
}
