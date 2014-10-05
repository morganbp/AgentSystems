package no.agentsystems_dhom.supplier;

public class Supplier {
	private Component product1;
	private Component product2;
	
	public Supplier(int component1_ID, int component2_ID)
	{
		product1 = new Component(component1_ID);
		product2 = new Component(component2_ID);
	}
	
	protected Component GetComponent1()
	{
		return product1;
	}
	
	protected Component GetComponent2()
	{
		return product2;
	}
}