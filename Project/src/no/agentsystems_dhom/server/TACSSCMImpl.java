package no.agentsystems_dhom.server;

import org.omg.CORBA.ORB;


public class TACSSCMImpl extends SCMPOA{
	private ORB orb;
	private SCM_Server server;
	
	public TACSSCMImpl(ORB o, SCM_Server ser){
		
	}
	public boolean status(){
		return false;
	}
	public short getTime(){
		return (short) server.getTime();
	}
	public double getInterestRate(){
		return 0;
	}
	public double getLoanInterestRate(){
		return 0;
	}
	public double getStorageCost(){
		return 0;
	}
	public String send(String msg){
		return "zup bro";
	}
}
