package no.agentsystems_dhom.server;

import java.util.ArrayList;

public class InventoryCollection {

	ArrayList<Inventory> _inventories;
	
	public void AddInventory(Inventory inventory)
	{
		_inventories.add(inventory);
	}
	
	public Inventory GetInventory(Agent agent)
	{
		Inventory selectedInventory = null;
		for(Inventory inventory : _inventories)
		{
			if(inventory.GetAgent().getId() == agent.getId())
			{
				selectedInventory = inventory;
				break;
			}
		}
		return selectedInventory;
	}
	
}
