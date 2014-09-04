package no.agentsystems_dhom.game_elements;

import no.agentsystems_dhom.agent.Agent;

public class BankAccount {
	private Agent agent;
	private double balance = 0;
	private double credit = 0;
	private double debit = 0;
	
	
	public BankAccount(Agent a) {
		
	}

	public void addCredit(double c){
		credit += c;
	}
	
	public Agent getAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateBalance(double loanInterestRate, double interestRate) {
		double alfa = 0;
		if(balance <= 0){
			alfa = loanInterestRate;
		}else{
			alfa = interestRate;
		}
		balance = ((1 + (alfa/TAC_Ontology.numberOfTacDays))*balance) + credit - debit;
	}
	
	public void setBalance(double _balance){
		balance = _balance;
	}
	
	public double getBalance(){
		return balance;
	}

	public void setCredit(double _credit){
		credit = _credit;
	}
	
	public double getCredit(){
		return credit;
	}
	
	public void setDebit(double _debit){
		debit = _debit;
	}
	
	public double getDebit(){
		return debit;
	}
	

}
