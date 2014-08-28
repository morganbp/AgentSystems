package no.agentsystems_dhom.server;

<<<<<<< HEAD
public class SCM_Server {
=======

public class SCM_Server extends Thread{
	private SCM server;
	private GUI serverView;
	private int interval;
	private int day;
	private boolean isOn;
	
	public SCM_Server(){
		//TODO construction of the object
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
	}
	
	public double getLoanInterestRate(){
		//TODO return loan interest rate
	}
	
	public double getStorageCosts(){
		//TODO return storage costs
	}
	
	public Message agentRegistering(Message kqml){
		//TODO register agent
	}
>>>>>>> 17f950db827cd0bfd13506ce3e2e64bc7855270d
	
}
