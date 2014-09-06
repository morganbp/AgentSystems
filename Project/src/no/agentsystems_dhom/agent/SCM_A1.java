package no.agentsystems_dhom.agent;

public class SCM_A1 extends SCM_Agent{
	public SCM_A1(){
		interval = scmImpl.getTime();
		
	}
	public static void main(String[] args){
		initSCMImpl(args);
		new SCM_A1();
	}

}
