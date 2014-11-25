package no.agentsystems_dhom.customer;

import no.agentsystems_dhom.server.TAC_Ontology;
import no.agentsystems_dhom.server.Util;
import no.agentsystems_dhom.supplier.Component;


public class PC {
	// PC type
	private int SKU;
	private int basePrice = 0;
	private int cycles;
	private int segment;
	private int componentIds[] = new int[4];
	public static String PC_FIELD_SEPERATOR = "<pc_field_seperator>";
	public static String PC_OBJECT_SEPERATOR = "<pc_object_seperator>";
	// constructor
	public PC(int SKU) {
		this.SKU = SKU;
		initiate();
		compBasePrice();
	}
	public int getSKU() {
		return SKU;
	}
	public int[] getComponents() {
		return componentIds;
	}
	public int getComponent(int index) {
		return componentIds[index];
	}

	private void initiate() {
		switch(SKU) {
		case 1: 
			cycles = 4;
			segment = TAC_Ontology.low;
			componentIds[0] = 100;
			componentIds[1] = 200;
			componentIds[2] = 300;
			componentIds[3] = 400;
			break;
		case 2: 
			cycles = 5;
			segment = TAC_Ontology.low;
			componentIds[0] = 100;
			componentIds[1] = 200;
			componentIds[2] = 300;
			componentIds[3] = 401;
			break;
		case 3: 
			cycles = 5;
			segment = TAC_Ontology.mid;
			componentIds[0] = 100;
			componentIds[1] = 200;
			componentIds[2] = 301;
			componentIds[3] = 400;
			break;
		case 4: 
			cycles = 6;
			segment = TAC_Ontology.mid;
			componentIds[0] = 100;
			componentIds[1] = 200;
			componentIds[2] = 301;
			componentIds[3] = 401;
			break;
		case 5: 
			cycles = 5;
			segment = TAC_Ontology.mid;
			componentIds[0] = 101;
			componentIds[1] = 200;
			componentIds[2] = 300;
			componentIds[3] = 400;
			break;
		case 6: 
			cycles = 6;
			segment = TAC_Ontology.high;
			componentIds[0] = 101;
			componentIds[1] = 200;
			componentIds[2] = 300;
			componentIds[3] = 401;
			break;
		case 7: 
			cycles = 6;
			segment = TAC_Ontology.high;
			componentIds[0] = 101;
			componentIds[1] = 200;
			componentIds[2] = 301;
			componentIds[3] = 400;
			break;
		case 8: 
			cycles = 7;
			segment = TAC_Ontology.high;
			componentIds[0] = 101;
			componentIds[1] = 200;
			componentIds[2] = 301;
			componentIds[3] = 401;
			break;
		case 9: 
			cycles = 4;
			segment = TAC_Ontology.low;
			componentIds[0] = 110;
			componentIds[1] = 210;
			componentIds[2] = 300;
			componentIds[3] = 400;
			break;
		case 10: 
			cycles = 5;
			segment = TAC_Ontology.low;
			componentIds[0] = 110;
			componentIds[1] = 210;
			componentIds[2] = 300;
			componentIds[3] = 401;
			break;
		case 11: 
			cycles = 5;
			segment = TAC_Ontology.low;
			componentIds[0] = 110;
			componentIds[1] = 210;
			componentIds[2] = 301;
			componentIds[3] = 400;
			break;
		case 12: 
			cycles = 6;
			segment = TAC_Ontology.mid;
			componentIds[0] = 110;
			componentIds[1] = 210;
			componentIds[2] = 301;
			componentIds[3] = 401;
			break;
		case 13: 
			cycles = 5;
			segment = TAC_Ontology.mid;
			componentIds[0] = 111;
			componentIds[1] = 210;
			componentIds[2] = 300;
			componentIds[3] = 400;
			break;
		case 14: 
			cycles = 6;
			segment = TAC_Ontology.mid;
			componentIds[0] = 111;
			componentIds[1] = 210;
			componentIds[2] = 300;
			componentIds[3] = 401;
			break;
		case 15: 
			cycles = 6;
			segment = TAC_Ontology.high;
			componentIds[0] = 111;
			componentIds[1] = 210;
			componentIds[2] = 301;
			componentIds[3] = 400;
			break;
		case 16: 
			cycles = 7;
			segment = TAC_Ontology.high;
			componentIds[0] = 111;
			componentIds[1] = 210;
			componentIds[2] = 301;
			componentIds[3] = 401;
			break;
		}
	}
	
	private void compBasePrice() {
		for (int i = 0; i < 4; i++) {
			Component a = new Component(componentIds[i]);
			basePrice += a.getBasePrice();
		}
	}
	
	public int getbasePrice() {
		return basePrice;
	}
	
	public int getCycles() {
		return cycles;
	}
	
	public int getSegment() {
		return segment;
	}
	
	public static int SKU (int segment) {
		int n = 0;
		if (segment == TAC_Ontology.low) {
			int tmp[] = {1, 2, 9, 10, 11};
			int i = Util.random(0, tmp.length);
			n = tmp[i];
		}
		if (segment == TAC_Ontology.mid) {
			int tmp[] = {3, 4, 5, 12, 13, 14};
			int i = Util.random(0, tmp.length);
			n = tmp[i];
		}
		if (segment == TAC_Ontology.high) {
			int tmp[] = {6, 7, 8, 15, 16};
			int i = Util.random(0, tmp.length);
			n = tmp[i];
		}
		return n;
	}
	
	public String toString() {
		String components = "";
		for (int i = 0; i < componentIds.length-1; i++) {
			components += componentIds[i] +",";
		}
		components += componentIds[componentIds.length-1] + " ";
		return SKU + " " + components + cycles + " " + segment + " " + basePrice;
	}
	
	
	
	// test the class
    public static void main(String[] args) {
    	PC a = new PC(16);
    	System.out.println(a.toString());
    }	
}
