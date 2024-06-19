public class UnitData {
	private String currency1, currency_symbol, factor;

	UnitData(){}
	UnitData(String c1, String cs, String fa)
	{
		currency1 = c1;
		currency_symbol = cs;
		factor = fa;
	}
	
	public String getCurrency1() {
		return currency1;
	}

	public void setCurrency1(String currency1) {
		this.currency1 = currency1;
	}

	public String getCurrency_symbol() {
		return currency_symbol;
	}

	public void setCurrency_symbol(String currency_symbol) {
		this.currency_symbol = currency_symbol;
	}

	public String getFactor() {
		return factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}

}
