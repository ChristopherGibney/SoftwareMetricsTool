
//class B is used in A and also uses attribute from A, cbo result should be 1 
//as number of classes coupled with B is 1
public class CouplingBetweenObjectClasses_TestResourceB {
	int attributeUsedInA;
	CouplingBetweenObjectClasses_TestResourceA objectA = new CouplingBetweenObjectClasses_TestResourceA();
	
	public void methodUsedInA() {
		attributeUsedInA = objectA.att;
	}
}
