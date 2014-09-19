package no.agentsystems_dhom.server;

public class Offer {
	private String bidder;
	private String receiver;
	private double offer;
	private RFQ rfq;

	protected static String OFFER_FIELD_SEPERATOR = "&";
	protected static String OFFER_OBJECT_SEPERATOR = "¤";

	public Offer(String bidder, String receiver, double offer, RFQ rfq) {
		this.bidder = bidder;
		this.receiver = receiver;
		this.offer = offer;
		this.rfq = rfq;
	}

	public String getBidder() {
		return bidder;
	}

	public String getReceiver() {
		return receiver;
	}

	public double getOffer() {
		return offer;
	}

	public RFQ getRFQ() {
		return rfq;
	}

	@Override
	public String toString() {
		return bidder + OFFER_FIELD_SEPERATOR + receiver
				+ OFFER_FIELD_SEPERATOR + Double.toString(offer)
				+ OFFER_FIELD_SEPERATOR + rfq.toString();
	}
}
