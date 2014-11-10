package no.agentsystems_dhom.supplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import no.agentsystems_dhom.server.AgentOrder;
import no.agentsystems_dhom.server.AgentRequest;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.Offer;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.SupplierOffer;
import no.agentsystems_dhom.server.TAC_Ontology;
import no.agentsystems_dhom.server.Util;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import yinyang.Message;
import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;

public class SCM_Supplier {
	private boolean has_started;
	protected GUI suplView;
	protected int interval;
	protected SCM server;
	protected Supplier[] suppliers;
	protected List<SupplierOffer> allSupplierOffers;
	protected List<AgentOrder> allAgentOrders;
	
	protected List<SupplierOffer> supplierOffers;
	protected List<AgentOrder> activeAgentOrders;
	
	
	
	public static SCM initServer(String[] args){
		SCM server;
		try{
			// create and init the ORB
			ORB orb = ORB.init(args, null);
			
			// get the  root naming context
			org.omg.CORBA.Object objRef = 
					orb.resolve_initial_references("NameService");
			// Using NamingContextExt instead of NamingContext. This is 
			// part of the Naming Service
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			// resolve the Object Reference in Naming
			String name = "TACSCM";
			server = SCMHelper.narrow(ncRef.resolve_str(name));
			
			
		}catch(Exception e){
			System.out.println("/n/nERROR : " + e);
			e.printStackTrace();
			return  null;
		}
		return server;
	}
	
	protected void startTheGame(){
		has_started = true;
		
		supplierOffers = new ArrayList<SupplierOffer>();
		activeAgentOrders = new ArrayList<AgentOrder>();
		allSupplierOffers = new ArrayList<SupplierOffer>();
		allAgentOrders = new ArrayList<AgentOrder>();
		
		initSuppliers();
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0 : interval;
		suplView.setText("---> Time : " + interval + " seconds ");
		
	}
	
	private void initSuppliers() {
		suppliers = new Supplier[8];
		suppliers[0] = new Supplier(100, 101, 0);
		suppliers[1] = new Supplier(110, 111, 1);
		suppliers[2] = new Supplier(200, 210, 2);
		suppliers[3] = new Supplier(300, 301, 3);
		suppliers[4] = new Supplier(400, 401, 4);
		suppliers[5] = new Supplier(200, 101, 5);
		suppliers[6] = new Supplier(300, 210, 6);
		suppliers[7] = new Supplier(401, 111, 7);
	}

	protected void closeTheGame()	{
		has_started = false;
		suplView.append("\n\n---> The TAC Game is closed");
	}
	
	protected boolean getStatus(){
		return has_started;
	}
	
	protected void setStatus(boolean status){
		has_started = status;
	}
	
	protected void addSupplierComponents() {
		// loop through all components (2 for each supplier)
		// and add the new components
		for (int i = 0; i < suppliers.length*2; i++) {
			int index = (int)Math.floor(i/2);
			Component comp = suppliers[index].getComponents()[i%2];
			comp.addComponents();
		}
	}
	
	protected void createSupplierOffers(List<AgentRequest> agentRequests, String bidder){
		// Sort AgentRequests by decreasing reputation
		Collections.sort(agentRequests, agentReputationComparator);
		
		// This list stores lists of agentRequests 
		List<List<AgentRequest>> agentReputationSeperationList = new ArrayList<List<AgentRequest>>();
		// Repuation of first agent
		double curReputation = getReputation(agentRequests.get(0).getAgent(), agentRequests.get(0).getSupplierId());
		List<AgentRequest> curList = new ArrayList<AgentRequest>();
		
		// loop trough sorted list and seperate agents with different reputation on 
		// different lists
		for(AgentRequest agentRequest : agentRequests){
			double reputation = getReputation(agentRequest.getAgent(), agentRequest.getSupplierId());
			if(reputation == curReputation){
				curList.add(agentRequest);
			}else{
				agentReputationSeperationList.add(curList);
				curReputation = reputation;
				curList = new ArrayList<AgentRequest>();
				curList.add(agentRequest);
			}
		}
		
		for(List<AgentRequest> requestList : agentReputationSeperationList){
			for(AgentRequest agentRequest : requestList){
				// TODO create action with agent request with equal reputation
				
				
			}
		}
	}
	
	protected void createSupplierOffer(AgentRequest agentRequest, String bidder){
		//SupplierOffer sup = new SupplierOffer(bidder, agentRequest.getAgent(), agentRequest.getPrice(), agentRequest.getQuantity(), agentRequest.getDueDate(), agentRequest);
		//supplierOffers.add(sup);
	}
	protected void sendYesterdaysOffers(String className){
		if (supplierOffers == null)
			return;
		Message kqml = Util.buildKQML(TAC_Ontology.sendSupplierOffers, className,
				SupplierOffer.listToString(supplierOffers));
		server.send(kqml.toString());
		suplView.append("\n#SupplierOffersToAgents: " + supplierOffers.size());
		supplierOffers.clear();
	}

	protected List<AgentRequest> getAgentRequests(String className){
		String content = "";
		Message kqml = Util.buildKQML(TAC_Ontology.getAgentRFQs, className,
				content);
		String resp = server.send(kqml.toString());
		Message response = Message.buildMessage(resp);
		List<AgentRequest> agentRequests = AgentRequest.stringToList(response.getContent());
		return agentRequests;
	}
	
	/**
	 * 
	 * @param agent the agent which we want to get the reputation of
	 * @return the reputation of agent 
	 */
	private double getReputation(String agent, int supplierID) {
		int qPurchased = 0;
		int qOffered = 0;
		for(AgentOrder order : allAgentOrders){
			if(order.getCustomer().equals(agent) && order.getSupplierOffer().getAgentRequest().getSupplierId() == supplierID){
				qPurchased += order.getSupplierOffer().getQuantity();
			}
		}
		for(SupplierOffer offer : allSupplierOffers){
			if(offer.getReciever().equals(agent) && offer.getAgentRequest().getSupplierId()== supplierID){
				qOffered += offer.getQuantity();
			}
		}
		if(qOffered == 0) return 0;
		return qPurchased/qOffered;
	}
	
	
	/**
	 * A comparator for  sorting an AgentRequest List by Agents reputation.
	 */
	private Comparator<AgentRequest> agentReputationComparator = new Comparator<AgentRequest>() {

		@Override
		public int compare(AgentRequest a1, AgentRequest a2) {
			double a1Rep = getReputation(a1.getAgent(),a1.getSupplierId());
			double a2Rep = getReputation(a2.getAgent(),a2.getSupplierId());
			if(a1Rep < a2Rep)
				return 1;
			else 
				return -1;
			
		}
	
	
	};

}
