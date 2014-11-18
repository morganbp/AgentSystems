package no.agentsystems_dhom.server;

import java.util.ArrayList;

public class Bank {

	private ArrayList<BankAccount> _accounts = new ArrayList<BankAccount>();
	private double _interestRate;
	private double _loanInterestRate;
	
	public void add(BankAccount acc){
		_accounts.add(acc);
	}
	
	public void updateBalance(){
		for(BankAccount b : _accounts){
			b.updateBalance(_interestRate, _loanInterestRate);
		}
	}
	
	public void setInterestRate(double interestRate) {
		_interestRate = interestRate;
		
	}

	public void setLoanInterestRate(double loanInterestRate) {
		_loanInterestRate = loanInterestRate;
		
	}

	public ArrayList<BankAccount> getBankAccounts() {
		return _accounts;
	}

	public BankAccount getBankAccount(Agent a) {
		for(BankAccount b : _accounts){
			if(b.getAgent().getName().equals(a.getName())){
				return b;
			}
		}
		return null;
	}

}
