package no.agentsystem_dhom.supplier;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.server.SCM_Server;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;

public class SCM_Supplier {
	private SCM_Server server;
	private boolean has_started;
	protected GUI suplView;
	protected int interval;
	
	public static SCM initSCMImpl(String[] args){
		SCM scmImpl;
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
			scmImpl = SCMHelper.narrow(ncRef.resolve_str(name));
			
			
		}catch(Exception e){
			System.out.println("/n/nERROR : " + e);
			e.printStackTrace();
			return  null;
		}
		return scmImpl;
	}
	
	public void startTheGame(){
		has_started = true;
		interval = 0;
		suplView.setText("---> TAC Game started.");
	}
	
	public void closeTheGame()	{
		has_started = false;
		suplView.append("\n\n---> The TAC Game is closed");
	}
	
	public boolean getStatus(){
		return has_started;
	}

}
