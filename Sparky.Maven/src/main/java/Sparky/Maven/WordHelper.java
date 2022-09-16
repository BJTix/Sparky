package Sparky.Maven;

import java.util.ArrayList;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
//import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;

public class WordHelper {
	//=============================================================================================
	//---------------------------------------------------------------------------------------------
	//	Static Word Assessment Functions
	//---------------------------------------------------------------------------------------------
	//=============================================================================================
	public static boolean isPOS(String wrd, POS _pos) throws JWNLException  {
		IndexWord[] wordArray = JWNL_Helper.getAllWords(wrd);
		int len = wordArray.length;
		for(int i = 0; i < len; i++) {
			IndexWord myWord = wordArray[i];
			if(myWord.getPOS() == _pos) return true;
		}
		return false;
	}
	public static boolean isAdjective(String wrd) throws JWNLException {
		return isPOS(wrd, POS.ADJECTIVE);
	}
	public static boolean isNoun(String wrd) throws JWNLException {
		return isPOS(wrd, POS.NOUN);
	}
	public static boolean isVerb(String wrd) throws JWNLException {
		return isPOS(wrd, POS.VERB);
	}
	public static boolean isAdverb(String wrd) throws JWNLException {
		return isPOS(wrd, POS.ADVERB);
	}
	public static boolean isPreposition(String wrd) {
		//Prepositions taken from https://www.citationmachine.net/resources/grammar-guides/preposition/prepositions-list/
		//Match a set list of prepositions:
		String lword = wrd.toLowerCase();
		switch(lword) { 
			case "about":
			case "above":	
			case "across":	
			case "after":	
			case "ago":	
			case "at":	
			case "back":	
			case "below":	
			case "by":	
			case "down":	
			case "during":	
			case "for":	
			case "from":	
			case "in":	
			case "into":	
			case "of":	
			case "off":	
			case "on":	
			case "oer":	
			case "past":	
			case "since":	
			case "through":	
			case "to":	
			case "under":	
			case "until":	
			case "up":	
			case "with":
			//Double Prepositions:
			case "amid":
			case "atop":
			case "inside":
			case "onto":
			case "outside":
			case "throughout":
			case "upon":
			case "within":
			case "without":
			//participle prepositions:
			case "considering":
			case "concerning":
			//case "during": //Already in list
			case "excluding":
			case "following":
			case "including":
			case "regarding":
					return true;
			default: return false;
		}
	}
	/**
	 * Determine whether a two-word phrase is a compound preposition.
	 * @param wrd1 The first word
	 * @param wrd2 The second word
	 * @return True if the two words form a preposition phrase.
	 */
	public static boolean isPrepositionPhrase(String wrd1, String wrd2) {
		//Prepositions taken from https://www.citationmachine.net/resources/grammar-guides/preposition/prepositions-list/
		String lw1 = wrd1.toLowerCase();
		String lw2 = wrd2.toLowerCase();
		switch(lw2) {
			//"to" phrases
			case "to":
				switch(lw1) {
					case "according":
					case "close":
					case "due":
					case "near":
					case "owing":
					case "prior":
					case "relative":
					case "right":
					case "subsequent":
					case "thanks":
					case "through":
						return true;
				}
			//"of" phrases
			case "of":
				switch(lw1) {
					case "ahead":
					case "because":
					case "inside":
					case "instead":
					case "out":
					case "outside":
					case "right":
						return true;
				}
			//"for" phrases
			case "for":
				switch(lw1) {
					case "apart":
					case "as":
					case "except":
						return true;
				}
			//"from" phrases
			case "from":
				switch(lw1) {
					case "apart":
					case "aside":
					case "out":
						return true;
				}
			//"As" phrases
			case "as":
				switch(lw1) {
					case "where":
						return true;
				}
			//"than" phrases
			case "than":
				switch(lw1) {
					case "rather":
						return true;
				}
			//"per" phrases
			case "per":
				switch(lw1) {
					case "as":
						return true;
				}
			default: return false;
		}
	}
	/**
	 * Determine whether a three-word phrase is a compound preposition.
	 * @param wrd1 The first word
	 * @param wrd2 The second word
	 * @return True if the two words form a preposition phrase.
	 */
	public static boolean isPrepositionPhrase(String wrd1, String wrd2, String wrd3) {
		//Prepositions taken from https://www.citationmachine.net/resources/grammar-guides/preposition/prepositions-list/
		String phrase = wrd1.toLowerCase() + " " + wrd2.toLowerCase() + " " + wrd3.toLowerCase();
		switch(phrase){
			case "as far as":
			case "as long as":
			case "as soon as":
			case "as well as":
			case "in addition to":
			case "in regard to":
			case "in spite of":
			case "on top of":
			case "with regard to":
			case "with the exception of": //note, this is not a 3 word phrase! Probably need to re-work the function to take a list, or make a 4-word test.
				return true;
			default: return false;
		}
	}
	public static boolean isDeterminer(String wrd) {
		
		//Match a set list of prepositions:
		String lword = wrd.toLowerCase();
		switch(lword) { 
			case "a":
			case "an":	
			case "that":	
			case "the":	
			case "these":	
			case "this":	
			case "those":	
					return true;
			default: return false;
		}
	}
	public static boolean isPossessive(String wrd) {
		
		//Match a set list of prepositions:
		String lword = wrd.toLowerCase();
		switch(lword) { 
			case "his":
			case "her":	
			case "their":	
			case "my":	
			case "your":	
			case "our":	
			case "its":	
					return true;
			default: return false;
		}
	}
	public static boolean isConjunction(String wrd) {

		//Match a set list of conjunctions:
		String lword = wrd.toLowerCase();
		switch(lword) { 
			case "for":
			case "and":	
			case "nor":	
			case "but":	
			case "or":		
			case "yet":	
			case "so":
					return true;
			default: return false;
		}
	}
	
