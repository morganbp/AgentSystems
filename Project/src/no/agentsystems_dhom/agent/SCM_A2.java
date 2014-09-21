package no.agentsystems_dhom.agent;

import java.util.List;

import TACSCMApp.SCM;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.TAC_Ontology;

public class SCM_A2 extends SCM_Agent{
	
	private static final String CLASS_NAME = "SCM_A2";
	
	public SCM_A2(SCM _server){
		server = _server;

		agentView = new GUI(CLASS_NAME);
		interval = server.getTime();
		
		try{
			for(;;){
				
				
				if(server.status() && !getStatus()){
					startTheGame();
					agentRegistering(CLASS_NAME, 1512632);
				}
				else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				if(time == 0 && getStatus()){
					int day = interval / TAC_Ontology.lengthOfADay;
					agentView.append("\nday : " + day);
				}
				
				if(time == 2 && getStatus())
				{
					//GET RFQs. Bid on them and send offers back to server.
					List<RFQ> RFQList = getRFQsFromServer(CLASS_NAME);
					if(RFQList != null)
					{
						for(RFQ rfq : RFQList)
						{
							createOffer(CLASS_NAME, Integer.toString(rfq.getRFQId()), (double)rfq.getReservePrice(), rfq);
						}
						sendOffersToServer(CLASS_NAME);
					}
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
		new SCM_A2(ser);
	}
}
