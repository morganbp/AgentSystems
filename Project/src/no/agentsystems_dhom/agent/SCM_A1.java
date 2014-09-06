package no.agentsystems_dhom.agent;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

public class SCM_A1 extends SCM_Agent{
	public SCM_A1(){
		String guiField = "";
		interval = scmImpl.getTime();
		// skal kalle denne, men hva skal inni?
		//scmImpl.send("");
		if(interval > 0 && scmImpl.status()){
			setStatus(true);
		}
		agentView = new GUI("SCM_A1");
		try{
			for(;;){
				agentView.setText("---> Time : " + interval + " seconds ");
				
				if(scmImpl.status() && !getStatus()){
					startTheGame();
				}
				else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				if(time == 0 && getStatus()){
					int day = interval / TAC_Ontology.lengthOfADay;
					guiField += ("\nday : " + day);
				}
				agentView.append(guiField);
				interval++;
				
				Thread.sleep(TAC_Ontology.sec);
			}
		}
		catch(InterruptedException e){
			
		}
		
	}

	public static void main(String[] args){
		initSCMImpl(args);
		new SCM_A1();
	}

}
