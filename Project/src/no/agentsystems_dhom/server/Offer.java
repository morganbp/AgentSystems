package no.agentsystems_dhom.server;

import java.util.List;

public class Offer {
	private String bidder;
	private String reciever;
	private double offer;
	private RFQ rfq;

	protected static String OFFER_FIELD_SEPERATOR = "&";
	protected static String OFFER_OBJECT_SEPERATOR = "¤";

	public Offer(String bidder, String reciever, double offer, RFQ rfq) {
		this.bidder = bidder;
		this.reciever = reciever;
		this.offer = offer;
		this.rfq = rfq;
	}

	public String getBidder() {
		return bidder;
	}

	public String getreciever() {
		return reciever;
	}

	public double getOffer() {
		return offer;
	}

	public RFQ getRFQ() {
		return rfq;
	}

	@Override
	public String toString() {
		return bidder + OFFER_FIELD_SEPERATOR + reciever
				+ OFFER_FIELD_SEPERATOR + Double.toString(offer)
				+ OFFER_FIELD_SEPERATOR + rfq.toString();
	}
	
	public static List<Offer> stringToList(String offerString){
		return null;
	}
}
