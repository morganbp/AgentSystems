package no.agentsystems_dhom.supplier;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;

public class SCM_Supplier {
	private boolean has_started;
	protected GUI suplView;
	protected int interval;
	protected SCM server;
	
	public static SCM initServer(String[] args){
		SCM server;
		try{
			// create and init the ORB
			ORB orb = ORB.init(args, null);
			
			// get the  root naming context
			org.omg.CORBA.Object objRef = 
					orb.resolve_initial_references("NameService");
			// Using NamingContextExt instead of NamingContext. This is 
			// part of the Naming Service
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			
			// resolve the Object Reference in Naming
			String name = "TACSCM";
			server = SCMHelper.narrow(ncRef.resolve_str(name));
			
			
		}catch(Exception e){
			System.out.println("/n/nERROR : " + e);
			e.printStackTrace();
			return  null;
		}
		return server;
	}
	
	protected void startTheGame(){
		has_started = true;
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0 : interval;
		suplView.setText("---> Time : " + interval + " seconds ");
	}
	
	protected void closeTheGame()	{
		has_started = false;
		suplView.append("\n\n---> The TAC Game is closed");
	}
	
	protected boolean getStatus(){
		return has_started;
	}
	
	protected void setStatus(boolean status){
		has_started = status;
	}

}
