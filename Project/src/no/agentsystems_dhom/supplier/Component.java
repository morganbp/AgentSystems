package no.agentsystems_dhom.supplier;

import java.util.Random;

import no.agentsystems_dhom.server.TAC_Ontology;

public class Component {
	private int id; // 100, 101, 110, 111, 200, 210, 300, 301, 400, 401
	private int basePrice;
	private Random rand = new Random();
	private String componentName;
	private int Cyesterday = TAC_Ontology.cNominal*(((rand.nextInt(70) - 35)/100) + 1);
	
	// constructor
	public Component(int id) {
		this.id = id;
		initiate();
	}
 
	// get id
	public int getId() {
		return id;
	}
	// get base price
	public int getBasePrice() {
		return basePrice;
	}
	// set up price and component name, see table 6: Component Catalog 
	private void initiate() {
		switch(id) {
		case 100: 
			basePrice = 1000; 
			componentName = "Pintel CPU 2.0 GHz";
			break;
		case 101: 
			basePrice = 1500; 
			componentName = "Pintel CPU 5.0 GHz";
			break;
		case 110: 
			basePrice = 1000; 
			componentName = "IMD CPU 2.0 GHz";
			break;
		case 111: 
			basePrice = 1500; 
			componentName = "IMD CPU 5.0 GHz";
			break;
		case 200: 
			basePrice = 250; 
			componentName = "Pintel Motherboard";
			break;
		case 210: 
			basePrice = 250; 
			componentName = "IMD Motherboard";
			break;
		case 300: 
			basePrice = 100; 
			componentName = "Memory 1 GB";
			break;
		case 301: 
			basePrice = 200; 
			componentName = "Memory 2 GB";
			break;
		case 400: 
			basePrice = 300; 
			componentName = "Hard disk 300 GB";
			break;
		case 401: 
			basePrice = 400; 
			componentName = "Hard disk 500 GB";
			break;
		}
	}
	public String toString() {
		return id + " " + basePrice + " " + componentName;
	}
	
	public int GetCapacity()
	{
		Double doubleCapacity =  Math.max(1, Cyesterday + ((rand.nextInt(10) - 5)/100)*TAC_Ontology.cNominal + 0.01*(TAC_Ontology.cNominal - Cyesterday));
		Cyesterday = doubleCapacity.intValue();
		return doubleCapacity.intValue();
	}
	
	public int GetCapacity(int days)
	{
		int capacity = GetCapacity();
		for(int i = 0; i <= days; i++)
		{
			Double tempCapacity = 0.99*capacity + 0.01*TAC_Ontology.cNominal;
			capacity = tempCapacity.intValue();
		}
		return capacity;
	}
	
}