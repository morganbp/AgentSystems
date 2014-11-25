package no.agentsystems_dhom.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.agentsystems_dhom.agent.Assembly;
import no.agentsystems_dhom.agent.Inventory;
import no.agentsystems_dhom.customer.PC;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import yinyang.Message;
import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;
import TACSCMApp.SCMPOA;

public class SCM_Server extends Thread {
	// private variables

	private GUI serverView;

	// RFQ list to keep track of todays RFQs
	private List<RFQ> TodaysRFQs;

	// gameId is the date and time for a game round

	private String gameId;

	// date format

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// the clock count in seconds

	private int interval = -30;

	private int day = 0;

	private Date startTime, endTime;

	private boolean isOn = false;

	// an agent list

	private ArrayList<Agent> agentList = new ArrayList<Agent>();

	// Bank

	private Bank bank;

	// Storage cost per year

	private double storageCost = 0;

	private double interestRate = 0;

	private double loanInterestRate = 0;

	// Store the offers from agents

	private List<Offer> agentOffers;

	// Store the orders from custommer

	private List<Order> customerOrders;

	
	private List<Order> todaysCustomerOrders;
	// Store AgentRequest from Agent

	private List<AgentRequest> agentRequests;

	// Store SupplierOffer from Supplier

	private List<SupplierOffer> supplierOffers;

	// Store AgentOrder from agent

	private List<AgentOrder> agentOrders;

	// The product schedule which tells the assembly what to build

	private List<Order> todaysProductSchedule;

	// The delivery schedule which sends PCs to Customer

	private List<Order> todaysDeliverySchedule;

	// Store supplier components(in the form of AgentOrders) from supplier

	private List<AgentOrder> supplierComponents;
	
	//Store daily finished pcs
	private List<Order> finishedOrders;
	
	//Store all text in server gui to variable
	
	private String guiTextResult = "";

	public SCM_Server() {
		serverView = new GUI("SCM_server");
		bank = new Bank();
	}

	public static void main(String[] args) {
		try {
			SCM_Server server = new SCM_Server();
			server.start();

			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get reference to root poa & activate the POAManager
			POA rootpoa = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();

			// create servant and register it with the ORB
			TACSCMImpl SCMImpl = new TACSCMImpl(orb, server);

			// get object reference from the servant
			org.omg.CORBA.Object ref = rootpoa.servant_to_reference(SCMImpl);
			SCM href = SCMHelper.narrow(ref);

			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");

			// Use NamingContextExt which is part of the network-operable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// bind the Object Reference in Naming
			String name = "TACSCM";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, href);
			System.out.println("TACServer starting ...");

			// wait for invocations from clients
			orb.run();

		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}

