package no.agentsystems_dhom.supplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import no.agentsystems_dhom.server.AgentOrder;
import no.agentsystems_dhom.server.AgentRequest;
import no.agentsystems_dhom.server.GUI;
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

	public static SCM initServer(String[] args) {
		SCM server;
		try {
			// create and init the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			// Using NamingContextExt instead of NamingContext. This is
			// part of the Naming Service
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "TACSCM";
			server = SCMHelper.narrow(ncRef.resolve_str(name));

		} catch (Exception e) {
			System.out.println("/n/nERROR : " + e);
			e.printStackTrace();
			return null;
		}
		return server;
	}

	protected void startTheGame() {
		has_started = true;
		supplierOffers = new ArrayList<SupplierOffer>();
		activeAgentOrders = new ArrayList<AgentOrder>();
		allSupplierOffers = new ArrayList<SupplierOffer>();
		allAgentOrders = new ArrayList<AgentOrder>();
		initSuppliers();
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0
				: interval;
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

	protected void closeTheGame() {
		has_started = false;
		suplView.append("\n\n---> The TAC Game is closed");
	}

	protected boolean getStatus() {
		return has_started;
	}

	protected void setStatus(boolean status) {
		has_started = status;
	}

	protected void addSupplierComponents() {
		// loop through all components (2 for each supplier)
		// and add the new components
		for (int i = 0; i < suppliers.length * 2; i++) {
			int index = (int) Math.floor(i / 2);
			Component comp = suppliers[index].getComponents()[i % 2];
			comp.addComponents();
		}
	}
	
	protected void printInventory(){
		suplView.append("\n");
		for(Supplier s : suppliers){
			for(Component c : s.getComponents()){
				int qnty = c.getInventory();
				suplView.append(qnty + " ");
			}
		}
	}

	protected void createSupplierOffers(List<AgentRequest> agentRequests,
			String bidder, int day) {
		// Sort AgentRequests by decreasing reputation
		// Collections.sort(agentRequests, agentReputationComparator);
		for (AgentRequest agentRequest : agentRequests) {
			int sID = agentRequest.getSupplierId();
			int cID = agentRequest.getComponentId();
			int dueDate = agentRequest.getDueDate();
			if (dueDate <= day)
				continue;
			int quantity = agentRequest.getQuantity();
			Component prod = suppliers[sID].getProduct(cID);
			int n = prod.getExpectedCapacity(day, dueDate - 1);
			if (n >= quantity) {
				double price = prod.decidePriceOfComponent(agentRequest,
						dueDate - 1);

				createSupplierOffer(bidder, agentRequest.getAgent(), price,
						quantity, dueDate, agentRequest);
			} else {
				double price = prod.decidePriceOfComponent(agentRequest,
						dueDate - 1);
				// Make an offer with new quantity
				createSupplierOffer(bidder, agentRequest.getAgent(), price, n,
						dueDate, agentRequest);

				int nextDueDate = dueDate;
				while (nextDueDate < TAC_Ontology.numberOfTacDays) {
					int newN = prod.getExpectedCapacity(day, nextDueDate);
					if (newN >= quantity) {
						// Make an offer with new DueDate
						createSupplierOffer(bidder, agentRequest.getAgent(),
								price, quantity, nextDueDate, agentRequest);
					}
					nextDueDate++;
				}
			}
		}

	}

	protected void createSupplierOffer(String bidder, String reciever,
			double price, int quantity, int dueDate, AgentRequest aRequest) {
		SupplierOffer sup = new SupplierOffer(bidder, reciever, price,
				quantity, dueDate, aRequest);
		supplierOffers.add(sup);
		allSupplierOffers.add(sup);
	}

	protected void sendYesterdaysOffers(String className) {
		if (supplierOffers == null)
			return;
		Message kqml = Util.buildKQML(TAC_Ontology.sendSupplierOffers,
				className, SupplierOffer.listToString(supplierOffers));
		server.send(kqml.toString());
		
		suplView.append("\n#SupplierOffersToAgents: " + supplierOffers.size());
		supplierOffers.clear();
	}

	protected List<AgentRequest> getAgentRequests(String className) {
		String content = "";
		Message kqml = Util.buildKQML(TAC_Ontology.getAgentRFQs, className,
				content);
		String resp = server.send(kqml.toString());
		Message response = Message.buildMessage(resp);
		List<AgentRequest> agentRequests = AgentRequest.stringToList(response
				.getContent());
		return agentRequests;
	}

	/**
	 * Adds all agentorders for the specified supplier(className) to
	 * activeAgentOrders
	 * 
	 * @param className
	 *            The name of the supplier
	 */
	protected void getAgentOrders(String className) {
		List<AgentOrder> agentOrders = new ArrayList<AgentOrder>();
		Message msg = Util.buildKQML(TAC_Ontology.getAgentOrders, className,
				"");
		String resp = server.send(msg.toString());
		Message response = Message.buildMessage(resp);
		agentOrders = AgentOrder.stringToList(response.getContent());
		for(int i = 0; i < agentOrders.size(); i++){
			System.out.println(i + ": " + agentOrders.get(i));
		}
		this.activeAgentOrders.addAll(agentOrders);
	}

	/**
	 * Handles AgentOrders (activeOrders). E.g. sorts, and take out components
	 * from the order and update the inventory.
	 */
	protected void handleOrders(String className) {
		if (this.activeAgentOrders.size() <= 0)
			return;
		
		List<AgentOrder> componentBundle = new ArrayList<AgentOrder>();
		// Sorting by duedate
		Collections.sort(this.activeAgentOrders, agentOrderDueDateComparator);
		// Gets the first element in the sorted list
		int earliestDueDate = this.activeAgentOrders.get(0).getDueDate();
		for (AgentOrder order : this.activeAgentOrders) {
			// adding AgentOrders with the same duedate to the temp list
			if (order.getDueDate() == earliestDueDate) {
				componentBundle.add(order);
			}
		}
		//These are the orders we are going to process today(?)
		for (AgentOrder order : componentBundle) {
			this.activeAgentOrders.remove(order);
			int supplierId = order.getSupplierOffer().getAgentRequest()
					.getSupplierId();
			int componentId = order.getSupplierOffer().getAgentRequest()
					.getComponentId();
			int quantity = order.getSupplierOffer().getQuantity();
			Supplier supplier = this.suppliers[supplierId];
			Component chosenProduct =  supplier.getProduct(componentId);
			chosenProduct.updateInventory(-quantity);
		}
		Message kqml = Util.buildKQML(TAC_Ontology.supplierSendComponents, className, AgentOrder.listToString(componentBundle));
		server.send(kqml.toString());
	}

	/**
	 * 
	 * @param agent
	 *            the agent which we want to get the reputation of
	 * @return the reputation of agent
	 */
	private double getReputation(String agent, int supplierID) {
		int qPurchased = 0;
		int qOffered = 0;
		for (AgentOrder order : allAgentOrders) {
			if (order.getCustomer().equals(agent)
					&& order.getSupplierOffer().getAgentRequest()
							.getSupplierId() == supplierID) {
				qPurchased += order.getSupplierOffer().getQuantity();
			}
		}
		for (SupplierOffer offer : allSupplierOffers) {
			if (offer.getReciever().equals(agent)
					&& offer.getAgentRequest().getSupplierId() == supplierID) {
				qOffered += offer.getQuantity();
			}
		}
		if (qOffered == 0)
			return 0;
		return qPurchased / qOffered;
	}

	/**
	 * A comparator for sorting an AgentRequest List by Agents reputation.
	 */
	private Comparator<AgentRequest> agentReputationComparator = new Comparator<AgentRequest>() {

		@Override
		public int compare(AgentRequest a1, AgentRequest a2) {
			double a1Rep = getReputation(a1.getAgent(), a1.getSupplierId());
			double a2Rep = getReputation(a2.getAgent(), a2.getSupplierId());
			if (a1Rep < a2Rep)
				return 1;
			else
				return -1;

		}

	};

	private Comparator<AgentOrder> agentOrderDueDateComparator = new Comparator<AgentOrder>() {

		@Override
		public int compare(AgentOrder a1, AgentOrder a2) {
			int a1dueDate = a1.getDueDate();
			int a2dueDate = a2.getDueDate();
			if (a1dueDate < a2dueDate)
				return 1;
			else
				return -1;

		}

	};

}
