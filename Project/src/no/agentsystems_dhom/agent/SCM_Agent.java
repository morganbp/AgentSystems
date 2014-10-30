package no.agentsystems_dhom.agent;

import java.util.ArrayList;
import java.util.List;

import no.agentsystems_dhom.customer.PC;
import no.agentsystems_dhom.server.AgentOrder;
import no.agentsystems_dhom.server.AgentRequest;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.Offer;
import no.agentsystems_dhom.server.Order;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.SupplierOffer;
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
	protected List<AgentOrder> todaysAgentOrder;
	protected List<Order> activeOrders;
	protected int cDemand[][] = new int[10][TAC_Ontology.numberOfTacDays];
	protected static int components[] = { 100, 101, 110, 111, 200, 210, 300,
			301, 400, 401 };

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

	protected List<AgentRequest> makeAgentRFQs(String className, int day,
			List<Order> customerOrders) {
		int numberOfSuppliers = 8;
		List<AgentRequest> agentRFQs = new ArrayList<AgentRequest>();

		for (int sId = 0; sId < numberOfSuppliers; sId++) {
			Supplier sup = new Supplier(sId);
			Component prod[] = sup.getComponents();
			for (int j = 0; j < 5; j++) {
				for (int k = 0; k < 2; k++) {
					// Send send 5 offers for each component
					int cid = prod[k].getId();
					int dueDate = day + 2;
					int quantity = cDemand[getIndex(cid)][dueDate];

					AgentRequest agentReq = new AgentRequest(sId, cid,
							quantity, dueDate, 0, className);
					agentRFQs.add(agentReq);
					cDemand[getIndex(cid)][dueDate] = 0;
				}
			}
		}

		return agentRFQs;
	}

	protected void computeRequirements(List<Order> customerOrders, int day) {
		for (Order o : customerOrders) {
			int sku = o.getOffer().getRFQ().getPC();
			int offerQuantity = o.getOffer().getRFQ().getQuantity();
			int d = o.getDueDate();
			PC pc = new PC(sku);
			int componentsIds[] = pc.getComponents();
			for (int i = 0; i < componentsIds.length; i++) {
				cDemand[getIndex(componentsIds[0])][d] += offerQuantity;
			}
		}
	}

	protected void sendAgentRFQs(String className,
			List<AgentRequest> agentRequests) {
		String kqmlContent = AgentRequest.listToString(agentRequests);
		Message kqml = Util.buildKQML(TAC_Ontology.Agent_RFQs, className,
				kqmlContent);
		String resp = server.send(kqml.toString());
		Message response = Message.buildMessage(resp);
		agentView.append("\n#RFQs to Supplier: " + response.getContent());
	}

	protected static int getIndex(int cId) {
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

	protected void sendSupplierOrders(String className) {
		if (todaysAgentOrder == null)
			return;

		Message kqml = Util.buildKQML(TAC_Ontology.sendAgentOrders,
				className, AgentOrder.listToString(todaysAgentOrder));
		server.send(kqml.toString());
		agentView.append("\n#Number of SupplierOrders: " + todaysAgentOrder.size());
		todaysAgentOrder.clear();
	}

	protected List<SupplierOffer> getSupplierOffers(String className) {
		String content = "";
		Message kqml = Util.buildKQML(TAC_Ontology.getSupplierOffers,
				className, content);
		System.out.println(kqml.toString());
		String respond = server.send(kqml.toString());
		Message response = Message.buildMessage(respond);
		List<SupplierOffer> offerList = SupplierOffer.stringToList(response
				.getContent());
		List<SupplierOffer> supplierOfferList = new ArrayList<SupplierOffer>();
		for (SupplierOffer supplierOffer : offerList) {
			if (supplierOffer.getReciever().equals(className)) {
				supplierOfferList.add(supplierOffer);
			}
		}
		agentView.append("\n#Supplier Offers : " + offerList.size());
		return supplierOfferList;
	}
	
	protected void createAgentOrder(SupplierOffer supplierOffer){
		String customer = supplierOffer.getReciever();
		String provider = supplierOffer.getBidder();
		AgentOrder agentOrder = new AgentOrder(customer, provider, supplierOffer);
		todaysAgentOrder.add(agentOrder);
	}

	protected void startTheGame() {
		todaysOffers = new ArrayList<Offer>();
		todaysAgentOrder = new ArrayList<AgentOrder>();
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
