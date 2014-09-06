package no.agentsystems_dhom.customer;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

public class SCM_C1 extends SCM_Customer {


	public SCM_C1() {
		interval = scmImpl.getTime();
		custView = new GUI("SCM_C1");
		try {
			custView.setText("SCM_C1 is connected!");

			for (;;) {

				if (scmImpl.status() && !getStatus()) {
					startTheGame();
				}else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				

				int time = interval % TAC_Ontology.lengthOfADay;

				if (time == 0 && getStatus()) {
					int day = interval / TAC_Ontology.lengthOfADay;
					custView.append("\nday : " + day);
				}

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
