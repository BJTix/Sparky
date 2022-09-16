package Sparky.Maven.Pattern;

import java.util.ArrayList;

import org.apache.bcel.classfile.Code;

import Sparky.Maven.Pattern.Interpretation.ClauseInterpretation;
import Sparky.Maven.Pattern.Interpretation.InterpretationCombinationRule;
import net.didion.jwnl.JWNLException;
/**
 * 2022-08-26
 * @author Bernadette Jzexoia Tix
 * A sequence of words, plus some helpful utility functions to make it easier to pass phrases around during processing.
 * 
 * 2022-08-27 BJT	Added size and get pass-through functions.
 * 2022-08-30 BJT	Added typecode and incorporated into toString function.
 * 2022-09-14 BJT	Added an interpretation to each phrase.
 */
public class Phrase {
	public ArrayList<Word> words;
	public ArrayList<Phrase> subphrases;
	public String typecode;
	public ClauseInterpretation interpretation;
	public InterpretationCombinationRule interpretationRule;
	
	public static char OpenBracket = '[';
	public static char CloseBracket = ']';
	
	public Phrase(InterpretationCombinationRule rule){
		words = new ArrayList<Word>();
		subphrases = new ArrayList<Phrase>();
		interpretation = new ClauseInterpretation();
		interpretationRule = rule;
	}
	public Phrase() {
		this(InterpretationCombinationRule.Wordbag);
	}
	
	public void updateInterpretation() throws JWNLException {
		interpretation.initialize(this);
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//	Convenience functions for easy construction:
	///////////////////////////////////////////////////////////////////////////////////////////////////
	public Phrase add(Word w) throws JWNLException {
		words.add(w);
		//update the interpretation:
		updateInterpretation();
		return this;
	}
	public Phrase appendWords(ArrayList<Word> w, boolean AppendAtBeginning) {
		if(null == w || w.isEmpty()) return this;
		if(AppendAtBeginning) words.addAll(0,w);
		else words.addAll(w);
		return this;
	}
	public Phrase appendPhrase(Phrase p, boolean AppendAtBeginning) {
		if(null == p || p.isEmpty()) return this;
		appendWords(p.words,AppendAtBeginning);
		if(AppendAtBeginning) subphrases.add(0, p);
		else subphrases.add(p);
		return this;
	}
	public Phrase appendAllSubphrases(Phrase p) {
		//input washing
		if(null == p || p.isEmpty()) return this;
		if(p.subphrases.isEmpty()) return this;
		else {
			for(Phrase sp : p.subphrases) {
				appendPhrase(sp);
			}
		}
		return this;
	}
	public Phrase appendPhrase(Phrase p) {
		return appendPhrase(p, false);
	}
	public boolean isEmpty() {
		return words.isEmpty();
	}
	/**
	 * A composite phrase combines the subphrases of two pre-existing phrases
	 * @param p1 One of the phrases to combine
	 * @param p2 The other phrase to combine
	 * @param newcode the code for the new phrase
	 * @param rule The rule for combining phrase interpretations
	 * @return
	 * @throws JWNLException 
	 */
	public static Phrase CreateCompositePhrase(Phrase p1, Phrase p2, String newcode, InterpretationCombinationRule rule) throws JWNLException {		
		Phrase result = new Phrase(rule);	
		result.typecode = newcode;
		//if p1 has words but no substrings add it as its own phrase
		//Also add it as a whole phrase if it is of a different type.
		if(null == p1 || p1.isEmpty()) {/*Do nothing for an empty phrase!*/}
		else if(p1.subphrases.isEmpty() || p1.typecode != newcode) result.appendPhrase(p1); 
		else result.appendAllSubphrases(p1);
		//if p2 has words but no substrings, add it as its own phrase
		//Also add it as a whole phrase if it is of a different type.
		if(null == p2 || p2.isEmpty()) {/*Do nothing for an empty phrase!*/}
		else if(p2.subphrases.isEmpty() || p2.typecode != newcode) result.appendPhrase(p2); 
		else result.appendAllSubphrases(p2);
		//Perform interpretation combination:
		result.interpretation = p1.interpretation.Combine(p2.interpretation, rule);
		//return final results:
		return result;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//	Print and display functions:
	///////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String toString() {
		return FlatStringWithCode();
	}
	
	/**
	 * Print out the phrase cleanly in plain English, not as a list.
	 * @param sentence If true, capitalize first letter and add a period. If false, do neither.
	 * @return a plain English string of the phrase.
	 */
	public String FlatString(boolean sentence) {
		String result = "";
		for(int i = 0; i < words.size(); i++) {
			result += words.get(i).toString();
			if(i == words.size() - 1) {
				if(sentence) result += ".";
			}
			else result += " ";
		}
		//Capitalize the first letter:
		if(sentence) result = result.substring(0, 1).toUpperCase() + result.substring(1);
		return result;
	}
	
	/**
	 * Get a string of the phrase cleanly in plain English, not as a list. Include the type code.
	 * @param sentence If true, capitalize first letter and add a period. If false, do neither.
	 * @return a plain English string of the phrase, plus the typecode, all in brackets.
	 */
	public String FlatStringWithCode() {
		if(null == typecode || 0 == typecode.length()) return words.toString();
		if(null == words || words.isEmpty()) return "";
		else {
			String result = OpenBracket + typecode + ": ";
			for(int i = 0; i < words.size(); i++) {
				result += words.get(i).toString(); 
				if(i < words.size() - 1) result += " ";
			}
			result += CloseBracket;
			return result;
		}
	}
	/**
	 * @return A string listing each of the flatstring (with code) of each subphrase of this phrase.
	 */
	public String TwoLayerTreeString(){
		if(null == subphrases || subphrases.isEmpty()) return FlatStringWithCode();
		else {
			String result = OpenBracket + typecode + ": ";
			for(Phrase p : subphrases) {
				result += p.FlatStringWithCode();
			}
			result += CloseBracket;
			return result;
		}
		
	}
	/**
	 * @return A string representing the full tree of this phrase and all subphrases
	 */
	public String FullTreeString(){
		if(null == subphrases || subphrases.isEmpty()) return FlatStringWithCode();
		else {
			String result = OpenBracket + typecode + ": ";
			for(Phrase p : subphrases) {
				result += p.FullTreeString();
			}
			result += CloseBracket;
			return result;
		}	
	}
	
	public int TreeDepth() {
		//if there are no subphrases, the tree depth is 1
		if(null == subphrases || subphrases.isEmpty()) return 1;
		//Otherwise, get 1 + the maximum tree depth of each subphrase:
		int max = 1;
		for(Phrase p : subphrases) {
			int depth = p.TreeDepth();
			if(max < depth) max = depth;
		}
		return max + 1;
	}
	
	public String DetailedReadout() {
		String result = "=======================================================" + System.lineSeparator()
			+ typecode + " Detailed Readout" + System.lineSeparator()
			+ FlatString(false) + System.lineSeparator();
		int treeDepth = TreeDepth();
		if(2 <= treeDepth) result += TwoLayerTreeString() + System.lineSeparator();
		if(3 <= treeDepth) result += FullTreeString() + System.lineSeparator();
		if(null != interpretation) result += interpretation + System.lineSeparator();
		result += "=======================================================";
		return result;
	}
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//	Convenience pass-through functions:
	///////////////////////////////////////////////////////////////////////////////////////////////////
	public int size() {
		return words.size();
	}
	public Word get(int i) {
		return words.get(i);
	}
}
