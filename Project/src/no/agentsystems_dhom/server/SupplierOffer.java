package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class SupplierOffer {

	private String bidder;
	private String reciever;
	private double offerPrice;
	private int quantity, dueDate;
	private AgentRequest agentRequest;

	protected static String SUPPLIEROFFER_FIELD_SEPERATOR = "<SUPPLIEROFFER_FIELD_SEPERATOR>";
	protected static String SUPPLIEROFFER_OBJECT_SEPERATOR = "<SUPPLIEROFFER_OBJECT_SEPERATOR>";

	public SupplierOffer(String bidder, String reciever, double offerPrice,
			int quantity, int dueDate, AgentRequest agentRequest) {
		this.bidder = bidder;
		this.reciever = reciever;
		this.offerPrice = offerPrice;
		this.quantity = quantity;
		this.dueDate = dueDate;
		this.agentRequest = agentRequest;
	}

	public String getBidder() {
		return bidder;
	}

	public String getReciever() {
		return reciever;
	}

	public double getSupplierOfferPrice() {
		return offerPrice;
	}

	public AgentRequest getAgentRequest() {
		return agentRequest;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getDueDate() {
		return dueDate;
	}

	@Override
	public String toString() {
		return bidder + SUPPLIEROFFER_FIELD_SEPERATOR + reciever
				+ SUPPLIEROFFER_FIELD_SEPERATOR + Double.toString(offerPrice)
				+ SUPPLIEROFFER_FIELD_SEPERATOR + Integer.toString(quantity)
				+ SUPPLIEROFFER_FIELD_SEPERATOR + Integer.toString(dueDate)
				+ SUPPLIEROFFER_FIELD_SEPERATOR + agentRequest.toString();
	}

	public static String listToString(List<SupplierOffer> offerPricesList) {
		StringBuilder builder = new StringBuilder();
		boolean firstrun = true;
		for (SupplierOffer offerPrice : offerPricesList) {
			if (!firstrun)
				builder.append(SUPPLIEROFFER_OBJECT_SEPERATOR);
			else
				firstrun = false;

			builder.append(offerPrice.toString());
		}
		return builder.toString();
	}

	public static List<SupplierOffer> stringToList(String offerPricesString) {
		List<SupplierOffer> rtnList = new ArrayList<>();
		String[] offerPrices = offerPricesString
				.split(SUPPLIEROFFER_OBJECT_SEPERATOR);
		for (String offerPrice : offerPrices) {
			SupplierOffer o = toSupplierOffer(offerPrice);
			if (o == null)
				continue;
			rtnList.add(o);
		}
		return rtnList;
	}

	public static SupplierOffer toSupplierOffer(String offerPrice) {
		String[] offFields = offerPrice.split(SUPPLIEROFFER_FIELD_SEPERATOR);
		if (offFields.length != 6)
			return null;

		AgentRequest agentRequest = AgentRequest.toAgentRequest(offFields[5]);
		if (agentRequest == null)
			return null;

		return new SupplierOffer(offFields[0], offFields[1],
				Double.parseDouble(offFields[2]),
				Integer.parseInt(offFields[3]), Integer.parseInt(offFields[4]), agentRequest);
	}

}
