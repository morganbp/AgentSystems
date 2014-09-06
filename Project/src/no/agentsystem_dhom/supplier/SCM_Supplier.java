package no.agentsystem_dhom.supplier;

import no.agentsystems_dhom.game_elements.GUI;
import no.agentsystems_dhom.server.SCM_Server;

public class SCM_Supplier {
	private SCM_Server server;
	private boolean has_started;
	protected GUI suplView;
	protected int interval;
	
	public void initSCMImpl(String[] args){
		try{
			
		}catch(Exception e){
			System.out.println();
		}
	}
	
	public void startTheGame(){
		has_started = true;
		interval = 0;
		suplView.setText("---> TAC Game started.");
	}
	
	public void closeTheGame()	{
		has_started = false;
	}
	
	public boolean getStatus(){
		return has_started;
	}

}
