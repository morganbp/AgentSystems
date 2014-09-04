package no.agentsystems_dhom.customer;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TACSCMApp.SCMHelper;


public class SCM_Customer {
	
	private boolean has_started;
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
	
		}catch(Exception e){
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}
	}
	
	protected void startTheGame(){
		has_started = true;
		
	}
	
	protected void closeTheGame(){
		has_started = false;
		
	}
	
	protected boolean getStatus(){
		return has_started;
	}
}
