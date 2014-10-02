package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class Offer {
	private String bidder;
	private String reciever;
	private double offer;
	private RFQ rfq;

	protected static String OFFER_FIELD_SEPERATOR = "<offer_field_seperator>";
	protected static String OFFER_OBJECT_SEPERATOR = "<offer_object_seperator>";

	public Offer(String bidder, String reciever, double offer, RFQ rfq) {
		this.bidder = bidder;
		this.reciever = reciever;
		this.offer = offer;
		this.rfq = rfq;
	}

	public String getBidder() {
		return bidder;
	}

	public String getReciever() {
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
	
	public static String listToString(List<Offer> offersList){
		StringBuilder builder = new StringBuilder();
		boolean firstrun = true;
		for(Offer offer : offersList){
			if(!firstrun)
				builder.append(OFFER_OBJECT_SEPERATOR);
			else
				firstrun = false;
			
			builder.append(offer.toString());
		}
		return builder.toString();
	}
	
	public static List<Offer> stringToList(String offersString){
		List<Offer> rtnList = new ArrayList<>();
		String[] offers = offersString.split(OFFER_OBJECT_SEPERATOR);
		for(String offer : offers ){
			Offer o = toOffer(offer);
			if(o == null) continue;
			rtnList.add(o);
		}
		return rtnList;
	}
	
	public static Offer toOffer(String offer){
		String[] offFields = offer.split(OFFER_FIELD_SEPERATOR);
		if(offFields.length != 4) return null;
		
		RFQ rfq = RFQ.toRFQ(offFields[3]);
		if(rfq == null) return null;
		
		return new Offer(offFields[0], offFields[1], Double.parseDouble(offFields[2]), rfq);
	}
	
}
