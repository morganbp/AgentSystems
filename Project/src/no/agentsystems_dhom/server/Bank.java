package no.agentsystems_dhom.server;

import java.util.ArrayList;

public class Bank {

	private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
	private double interestRate;
	private double loanInterestRate;
	
	public void add(BankAccount acc){
		accounts.add(acc);
	}
	
	public void updateBalance(){
		for(BankAccount b : accounts){
			b.updateBalance(interestRate, loanInterestRate);
		}
	}
	
	public void setInterestRate(double interestRate) {
		// TODO Auto-generated method stub
		
	}

	public void setLoanInterestRate(double loanInterestRate) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<BankAccount> getBankAccounts() {
		// TODO Auto-generated method stub
		return accounts;
	}

	public BankAccount getBankAccount(Agent a) {
		for(BankAccount b : accounts){
			if(b.getAgent().getId() == a.getId()){
				return b;
			}
		}
		return null;
	}

}
