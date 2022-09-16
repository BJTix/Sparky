package Sparky.Maven.Pattern;
import java.util.ArrayList;

import net.didion.jwnl.JWNLException;

/**
 * 2022-08-26
 * @author Bernadette Jzexoia Tix
 * A static phrase match matches an exact sequence of word types, and is always the same length, 
 * 	as opposed to a dynamic phrase match which could match various possible word type sequences of varying lengths.
 */
public class StaticPhraseMatcher extends PhraseMatcher {
	private ArrayList<SingleWordMatcher> sequence;
	public StaticPhraseMatcher(String p_code, String p_name, boolean p_optional) {
		super(p_code, p_name, p_optional);
		sequence = new ArrayList<SingleWordMatcher>();
		NestingLevel = 0;
	}
	public StaticPhraseMatcher(String p_code, String p_name) {
		this(p_code, p_name, false);
	}
	/**
	 * Adds a new single word match to the sequence
	 * @param pos the type of match to add.
	 * @return a reference to this static phrase match, to allow easy adding of new match nodes.
	 */
	public StaticPhraseMatcher add(ExtendedPOS pos) {
		sequence.add(SingleWordMatcher.get(pos));
		return this;
	}
	/**
	 * For a static phrase match, all we need to do in order to check for a match is to check the words
	 * in the phrase one at a time and see if each one matches the remplate in our static list.
	 * @throws JWNLException 
	 */
	@Override
	public ArrayList<Phrase> findAllMatches(Phrase source, int start) throws JWNLException {
		//Input washing:
		//If the search would take us past the end, we can return null
		int length = sequence.size();
		int end = start + length;
		if(source.size() < end) return new ArrayList<Phrase>();
		//Assuming we got past input washing, we just look at the words one by one.
		for(int i = 0; i < length; i++) {
			//any failure of one part is a failure of the whole.
			if(!sequence.get(i).matches(source.get(start + i))) return new ArrayList<Phrase>();
		}
		//If we've gotten through with no failures, then we can assign and return the match.
		match = new Phrase();
		match.typecode = Code;
		for(int i = 0; i < length; i++) {
			match.add(source.words.get(start + i));
		}
		
		ArrayList<Phrase> result = new ArrayList<Phrase>();
		result.add(match);
		return result;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//	Convenience functions for easy construction:
	///////////////////////////////////////////////////////////////////////////////////////////////////
	public StaticPhraseMatcher Noun() {
		return add(ExtendedPOS.Noun);
	}
	public StaticPhraseMatcher Verb() {
		return add(ExtendedPOS.Verb);
	}
	public StaticPhraseMatcher Adjective() {
		return add(ExtendedPOS.Adjective);
	}
	public StaticPhraseMatcher Adverb() {
		return add(ExtendedPOS.Adverb);
	}
	public StaticPhraseMatcher Preposition() {
		return add(ExtendedPOS.Preposition);
	}
	public StaticPhraseMatcher Determiner() {
		return add(ExtendedPOS.Determiner);
	}
	
	@Override
	public PhraseMatcher copy(String p_code, String p_name, boolean p_optional) {
		//start by creating a new phrase match object with the given code and name:
		StaticPhraseMatcher result = new StaticPhraseMatcher(p_code, p_name, p_optional);
		//then copy all values over:
		result.sequence = sequence;
		result.interpretationRule = interpretationRule;
		//return the result
		return result;
	}
}
