package no.agentsystems_dhom.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;
import TACSCMApp.SCM;
import no.agentsystems_dhom.game_elements.RFQ;;

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
	
	
	//W.I.P. 
	public void sendDailyRFQs(int RFQQuantity, int currentDay)
	{
		Random rand = new Random();
		List<RFQ> RFQs = new ArrayList<RFQ>();
		
		//Decide the number of RFQs to send to the server
		double trend = 1;
		trend = Math.max(TAC_Ontology.Tmin,  Math.min(TAC_Ontology.Tmax, trend + (0.02*rand.nextDouble() - 0.01)));
		double RFQavg = TAC_Ontology.HLRFQmin + (TAC_Ontology.HLRFQmax - TAC_Ontology.HLRFQmin)* rand.nextDouble(); 
		//Setting trend to 1 if it exceeds the boundaries
		if(trend*RFQavg < TAC_Ontology.HLRFQmin || trend*RFQavg > TAC_Ontology.HLRFQmax)
		{
			trend = 1;
		}
		
		//Compute the number of RFQs for the current day for each segment(high, low, mid)
		//Low and high segment
		double HLRFQavg = Math.min(TAC_Ontology.HLRFQmax,  Math.max(TAC_Ontology.HLRFQmin, RFQavg)*trend); 
		//DO POISSON
		
		
		//Mid segment
		double MRFQavg = Math.min(TAC_Ontology.MRFQmax, Math.max(TAC_Ontology.MRFQmin, RFQavg)*trend);
		//DO POISSON
		
		//Add RFQs with random content to the list
		for(int i = 0; i < RFQQuantity; i++)
		{
			//NOT FINISHED HERE
			int reservePrice = rand.nextInt(TAC_Ontology.PCpmax - TAC_Ontology.PCpmin)+ TAC_Ontology.PCpmin;
			int penalty = rand.nextInt(TAC_Ontology.Pmax - TAC_Ontology.Pmin) + TAC_Ontology.Pmin;
			
			int PC = 1;
			int quantity = rand.nextInt(TAC_Ontology.Qmax - TAC_Ontology.Qmin) + TAC_Ontology.Qmin;
			int dueDate = currentDay + rand.nextInt(TAC_Ontology.Dmax - TAC_Ontology.Dmin) + TAC_Ontology.Dmin;
			RFQ tempRFQ = new RFQ(PC, quantity, dueDate, penalty, reservePrice);
			RFQs.add(tempRFQ);
		}
		
		
	}

}
