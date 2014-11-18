package no.agentsystems_dhom.agent;

import no.agentsystems_dhom.server.TAC_Ontology;

public class Assembly {
	private int numberOfCycles;
	private int cycles[] = { 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 5, 6, 7 };

	public Assembly() {
		numberOfCycles = TAC_Ontology.agentCellCapacity;
	}

	public void updateCycle(int sku, int n) {
		numberOfCycles -= n * cycles[sku - 1];
	}

	public void resetAssembly() {
		numberOfCycles = TAC_Ontology.agentCellCapacity;
	}

	/**
	 * 
	 * @param sku
	 *            the sku number of the PC
	 * @param n
	 *            the quantity of PC to be assembled
	 * @return true if there is available capacity to produce n of sku, false if
	 *         there's not
	 */
	public boolean isCapacityAvailable(int sku, int n) {
		if (numberOfCycles - (n * cycles[sku - 1]) < 0) {
			return false;
		} else {
			return true;
		}
	}

	public int getNumberOfCycles() {
		return numberOfCycles;
	}

}