		System.out.println("TACServer Exiting ...");
	}

	@Override
	public void run() {
		for (;;) {

			try {

				if (interval < 0) {
					serverView.setText("Server is waiting ... ");
				} else if (interval == 0
						|| interval == TAC_Ontology.gameInterval) {

					startTheGame(); // start the game at a specific time

				} else if (interval == TAC_Ontology.gameLength) {

					endTheGame(); // close the game

				}

				// compute the day

				day = (int) (interval / TAC_Ontology.lengthOfADay);

				// compute the time

				int time = interval % TAC_Ontology.lengthOfADay;

				if (time == 0 && isOn) {
					writeToGUI("\nday: " + day);
					
					//Clearing lists
					supplierOffers.clear();
					agentOffers.clear();
					agentOrders.clear();
					TodaysRFQs.clear();
					agentRequests.clear();
					///////////////////////////////
					///////////////////////////////
					performProductSchedule();
					dealSupplierBill();
					supplierComponents.clear();
					
					/*for(BankAccount ba : bank.getBankAccounts()){
						serverView.append("\n" + ba.getAgent().getName()+ ": " + ba.getBalance());
					}*/
					
				}
				
				if (time == 7 && isOn) {
					processDeliverySchedule();
				}
				if (time == 8 && isOn) {
					controlOrders();
				}

				if (time == 9 && isOn) {
					processStorage();
					updateBalance();
					writeToGUI("\nAggregate customer orders: " + customerOrders.size());
					if(day == 9) {
						printAgentBalance();
						announceAWinningAgent();
					}
					else
						printAgentBalance();
				}

				interval++;

				sleep(TAC_Ontology.sec);

			}

			catch (InterruptedException e) {
				e.printStackTrace();
			}

		} // end while
	}


	private void processDeliverySchedule() {
		// get information from deliverySchedule of each agent
		
		Map<String, Integer> numberOfOrders = new HashMap<String, Integer>();
		for(Agent a : agentList){
			numberOfOrders.put(a.getName(), 0);
		}
		
		for (Order order : todaysDeliverySchedule) {

			// find the agent that receivers that delivery order
			System.out.println("process");
			Agent a = find(order.getProvider());
			
			
			// count the number of delivery order of each agent

			if(delivery(a, order)){
				int newValue = numberOfOrders.get(a.getName()) + 1;
				numberOfOrders.put(a.getName(), newValue);
			}
			
			
		}

		for(Agent a : agentList){
			writeToGUI("\n"+ a.getName() + " delivers for " + numberOfOrders.get(a.getName()) + " orders");
		}
		todaysDeliverySchedule.clear();
	}

	// process delivery of an order from agent

	private boolean delivery(Agent agent, Order order) {

		// get PC from the agent's inventory
		
		// update the number of PC in the agent's inventory
		
		System.out.println("delivery");
		
		int sku = order.getOffer().getRFQ().getPC();
		int quantity = order.getOffer().getRFQ().getQuantity();
		
		Inventory inventory = agent.getInventory();
		if(!inventory.isEnoughPCs(sku, quantity))
			return false;
		
		inventory.updateNumberOfPcs(sku, -quantity); 
		
		
		
		// get price

		double price = order.getPrice();

		// the agent get pay

		BankAccount ba = bank.getBankAccount(agent);

		ba.addCredit(price * quantity);
		finishedOrders.add(order);
		return true;

	}

	
	private void controlOrders()
	{
		List<Order> ordersToRemove = new ArrayList<Order>();
		for(Order order : customerOrders)
		{
			if((day - order.getDueDate()) >= 0 || (day - order.getDueDate()) <= 4)
			{
				Agent agent = this.findAgent(order.getProvider());
				
				bank.getBankAccount(agent).addDebit(order.getPenalty());
			}
			if((day - order.getDueDate()) == 4)
			{
				ordersToRemove.add(order);
			}
		}
		customerOrders.removeAll(ordersToRemove);
	}

	private void processStorage() {
		for (int i = 0; i < agentList.size(); i++) {
			Agent agent = agentList.get(i);
			int[] numberOfPCs = agent.getInventory().getNumberOfPCs();

			bank.getBankAccount(agent).addDebit(-storageCost);

			writeToGUI("\nAgent" + i + " number of PCs: "
					+ numberOfPCs.length);
		}
	}

	private synchronized void dealSupplierBill() {
		for (AgentOrder agentOrder : supplierComponents) {
			// Find agent with this order
			Agent agent = findAgent(agentOrder.getCustomer());
			int componentId = agentOrder.getSupplierOffer().getAgentRequest()
					.getComponentId();
			int quantity = agentOrder.getSupplierOffer().getQuantity();
			agent.getInventory().updateQuantity(componentId, quantity);
			double amount = agentOrder.getPrice();
			bank.getBankAccount(agent).addDebit(amount * quantity);
		}
	}

	private Agent findAgent(String agentName) {
		for (Agent agent : agentList) {
			if (agent.getName().toLowerCase().trim().equals(agentName.toLowerCase()
					.trim())) {
				return agent;
			}
		}
		return null;
	}

	private void startTheGame() {

		Date st = new Date();

		gameId = df.format(st);

		startTime = st;

		endTime = tacTime(TAC_Ontology.gameLength * TAC_Ontology.sec);

		isOn = true;

		interval = 0;

		day = 0;

		tacStatus();

		storageCost();

		// reset agent

		agentList = new ArrayList<Agent>();

		// reset bank

		bank = new Bank();

		// reset interest rate

		interestRate = Util.random(TAC_Ontology.depositRateMin,
				TAC_Ontology.depositRateMax) * 0.01;

		bank.setInterestRate(interestRate);

		// reset loan interest rate

		loanInterestRate = 2 * interestRate;

		bank.setLoanInterestRate(loanInterestRate);

		agentOffers = new ArrayList<Offer>();

		TodaysRFQs = new ArrayList<RFQ>();

		customerOrders = new ArrayList<Order>();
		
		todaysCustomerOrders = new ArrayList<Order>();

		agentRequests = new ArrayList<AgentRequest>();

		supplierOffers = new ArrayList<SupplierOffer>();

		agentOrders = new ArrayList<AgentOrder>();

		supplierComponents = new ArrayList<AgentOrder>();

		todaysProductSchedule = new ArrayList<Order>();

		todaysDeliverySchedule = new ArrayList<Order>();
		
		finishedOrders = new ArrayList<Order>();
	}

	// end the game

	private void endTheGame() {

		isOn = false;
		
		writeToGUI("\n\n---> The TAC Game is closed");
		writeToGUI("\n Next Game: "
				+ df.format(tacTime(TAC_Ontology.gameInterval
						* TAC_Ontology.sec)));

		saveServerGuiToText();
	}

	public void finalize() throws Throwable {

	}

	// compute storage cost

	public void storageCost() {

		storageCost = Util.random(TAC_Ontology.SCmin, TAC_Ontology.SCmax) * 0.01;

		storageCost /= (TAC_Ontology.numberOfTacDays);

	}

	public boolean getStatus() {
		return isOn;
	}

	public int getTime() {
		return interval;
	}

	// check if an agent is registered in the agent list

	private boolean agentRegistered(String name, ArrayList<Agent> list) {

		if (list.isEmpty())
			return false;

		for (Agent a : list)

			if (a.getName().equals(name))
				return true;

		return false;

	}

	public double getInterestRate() {
		return interestRate;
	}

	public double getLoanInterestRate() {
		return loanInterestRate;
	}

	private void tacStatus() {

		writeToGUI("\n------------------ The TAC Game --------------------"
					+ "\nThe Game: " + gameId
					+ "\n Start: " + df.format(startTime)
					+ "\n End: " + df.format(endTime)
					+ "\n Next Game: "
							+ df.format(tacTime(TAC_Ontology.gameInterval
									* TAC_Ontology.sec))
					+ "\n----------------------------------------------------\n");

	}

	// compute the time from now after a long interval i

	private Date tacTime(long i) {

		Date d = new Date();

		long t = startTime.getTime();

		t += i;

		d.setTime(t);

		return d;

	}

	public double getStorageCosts() {
		return storageCost;
	}

	// agents register

	public synchronized Message agentRegistering(Message kqml) {

		Message resp = new Message();

		resp.setReceiver(kqml.getSender());

		String name = kqml.getSender();

		int id = Integer.parseInt(kqml.getContent());

		Agent a = new Agent(name, id);

		if (agentRegistered(name, agentList))

			name += " is already registered!";

		else {

			agentList.add(a);

			bank.getBankAccounts().add(new BankAccount(a));

			name += " is registered! Your id is " + id;

		}

		resp.setContent("(" + name + ")");

		writeToGUI("\n" + name);

		return resp;

	}

	// Run this when customer RFQs have arrived
	public synchronized Message customer_RFQs(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String stringRFQs = kqml.getContent();
		List<RFQ> RFQs = RFQ.stringToList(stringRFQs);
		// Saving the RFQs to the server's RFQ list
		TodaysRFQs = RFQs;
		writeToGUI("\nRFQs from " + name + ": " + RFQs.size());

		resp.setContent(TodaysRFQs.size() + "");
		return resp;
	}

	// Run this when agent requests RFQs
	public synchronized Message getCustomer_RFQs(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		resp.setContent(RFQ.listToString(TodaysRFQs));

		return resp;

	}

	// Run this when we receive Offers from Agent
	public synchronized Message agentOffers(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String messageContent = kqml.getContent();
		List<Offer> offers = Offer.stringToList(messageContent);
		agentOffers.addAll(offers);
		writeToGUI("\n" + name + " has sent the server " + offers.size()
				+ " offers.");

		resp.setContent("" + offers.size());
		return resp;
	}

	// The customer gets all the offers from agents
	public synchronized Message getAgentOffers(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String agentOffersStr = Offer.listToString(agentOffers);
		resp.setContent(agentOffersStr);
		return resp;
	}

	// the customer sends orders to agents
	public synchronized Message customerOrders(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String messageContent = kqml.getContent();
		List<Order> orders = Order.stringToList(messageContent);
		customerOrders.addAll(orders);
		todaysCustomerOrders.addAll(orders);
		writeToGUI("\n" + name + " sends orders: " + orders.size());

		resp.setContent(orders.size() + "");
		return resp;
	}

	// The agent gets the orders from customers
	public synchronized Message getCustomerOrders(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		List<Order> ordersToSender = new ArrayList<Order>();
		// Add all orders which are intended for the Sender
		// (or the one calling this method)
		for (Order o : todaysCustomerOrders) {
			if (o.getProvider().equals(name))
				ordersToSender.add(o);
		}
		resp.setReceiver(name);
		String customerOrderStr = Order.listToString(ordersToSender);
		resp.setContent(customerOrderStr);
		return resp;
	}

	public synchronized Message agentRFQs(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String content = kqml.getContent();
		List<AgentRequest> newAgentReq = AgentRequest.stringToList(content);
		agentRequests.addAll(newAgentReq);
		resp.setContent(newAgentReq.size() + "");
		return resp;
	}

	public synchronized Message getAgentRFQs(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String strAgentReq = AgentRequest.listToString(agentRequests);
		resp.setContent(strAgentReq);
		return resp;
	}

	public synchronized Message sendSupplierOffers(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String content = kqml.getContent();
		List<SupplierOffer> newSupplierOffers = SupplierOffer
				.stringToList(content);
		supplierOffers.addAll(newSupplierOffers);
		resp.setContent(newSupplierOffers.size() + "");
		writeToGUI("\nOffers from Supplier: " + supplierOffers.size());

		return resp;
	}

	public synchronized Message getSupplierOffers(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		List<SupplierOffer> offersToReceiver = new ArrayList<SupplierOffer>();
		for (SupplierOffer supOff : supplierOffers) {
			if (supOff.getReciever().equals(name)) {
				offersToReceiver.add(supOff);
			}
		}
		String strSupplierOffers = SupplierOffer.listToString(offersToReceiver);
		resp.setContent(strSupplierOffers);
		return resp;
	}

	public synchronized Message sendAgentOrders(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String content = kqml.getContent();
		List<AgentOrder> newAgentOrders = AgentOrder.stringToList(content);
		agentOrders.addAll(newAgentOrders);
		resp.setContent(newAgentOrders.size() + "");
		return resp;
	}

	public synchronized Message getAgentOrders(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String strAgentOrd = AgentOrder.listToString(agentOrders);
		resp.setContent(strAgentOrd);
		return resp;
	}

	public synchronized Message sendSupplierComponents(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
	
		resp.setReceiver(name);
		String messageContent = kqml.getContent();
		List<AgentOrder> components = AgentOrder.stringToList(messageContent);
		supplierComponents.addAll(components);
		resp.setContent(components.size() + "");
		writeToGUI("\n" + name + " has sent the server "
				+ components.size() + " components.");

		return resp;
	}

	/*
	 * public synchronized Message getSupplierComponents(Message kqml) { Message
	 * resp = new Message(); String name = kqml.getSender();
	 * resp.setReceiver(name); String strSupplierComponents = AgentOrder
	 * .listToString(supplierComponents);
	 * resp.setContent(strSupplierComponents); return resp; }
	 */

	public synchronized Message productSchedule(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String messageContent = kqml.getContent();
		List<Order> productSchedule = Order.stringToList(messageContent);
		todaysProductSchedule.addAll(productSchedule);
		return resp;
	}

	public synchronized Message deliverySchedule(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		String messageContent = kqml.getContent();
		List<Order> deliverySchedule = Order.stringToList(messageContent);
		todaysDeliverySchedule.addAll(deliverySchedule);
		return resp;
	}
	
	public Message sendFinishedOrders(Message kqml) {
		Message resp = new Message();
		String name = kqml.getSender();
		resp.setReceiver(name);
		resp.setContent(Order.listToString(finishedOrders));
		finishedOrders.clear();
		return resp;
	}

	/**
	 * Send the a list over products to create to the assembly
	 */
	private synchronized void performProductSchedule() {
		if (todaysProductSchedule.size() == 0)
			return;

		for (Order order : todaysProductSchedule) {

			Agent agent = find(order.getProvider());

			int sku = order.getOffer().getRFQ().getPC();

			int quantity = order.getOffer().getRFQ().getQuantity();

			makePC(agent, sku, quantity);

		}

		todaysProductSchedule.clear();
	}

	/**
	 * 
	 * @param agent
	 *            the agent which makes the PC
	 * @param sku
	 *            the SKU number of the PC
	 * @param quantity
	 *            how many PCs which gonna be made.
	 */
	private void makePC(Agent agent, int sku, int quantity) {
		// Assembly of the agent a

		Assembly assembly = agent.getAssembly();

		// Inventory of the agent a

		Inventory inventory = agent.getInventory();

		// if there is not enough assembly cycles - do nothing
		if (!assembly.isCapacityAvailable(sku, quantity))
			return;

		// if there is not enough components - do nothing
		if (!inventory.isEnoughComponents(sku, quantity))
			return;

		// make q PCs

		// update the components that are used to build PCs in agent's inventory

		PC pc = new PC(sku);
		for (int cid : pc.getComponents()) {
			inventory.updateQuantity(cid, quantity);
		}

		// update the number of PCs in inventory

		inventory.updateNumberOfPcs(sku, quantity);

		// update assembly i.e. reduce the number of cycles

		assembly.updateCycle(sku, quantity);
	}

	/**
	 * 
	 * @param provider
	 *            the string id to the agent we want to find
	 * @return an agent with string id equal to parameter
	 */
	private Agent find(String provider) {
		for (Agent a : agentList) {
			if (a.getName().equals(provider)) {
				return a;
			}
		}
		return null;
	}
	
	//Method for writing to gui and filevariable
	private void writeToGUI(String str){
		serverView.append(str);
		guiTextResult += str;
	}
	
	private void announceAWinningAgent(){
		double winningBalance = 0;
		double currentAgentBalance;
		Agent winningAgent = null;
		for(Agent a : agentList){
			currentAgentBalance = getBankBalance(a);
				if(winningBalance == 0){
					winningBalance = currentAgentBalance;
					winningAgent = a;
				}
				if(currentAgentBalance > winningBalance){
					winningBalance = getBankBalance(a);
					winningAgent = a;
				}
				
			}
			writeToGUI("\n" + winningAgent.getName() + " is the winner!");
		}
	
	
	private void printAgentBalance(){
		double currentAgentsBankBalance = 0;
		for(Agent a : agentList){
			String agent = a.toString();
			currentAgentsBankBalance = getBankBalance(a);
			if(currentAgentsBankBalance != 0)
				writeToGUI("\n" + a.getName() + "s balance is: "+ getBankBalance(a));
		}
	}

	// get bank account balance
	private double getBankBalance(Agent a) {
		return bank.getBankAccount(a).getBalance();
	}

	// update balance of all bank account at the end of the day

	public void updateBalance() {

		for (Agent a : agentList) {

			BankAccount ba = bank.getBankAccount(a);

			ba.updateBalance(loanInterestRate, interestRate);

			getBankBalance(a);

		}

	}

	public void saveServerGuiToText() {
		File outputFile = new File("C:/Users/David/agent5/agentReport_.txt");
		//String guiContent = serverView.output.getText();
		PrintWriter out = null;
		try {
			out = new PrintWriter(outputFile);
			serverView.append("\nLog saved to file: " + outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			serverView.append("\nCould not save to file");
		}
		out.print(guiTextResult);
		//out.println("\n");
		out.close();
	}

} // end of SCM_Server

