package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class AgentOrder {
	private String customer;
	private String provider;
	private SupplierOffer supplierOffer;

	protected static String AGENTORDER_FIELD_SEPERATOR = "<agentOrder_field_seperator>";
	protected static String AGENTORDER_OBJECT_SEPERATOR = "<agentOrder_object_seperator>";

	public AgentOrder(SupplierOffer supplierOffer) {
		this.customer = supplierOffer.getReciever();
		this.provider = supplierOffer.getBidder();
		this.supplierOffer = supplierOffer;
	}
	
	public AgentOrder(String customer, String provider, SupplierOffer supplierOffer) {
		this.customer = customer;
		this.provider = provider;
		this.supplierOffer = supplierOffer;
	}
	
	
	public String getCustomer() {
		return customer;
	}

	public String getProvider() {
		return provider;
	}


	public SupplierOffer getSupplierOffer() {
		return supplierOffer;
	}
	
	public int getDueDate(){
		return supplierOffer.getAgentRequest().getDueDate();
	}
	
	public double getPrice(){
		return supplierOffer.getSupplierOfferPrice();
	}

	@Override
	public String toString() {
		return customer + AGENTORDER_FIELD_SEPERATOR + provider
				+ AGENTORDER_FIELD_SEPERATOR + supplierOffer.toString();
	}
	
	public static String listToString(List<AgentOrder> agentOrdersList){
		StringBuilder builder = new StringBuilder();
		boolean firstrun = true;
		for(AgentOrder agentOrder : agentOrdersList){
			if(!firstrun)
				builder.append(AGENTORDER_OBJECT_SEPERATOR);
			else
				firstrun = false;
			
			builder.append(agentOrder.toString());
		}
		return builder.toString();
	}
	
	public static List<AgentOrder> stringToList(String agentOrdersString){
		List<AgentOrder> rtnList = new ArrayList<>();
		String[] agentOrders = agentOrdersString.split(AGENTORDER_OBJECT_SEPERATOR);
		for(String agentOrder : agentOrders ){
			AgentOrder o = toAgentOrder(agentOrder);
			if(o == null) continue;
			rtnList.add(o);
		}
		return rtnList;
	}
	
	public static AgentOrder toAgentOrder(String agentOrder){
		String[] ordFields = agentOrder.split(AGENTORDER_FIELD_SEPERATOR);;
		if(ordFields.length != 3) return null;
		
		SupplierOffer supplierOffer = SupplierOffer.toSupplierOffer(ordFields[2]);
		if(supplierOffer == null) return null;
		
		return new AgentOrder(ordFields[0], ordFields[1], supplierOffer);
	}
	
}
