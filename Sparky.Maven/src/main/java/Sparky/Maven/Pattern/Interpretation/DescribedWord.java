package Sparky.Maven.Pattern.Interpretation;

import java.util.ArrayList;

import Sparky.Maven.Pattern.Phrase;
import Sparky.Maven.Pattern.Word;
import net.didion.jwnl.JWNLException;
/**
 * 2022 09 12
 * @author Bernadette Jzexoia Tix
 *
 * A described word can be either a described noun or a described verb, along with a list of adverbs or adjectives.
 */
public class DescribedWord {
	public Word MainWord;
	public ArrayList<Word> Descriptors;
	public Word Determiner;
	private String MainWordLabel;
	private String DescriptorLabel;
	
	//Private constructors describe the general mechanism for initializing the object but cannot be invoked from the outside.
	private DescribedWord(String value) {
		MainWord = new Word(value);
		Descriptors = new ArrayList<>();
	}
	
	private DescribedWord(String value, String p_MainLabel, String p_DescLabel) {
		this(value);
		MainWordLabel = p_MainLabel;
		DescriptorLabel = p_DescLabel;
	}
	public DescribedWord() {
		this("");
	}
	
	//These factory methods are used to actually create new objects:
	public static DescribedWord NewDescribedNoun(String p_noun) {
		return new DescribedWord(p_noun, "Name", "Adjectives");
	}
	public static DescribedWord NewDescribedNoun(Word p_noun) {
		return NewDescribedNoun(p_noun.GetWord());
	}
	
	public static DescribedWord NewDescribedVerb(String p_verb) {
		return new DescribedWord(p_verb, "Action", "Adverbs");
	}
	public static DescribedWord NewDescribedVerb(Word p_verb) {
		return NewDescribedVerb(p_verb.GetWord());
	}
	
	//The toString method prints out a description of this interpretation:
	@Override
	public String toString() {
		if(isEmpty()) return "";
		String result = MainWordLabel + ": " + MainWord + System.lineSeparator() + "";
		if(null != Determiner && !Determiner.GetWord().isBlank()) result += "Determiner: " + Determiner + System.lineSeparator();
		if(null != Descriptors && !Descriptors.isEmpty()) {
			result += DescriptorLabel + ": ";
			for(int i = 0; i < Descriptors.size(); i++) {
				result += Descriptors.get(i);
				if(i+1 < Descriptors.size()) result += ", ";
			}
		}
		return result;
	}
	
	//Append methods just make it easier to fill out descriptors:
	public void AppendDescriptors(ArrayList<Word> words) throws JWNLException {
		for(Word w : words) {
			if(w.isAdjective()) Descriptors.add(w);
			else if(w.isDeterminer()) Determiner = w.copy();
		}
	}
	public void AppendDescriptors(Phrase p) throws JWNLException {
		AppendDescriptors(p.words);
	}
	
	/**
	 * Combine this described word with another described word.
	 * This will keep the main word of the calling object if there are multiple main words, but adopt the incoming mainword if the current one is blank or null
	 * It will also combine the list of descriptors.
	 * A new object is returned, the calling object will not be modified.
	 * @param dw The described word to combine with
	 * @return the combined described word.
	 * @throws JWNLException 
	 */
	public DescribedWord combine(DescribedWord dw) throws JWNLException {
		DescribedWord result = new DescribedWord("", MainWordLabel, DescriptorLabel);
		
		//Set the mainword
		if((null == MainWord || "" == MainWord.GetWord()) && null != dw.MainWord) {
			result.MainWord = dw.MainWord;
		}
		else {
			result.MainWord = MainWord;
		}
		
		//combine all descriptors:
		result.AppendDescriptors(Descriptors);
		result.AppendDescriptors(dw.Descriptors);
		
		//Combine Determiner:
		if(null != Determiner && !Determiner.GetWord().isBlank()) result.Determiner = Determiner;
		else result.Determiner = dw.Determiner;
		
		//return final results
		return result;
	}
	
	/**
	 * isEmpty function to determine if this is a blank / empty object
	 * @return true is the main word is blank or null and there are no descriptors. 
	 */
	public boolean isEmpty() {
		return (null == MainWord || MainWord.GetWord().isBlank())
			&& (null == Descriptors || Descriptors.isEmpty()); 
	}
}
