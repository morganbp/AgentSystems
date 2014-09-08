package no.agentsystems_dhom.agent;

import TACSCMApp.SCM;
import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

public class SCM_A2 extends SCM_Agent{
	
	public SCM_A2(SCM _server){
		server = _server;

		agentView = new GUI("SCM_A2");
		interval = server.getTime();
		
		try{
			for(;;){
				
				
				if(server.status() && !getStatus()){
					startTheGame();
					agentRegistering("SCM_A2", 1512632);
				}
				else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				if(time == 0 && getStatus()){
					int day = interval / TAC_Ontology.lengthOfADay;
					agentView.append("\nday : " + day);
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
		new SCM_A2(ser);
	}
}