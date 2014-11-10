package no.agentsystems_dhom.supplier;

import java.util.List;

import no.agentsystems_dhom.server.AgentOrder;
import no.agentsystems_dhom.server.AgentRequest;
import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.TAC_Ontology;
import TACSCMApp.SCM;

public class SCM_S1 extends SCM_Supplier {

	private static final String CLASS_NAME = "SCM_S1";

	public SCM_S1(SCM _server) {
		server = _server;
		interval = server.getTime();

		suplView = new GUI("SCM_S1");
		try {

			for (;;) {
				if (server.status() && !getStatus()) {
					startTheGame();
				} else if (interval == TAC_Ontology.gameLength) {
					closeTheGame();
				}

				int time = interval % TAC_Ontology.lengthOfADay;

				if (time == 0 && getStatus()) {
					int day = interval / TAC_Ontology.lengthOfADay;
					suplView.append("\nday: " + day);
					if (day != 0)
						addSupplierComponents();
				}
				if (time == 1 && getStatus()) {

				}
				if (time == 2 && getStatus()) {
					sendYesterdaysOffers(CLASS_NAME);
				}
				if (time == 6 && getStatus()) {
					List<AgentRequest> agentRequests = getAgentRequests(CLASS_NAME);

					createSupplierOffers(agentRequests, CLASS_NAME);
				}
				// Not sure if time == 8
				if (time == 8 && getStatus()) {
					//WEEK 44 - 2
					//Get orders from Agents and add to ActiveOrders(?)
					getAgentOrders(CLASS_NAME);
					
					//WEEK 44 - 3
					//Handle orders
					//WRAP TO NEW CLASS? COMPONENTBUNDLE -> (agent, components)
					
					
					//WEEK 44 - 3
					//Send components to agents(server)
					
					
				}
				interval++;

				Thread.sleep(TAC_Ontology.sec);
			}
		} catch (InterruptedException e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		SCM ser = initServer(args);
		new SCM_S1(ser);
	}

}