package no.agentsystems_dhom.agent;

import no.agentsystems_dhom.customer.PC;

public class Inventory {
	private int components[] = { 100, 101, 110, 111, 200, 210, 200, 301, 400,
			401 };
	int[] quantity = new int[10];
	int[] numberOfPCs = new int[16];

	public void updateQuantity(int cid, int q) {
		quantity[SCM_Agent.getIndex(cid)] += q;
	}

	public void updateNumberOfPcs(int sku, int q) {
		numberOfPCs[sku - 1] += q;
	}

	/**
	 * 
	 * @param sku
	 *            the sku number of the PC we want to check
	 * @param q
	 *            the number of PCs wanted
	 * @return true if there is enough of all PC Components in the Inventory,
	 *         false if not.
	 */
	public boolean isEnoughComponents(int sku, int q) {
		PC pc = new PC(sku);
		for (int cid : pc.getComponents()) {
			if (quantity[SCM_Agent.getIndex(cid)] < q)
				return false;
		}
		return true;
	}

	public int[] getNumberOfPCs() {
		return numberOfPCs;
	}
}
