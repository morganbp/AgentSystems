package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class AgentRequest {
	private static int nextId = 1;
	private int id, supplierId, componentId, dueDate, quantity;
	private double price;
	private String agent;
	private static final String CLASSNAME = "AgentRequest";
	private static final String AGENTREQUEST_DIVIDER = "<AGENTREQUEST_DIVIDER>";
	public static final String AGENTREQUEST_FIELDS_DIVIDER = "<AGENTREQUEST_FIELDS_DIVIDER>";
	
	public AgentRequest(int supplierId, int componentId, int dueDate, int quantity, double price, String agent) {
		this(nextId++, supplierId, componentId, dueDate, quantity, price, agent);
	}
	
	public AgentRequest(int id, int supplierId, int componentId, int dueDate, int quantity, double price, String agent){
		this.id = id;
		this.supplierId = supplierId;
		this.componentId = componentId;
		this.dueDate = dueDate;
		this.quantity = quantity;
		this.price = price;
		this.agent = agent;
	}
	
	
	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getDueDate() {
		return dueDate;
	}

	public void setDueDate(int dueDate) {
		this.dueDate = dueDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	
	public static String listToString(List<AgentRequest> agentRequests) {
		StringBuilder string = new StringBuilder();
		boolean firstRun = true;
		for (AgentRequest agentRequest : agentRequests) {
			if (!firstRun)
				string.append(AGENTREQUEST_DIVIDER);
			else
				firstRun = false;

			string.append(agentRequest.toString());
		}
		return string.toString();
	}

	public static List<AgentRequest> stringToList(String AgentRequests) {
		List<AgentRequest> agentRequestList = new ArrayList<AgentRequest>();
		// AgentRequest String List
		String[] agentRequestsl = AgentRequests.split(AGENTREQUEST_DIVIDER);
		for (String agentRequest : agentRequestsl) {
			AgentRequest r = toAgentRequest(agentRequest);
			if(r == null) continue;
			agentRequestList.add(r);
		}
		
		if(agentRequestList.size() == 0) return null;
		
		return agentRequestList;
	}

	@Override
	public String toString() {
		return id + AGENTREQUEST_FIELDS_DIVIDER + supplierId + AGENTREQUEST_FIELDS_DIVIDER + componentId
				+ AGENTREQUEST_FIELDS_DIVIDER + dueDate + AGENTREQUEST_FIELDS_DIVIDER + quantity
				+ AGENTREQUEST_FIELDS_DIVIDER + price + AGENTREQUEST_FIELDS_DIVIDER + agent;
	}

	public static AgentRequest toAgentRequest(String agentRequeststring) {
		String[] AgentRequests = agentRequeststring.split(AGENTREQUEST_FIELDS_DIVIDER);
		if(AgentRequests.length != 7) return null;
		
		return new AgentRequest(Integer.parseInt(AgentRequests[0]), Integer.parseInt(AgentRequests[1]),
				Integer.parseInt(AgentRequests[2]), Integer.parseInt(AgentRequests[3]),
				Integer.parseInt(AgentRequests[4]), Integer.parseInt(AgentRequests[5]), AgentRequests[6]);

	}

}
