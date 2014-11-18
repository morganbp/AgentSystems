/*******************************************************************
   TAC_Ontology.java         
   In Text : the ontology of the supply chain management game defined as static variable

   Authors:  Ky van Ha

   Classes:  TAC_Ontology

   Date: 16.01.2013
*******************************************************************/

package no.agentsystems_dhom.server;

public class TAC_Ontology {
	// Ontology name
	public static String ontology = "TAC_SCM_Ontology";

    // agent name
    public static String SCM_A1 = "SCM_A1"; 
    public static String SCM_A2 = "SCM_A2"; 
    public static String SCM_A3 = "SCM_A3"; 
    public static String SCM_A4 = "SCM_A4"; 
    public static String SCM_A5 = "SCM_A5"; 
    public static String SCM_A6 = "SCM_A6"; 
    
    // RFQ
    public static String RFQ = "Request For Quotes";
    // server
    public static String server = "SCM_Server";
    // customer
    public static String C1 = "SCM_C1";   
    // supplier
    public static String S1 = "SCM_S1";        
 
    // separator used to separate RFQs in a bundle
    public static String separator = "/";        
    
	// the second = 1000 millisecond
	public static int sec = 1000;  
	// the minute = 60 seconds
	public static int min = 60*sec;
    // numbers of TAC days
//  public static int numberOfTacDays = 220; 
	public static int numberOfTacDays = 2; 
    // The length of a TAC day in second
//  public static int lengthOfADay = 15; 
  public static int lengthOfADay = 10; 
    // the game length in seconds
	public static int gameLength= numberOfTacDays*lengthOfADay;
    // the game interval in seconds
	public static int gameInterval= gameLength + 10;	
	
	// High, Mid, Low range
	public static int high = 0;
	public static int mid = 1;
	public static int low = 2;
	
	// maximum of PC type 16
	public static int maxPCType = 16;
	// Nominal capacity of all suppliers assembly lines per day
//	public static int cNominal = 550;
	public static int cNominal = 450;
	// Number of Agent cell capacity per day
	public static int agentCellCapacity = 2000;
	
	// Acceptable purchase ratio for single-source suppliers
	public static double apr1 = 0.75;
	// Acceptable purchase ratio for two-source suppliers
	public static double apr2 = 0.45;
	
	// average  number of customer RFQs [RFQmin, RFQmax] in High and Low range
	public static int HLRFQmin = 25;
	public static int HLRFQmax = 100;
	// average  number of customer RFQs [RFQmin, RFQmax] in Mid range
	public static int MRFQmin = 30;
	public static int MRFQmax = 120;
	// RFQ trend for customer [Tmin, Tmax] (all customer segments)
	public static double Tmin = 0.95;
	public static double Tmax = 1/0.95;
	// Range of quantities for customer RFQ [Qmin, Qmax]
	public static int Qmin = 1;
	public static int Qmax = 20;
	//Range of due date for customer RFQ [Dmin, Dmax]
	public static int Dmin = 5;
	public static int Dmax = 12;
	//Range of penalties [Pmin, Pmax] in percent per day
	public static int Pmin = 5;
	public static int Pmax = 15;
	
	//Customer Reserver Price [PCpmin, PCpmax] in percent of base price of PC components
	public static int PCpmin = 75;
	public static int PCpmax = 125;
	
	//Component storage cost [SCmin, SCmax] in percent of base price of components
	public static int SCmin = 25;
	public static int SCmax = 50;

	//Bank debt interest rate in percent
	public static int debtRateMin = 6;
	public static int debtRateMax = 12;
	
	//Bank deposit interest rate in percent
	public static int depositRateMin = 3;
	public static int depositRateMax = 6;
	
	// Short-term horizon for supplier commitment = 20 days 
	//public static int TShort = 20;
	public static int TShort = 5;
	// Daily reduction in supplier available capacity for long-term commitments 0.5%
	public static double zR = 0.5/100;

	// Performative
	
    // agent registering. Sender: agent's name, Content: agent'id
    public static String agent_registering = "agent_registering"; 
    // customer registering
    public static String customer_registering = "customer_registering"; 
    // supplier registering
    public static String supplier_registering = "supplier_registering";
	
    // Customers send RFQs to the server
    public static String Customer_RFQs = "Customer_RFQs";
    // Agents get customer RFQs from the server
    public static String getCustomer_RFQs = "getCustomer_RFQs";
    
    // Agents send offers to customers
    public static String agentOffers = "agentOffers";
    // Customers get offers from agents
    public static String getAgentOffers = "getAgentOffers";
    
    // Customers send orders to agents
    public static String customerOrders = "customerOrders";
    // Agents get orders from customers
    // sender: agent name, content: agent id
    public static String getCustomerOrders = "getCustomerOrders";

    // Agents get own bank account balance
    // sender: agent name, content: agent id
    public static String getAgentBankAccountBalance = "getAgentBankAccountBalance";
    
    // Agent sends RFQs to Supplier
    public static String Agent_RFQs = "Agent_RFQs";
    // Supplier get RFQs from agents
    public static String getAgentRFQs = "getAgentRFQs";
    // Supplier sends offers to agents
    public static String sendSupplierOffers = "sendSupplierOffers";
    // Agents get Offers from Supplier
    // sender: agent name, content: agent id
    public static String getSupplierOffers = "getSupplierOffer";
    // agents send order to supplier
    public static String sendAgentOrders = "sendAgentOrders";
    // supplier get agent orders
    public static String getAgentOrders = "getAgentOrders";
    // supplier sends bill and components to agents
    public static String supplierSendComponents = "supplierSendComponents";
    // agent wants to get components from server
    public static String getSupplierComponents = "getSupplierComponents";
    // agents send product schedule to Assembly
    public static String productSchedule = "productSchedule";
    // agents send delivery schedule to Inventory
    public static String deliverySchedule = "deliverySchedule";  
}
