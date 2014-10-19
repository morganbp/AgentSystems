package no.agentsystems_dhom.supplier;

import javax.swing.text.html.MinimalHTMLWriter;

import no.agentsystems_dhom.server.Offer;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.TAC_Ontology;

public class Component {
	private int id; // 100, 101, 110, 111, 200, 210, 300, 301, 400, 401
	private int basePrice;
	private String componentName;
	private int capacity;
	private int inventory;

	// constructor
	public Component(int id) {
		this.id = id;
		capacity = (int) (TAC_Ontology.cNominal + ((Math.random() * 70) - 35));
		inventory = 0;
		initiate();
	}

	public int getId() {
		return id;
	}

	public int getBasePrice() {
		return basePrice;
	}
	
	public String getComponentName(){
		return componentName;
	}

	// set up price and component name, see table 6: Component Catalog
	private void initiate() {
		switch (id) {
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
	
	public void addComponents(){
		capacity = capacityRandomWalk();
	}
	
	public int getInventory(){
		return inventory;
	}
	public int capacityRandomWalk() {
		int newComponents = (int) Math.max(1, capacity + ((((Math.random()*10)-5)/100)
				* TAC_Ontology.cNominal) + (0.01
				* (TAC_Ontology.cNominal - capacity)));
		inventory += newComponents;
		return newComponents;
	}

	public int getCapacity() {
		return capacity;
	}

	// Get the expected capacity after n days
	public int getExpectedCapacity(int n) {
		return getExpectedCapacity(n, capacity);
	}

	private int getExpectedCapacity(int days, int currentCapacity) {
		if (days == 0) {
			return currentCapacity;
		}
		int newCapacity = (int) (0.99 * currentCapacity + 0.01 * TAC_Ontology.cNominal);
		return getExpectedCapacity(days - 1, newCapacity);
	}
	
	private int availabilityPriorToDayI(int d){
		int suppliersActualCapacity = getCapacity();
		int i;
		for(int j = d +1;j <= i; j++){
			i = j;
		}
		int totalOfferQuantityToBeShipped = whatafuck;
		
		int I = getInventory();
		
		
		return suppliersActualCapacity - i *? totalOfferQuantityToBeShipped + Math.min(0, I - i *? ettellerannet)
	}
	private int negativeOfCapacityRequiredOnOrBeforeDayI(){
		int suppliersActualCapacity = getCapacity();
		int totalOfferQuantityToBeShipped = whatafuck;
		int I = getInventory();
		int inventoryRemainingAfterComputingAvailabilityPriorToDayI = Math.max(0, I - enSUM *? etterllerannet);
		return Math.min(0,(k - d - i) suppliersActualCapacity - enSUM *? totalOfferQuantityToBeShipped + Math.min(0, 
				inventoryRemainingAfterComputingAvailabilityPriorToDayI - enSUM *? ettellerannet))
	}
	public void decidePriceOfComponent(Offer offer, Component c, Supplier sup){
		double offerPrice = offer.getOfferPrice();
		double priceDiscountFactor = 0.5;
		double baselinePrice = c.getBasePrice();
		int suppliersActualCapacity = getCapacity();
		int availability = availabilityPriorToDayI() + negativeOfCapacityRequiredOnOrBeforeDayI();
	}

}