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

	protected String makeAgentRFQs(String agent, int day, List<Order> customerOrders){
		int numberOfSuppliers = 8;
		
		for(int sId = 0; sId < numberOfSuppliers; sId++) {
			//Burde ikke supplier støtte et parameter?
			Supplier sup = new Supplier(sId);
			Component prod[] = sup.getComponents();
			for (int j = 0; j<5; j++){
				for(int k = 0; k<2; k++) {
					int cid = prod[k].getId();
					/*for(int i=day; i<TAC_Ontology.numberOfTacDays; i++){
						if(cDemand[getIndex(cid)[i] > 0){
							ol = i;
							break;
						}
					}*/
					for (Order o : customerOrders) {
						int sku = o.getOffer().getRFQ().getPC();
						int q = o.getOffer().getRFQ().getQuantity();
						int d = o.getDueDate();
						switch (sku) {
						case 1:
							cDemand[getIndex(100)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 2:
							cDemand[getIndex(100)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						case 3:
							cDemand[getIndex(100)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 4:
							cDemand[getIndex(100)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						case 5:
							cDemand[getIndex(101)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 6:
							cDemand[getIndex(101)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						case 7:
							cDemand[getIndex(101)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 8:
							cDemand[getIndex(101)][d] += q;
							cDemand[getIndex(200)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						case 9:
							cDemand[getIndex(110)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 10:
							cDemand[getIndex(110)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						case 11:
							cDemand[getIndex(110)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 12:
							cDemand[getIndex(110)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						case 13:
							cDemand[getIndex(111)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 14:
							cDemand[getIndex(111)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(300)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						case 15:
							cDemand[getIndex(111)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(400)][d] += q;
							break;
						case 16:
							cDemand[getIndex(111)][d] += q;
							cDemand[getIndex(210)][d] += q;
							cDemand[getIndex(301)][d] += q;
							cDemand[getIndex(401)][d] += q;
							break;
						}
						int quantity = cDemand[getIndex(cid)][d];
						int dueDate = d-2;
						if(dueDate < day+2) continue;
						//dueDate må være 2 dager før idag	
						AgentRequest rfq = new AgentRequest(
							sId, cid, quantity, dueDate, 0, agent);
						agentRFQs.add(rfq);
						cDemand[getIndex(cid)][d] = 0; 
					}
					    	  
						
			      }
			}
		}
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
