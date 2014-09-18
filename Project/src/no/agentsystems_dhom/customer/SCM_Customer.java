package no.agentsystems_dhom.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.RFQ;
import no.agentsystems_dhom.server.TAC_Ontology;
import no.agentsystems_dhom.server.Util;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import yinyang.Message;
import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;

public class SCM_Customer {

	private boolean has_started;
	protected SCM server;
	protected GUI custView;
	protected int interval;

	public static SCM initSCMImpl(String[] args) {
		SCM server = null;
		try {
			// create and initialize the ORB
			ORB orb = ORB.init(args, null);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb
					.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "TACSCM";
			server = SCMHelper.narrow(ncRef.resolve_str(name));

		} catch (Exception e) {
			System.out.println("ERROR : " + e);
			e.printStackTrace();
			return null;
		}
		return server;
	}

	protected void startTheGame() {
		has_started = true;
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0
				: interval;
		custView.setText("---> Time : " + interval + " seconds ");
	}

	protected void closeTheGame() {
		has_started = false;

		custView.append("\n\n---> The TAC Game is closed");

	}

	protected boolean getStatus() {
		return has_started;
	}

	protected void setStatus(boolean status) {
		has_started = status;
	}

	// W.I.P.
	protected void sendDailyRFQs(int currentDay, String className) {
		Random rand = new Random();
		List<RFQ> RFQs = new ArrayList<RFQ>();

		// Decide the number of RFQs to send to the server
		double trend = 1;
		trend = Math.max(
				TAC_Ontology.Tmin,
				Math.min(TAC_Ontology.Tmax, trend
						+ (0.02 * rand.nextDouble() - 0.01)));
		double RFQavg = TAC_Ontology.HLRFQmin
				+ (TAC_Ontology.HLRFQmax - TAC_Ontology.HLRFQmin)
				* rand.nextDouble();
		// Setting trend to 1 if it exceeds the boundaries
		if (trend * RFQavg < TAC_Ontology.HLRFQmin || trend * RFQavg > TAC_Ontology.HLRFQmax) {
			trend = 1;
		}

		// Compute the number of RFQs for the current day for each segment
		// High segment
		//HRFQavg is the number of RFQs we are going to make for this segment
		double HRFQavg = Math.min(TAC_Ontology.HLRFQmax,
		Math.max(TAC_Ontology.HLRFQmin, RFQavg) * trend);
		List<RFQ> highSegmentRFQs = createRFQs(HRFQavg, TAC_Ontology.high, currentDay);

		// Low segment
		//LRFQavg is the number of RFQs we are going to make for this segment
		double LRFQavg = Math.min(TAC_Ontology.HLRFQmax,
		Math.max(TAC_Ontology.HLRFQmin, RFQavg) * trend);
		List<RFQ> lowSegmentRFQs = createRFQs(LRFQavg, TAC_Ontology.low, currentDay);

		// Mid segment
		//MRFQavg is the number of RFQs we are going to make for this segment
		double MRFQavg = Math.min(TAC_Ontology.MRFQmax,
		Math.max(TAC_Ontology.MRFQmin, RFQavg) * trend);
		List<RFQ> midSegmentRFQs = createRFQs(MRFQavg, TAC_Ontology.mid, currentDay);

		//Adding RFQs from each segment to the main list
		RFQs.addAll(lowSegmentRFQs);
		RFQs.addAll(midSegmentRFQs);
		RFQs.addAll(highSegmentRFQs);
		
		//Logic to send the RFQs to the server
		Message kqml = Util.buildKQML(TAC_Ontology.Customer_RFQs, className, ""
				+ RFQs.toString());
	}

	//create RFQ based on segment, what day it is and how many we want to create.
	protected List<RFQ> createRFQs(double RFQquantity, int segment, int currentDay) {
		Random rand = new Random();
		List<RFQ> RFQs = new ArrayList<RFQ>();
		
		for (int i = 0; i < RFQquantity; i++) {
			//Get SKU and create PC in chosen segment
			int SKU =  PC.SKU(segment);
			PC pc = new PC(SKU);
			

			int reservePrice = pc.getbasePrice()*((rand.nextInt(TAC_Ontology.PCpmax
					- TAC_Ontology.PCpmin)
					+ TAC_Ontology.PCpmin)/100);
			
			int penalty = (rand.nextInt(TAC_Ontology.Pmax - TAC_Ontology.Pmin)
					+ TAC_Ontology.Pmin) * reservePrice/100;

			int PCsku = pc.getSKU();
			
			int quantity = rand.nextInt(TAC_Ontology.Qmax - TAC_Ontology.Qmin)
					+ TAC_Ontology.Qmin;
			
			int dueDate = currentDay
					+ rand.nextInt(TAC_Ontology.Dmax - TAC_Ontology.Dmin)
					+ TAC_Ontology.Dmin;
			
			RFQ tempRFQ = new RFQ(PCsku, quantity, dueDate, penalty, reservePrice);
			RFQs.add(tempRFQ);
		}
		return RFQs;
	}

}
