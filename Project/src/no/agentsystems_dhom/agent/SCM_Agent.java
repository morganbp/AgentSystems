package no.agentsystems_dhom.agent;

import no.agentsystems_dhom.server.GUI;
import no.agentsystems_dhom.server.TAC_Ontology;
import no.agentsystems_dhom.server.Util;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import yinyang.Message;
import TACSCMApp.SCM;
import TACSCMApp.SCMHelper;

public class SCM_Agent {
	protected SCM server;
	protected int interval;
	private boolean has_started;
	protected GUI agentView;

	public static SCM initServer(String[] args) {
		SCM rtnServer = null;
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
			rtnServer = SCMHelper.narrow(ncRef.resolve_str(name));

		} catch (Exception e) {
			System.out.println("ERROR :" + e);
			e.printStackTrace();
			return null;
		}
		return rtnServer;
	}

	protected void startTheGame() {
		has_started = true;
		interval = (interval < 0 || interval > TAC_Ontology.gameLength) ? 0 : interval;
		agentView.setText("---> Time : " + interval + " seconds ");
	}

	protected void closeTheGame() {
		has_started = false;
		agentView.append("\n\n---> The TAC Game is closed");
	}

	protected boolean getStatus() {
		return has_started;
	}

	protected void agentRegistering(String name, int id) {
		Message kqml = Util.buildKQML(TAC_Ontology.agent_registering, name, ""
				+ id);

		String resp = server.send(kqml.toString());

		Message response = Message.buildMessage(resp);
		agentView.append("\n" + response.getContent());
	}

}
