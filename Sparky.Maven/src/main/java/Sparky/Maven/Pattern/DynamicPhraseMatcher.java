package Sparky.Maven.Pattern;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import Sparky.Maven.Pattern.Interpretation.ClauseInterpretation;
import Sparky.Maven.Pattern.Interpretation.InterpretationCombinationRule;
import net.didion.jwnl.JWNLException;

public class DynamicPhraseMatcher extends PhraseMatcher {

	/**
	 * This list of lists is essentially a list of all the possible sequences the dynamic match can match to.
	 * Since each sequence is a list of the abstract class PhraseMatch, sequences can be composed of 
	 * singleWordMatches, StaticPhraseMatches,or other DynamicPhraseMatches.
	 */	
	ArrayList<ArrayList<PhraseMatcher>> sequences;
	
	public DynamicPhraseMatcher(String p_code, String p_name, boolean p_optional, int p_NestingLevel, InterpretationCombinationRule ir) {
		super(p_code, p_name, p_optional);
		NestingLevel = p_NestingLevel;
		interpretationRule = ir;
	}
	public DynamicPhraseMatcher(String p_code, String p_name, int p_NestingLevel, InterpretationCombinationRule ir) {
		this(p_code, p_name, false, p_NestingLevel, ir);
	}
	
	/**
	 * Create a new sequence
	 * @return the index of the sequence.
	 */
	public DynamicPhraseMatcher createSequence(){
		if(sequences == null) sequences = new ArrayList<ArrayList<PhraseMatcher>>();
		sequences.add(new ArrayList<PhraseMatcher>());
		return this;
	}
	
	
	@Override
	public ArrayList<Phrase> findAllMatches(Phrase source, int start) throws JWNLException {
		//SUPER SPECIAL SECRET BASE CASES!!
		if(source.size() <= start) return new ArrayList<Phrase>();
		if(null == source || 0 == source.size()) return new ArrayList<Phrase>();
		
		//declare an empty result set:
		ArrayList<Phrase> results = new ArrayList<Phrase>();
		//Call the recursive function to evaluate every possible branch:
		for(ArrayList<PhraseMatcher> seq : sequences) {
			results.addAll(findAllMatchesRecursive(source, start, seq, new Phrase()));
		}
		return results;
	}
	
