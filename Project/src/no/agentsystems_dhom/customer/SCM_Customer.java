package no.agentsystems_dhom.customer;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;


public class SCM_Customer {
	
	private boolean has_started;
	protected SCM server;
	protected  GUI custView;
	protected int interval;
	
	public static SCM initSCMImpl(String[] args){
		SCM server = null;
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
	        server = SCMHelper.narrow(ncRef.resolve_str(name));
	
		}catch(Exception e){
			System.out.println("ERROR : " + e);
			e.printStackTrace();
			return null;
		}
		return server;
	}
	
	protected void startTheGame(){
		has_started = true;
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0 : interval;
		custView.setText("---> Time : " + interval + " seconds ");
	}
	
	protected void closeTheGame(){
		has_started = false;
		
		custView.append("\n\n---> The TAC Game is closed");
		
	}
	
	protected boolean getStatus(){
		return has_started;
	}
	
	protected void setStatus(boolean status){
		has_started = status;
	}
	

}
