package no.agentsystems_dhom.customer;

import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.TAC_Ontology;
import TACSCMApp.SCM;

public class SCM_C1 extends SCM_Customer {

	private final static String CLASS_NAME = "SCM_C1";
	
	public SCM_C1(SCM _server) {
		server = _server;
		
		interval = server.getTime();
		custView = new GUI(CLASS_NAME);
		try {
			
			int day;
			for (;;) {
				
				if (server.status() && !getStatus()) {
					startTheGame();
				}else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				
				int time = interval % TAC_Ontology.lengthOfADay;
				day = interval / TAC_Ontology.lengthOfADay;	
				if (time == 0 && getStatus()) {
					custView.append("\nday: " + day);
				}
				if(time == 1 && getStatus()){
					sendDailyRFQs(day, CLASS_NAME);
				}
				interval++;

				Thread.sleep(TAC_Ontology.sec);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SCM ser = initSCMImpl(args);
		new SCM_C1(ser);
	}
	
	
	

}
