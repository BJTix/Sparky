package Sparky.Maven.Pattern;
/**
 * 
 * @author Bernadette Jzexoia Tix
 * Allows us to collapse quotes and treat them as a single node within pattern matching.
 */
public class Quote extends Word {

	public String fullQuote;
	public Quote(String newWord) {
		super("[quote]");
		fullQuote = newWord;
	}
	@Override
		public String toString() {
			return fullQuote;
		}
	
}
