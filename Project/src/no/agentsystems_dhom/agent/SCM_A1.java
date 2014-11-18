package no.agentsystems_dhom.agent;

import java.util.List;

import no.agentsystems_dhom.customer.PC;
import no.agentsystems_dhom.server.AgentRequest;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.Order;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.SupplierOffer;
import no.agentsystems_dhom.server.TAC_Ontology;
import TACSCMApp.SCM;

public class SCM_A1 extends SCM_Agent{
	
	private static final String CLASS_NAME = "SCM_A1";
	
	public SCM_A1(SCM _server){
		server = _server;

		agentView = new GUI(CLASS_NAME);
		interval = server.getTime();
		
		try{
			for(;;){
				
				if(server.status() && !getStatus()){
					startTheGame();
					agentRegistering(CLASS_NAME, 1512631);
				}
				else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				int day = interval / TAC_Ontology.lengthOfADay;
				if(time == 0 && getStatus()){
					agentView.append("\nday : " + day);
				}
				
				if(time == 2 && getStatus()){
					// Get orders from the server, 
					// and store them.
					List<Order> newOrders = getOrderFromServer(CLASS_NAME);
					activeOrders.addAll(newOrders);
					computeRequirements(newOrders);
				}
				
				if(time == 3 && getStatus()){
					// Get supplier offers
					List<SupplierOffer> supplierOffers = getSupplierOffers(CLASS_NAME);
					for(SupplierOffer supplierOffer : supplierOffers){
						createAgentOrder(supplierOffer);
					}
				}
				
				if(time == 4 && getStatus()){
					// send supplier orders
					sendSupplierOrders(CLASS_NAME);
				}
				
				if(time == 5 && getStatus()){
					// Make agentRFQS
					List<AgentRequest> agentRequests = makeAgentRFQs(CLASS_NAME, interval/TAC_Ontology.lengthOfADay, getOrderFromServer(CLASS_NAME));
					sendAgentRFQs(CLASS_NAME, agentRequests);
				}
				
				if(time == 6 && getStatus()){
					// Get RFQS From server,
					// bid and send offers
					// back to server
					List<RFQ> RFQList = getRFQsFromServer(CLASS_NAME);
					if(RFQList != null){
						for(RFQ rfq : RFQList){
							PC pc = new PC(rfq.getPC());
							if(rfq.getQuantity() < 10) continue;
							
							createOffer(CLASS_NAME, Integer.toString(rfq.getRFQId()), (double)rfq.getReservePrice(), rfq);
						}
					}
				}
				
				if(time == 7){
					sendOffersToServer(CLASS_NAME);
				}
				
				if(time == 8){
					deliverySchedule(CLASS_NAME);
				}
				
				if(time == 9){
					productSchedule(CLASS_NAME, day);
				}
				
				interval++;
				
				Thread.sleep(TAC_Ontology.sec);
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args){
		SCM ser = initServer(args);
		new SCM_A1(ser);
	}

}
