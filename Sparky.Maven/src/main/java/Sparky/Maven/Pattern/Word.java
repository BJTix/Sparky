package Sparky.Maven.Pattern;

import Sparky.Maven.WordHelper;
import net.didion.jwnl.JWNLException;

/**
 * 
 * @author Bernadette Jzexoia Tix
 * Created: 2022-08-17
 * Description: The simplest type of pattern node. This one matches a single exact word.
 */
public class Word {

	private String word;  

	public Word(String newWord) {
		word = newWord;
	}
	public boolean equals(PatternNode n) {
		return word.toLowerCase().equals(n.toString().toLowerCase());
	}
	public boolean equals(Word w) {
		return word.toLowerCase().equals(w.toString().toLowerCase());
	}
	@Override
	public String toString() {
		return word;
	}
	
	public Word copy() {
		return new Word(word);
	}

	//===========================================================
	// Word and WordNet Helper functions:
	//===========================================================
	public String GetWord() { return word;}
	
	public boolean isNoun() throws JWNLException { return WordHelper.isNoun(word); }
	public boolean isVerb() throws JWNLException { return WordHelper.isVerb(word); }
	public boolean isAdjective() throws JWNLException { return WordHelper.isAdjective(word); }
	public boolean isAdverb() throws JWNLException { return WordHelper.isAdverb(word); }
	public boolean isPreposition() { return WordHelper.isPreposition(word); }
	public boolean isPossessive() { return WordHelper.isPossessive(word); }
	public boolean isDeterminer() { return WordHelper.isDeterminer(word); }
	public boolean isConjunction() { return WordHelper.isConjunction(word); }
	
	
	
	
}
