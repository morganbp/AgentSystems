package no.agentsystems_dhom.supplier;

import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.TAC_Ontology;

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
	protected Supplier[] suppliers;
	
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
		initSuppliers();
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0 : interval;
		suplView.setText("---> Time : " + interval + " seconds ");
		
	}
	
	private void initSuppliers() {
		suppliers = new Supplier[8];
		suppliers[0] = new Supplier(100, 101);
		suppliers[1] = new Supplier(110, 111);
		suppliers[2] = new Supplier(200, 210);
		suppliers[3] = new Supplier(300, 301);
		suppliers[4] = new Supplier(400, 401);
		suppliers[5] = new Supplier(200, 101);
		suppliers[6] = new Supplier(300, 210);
		suppliers[7] = new Supplier(401, 111);
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
	
	protected void addSupplierComponents() {
		// loop through all components (2 for each supplier)
		// and add the new components
		for (int i = 0; i < suppliers.length*2; i++) {
			int index = (int)Math.floor(i/2);
			Component comp = suppliers[index].getComponents()[i%2];
			comp.addComponents();
			suplView.append("\n#"+comp.getComponentName() + ": " + comp.getCapacity());
		}
	}

}