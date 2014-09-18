package no.agentsystems_dhom.server;

public class RFQ {

	static int nextId = 1;
	int RFQId;
	int PC;
	int quantity;
	int dueDate;
	int penalty;
	int reservePrice;
	
	public RFQ(int PC, int quantity, int dueDate, int penalty, int reservePrice)
	{
		//Setting a unique ID for the RFQ
		this.RFQId = nextId;
		nextId++;
		
		this.PC = PC;
		this.quantity = quantity;
		this.dueDate = dueDate;
		this.penalty = penalty;
		this.reservePrice = reservePrice;
	}
	
}

