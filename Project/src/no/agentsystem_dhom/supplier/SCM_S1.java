package no.agentsystem_dhom.supplier;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;
import TACSCMApp.SCM;

public class SCM_S1 extends SCM_Supplier {
	
	public SCM_S1(SCM scmImpl){
		interval = scmImpl.getTime();
		suplView = new GUI("SCM_S1");
		try{
			suplView.setText("SCM_S1 is connected");
			
			for(;;){
				
				if(scmImpl.status() && !getStatus()){
					startTheGame();
				}else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				
				if(time == 0 && getStatus()){
					int day = interval / TAC_Ontology.lengthOfADay;
					suplView.append("\nday: " + day);
				}
				
				interval++;
				
				Thread.sleep(TAC_Ontology.sec);
			}
		}catch(InterruptedException e){
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		SCM scmImpl = initSCMImpl(args);
		new SCM_S1(scmImpl);
	}
	
	
}
