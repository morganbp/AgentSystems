package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/** 
 *
 * @author Morgan Buckholm Pettersen
 * @author David Helgestad
 * @author Hans-Ole Gudim
 *
 * This class is spesific for Orders which are
 * made by customers.
 * 
 */
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
	
	public double getPrice(){
		return offer.getOfferPrice();
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
	
	/**
	 * Comparator which is used to sort Orders after due date
	 */
	public static final Comparator<Order> DUE_DATE_COMPARATOR = new Comparator<Order>(){

		@Override
		public int compare(Order o1, Order o2) {
			if(o1.getDueDate() == o2.getDueDate()){
				return 0;
			}else if(o1.getDueDate() < o2.getDueDate()){
				return 1;
			}else{
				return -1;
			}
		}
		
	};
	
}
