package no.agentsystems_dhom.supplier;

import java.util.ArrayList;
import java.util.List;

import no.agentsystems_dhom.server.AgentRequest;
import no.agentsystems_dhom.server.Offer;
import no.agentsystems_dhom.server.TAC_Ontology;

public class Component {
	
	public final static int[] idList = {100, 101, 110, 111, 200, 210, 300, 301, 400, 401};
	private int id; // 100, 101, 110, 111, 200, 210, 300, 301, 400, 401
	private int basePrice;
	private String componentName;
	private int capacity;
	private int inventory;
	private List<AgentRequest> considerations;
	private List<AgentRequest> commitedRequests;

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
		considerations = new ArrayList<AgentRequest>();
		commitedRequests = new ArrayList<AgentRequest>(); 
		
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
	
	/**
	 * Updates the amount of this specific component in the suppliers inventory
	 * Not sure if inventory is the right thing to use here.
	 * @param quantity
	 */
	public void updateInventory(int quantity)
	{
		this.inventory += quantity;
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
	public int getExpectedCapacity(int day,int dueDate) {
		return getExpectedCapacity(dueDate - day, inventory, true);
	}

	private int getExpectedCapacity(int days, int currentCapacity, boolean recursion) {
		if (days == 0) {
			return currentCapacity;
		}
		int newCapacity = (int) (0.99 * currentCapacity + 0.01 * TAC_Ontology.cNominal);
		return getExpectedCapacity(days - 1, newCapacity, true);
	}
	
	private double availabilityPriorToDayI(int day, int dueDate){
		int suppliersActualCapacity = getCapacity();
		int totalOfferQuantityToBeShipped = 0;
		
		for(int j = day+1;j <= dueDate; j++){
			for(AgentRequest a : considerations){
				totalOfferQuantityToBeShipped += a.getQuantity();
			}
		}
		int totalQuantityOfCommittedRequests = 0;
		for(int j= day+1; j <= dueDate; j++){
			for(AgentRequest a : commitedRequests){
				totalQuantityOfCommittedRequests += a.getQuantity();
			}
		}
		return day * suppliersActualCapacity - totalOfferQuantityToBeShipped + Math.min(0, inventory - totalQuantityOfCommittedRequests);
	}
	
	
	private int negativeOfCapacityRequiredOnOrBeforeDayI(int day, int dueDate){
		int k = day + dueDate + 1;
		
		int suppliersActualCapacity = getCapacity();
		int totalOfferQuantityToBeShipped = 0;
		
		for(int i = day + dueDate + 1; i <= k; i++){
			for(AgentRequest a : considerations){
				totalOfferQuantityToBeShipped += a.getQuantity();
			}
		}
		int totalQuantityOfCommittedRequests = 0;
		for(int j= day + dueDate + 1; j <= k; j++){
			for(AgentRequest a : commitedRequests){
				totalQuantityOfCommittedRequests += a.getQuantity();
			}
		}
		
		int inventoryRemainingAfterComputingAvailabilityPriorToDayI = getInventoryRemainingAfterComputingAvailabilityPriorToDayI(day, dueDate);
		
		return Math.min(0,(k - day - dueDate)* suppliersActualCapacity - totalOfferQuantityToBeShipped + Math.min(0, 
				inventoryRemainingAfterComputingAvailabilityPriorToDayI - totalQuantityOfCommittedRequests));
	}
	
	
	
	
	private int getInventoryRemainingAfterComputingAvailabilityPriorToDayI(int day, int dueDate) {
		int totalQuantityOfCommittedRequests = 0;
		for(int j= day+1; j <= dueDate; j++){
			for(AgentRequest a : commitedRequests){
				totalQuantityOfCommittedRequests += a.getQuantity();
			}
		}
		
		return Math.max(0, inventory - totalQuantityOfCommittedRequests);
		
	}

	public double decidePriceOfComponent(AgentRequest agentRequest, int day){
		int offerDueDate = agentRequest.getDueDate();
		double priceDiscountFactor = 0.5;
		double baselinePrice = getBasePrice();
		// Calculate availability
		double cPrior =  availabilityPriorToDayI(day, offerDueDate);
		double cPost = negativeOfCapacityRequiredOnOrBeforeDayI(day, offerDueDate);
		double availability = cPrior + cPost;

		//	double expectedCapacity =  getExpectedCapacity(day);
		System.out.println(baselinePrice + " * ( 1 - " + priceDiscountFactor + " * ( " + availability + " / " + inventory + ")");
		return baselinePrice * (1 - priceDiscountFactor * (availability / inventory));
	}
	
	
	
	
}