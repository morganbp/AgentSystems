package no.agentsystems_dhom.agent;

import java.util.ArrayList;
import java.util.List;

import no.agentsystems_dhom.server.AgentRequest;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.Offer;
import no.agentsystems_dhom.server.Order;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.TAC_Ontology;
import no.agentsystems_dhom.server.Util;
import no.agentsystems_dhom.supplier.Component;
import no.agentsystems_dhom.supplier.Supplier;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import yinyang.Message;
import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;

public class SCM_Agent {
	protected SCM server;
	protected int interval;
	private boolean has_started;
	protected GUI agentView;
	protected List<Offer> todaysOffers;
	protected List<Order> activeOrders;
	protected int cDemand[][] = new int[10][TAC_Ontology.numberOfTacDays];
	protected int components[] = { 100, 101, 110, 111, 200, 210, 300, 301, 400,
			401 };
	

	public static SCM initServer(String[] args) {
		SCM rtnServer = null;
		try {
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "TACSCM";
			rtnServer = SCMHelper.narrow(ncRef.resolve_str(name));

		} catch (Exception e) {
			System.out.println("ERROR :" + e);
			e.printStackTrace();
			return null;
		}
		return rtnServer;
	}

	protected List<AgentRequest> makeAgentRFQs(String className, int day, List<Order> customerOrders){
		int numberOfSuppliers = 8;
		List<AgentRequest> agentRFQs = new ArrayList<AgentRequest>();
		
		for(int sId = 0; sId < numberOfSuppliers; sId++) {
			Supplier sup = new Supplier(sId);
			Component prod[] = sup.getComponents();
			for (int j = 0; j<5; j++){
				for(int k = 0; k < 2; k++) {
					// Send send 5 offers for each component 
					int cid = prod[k].getId();
					int dueDate = day + 2;
					int quantity = cDemand[getIndex(cid)][dueDate];
					
					AgentRequest agentReq = new AgentRequest(sId, cid, quantity, dueDate, 0 ,className);
					agentRFQs.add(agentReq);
					cDemand[getIndex(cid)][dueDate] = 0; 			
			      }
			}
		}
		
		return agentRFQs;
	}
	
	protected void computeRequirements(List<Order> customerOrders, int day){
		for (Order o : customerOrders) {
			int sku = o.getOffer().getRFQ().getPC();
			int offerQuantity = o.getOffer().getRFQ().getQuantity();
			int d = o.getDueDate();			
			switch (sku) {
			case 1:
				cDemand[getIndex(100)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 2:
				cDemand[getIndex(100)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			case 3:
				cDemand[getIndex(100)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 4:
				cDemand[getIndex(100)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			case 5:
				cDemand[getIndex(101)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 6:
				cDemand[getIndex(101)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			case 7:
				cDemand[getIndex(101)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 8:
				cDemand[getIndex(101)][d] += offerQuantity;
				cDemand[getIndex(200)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			case 9:
				cDemand[getIndex(110)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 10:
				cDemand[getIndex(110)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			case 11:
				cDemand[getIndex(110)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 12:
				cDemand[getIndex(110)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			case 13:
				cDemand[getIndex(111)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 14:
				cDemand[getIndex(111)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(300)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			case 15:
				cDemand[getIndex(111)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(400)][d] += offerQuantity;
				break;
			case 16:
				cDemand[getIndex(111)][d] += offerQuantity;
				cDemand[getIndex(210)][d] += offerQuantity;
				cDemand[getIndex(301)][d] += offerQuantity;
				cDemand[getIndex(401)][d] += offerQuantity;
				break;
			}
		}	
	}
	
	protected void sendAgentRFQs(String className, List<AgentRequest> agentRequests)
	{
		String kqmlContent = AgentRequest.listToString(agentRequests);
		Message kqml = Util.buildKQML(TAC_Ontology.Agent_RFQs, className, kqmlContent);
		String resp = server.send(kqml.toString());
		Message response = Message.buildMessage(resp);
		agentView.append("#RFQs to Supplier: " + response.getContent()); 
	}

	protected int getIndex(int cId) {
		for (int i = 0; i < components.length; i++) {
			if (components[i] == cId)
				return i;
		}
		return -1;
	}

	protected List<RFQ> getRFQsFromServer(String className) {
		String content = "";
		Message kqml = Util.buildKQML(TAC_Ontology.getCustomer_RFQs, className,
				content);
		String resp = server.send(kqml.toString());
		Message response = Message.buildMessage(resp);
		List<RFQ> RFQList = RFQ.stringToList(response.getContent());

		return RFQList;
	}

	protected void sendOffersToServer(String className) {
		if (todaysOffers == null)
			return;

		Message kqml = Util.buildKQML(TAC_Ontology.agentOffers, className,
				Offer.listToString(todaysOffers));
		server.send(kqml.toString());
		agentView.append("\nNumber of Offers: " + todaysOffers.size());
		todaysOffers.clear();
	}

	protected void createOffer(String bidder, String reciever,
			double offerPrice, RFQ rfq) {
		todaysOffers.add(new Offer(bidder, reciever, offerPrice, rfq));
	}

	protected List<Order> getOrderFromServer(String className) {
		String content = "";
		Message kqml = Util.buildKQML(TAC_Ontology.getCustomerOrders,
				className, content);
		String respond = server.send(kqml.toString());
		Message response = Message.buildMessage(respond);
		List<Order> orderList = Order.stringToList(response.getContent());

		agentView.append("\n#Customer Orders: " + orderList.size());
		return orderList;
	}

	protected void startTheGame() {
		todaysOffers = new ArrayList<Offer>();
		activeOrders = new ArrayList<Order>();
		has_started = true;
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0
				: interval;
		agentView.setText("---> Time : " + interval + " seconds ");
	}

	protected void closeTheGame() {
		has_started = false;
		agentView.append("\n\n---> The TAC Game is closed");
	}

	protected boolean getStatus() {
		return has_started;
	}

	protected void agentRegistering(String name, int id) {
		Message kqml = Util.buildKQML(TAC_Ontology.agent_registering, name, ""
				+ id);

		String resp = server.send(kqml.toString());
		Message response = Message.buildMessage(resp);
		agentView.append("\n" + response.getContent());
	}

}
