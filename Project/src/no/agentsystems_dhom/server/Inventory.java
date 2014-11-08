package no.agentsystems_dhom.server;

import java.util.ArrayList;

import no.agentsystems_dhom.supplier.Component;

public class Inventory {
	
	Agent _agent;
	ArrayList<Component> _agentComponents;
	
	public Inventory(Agent agent)
	{
		_agent = agent;
	}
	
	public Agent GetAgent()
	{
		return _agent;
	}
	
	public ArrayList<Component> GetAgentComponents()
	{
		return _agentComponents;
	}
	
	public void AddComponent(Component component)
	{
		_agentComponents.add(component);
	}
	
	public void AddComponents(ArrayList<Component> components)
	{
		for(Component component : components)
		{
			this.AddComponent(component);
		}
	}
}
