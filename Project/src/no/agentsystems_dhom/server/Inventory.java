package no.agentsystems_dhom.server;

import java.util.ArrayList;

import no.agentsystems_dhom.supplier.Component;

public class Inventory {
	
	Agent _agent;
	ArrayList<AgentOrder> _agentOrders;
	
	public Inventory(Agent agent)
	{
		_agent = agent;
	}
	
	public Agent GetAgent()
	{
		return _agent;
	}
	
	public ArrayList<AgentOrder> GetAgentOrders()
	{
		return _agentOrders;
	}
	
	public void AddComponent(AgentOrder agentOrder)
	{
		_agentOrders.add(agentOrder);
	}
}
