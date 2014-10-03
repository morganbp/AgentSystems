package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private String customer;
	private String provider;
	private Offer offer;

	protected static String ORDER_FIELD_SEPERATOR = "<order_field_seperator>";
	protected static String ORDER_OBJECT_SEPERATOR = "<order_object_seperator>";

	public Order(Offer offer) {
		this.customer = offer.getReciever();
		this.provider = offer.getBidder();
		this.offer = offer;
	}
	
	public Order(String customer, String provider, Offer offer) {
		this.customer = customer;
		this.provider = provider;
		this.offer = offer;
	}
	
	
	public String getCustomer() {
		return customer;
	}

	public String getProvider() {
		return provider;
	}


	public Offer getOffer() {
		return offer;
	}
	
	public int getDueDate(){
		return offer.getRFQ().getDueDate();
	}
	public int getPenalty(){
		return offer.getRFQ().getPenalty();
	}

	@Override
	public String toString() {
		return customer + ORDER_FIELD_SEPERATOR + provider
				+ ORDER_FIELD_SEPERATOR + offer.toString();
	}
	
	public static String listToString(List<Order> ordersList){
		StringBuilder builder = new StringBuilder();
		boolean firstrun = true;
		for(Order order : ordersList){
			if(!firstrun)
				builder.append(ORDER_OBJECT_SEPERATOR);
			else
				firstrun = false;
			
			builder.append(order.toString());
		}
		return builder.toString();
	}
	
	public static List<Order> stringToList(String ordersString){
		List<Order> rtnList = new ArrayList<>();
		String[] orders = ordersString.split(ORDER_OBJECT_SEPERATOR);
		for(String order : orders ){
			Order o = toOrder(order);
			if(o == null) continue;
			rtnList.add(o);
		}
		return rtnList;
	}
	
	public static Order toOrder(String order){
		String[] ordFields = order.split(ORDER_FIELD_SEPERATOR);;
		if(ordFields.length != 3) return null;
		
		Offer offer = Offer.toOffer(ordFields[2]);
		if(offer == null) return null;
		
		return new Order(ordFields[0], ordFields[1], offer);
	}
	
	
}
