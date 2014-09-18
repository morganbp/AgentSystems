package no.agentsystems_dhom.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import TACSCMApp.SCM;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.TAC_Ontology;

public class SCM_C1 extends SCM_Customer {


	public SCM_C1(SCM _server) {
		server = _server;
		
		interval = server.getTime();
		custView = new GUI("SCM_C1");
		try {
			

			for (;;) {
				
				if (server.status() && !getStatus()) {
					startTheGame();
				}else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				

				int time = interval % TAC_Ontology.lengthOfADay;

				if (time == 0 && getStatus()) {
					int day = interval / TAC_Ontology.lengthOfADay;	
					custView.append("\nday: " + day);
					//Send RFQs to server
				}
				interval++;

				Thread.sleep(TAC_Ontology.sec);
			}
		} catch (InterruptedException e) {

		}
	}

	public static void main(String[] args) {
		SCM ser = initSCMImpl(args);
		new SCM_C1(ser);
	}
	
	
	

}
