package Sparky.Maven.Pattern;
import java.util.ArrayList;

import Sparky.Maven.Pattern.Interpretation.ClauseInterpretation;
import Sparky.Maven.Pattern.Interpretation.InterpretationCombinationRule;
import net.didion.jwnl.JWNLException;
/**
 * 2022-08-26
 * @author Bernadette Jzexoia Tix
 * Abstract class, serves as the basis for static and dynamic phrase matching. 
 * 
 * 2022-08-27 BTJ	findMatch now returns a list of results, not just one.
 * 					Added convenience functions for matching strings and calling findMatch with no index.
 */
public abstract class PhraseMatcher {
	public Phrase match;
	public boolean optional;
	
	/**
	 * Base constructor. Sets the code and adds the phrase to the library.
	 * @param p_code
	 */
	public PhraseMatcher(String p_code, String p_name, boolean p_optional) {
		//Input washing:
		if(null == p_code || p_code.isBlank() || null == p_name || p_name.isBlank())
			throw new IllegalArgumentException("Phrase Match must have a code and a name!");
		if(false == p_optional && p_code.contains("*"))
			throw new IllegalArgumentException("* is not allowed in phrase match codes.");
			
		//initialize the library if it has not been initialized:
		if(null == Library) InitializeLibrary();
		//Check if a phrase match with the same code already exists:
		for(PhraseMatcher p : Library)
			if(p_code == p.Code) throw new IllegalArgumentException("A Phrase Match with code " + p_code + " already exists!");
		
		Code = p_code;
		Name = p_name;
		optional = p_optional;
		Library.add(this);
				
	}
	/**
	 * Base constructor. Sets the code and adds the phrase to the library.
	 * @param p_code
	 */
	public PhraseMatcher(String p_code, String p_name) {
		//by default, optional is always false. There will be a separate method to get an optional copy of the phrase match
		this(p_code, p_name, false);
				
	}
	/**
	 * This is a method with a side effect. It will search a source phrase from a starting index.
	 * If there is a match to this phrase-matchers template, then the "match" value for the PhraseMatch will be
	 * set and that phrase will be returned.
	 * 
	 * Since there may be multiple possible matches, we need a list of possible phrases as the return result, 
	 * not just one phrase.
	 * 
	 * If there is no match, null will be returned.
	 * 
	 * @param source The phrase to search
	 * @param start the starting index for the search.
	 * @return A Phrase, or null.
	 * @throws JWNLException 
	 */
	public abstract ArrayList<Phrase> findAllMatches(Phrase source, int start) throws JWNLException;
	
	public abstract PhraseMatcher copy(String p_code, String p_name, boolean p_optional);
	public PhraseMatcher optionalCopy() {  
		PhraseMatcher result = copy(Code + "*", "Optional " + Name, true);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//	Convenience functions for easier matching against multiple formats or without a start index.
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<Phrase> findAllMatches(String source, int start) throws JWNLException
	{
		//Input Washing:
		if(source == null) return null;
		if(source.isEmpty()) return null;
		
		//extract the string into a phrase of words:
		Phrase parsedSource = Extractor.extractWords(source);
		return findAllMatches(parsedSource, start);	
	}
	public ArrayList<Phrase> findAllMatches(Phrase source) throws JWNLException
	{
		return findAllMatches(source,0);
	}
	public ArrayList<Phrase> findAllMatches(String source) throws JWNLException
	{
		return findAllMatches(source,0);		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Identifying information:
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * A short code describing the matcher (ex: "NNP" for Noun Phrase)
	 */
	protected String Code;
	/**
	 * The full name of the matcher (ex: "Noun Phrase")
	 */
	protected String Name;
	/**
	 * Nesting level is used to manage nesting infinite loops within a dynamic matchers
	 * (Currently unimplemented 
	 */
	protected int NestingLevel;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Interpretation:
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	InterpretationCombinationRule interpretationRule;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Library:
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static ArrayList<PhraseMatcher> Library;
	private static void InitializeLibrary() {
		Library = new ArrayList<PhraseMatcher>();
		//Create single-word matches:
		SingleWordMatcher.Initialize();
	}
	/**
	 * Returns an already created phrase matcher from the phrase match library.
	 * @param p_code the code of the phrase match
	 * @return the phrase matcher from the library.
	 */
	public static PhraseMatcher getFromLibrary(String p_code) {
		for(PhraseMatcher pm : Library) {
			if(pm.Code == p_code) return pm;
		}
		throw new IllegalArgumentException(p_code + " is not a Phrase Match that is currently stored in the Phrase Match Library.");
	}
	/**
	 * Returns the optional version of a phrase match.
	 * @param p_code the code of the original (non optional) phrase match.
	 * @return The optional version of the requested phrase match.
	 */
	public static PhraseMatcher getOptional(String p_code) {
		return getFromLibrary(p_code).optionalCopy();
	}
	
}
