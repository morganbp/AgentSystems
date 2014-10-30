package no.agentsystems_dhom.agent;

import no.agentsystems_dhom.server.TAC_Ontology;

public class Assembly {
	private int numberOfCycles = 0;
	private int cycles[] = {4,5,5,6,5,6,6,7,4,5,5,6,5,5,6,7};
	public Assembly(){
		numberOfCycles = TAC_Ontology.agentCellCapacity;
	}
	public void updateCycle(int sku, int n) {
		numberOfCycles -= n * cycles[sku-1];
	}
	public void resetAssenbly() {
		numberOfCycles = TAC_Ontology.agentCellCapacity;
  	}

}