	public static boolean isNumber(String wrd) {
		if (isNumeric(wrd)) return true;
		String lword = wrd.toLowerCase();
		switch(lword) { 
		case "zero":
		case "one":
		case "two":
		case "three":
		case "four":
		case "five":
		case "six":
		case "seven":
		case "eight":
		case "nine":
		case "ten":
		case "eleven":
		case "twelve":
		case "thirteen":
		case "fourteen":
		case "fifteen":
		case "sixteen":
		case "seventeen":
		case "eighteen":
		case "nineteen":
		case "twenty":
		case "thirty":
		case "fourty":
		case "fifty":
		case "sixty":
		case "seventy":
		case "eighty":
		case "ninety":
		case "hundred":
		case "thousand":
		case "million":
		case "billion":
		case "trillion":
			return true;
		default: return false;
		}
	}
	
	/**
	 * Adapted from https://www.baeldung.com/java-check-string-number.
	 * Checks for numeric characters.
	 * @param strNum
	 * @return
	 */
	private static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static Word[] allSenses(String wrd, POS _pos) throws JWNLException  {
		ArrayList<Word> results = new ArrayList<Word>();
		IndexWord[] wordArray = JWNL_Helper.getAllWords(wrd);
		int len = wordArray.length;
		for(int i = 0; i < len; i++) {
			IndexWord myWord = wordArray[i];
			Synset[] senses = myWord.getSenses();
			for(int j = 0; j < senses.length; j++) {
				Word[] words = senses[j].getWords();
				for(int k = 0; k < words.length; k++) {
					if(_pos == words[k].getPOS()) results.add(words[k]);
				}
			}
		}
		Word[] resultArray = new Word[results.size()];
		return results.toArray(resultArray);
	}
	public static void printAllSenses(String wrd, POS _pos) throws JWNLException  {
		Word[] allsenses = allSenses(wrd, _pos);
		for(int i = 0; i < allsenses.length; i++) {
			System.out.println(allsenses[i]);
		}
	}
	public static void printAllSenses(String wrd) throws JWNLException  {
		printAllSenses(wrd, POS.ADJECTIVE);
		printAllSenses(wrd, POS.ADVERB);
		printAllSenses(wrd, POS.NOUN);
		printAllSenses(wrd, POS.VERB);
	}
	public static boolean Synonyms(String wrd1, String wrd2, POS _pos) throws JWNLException {
		Word[] allSenses1 = allSenses(wrd1, _pos);
		for(int i = 0; i < allSenses1.length; i++) {
			//System.out.println(allSenses1[i].getLemma().toLowerCase());
			if(allSenses1[i].getLemma().toLowerCase().compareTo(wrd2.toLowerCase()) == 0) return true;
		}
		return false;
	}
}
