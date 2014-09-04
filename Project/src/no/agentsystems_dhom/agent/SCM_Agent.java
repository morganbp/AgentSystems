package no.agentsystems_dhom.agent;

import no.agentsystems_dhom.game_elements.GUI;
import TACSCMApp.SCM;

public class SCM_Agent {
	private SCM server;
	private int interval;
	private boolean has_started;
	private GUI gui;
	
	protected void startTheGame(){
		
	}
	protected void closeTheGame(){
		
	}
	protected boolean getStatus(){
		return has_started;
	}
}
