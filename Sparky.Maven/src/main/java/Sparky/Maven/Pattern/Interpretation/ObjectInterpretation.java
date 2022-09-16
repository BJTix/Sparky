package Sparky.Maven.Pattern.Interpretation;
/**
 * 2022-09-12
 * @author Bernadette Jzexoia Tix
 * Logical interpretation of the "object" part of a clause.
 */

import Sparky.Maven.Pattern.Phrase;

public class ObjectInterpretation {
	public DescribedWord MyDescribedWord;
	public Phrase PrepositionPhrase;
	
	@Override
	public String toString() {
		return MyDescribedWord.toString() + System.lineSeparator() + "Preposition Phrase: " + PrepositionPhrase;
	}
	
	public ObjectInterpretation(DescribedWord dw) {
		MyDescribedWord = dw;
		PrepositionPhrase = new Phrase();
	}
	public ObjectInterpretation() {
		this(new DescribedWord());
	}
}
