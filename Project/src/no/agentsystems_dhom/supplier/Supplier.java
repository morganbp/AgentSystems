package no.agentsystems_dhom.supplier;

import no.agentsystems_dhom.server.Inventory;

public class Supplier {
	private Component[] products = new Component[2];
	private int supplierID;
	
	public Supplier(int id){
		
		supplierID = id;
		switch(id){
		case 0:
			setProducts(100,101);
			break;
		case 1: 
			setProducts(110,111);
			break;
		case 2: 
			setProducts(200,210);
			break;
		case 3:
			setProducts(300,301);
			break;
		case 4:
			setProducts(400,401);
			break;
		case 5:
			setProducts(200,101);
			break;
		case 6:
			setProducts(300,210);
			break;
		case 7:
			setProducts(401,111);
			break;
		}
	}
	
	
	public Supplier(int component1_ID, int component2_ID, int id)
	{
		setProducts(component1_ID, component2_ID);
		supplierID = id;
		
	}
	
	public void setProducts(int component1_ID, int component2_ID){
		products[0] = new Component(component1_ID);
		products[1] = new Component(component2_ID);
	}

	public int getSupplierID(){
		return supplierID;
	}
	
	public Component getProduct(int componentId)
	{
		for(Component component : products)
		{
			if(componentId == component.getId())
			{
				return component;
			}
		}
		return null;
	}
	
	public Component[] getComponents(){
		return products;
	}
}