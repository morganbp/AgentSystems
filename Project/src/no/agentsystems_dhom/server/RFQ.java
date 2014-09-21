package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class RFQ {
	private static int nextId = 1;
	private int RFQId;
	private int PC;
	private int quantity;
	private int dueDate;
	private int penalty;
	private int reservePrice;

	public static final String RFQ_DIVIDER = "#";
	public static final String RFQ_FIELDS_DIVIDER = ":";

	public RFQ(int PC, int quantity, int dueDate, int penalty, int reservePrice) {
		// Setting a unique ID for the RFQ
		this.RFQId = nextId;
		nextId++;

		this.PC = PC;
		this.quantity = quantity;
		this.dueDate = dueDate;
		this.penalty = penalty;
		this.reservePrice = reservePrice;
	}

	public RFQ(int id, int PC, int quantity, int dueDate, int penalty,
			int reservePrice) {
		// Setting a unique ID for the RFQ
		this.RFQId = id;
		this.PC = PC;
		this.quantity = quantity;
		this.dueDate = dueDate;
		this.penalty = penalty;
		this.reservePrice = reservePrice;
	}

	public int getRFQId(){
		return RFQId;
	}
	
	public int getReservePrice(){
		return reservePrice;
	}
	
	public static String listToString(List<RFQ> RFQs) {
		StringBuilder string = new StringBuilder();
		boolean firstRun = true;
		for (RFQ rfq : RFQs) {
			if (!firstRun)
				string.append(RFQ_DIVIDER);
			else
				firstRun = false;

			string.append(rfq.toString());
		}
		return string.toString();
	}

	public static List<RFQ> stringToList(String RFQs) {
		List<RFQ> rfqList = new ArrayList<RFQ>();
		// RFQ String List
		String[] rfqsl = RFQs.split(RFQ_DIVIDER);
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
		return RFQId + RFQ_FIELDS_DIVIDER + PC + RFQ_FIELDS_DIVIDER + quantity
				+ RFQ_FIELDS_DIVIDER + dueDate + RFQ_FIELDS_DIVIDER + penalty
				+ RFQ_FIELDS_DIVIDER + reservePrice;
	}

	public static RFQ toRFQ(String rfqstring) {
		String[] RFQs = rfqstring.split(RFQ_FIELDS_DIVIDER);
		if(RFQs.length != 6) return null;
		
		return new RFQ(Integer.parseInt(RFQs[0]), Integer.parseInt(RFQs[1]),
				Integer.parseInt(RFQs[2]), Integer.parseInt(RFQs[3]),
				Integer.parseInt(RFQs[4]), Integer.parseInt(RFQs[5]));

	}

}
