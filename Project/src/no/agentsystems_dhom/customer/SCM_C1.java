package no.agentsystems_dhom.customer;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

public class SCM_C1 extends SCM_Customer {


	public SCM_C1() {
		String guiField = ""; 
		interval = scmImpl.getTime();
		if(interval > 0 && scmImpl.status()){
			setStatus(true);
		}
		custView = new GUI("SCM_C1");
		try {
			

			for (;;) {
				custView.setText("---> Time : " + interval + " seconds ");
				
				if (scmImpl.status() && !getStatus()) {
					startTheGame();
				}else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				

				int time = interval % TAC_Ontology.lengthOfADay;

				if (time == 0 && getStatus()) {
					int day = interval / TAC_Ontology.lengthOfADay;
					guiField += "\nday : " + day;	
				}
				custView.append(guiField);
				interval++;

				Thread.sleep(TAC_Ontology.sec);
			}
		} catch (InterruptedException e) {

		}
	}

	public static void main(String[] args) {
		initSCMImpl(args);
		new SCM_C1();
	}

}
