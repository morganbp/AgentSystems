package no.agentsystems_dhom.server;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private String bidder;
	private String reciever;
	private double order;
	private RFQ rfq;

	protected static String ORDER_FIELD_SEPERATOR = "<order_field_seperator>";
	protected static String ORDER_OBJECT_SEPERATOR = "<order_object_seperator>";

	public Order(String bidder, String reciever, double order, RFQ rfq) {
		this.bidder = bidder;
		this.reciever = reciever;
		this.order = order;
		this.rfq = rfq;
	}

	public String getBidder() {
		return bidder;
	}

	public String getReciever() {
		return reciever;
	}

	public double getOrder() {
		return order;
	}

	public RFQ getRFQ() {
		return rfq;
	}

	@Override
	public String toString() {
		return bidder + ORDER_FIELD_SEPERATOR + reciever
				+ ORDER_FIELD_SEPERATOR + Double.toString(order)
				+ ORDER_FIELD_SEPERATOR + rfq.toString();
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
		String[] offFields = order.split(ORDER_FIELD_SEPERATOR);
		if(offFields.length != 4) return null;
		
		RFQ rfq = RFQ.toRFQ(offFields[3]);
		if(rfq == null) return null;
		
		return new Order(offFields[0], offFields[1], Double.parseDouble(offFields[2]), rfq);
	}
	
	
}
