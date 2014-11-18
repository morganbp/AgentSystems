package no.agentsystems_dhom.server;


public class BankAccount {
	private Agent _agent;
	private double _balance = 0;
	private double _credit = 0;
	private double _debit = 0;
	
	
	public BankAccount(Agent agent) {
		_agent = agent;
	}

	public void addCredit(double credit){
		_credit += credit;
	}
	
	public Agent getAgent() {
		return _agent;
	}

	public void updateBalance(double loanInterestRate, double interestRate) {
		double alfa = 0;
		if(_balance <= 0){
			alfa = loanInterestRate;
		}else{
			alfa = interestRate;
		}
		_balance = ((1 + (alfa/TAC_Ontology.numberOfTacDays))*_balance) + _credit - _debit;
	}
	
	public void chargeAgent(double price){
		_balance -= price;
	}
	
	public void setBalance(double balance){
		_balance = balance;
	}
	
	public double getBalance(){
		return _balance;
	}

	public void setCredit(double credit){
		_credit = credit;
	}
	
	public double getCredit(){
		return _credit;
	}
	
	public void setDebit(double debit){
		_debit = debit;
	}
	
	public double getDebit(){
		return _debit;
	}
}
