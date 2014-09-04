package no.agentsystems_dhom.agent;

	
import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TACSCMApp.SCMHelper;

public class Agent extends Thread{
	private String name;
	private int id;
	static TACSCMApp.SCM scmImpl;
	private GUI agentView;
	
	public Agent(String _name, int _id) {
		name = _name;
		id = _id;
		agentView = new GUI(name);
	}
	
	
	public static void main(String[] args){
		try{
			Agent agent = new Agent("SCM1", 1);
			
			// Create and init the ORB
			ORB orb = ORB.init(args, null);
			
			// get the root naming context
			org.omg.CORBA.Object objRef = 
					orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			// resolve the Object Reference in Naming
			String name = "TACSCM";
			scmImpl = SCMHelper.narrow(ncRef.resolve_str(name));
			agent.start();
			
		}catch(Exception e){
			System.out.println("ERROR : " + e);
			e.printStackTrace(System.out);

			System.out.println("NOO!!");
		}
	}
	
	@Override
	public void run(){
		for(;;){
			try {
			agentView.setText(Integer.toString(scmImpl.getTime()));
			//agentView.append("\nHei");
			sleep(TAC_Ontology.sec);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
