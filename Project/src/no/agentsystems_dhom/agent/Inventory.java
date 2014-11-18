package no.agentsystems_dhom.agent;

public class Inventory {
	private int components[] = {100,101,110,111,200,210,200,301,400,401};
	int[] quantity = new int[10];
	int[] numberOfPcs = new int[16];
	
	public void updateQuantity(int cid, int q) {
		quantity[SCM_Agent.getIndex(cid)] += q;
	}
	
	public void updateNumberOfPcs(int sku, int q) {
		numberOfPcs[sku-1] += q;
	}
	
}
