package no.agentsystems_dhom.agent;

public class Agent {

	
	String name;
	int id;
	
	public Agent(String _name, int _id) {
		name = _name;
		id = _id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
}
