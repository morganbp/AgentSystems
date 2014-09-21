package no.agentsystems_dhom.agent;

import java.util.List;

import TACSCMApp.SCM;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.TAC_Ontology;

public class SCM_A3 extends SCM_Agent{
	
	private static final String CLASSNAME = "SCM_A3";
	
	public SCM_A3(SCM _server){
		server = _server;

		agentView = new GUI("SCM_A3");
		interval = server.getTime();
		
		try{
			for(;;){
				
				
				if(server.status() && !getStatus()){
					startTheGame();
					agentRegistering("SCM_A3", 1512633);
				}
				else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				if(time == 0 && getStatus()){
					int day = interval / TAC_Ontology.lengthOfADay;
					agentView.append("\nday : " + day);
				}
				if(time == 2 && getStatus()){
					List<RFQ> RFQList = getRFQsFromServer(CLASSNAME);
					if(RFQList != null){
						agentView.append("\nNumber of RFQ: " +RFQList.size());
						for(RFQ rfq : RFQList){
							createOffer(CLASSNAME, Integer.toString(rfq.getRFQId()),rfq.getReservePrice(), rfq);
						}
						sendOffersToServer(CLASSNAME);
					}
				}
				interval++;
				
				Thread.sleep(TAC_Ontology.sec);
			}
		}
		catch(InterruptedException e){
			
		}
		
	}

	public static void main(String[] args){
		SCM ser = initServer(args);
		new SCM_A3(ser);
	}

}
