package no.agentsystems_dhom.supplier;

import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.TAC_Ontology;
import TACSCMApp.SCM;

public class SCM_S1 extends SCM_Supplier {
	
	public SCM_S1(SCM _server){
		server = _server;
		interval = server.getTime();
		suppliers[0] = new Supplier(100,101);
		suppliers[1] = new Supplier(110,111);
		suppliers[2] = new Supplier(200,210);
		suppliers[3] = new Supplier(300,301);
		suppliers[4] = new Supplier(400, 401);
		suppliers[5] = new Supplier(200,101);
		suppliers[6] = new Supplier(300,210);
		suppliers[7] = new Supplier(401, 111);
		
		suplView = new GUI("SCM_S1");
		try{
			
			for(;;){
				if(server.status() && !getStatus()){
					startTheGame();
				}else if(interval == TAC_Ontology.gameLength){
					closeTheGame();
				}
				
				int time = interval % TAC_Ontology.lengthOfADay;
				
				if(time == 0 && getStatus()){
					int day = interval / TAC_Ontology.lengthOfADay;
					suplView.append("\nday: " + day);
					suplView.append("\nCapacity:");
					for(int capacity : getCapacity())
					{
						suplView.append(capacity + "\t");
					}
				}
				interval++;
				
				Thread.sleep(TAC_Ontology.sec);
			}
		}catch(InterruptedException e){
			System.out.println("ERROR : " + e);
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		SCM ser = initServer(args);
		new SCM_S1(ser);
	}
	
	public int[] getCapacity()
	{
		int[] componentCapacityArray = new int[16];
		int count = 0;
		for(Supplier supplier: suppliers)
		{
			componentCapacityArray[count] = supplier.GetComponent1().GetCapacity();
			componentCapacityArray[count+1] = supplier.GetComponent2().GetCapacity();
			count +=2;
		}
		return componentCapacityArray;
	}
	
	
}