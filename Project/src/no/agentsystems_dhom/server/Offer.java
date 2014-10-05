package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class Offer {
	private String bidder;
	private String reciever;
	private double offerPrice;
	private RFQ rfq;

	protected static String OFFER_FIELD_SEPERATOR = "<offerPrice_field_seperator>";
	protected static String OFFER_OBJECT_SEPERATOR = "<offerPrice_object_seperator>";

	public Offer(String bidder, String reciever, double offerPrice, RFQ rfq) {
		this.bidder = bidder;
		this.reciever = reciever;
		this.offerPrice = offerPrice;
		this.rfq = rfq;
	}

	public String getBidder() {
		return bidder;
	}

	public String getReciever() {
		return reciever;
	}

	public double getOfferPrice() {
		return offerPrice;
	}

	public RFQ getRFQ() {
		return rfq;
	}

	@Override
	public String toString() {
		return bidder + OFFER_FIELD_SEPERATOR + reciever
				+ OFFER_FIELD_SEPERATOR + Double.toString(offerPrice)
				+ OFFER_FIELD_SEPERATOR + rfq.toString();
	}
	
	public static String listToString(List<Offer> offerPricesList){
		StringBuilder builder = new StringBuilder();
		boolean firstrun = true;
		for(Offer offerPrice : offerPricesList){
			if(!firstrun)
				builder.append(OFFER_OBJECT_SEPERATOR);
			else
				firstrun = false;
			
			builder.append(offerPrice.toString());
		}
		return builder.toString();
	}
	
	public static List<Offer> stringToList(String offerPricesString){
		List<Offer> rtnList = new ArrayList<>();
		String[] offerPrices = offerPricesString.split(OFFER_OBJECT_SEPERATOR);
		for(String offerPrice : offerPrices ){
			Offer o = toOffer(offerPrice);
			if(o == null) continue;
			rtnList.add(o);
		}
		return rtnList;
	}
	
	public static Offer toOffer(String offerPrice){
		String[] offFields = offerPrice.split(OFFER_FIELD_SEPERATOR);
		if(offFields.length != 4) return null;
		
		RFQ rfq = RFQ.toRFQ(offFields[3]);
		if(rfq == null) return null;
		
		return new Offer(offFields[0], offFields[1], Double.parseDouble(offFields[2]), rfq);
	}
	
}