class TACSCMImpl extends SCMPOA {

	ORB orb = null;

	SCM_Server server = null;

	public TACSCMImpl(ORB o, SCM_Server ser) {

		orb = o;

		server = ser;

	}

	// implement send() method

	public String send(String str) {

		if (str == null)
			return null;

		Message kqml = Message.buildMessage(str);

		if (kqml == null)
			return null;

		String performative = kqml.getPerformative();

		// agent registering

		if (performative.equals(TAC_Ontology.agent_registering)) {

			Message resp = server.agentRegistering(kqml);

			if (resp != null)
				return resp.toString();

		}

		// server has received customer RFQs
		if (performative.equals(TAC_Ontology.Customer_RFQs)) {
			Message resp = server.customer_RFQs(kqml);
			if (resp != null)
				return resp.toString();
		}

		// server has received customer RFQs
		if (performative.equals(TAC_Ontology.getCustomer_RFQs)) {
			Message resp = server.getCustomer_RFQs(kqml);
			if (resp != null)
				return resp.toString();
		}

		// server has received offers from
		if (performative.equals(TAC_Ontology.agentOffers)) {
			Message resp = server.agentOffers(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}
		// Customer wants to get all the agent offers
		if (performative.equals(TAC_Ontology.getAgentOffers)) {
			Message resp = server.getAgentOffers(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		// Agent gets the orders from customers
		if (performative.equals(TAC_Ontology.getCustomerOrders)) {
			Message resp = server.getCustomerOrders(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		// Customer send orders to agent
		if (performative.equals(TAC_Ontology.customerOrders)) {
			Message resp = server.customerOrders(kqml);
			if (resp != null) {
				return resp.toString();
			}

		}

		if (performative.equals(TAC_Ontology.Agent_RFQs)) {
			Message resp = server.agentRFQs(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		if (performative.equals(TAC_Ontology.getAgentRFQs)) {
			Message resp = server.getAgentRFQs(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		if (performative.equals(TAC_Ontology.sendSupplierOffers)) {
			Message resp = server.sendSupplierOffers(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		if (performative.equals(TAC_Ontology.getSupplierOffers)) {
			Message resp = server.getSupplierOffers(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		if (performative.equals(TAC_Ontology.getAgentOrders)) {
			Message resp = server.getAgentOrders(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		if (performative.equals(TAC_Ontology.sendAgentOrders)) {
			Message resp = server.sendAgentOrders(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		if (performative.equals(TAC_Ontology.supplierSendComponents)) {
			Message resp = server.sendSupplierComponents(kqml);
			if (resp != null) {
				return resp.toString();
			}

		}

		if (performative.equals(TAC_Ontology.productSchedule)) {
			Message resp = server.productSchedule(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}

		if (performative.equals(TAC_Ontology.deliverySchedule)) {
			Message resp = server.deliverySchedule(kqml);
			if (resp != null) {
				return resp.toString();
			}
		}
		
		if(performative.equals(TAC_Ontology.finishedOrders)) {
			Message resp = server.sendFinishedOrders(kqml);
			if(resp != null) {
				return resp.toString();
			}
		}

		return null;

	}

	// get status

	public boolean status() {

		return server.getStatus();

	}

	// getTime interval in second

	public short getTime() {

		return (short) server.getTime();

	}

	// get interest rate from the bank

	public double getInterestRate() {

		return server.getInterestRate();

	}

	// get interest rate from the bank

	public double getLoanInterestRate() {

		return server.getLoanInterestRate();

	}

	// get interest rate from the bank

	public double getStorageCost() {

		return server.getStorageCosts();

	}

}
