package no.agentsystems_dhom.server;

import TACSCMApp.SCM;


public class SCM_Server extends Thread{
	private SCM server;
	private GUI serverView;
	private int interval;
	private int day;
	private boolean isOn;
	
	public SCM_Server(){
		// added a new comment
		// added another comment
	}
	
	public static void main(String[] args){
		//TODO the initialization of the program
	}
	
	@Override
	public void run(){
		//TODO action when the method start() has been invoked
	}
	
	public boolean getStatus(){
		return isOn;
	}
	
	public int getTime(){
		return day;
	}
	
	public double getInterestRate(){
		//TODO return interest rate 
		return 0.0;
	}
	
	public double getLoanInterestRate(){
		//TODO return loan interest rate
		return 0.0;
	}
	
	public double getStorageCosts(){
		//TODO return storage costs
		return 0.0;
	}
	
	public Message agentRegistering(Message kqml){
		//TODO register agent
		return null;
	}
	
}