	private ArrayList<Phrase> findAllMatchesRecursive(Phrase source, int start, ArrayList<PhraseMatcher> sequence_remaining, Phrase result_so_far) throws JWNLException{
		
		//Input Washing:
		if(null == source || 0 == source.size()) throw( new InvalidParameterException("Source Phrase cannot be empty."));
		if(start < 0) throw( new InvalidParameterException("Starting index must be positive."));
		if(null == sequence_remaining)  throw( new InvalidParameterException("Match Sequence cannot be null."));

		//Failure base cases / function is working but no match found (return an empty set):
		if(source.size() <= start) return new ArrayList<Phrase>();
		
		//If there are no more items left in the sequence, we are done. 
		if(0 == sequence_remaining.size()) throw( new InvalidParameterException("Match sequence must not be empty."));
		
		//Assuming no error, we always need to find the next set of matches:
		ArrayList<Phrase> new_results = sequence_remaining.get(0).findAllMatches(source, start + result_so_far.size());
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//BASE CASE::
		//If no matches were found, this branch is a dead end and we should return null or an empty set.
		//If the matcher is optional in this sequence, we don't need to return.
		if((null == new_results || new_results.isEmpty()) && false == optional) return new_results;
		
		ArrayList<Phrase> step_results = new ArrayList<Phrase>();
		//The results so far is just one phrase, but the new results are a list of phrases. We combine theses now:
		for(Phrase p : new_results) {
			//p.typecode = Code;
			step_results.add(Phrase.CreateCompositePhrase(result_so_far, p, Code, interpretationRule));
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//BASE CASE::
		//If there is only one item left in the sequence, then get the match from that item, append it to the results, and
		//return the final results.
		if(1 == sequence_remaining.size() && !new_results.isEmpty()) {
			//Since we had only one remaining result, these are the final results and can be returned.
			return step_results;
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// RECURSIVE CASE
		//Done with the base cases. Now onto the recursive case. 
		//If there are multiple phrase matches remaining in the sequence:
		else {
			//first off, we are done with element 0 of the remaining sequence:
			//It's important to first make a copy so we don't disrupt the original object.
			ArrayList<PhraseMatcher> new_sequence_remaining = new ArrayList<PhraseMatcher>();
			new_sequence_remaining.addAll(sequence_remaining);
			new_sequence_remaining.remove(0);
			//We will now need to recursively call this function once for each of the possible branches in the new result set.
			ArrayList<Phrase> all_results = new ArrayList<Phrase>();
			for(Phrase branch : step_results) {
				//the full results are all the new results found from all possible branches, appended to the results so far.
				all_results.addAll(findAllMatchesRecursive(source, start, new_sequence_remaining, branch));
			}
			return all_results;
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////
	//	Convenience functions for easy construction:
	///////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Adds a new single word match to the most recently constructed sequence
	 * @param pos the type of match to add.
	 * @return a reference to this dynamic phrase match, to allow easy adding of new match nodes.
	 */
	public DynamicPhraseMatcher add(ExtendedPOS pos) {
		if(sequences == null || 0 == sequences.size()) createSequence();
		sequences.get(sequences.size() - 1).add(SingleWordMatcher.get(pos));
		return this;
	}
	/**
	 * Adds a new optional single word match to the most recently constructed sequence
	 * @param pos the type of match to add.
	 * @return a reference to this dynamic phrase match, to allow easy adding of new match nodes.
	 */
	public DynamicPhraseMatcher addOptional(ExtendedPOS pos) {
		if(sequences == null || 0 == sequences.size()) createSequence();
		sequences.get(sequences.size() - 1).add(SingleWordMatcher.getOptional(pos));
		return this;
	}
	/**
	 * Add a dynamic matcher to the most recently created sequence
	 * @param dynm The dynamic match to add.
	 * @return a reference to this dynamic phrase match, to allow easy adding of new match nodes.
	 * @throws Exception Throws an exception for nesting level violations.
	 */
	public DynamicPhraseMatcher add(DynamicPhraseMatcher dynm) throws Exception {
		//As a safety measure to reduce the risk of infinite loops, you can only add a dynamic phrase with a lower nesting level, or a self-reference.
		if(NestingLevel <= dynm.NestingLevel && Code != dynm.Code) throw new Exception
			("Nesting Level violation. Trying to add [" + dynm.Code + "](Nesting Level " +dynm.NestingLevel + ") to [" + Code + "](Nesting Level " + NestingLevel + ")");
		if(sequences == null || 0 == sequences.size()) createSequence();
		sequences.get(sequences.size() - 1).add(dynm);
		return this;
	}
	
	/**
	 * Adds a recursive self-reference to the most recent sequence.
	 * @return a reference to this dynamic phrase match, to allow easy adding of new match nodes.
	 * @throws Exception Throws an error when trying to add a self-reference as the first element in a sequence.
	 */
	public DynamicPhraseMatcher addSelf() throws Exception {
		///NEVER allow self-add on the first entry in a list.
		if(sequences == null || 0 == sequences.size() || 0 == sequences.get(sequences.size() - 1).size()) {
			throw new Exception("A Dynamic Phrase Match cannot have a self-reference as the first element in a phrase match sequence.");
		}
		return add(this);
	}
	/**
	 * Add a dynamic phrase match from the phrase match library to the most recently created sequence 
	 * @param p_code the phrase match code
	 * @return a reference to this dynamic phrase match, to allow easy adding of new match nodes.
	 * @throws Exception
	 */
	public DynamicPhraseMatcher add(String p_code) throws Exception {
		if(p_code == Code) return addSelf();
		else return add((DynamicPhraseMatcher)getFromLibrary(p_code));
	}

	/**
	 * Add an optional dynamic phrase match from the phrase match library to the most recently created sequence 
	 * @param p_code the phrase match code
	 * @return a reference to this dynamic phrase match, to allow easy adding of new match nodes.
	 * @throws Exception
	 */
	public DynamicPhraseMatcher addOptional(String p_code) throws Exception {
		if(p_code == Code) return addSelf();
		else return add((DynamicPhraseMatcher)getOptional(p_code));
	}
	
	public DynamicPhraseMatcher Noun() {
		return add(ExtendedPOS.Noun);
	}
	public DynamicPhraseMatcher Verb() {
		return add(ExtendedPOS.Verb);
	}
	public DynamicPhraseMatcher Adjective() {
		return add(ExtendedPOS.Adjective);
	}
	public DynamicPhraseMatcher Adverb() {
		return add(ExtendedPOS.Adverb);
	}
	public DynamicPhraseMatcher Preposition() {
		return add(ExtendedPOS.Preposition);
	}
	public DynamicPhraseMatcher Determiner() {
		return add(ExtendedPOS.Determiner);
	}
	public DynamicPhraseMatcher Possessive() {
		return add(ExtendedPOS.Possessive);
	}
	
	public String sequenceReadout() {
		String result = "------------------------------------------------" + System.lineSeparator()
			+ Name + " [" + Code + "] Sequence Readout" + System.lineSeparator();
		for(int i = 0; i < sequences.size(); i ++) {
			result += i + ": ";
			ArrayList<PhraseMatcher> s = sequences.get(i);
			for(PhraseMatcher p : s) result += "[" + p.Code + "]";
			result += System.lineSeparator();
		}
		result += "------------------------------------------------";
		return result;
	}
	
	@Override
	public PhraseMatcher copy(String p_code, String p_name, boolean p_optional) {
		//start by creating a new phrase match object with the given code and name:
		DynamicPhraseMatcher result = new DynamicPhraseMatcher(p_code, p_name, p_optional, NestingLevel, interpretationRule);
		//then copy all values over:
		result.sequences = sequences;
		//return the result
		return result;
	}

}
