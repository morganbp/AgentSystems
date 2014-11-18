package no.agentsystems_dhom.server;

import no.agentsystems_dhom.agent.Inventory;

public class Agent {

	private Inventory _inventory;
	
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

	public Inventory get_inventory() {
		return _inventory;
	}

	public void set_inventory(Inventory _inventory) {
		this._inventory = _inventory;
	}
	
}
