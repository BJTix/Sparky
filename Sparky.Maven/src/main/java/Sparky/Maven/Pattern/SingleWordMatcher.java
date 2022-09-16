package Sparky.Maven.Pattern;

import java.util.ArrayList;

import Sparky.Maven.Pattern.Interpretation.ClauseInterpretation;
import Sparky.Maven.Pattern.Interpretation.InterpretationCombinationRule;
import net.didion.jwnl.JWNLException;

/**
 * Matches a single word to a single word type.
 * @param pos The type of word to match.
 */
public class SingleWordMatcher extends PhraseMatcher {
	private ExtendedPOS myPOS;
	public SingleWordMatcher(String code, String name, boolean p_optional) {
		super(code, name, p_optional);
	}
	private static SingleWordMatcher Create(ExtendedPOS pos) {
		InterpretationCombinationRule interpretationRule;
		switch(pos) {
			case Noun: 
				interpretationRule = InterpretationCombinationRule.SingleObject;
				break;
			case Verb: 
				interpretationRule = InterpretationCombinationRule.SingleAction;
				break;
			case Adjective: 
				interpretationRule = InterpretationCombinationRule.Adjectives; 
				break;
			case Adverb: 
				interpretationRule = InterpretationCombinationRule.Adverbs; 
				break;
			case Preposition: 
				interpretationRule = InterpretationCombinationRule.Wordbag;
				break;
			case Determiner: 
				interpretationRule = InterpretationCombinationRule.Wordbag;
				break;
			case Conjunction: 
				interpretationRule = InterpretationCombinationRule.Wordbag; 
				break;
			case Possessive: 
				interpretationRule = InterpretationCombinationRule.Wordbag; 
				break;
			default: 
				interpretationRule = InterpretationCombinationRule.Wordbag; 
				break;
		}
		SingleWordMatcher result = new SingleWordMatcher(getCode(pos), pos.toString(),false);
		result.myPOS = pos;
		result.NestingLevel = 0;
		result.interpretationRule = interpretationRule;
		return result;
	}
	public boolean matches(Word w) throws JWNLException {
		switch(myPOS) {
		case Noun: return w.isNoun();
		case Verb: return w.isVerb();
		case Adjective: return w.isAdjective();
		case Adverb: return w.isAdverb();
		case Preposition: return w.isPreposition();
		case Determiner: return w.isDeterminer();
		case Conjunction: return w.isConjunction();
		case Possessive: return w.isPossessive();
		default: return false;
		}
	}
	@Override
	public ArrayList<Phrase> findAllMatches(Phrase source, int start) throws JWNLException {
		/*
		 * For a single word match, finding all matches is pretty easy. There is only one word to check
		 * and only one or zero possible matches to return. The only reason we even need this method is
		 * for compatibility with PhraseMatch, which is important in order to include single words matches
		 * in dynamic phrase matching. 
		 */
		//Input washing for out of bounds exceptions:
		if(source == null) return new ArrayList<Phrase>();
		if (source.size() <= start) return new ArrayList<Phrase>();
		//perform the match and format the result:
		Word myWord = source.get(start);
		if(matches(myWord)) {
			Phrase myPhrase = new Phrase(interpretationRule);
			myPhrase.add(myWord);
			myPhrase.typecode = Code;
			myPhrase.interpretation.initialize(myPhrase);
			ArrayList<Phrase> result = new ArrayList<Phrase>();
			result.add(myPhrase);
			return result;
		}
		else return new ArrayList<Phrase>();
	}
	
	public static SingleWordMatcher get(ExtendedPOS pos) {
		return (SingleWordMatcher) getFromLibrary(getCode(pos));
	}
	
	public static SingleWordMatcher getOptional(ExtendedPOS pos) {
		return (SingleWordMatcher) getOptional(getCode(pos));
	}
	/**
	 * Create a set of single-word match objects, one for each part of speech.
	 */
	public static void Initialize() {
		Create(ExtendedPOS.Adjective);
		Create(ExtendedPOS.Adverb);
		Create(ExtendedPOS.Conjunction);
		Create(ExtendedPOS.Determiner);
		Create(ExtendedPOS.Noun);
		Create(ExtendedPOS.Possessive);
		Create(ExtendedPOS.Preposition);
		Create(ExtendedPOS.Verb);
	}
	
	/**
	 * Returns the phrase match code associated with each part of speech.
	 * @param pos the part of speech
	 * @return the phrase match code.
	 */
	public static String getCode(ExtendedPOS pos) { 

		switch(pos) {
			case Noun: 
				return "nn";
			case Verb: 
				return "vb"; 
			case Adjective: 
				return "adj";
			case Adverb: 
				return "adv";
			case Preposition: 
				return "prp"; 
			case Determiner: 
				return "det"; 
			case Conjunction: 
				return "cnj";
			case Possessive: 
				return "psv";
			default: 
				return "unknown";
		}
	}
	@Override
	public PhraseMatcher copy(String p_code, String p_name, boolean p_optional) {
		//start by creating a new phrase match object with the given code and name:
		SingleWordMatcher result = new SingleWordMatcher(p_code, p_name,p_optional);
		//then copy all values over:
		result.myPOS = myPOS;
		result.interpretationRule = interpretationRule;
		//return the result
		return result;
	}
	

}
