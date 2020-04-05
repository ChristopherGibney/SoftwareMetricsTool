
public class CouplingBetweenObjectClassesB {
	int attributeUsedInA;
	CouplingBetweenObjectClassesA objectA = new CouplingBetweenObjectClassesA();
	
	public void methodUsedInA() {
		attributeUsedInA = objectA.att;
	}
}
