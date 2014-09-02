package no.agentsystems_dhom.server;

import java.util.Random;

import no.agentsystems_dhom.game_elements.TAC_Ontology;
import no.agentsystems_dhom.yinyang.Message;

public class Util {
	   public static int poisson(double averageNumber)
	    {
	      double elambda = Math.exp(-averageNumber);
	      double product = 1;
	      int count = 0;
	      if (elambda == 0)
	      {
	        return (int) averageNumber;
	      }

	      while (product >= elambda)
	      {
	        product *= Math.random();
	        count++;
	      }
	      // the result should be one behind
	      return count == 0 ? 0 : (count - 1);
	    } 	
	   // round up a double with n decimal digits	
	   public static double roundUp(double x, int n) {
	      double y = x;
	      int d = 1;

	      for (int i = 0; i < n; i++) d *= 10;

	      y = roundUp(y*d);

	      return y/d ;
	   }
	   
	   // round up a double
	   public static double roundUp(double x) {
	      return (double) (Math.round(x)) ;
	   }
		
	   // generate a random number < n
	   public static int random(int n) {
			Random rand = new Random();
			return rand.nextInt(n);
		}
	   
		// random integer number between to integers a, b
		public static int random(int a, int b) {
			Random rand = new Random();
			return rand.nextInt(b - a) + a;
		}
		
		// random a double between a and b
		public static double random(double a, double b) {
			Random rand = new Random();
			return rand.nextDouble()*(b - a) + a;
		}
		
		// make a default KQML Message
		public static Message buildKQML(String performative, String sender, String content) {
			Message kqml = new Message();
			kqml.setPerformative(performative);
			kqml.setSender(sender);
			kqml.setContent(content);
			kqml.setOntology(TAC_Ontology.ontology);
			return kqml;
		}
		
}
