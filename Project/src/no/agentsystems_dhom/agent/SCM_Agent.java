package no.agentsystems_dhom.agent;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import no.agentsystems_dhom.game_elements.GUI;
import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;

public class SCM_Agent {
	private SCM server;
	protected int interval;
	private boolean has_started;
	private GUI agentView;
	static TACSCMApp.SCM scmImpl;
	
	public static void initSCMImpl(String[] args){
		try{

			// create and initialize the ORB
	        ORB orb = ORB.init(args, null);

	        // get the root naming context
	        org.omg.CORBA.Object objRef = 
	            orb.resolve_initial_references("NameService");
	        // Use NamingContextExt instead of NamingContext. This is 
	        // part of the naming Service.  
	        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
					
	        // resolve the Object Reference in Naming
	        String name = "TACSCM";
	        scmImpl = SCMHelper.narrow(ncRef.resolve_str(name));
			
		}
		catch(Exception e){
			System.out.println("ERROR :" + e);
			e.printStackTrace();
		}
	}
	
	protected void startTheGame(){
		has_started = true;
		interval = 0;
		agentView.append("---> TAC Game started.");
		
	}
	protected void closeTheGame(){
		has_started = false;
		agentView.append("\n\n---> The TAC Game is closed");
	}
	protected boolean getStatus(){
		return has_started;
	}
}
