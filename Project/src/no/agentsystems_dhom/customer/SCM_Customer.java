package no.agentsystems_dhom.customer;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.game_elements.TAC_Ontology;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import TACSCMApp.SCMHelper;


public class SCM_Customer {
	private SCM server;
	private int interval;
	private boolean has_started;
	static GUI custgui;
	static TACSCMApp.SCM scmImpl;
	
	public static void main(String[] args){
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
	        custgui = new GUI("SCM_C1");

        	short curTime = scmImpl.getTime();
        	custgui.setText("---->> Time: "+Short.toString(curTime) + " seconds");
        	
	        for(;;){
	        	
	        	System.out.println();
	        	if(curTime % 9 == 0){
	        		custgui.append("\nDay: " + Integer.toString(curTime/9));
	        	}
	        	
	        	
	        	Thread.sleep(TAC_Ontology.sec);
	        }
		}catch(InterruptedException e){
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}catch(Exception e){
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}
	}
	
	protected void startTheGame(){
		
	}
	
	protected void closeTheGame(){
		
	}
	
	protected boolean getStatus(){
		return has_started;
	}
}
