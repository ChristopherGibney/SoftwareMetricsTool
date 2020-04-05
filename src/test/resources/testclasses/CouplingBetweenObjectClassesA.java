
public class CouplingBetweenObjectClassesA {
	CouplingBetweenObjectClassesB bObject;
	int att = bObject.attributeUsedInA;
	int i = 0;
	
	public void UseMethodInB() {
		bObject.methodUsedInA();
	}
}
