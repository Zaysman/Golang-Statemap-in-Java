package Objects;

public class Literal {
	private String lex; // The lexeme that was analyzed.
	private String type; //What the lexeme was identified as.
	
	public Literal() {
		this.lex = new String();
		this.type = new String();
	}
	
	public Literal(String l, String t) {
		this.lex = l;
		this.type = t;
		
	}
	
	public String getLex() {
		return this.lex;
	}
	
	public String getType() {
		return this.type;
	}
	
	
	public void setLex(String l) {
		this.lex = l;
	}
	
	public void setType(String t) {
		this.type = t;
	}
	
	public String toString() {
		return this.lex + " - " + this.type;
	}
	
	
}
