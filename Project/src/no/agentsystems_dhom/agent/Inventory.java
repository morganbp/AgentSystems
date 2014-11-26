package no.agentsystems_dhom.agent;

import no.agentsystems_dhom.customer.PC;

public class Inventory {

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

	/**
	 * 
	 * @param sku
	 *            the sku of the PC to check
	 * @param quantity
	 *            number of PCs we wanna check 
	 * @return true if there is enough PCs, false if not.
	 */
	public boolean isEnoughPCs(int sku, int quantity) {
		if (numberOfPCs[sku - 1] > quantity) {
			return true;
		} else {
			return false;
		}

	}
}
