package no.agentsystems_dhom.agent;

import java.util.List;

import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.TAC_Ontology;
import TACSCMApp.SCM;

public class SCM_A1 extends SCM_Agent{
	
	private static final String CLASSNAME = "SCM_A1";
	
	public SCM_A1(SCM _server){
		server = _server;

		agentView = new GUI(CLASSNAME);
		interval = server.getTime();
		
		try{
			for(;;){
				
				if(server.status() && !getStatus()){
					startTheGame();
					agentRegistering(CLASSNAME, 1512631);
				}
				else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				if(time == 0 && getStatus()){
					int day = interval / TAC_Ontology.lengthOfADay;
					agentView.append("\nday : " + day);
				}
				if(time == 1 && getStatus()){
					List<RFQ> rfqs = getRFQsFromServer(CLASSNAME);
					if(rfqs != null)
						agentView.append("\nNumber of RFQ: " + rfqs.size());
				
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
		new SCM_A1(ser);
	}

}
