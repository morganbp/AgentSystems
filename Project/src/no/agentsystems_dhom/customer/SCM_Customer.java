package no.agentsystems_dhom.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.Offer;
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

	// Average RFQ values
	private double RFQAvgH, RFQAvgL, RFQAvgM;
	// Trend values
	private double trendH, trendL, trendM;

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
		initRFQandTrends();
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

	protected void initRFQandTrends() {
		// Generate trends for each of the segments.
		trendH = generateTrend();

		trendL = generateTrend();

		trendM = generateTrend();
		// Generate a RFQavg between 25 and 100 
		RFQAvgH = generateAverageRFQ();
		
		RFQAvgL = generateAverageRFQ();
		
		RFQAvgM = generateAverageRFQ();
		
	}
	
	private double getRFQ(double RFQavg, double trend) {
		double newTrend = checkBoundaries(RFQavg, trend);
		return Math.min(TAC_Ontology.HLRFQmax, Math.max(TAC_Ontology.HLRFQmin, RFQavg * newTrend));
	}

	private double checkBoundaries(double RFQavg, double trend){
		if (trend * RFQavg < TAC_Ontology.HLRFQmin
				|| trend * RFQavg > TAC_Ontology.HLRFQmax) {
			return 1.0;
		}
		return trend;
	}
	
	private double generateAverageRFQ() {
		Random rand = new Random();
		double RFQavg = TAC_Ontology.HLRFQmin // 25  - 100
				+ (TAC_Ontology.HLRFQmax - TAC_Ontology.HLRFQmin)
				* rand.nextDouble();
		return RFQavg;
	}

	private double generateTrend() {
		Random rand = new Random();
		double trend = 1;
		
		double rtnVal = Math.max(
				TAC_Ontology.Tmin,
				Math.min(TAC_Ontology.Tmax, trend
						+ (0.02 * rand.nextDouble() - 0.01)));
		return rtnVal;
	}
	
	private double generateTrend(double trend) {
		Random rand = new Random();
		
		double rtnVal = Math.max(
				TAC_Ontology.Tmin,
				Math.min(TAC_Ontology.Tmax, trend
						+ (0.02 * rand.nextDouble() - 0.01)));
		return rtnVal;
	}

	protected void sendDailyRFQs(int currentDay, String className) {

		List<RFQ> RFQs = new ArrayList<RFQ>();

		//Do random walk
		trendH = generateTrend(trendH);
		
		trendL = generateTrend(trendL);
		
		trendM = generateTrend(trendM);
		
		// Compute the number of RFQs for the current day for each segment
		// High segment
		// HRFQavg is the number of RFQs we are going to make for this segment
		double HRFQ = Util.poisson(getRFQ(RFQAvgH, trendH));
		List<RFQ> highSegmentRFQs = createRFQs(HRFQ, TAC_Ontology.high,
				currentDay);

		// Low segment
		// LRFQavg is the number of RFQs we are going to make for this segment
		double LRFQ = Util.poisson(getRFQ(RFQAvgL, trendL));
		List<RFQ> lowSegmentRFQs = createRFQs(LRFQ, TAC_Ontology.low,
				currentDay);

		// Mid segment
		// MRFQavg is the number of RFQs we are going to make for this segment
		double MRFQ = Util.poisson(getRFQ(RFQAvgM, trendM));
		List<RFQ> midSegmentRFQs = createRFQs(MRFQ, TAC_Ontology.mid,
				currentDay);

		// Adding RFQs from each segment to the main list
		RFQs.addAll(lowSegmentRFQs);
		RFQs.addAll(midSegmentRFQs);
		RFQs.addAll(highSegmentRFQs);

		custView.append("\n#RFQs: " + lowSegmentRFQs.size() + " "
				+ midSegmentRFQs.size() + " " + highSegmentRFQs.size() + " "
				+ RFQs.size());

		// Logic to send the RFQs to the server

		sendRFQToServer(className, RFQs);
	}

	private void sendRFQToServer(String className, List<RFQ> RFQs) {
		Message kqml = Util.buildKQML(TAC_Ontology.Customer_RFQs, className,
				RFQ.listToString(RFQs));
		String resp = server.send(kqml.toString());
		Message response = Message.buildMessage(resp);
		custView.append("\n#RFQs: " + response.getContent());
	}

	// create RFQ based on segment, what day it is and how many we want to
	// create.
	protected List<RFQ> createRFQs(double RFQquantity, int segment,
			int currentDay) {
		Random rand = new Random();
		List<RFQ> RFQs = new ArrayList<RFQ>();
		for (int i = 0; i < RFQquantity; i++) {
			// Get SKU and create PC in chosen segment
			int SKU = PC.SKU(segment);
			PC pc = new PC(SKU);

			int reservePrice = pc.getbasePrice()
					* ((rand.nextInt(TAC_Ontology.PCpmax - TAC_Ontology.PCpmin) + TAC_Ontology.PCpmin) / 100); // 0,75-1,25

			int penalty = (rand.nextInt(TAC_Ontology.Pmax - TAC_Ontology.Pmin) + TAC_Ontology.Pmin)
					* reservePrice / 100;

			int PCsku = pc.getSKU();

			int quantity = rand.nextInt(TAC_Ontology.Qmax - TAC_Ontology.Qmin) // 1-20
					+ TAC_Ontology.Qmin;

			int dueDate = currentDay
					+ rand.nextInt(TAC_Ontology.Dmax - TAC_Ontology.Dmin)
					+ TAC_Ontology.Dmin;

			RFQ tempRFQ = new RFQ(PCsku, quantity, dueDate, penalty,
					reservePrice);
			RFQs.add(tempRFQ);
		}
		return RFQs;
	}

	protected List<Offer> getAgentOffers(String className) {
		Message kqml = Util.buildKQML(TAC_Ontology.getAgentOffers, className,
				"");

		String resp = server.send(kqml.toString());
		Message respond = Message.buildMessage(resp);

		List<Offer> offersList = Offer.stringToList(respond.getContent());

		List<String> bidders = getBidders(offersList);

		custView.append("\nThe number of agents that send offers: "
				+ bidders.size());
		custView.append("\n#Offers: " + offersList.size());
		return offersList;
	}

	private List<String> getBidders(List<Offer> offersList) {
		List<String> bidders = new ArrayList<String>();
		for (Offer offer : offersList) {
			// Counting unique bidders
			if (!bidders.contains(offer.getBidder())) {
				bidders.add(offer.getBidder());
			}
		}
		return bidders;
	}
}
