package no.agentsystems_dhom.server;

import no.agentsystems_dhom.agent.Assembly;
import no.agentsystems_dhom.agent.Inventory;

public class Agent {

	private Inventory _inventory;
	private Assembly assembly;
	
	String name;
	int id;
	
	public Agent(String _name, int _id) {
		name = _name;
		id = _id;
		_inventory = new Inventory();
		assembly = new Assembly();
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}

	public Inventory getInventory() {
		return _inventory;
	}
	
	public Assembly getAssembly(){
		return assembly;
	}
	
}
