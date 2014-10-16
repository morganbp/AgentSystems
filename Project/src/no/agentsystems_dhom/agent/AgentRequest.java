package no.agentsystems_dhom.agent;

import java.util.ArrayList;
import java.util.List;

import no.agentsystems_dhom.server.Agent;
import no.agentsystems_dhom.server.RFQ;

public class AgentRequest {
	private static int nextId = 1;
	private int id, supplierId, componentId, dueDate, quantity;
	private double price;
	private Agent agent;
	private static final String CLASSNAME = "AgentRequest";
	private static final String AGENTREQUEST_DIVIDER = "<AGENTREQUEST_DIVIDER>";
	public static final String AGENTREQUEST_FIELDS_DIVIDER = "<AGENTREQUEST_FIELDS_DIVIDER>";
	
	public AgentRequest(int supplierId, int componentId, int dueDate, int quantity, double price, String _agent) {
		this.id = nextId++;
		agent = new Agent(_agent, id);
		
	}
	
	
	protected int getSupplierId() {
		return supplierId;
	}

	protected void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	protected int getDueDate() {
		return dueDate;
	}

	protected void setDueDate(int dueDate) {
		this.dueDate = dueDate;
	}

	protected int getQuantity() {
		return quantity;
	}

	protected void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	protected double getPrice() {
		return price;
	}

	protected void setPrice(double price) {
		this.price = price;
	}
	
	
	public static String listToString(List<RFQ> RFQs) {
		StringBuilder string = new StringBuilder();
		boolean firstRun = true;
		for (RFQ rfq : RFQs) {
			if (!firstRun)
				string.append(AGENTREQUEST_DIVIDER);
			else
				firstRun = false;

			string.append(rfq.toString());
		}
		return string.toString();
	}

	public static List<RFQ> stringToList(String RFQs) {
		List<RFQ> rfqList = new ArrayList<RFQ>();
		// RFQ String List
		String[] rfqsl = RFQs.split(AGENTREQUEST_DIVIDER);
		for (String rfq : rfqsl) {
			RFQ r = toRFQ(rfq);
			if(r == null) continue;
			rfqList.add(r);
		}
		
		if(rfqList.size() == 0) return null;
		
		return rfqList;
	}

	@Override
	public String toString() {
		return id + AGENTREQUEST_FIELDS_DIVIDER + supplierId + AGENTREQUEST_FIELDS_DIVIDER + componentId
				+ AGENTREQUEST_FIELDS_DIVIDER + dueDate + AGENTREQUEST_FIELDS_DIVIDER + quantity
				+ AGENTREQUEST_FIELDS_DIVIDER + price + AGENTREQUEST_FIELDS_DIVIDER + agent;
	}

	public static RFQ toRFQ(String rfqstring) {
		String[] RFQs = rfqstring.split(AGENTREQUEST_FIELDS_DIVIDER);
		if(RFQs.length != 7) return null;
		
		return new RFQ(Integer.parseInt(RFQs[0]), Integer.parseInt(RFQs[1]),
				Integer.parseInt(RFQs[2]), Integer.parseInt(RFQs[3]),
				Integer.parseInt(RFQs[4]), Integer.parseInt(RFQs[5]), RFQs[6]);

	}

}
