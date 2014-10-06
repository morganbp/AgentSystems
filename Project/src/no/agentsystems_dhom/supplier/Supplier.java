package no.agentsystems_dhom.supplier;

public class Supplier {
	private Component[] products = new Component[2];
	
	public Supplier(int component1_ID, int component2_ID)
	{
		products[0] = new Component(component1_ID);
		products[1] = new Component(component2_ID);
	}
	
	public Component[] getComponents(){
		return products;
	}
}