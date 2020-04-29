
public class DataAbstractionCoupling_TestResourceA {
	DataAbstractionCoupling_TestResourceB dacCount1 = new DataAbstractionCoupling_TestResourceB();
	DataAbstractionCoupling_TestResourceC dacCount2 = new DataAbstractionCoupling_TestResourceC();
	Map<DataAbstractionCoupling_TestResourceB, DataAbstractionCoupling_TestResourceC> dacCountThreeAndFour = new HashMap<>();
	
	public void method1() {
		DataAbstractionCoupling_TestResourceB dacNotCounted = new DataAbstractionCoupling_TestResourceB();
		DataAbstractionCoupling_TestResourceC dacNotCounted2 = new DataAbstractionCoupling_TestResourceC();
	}
}
