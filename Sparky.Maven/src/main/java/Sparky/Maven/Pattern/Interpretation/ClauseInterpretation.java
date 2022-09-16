package Sparky.Maven.Pattern.Interpretation;

import java.util.ArrayList;

import Sparky.Maven.Pattern.Phrase;
import Sparky.Maven.Pattern.Word;
import net.didion.jwnl.JWNLException;

/**
 * 2022 09 12
 * @author Bernadette Jzexoia Tix
 * A logical interpretation of a complete 1-verb phrase clause.
 * Includes a few extra pieces to help with clause construction as well.
 */
public class ClauseInterpretation {
	public DescribedWord Subject;
	public ArrayList<ObjectInterpretation> Objects;
	public DescribedWord Action;
	public DescribedWord UnknownItem;
	public ArrayList<Word> wordbag;
	
	//A basic constructor just to get a few objects initialized.
	public ClauseInterpretation(){
		wordbag = new ArrayList<Word>();
		UnknownItem = DescribedWord.NewDescribedNoun("");
		Objects = new ArrayList<ObjectInterpretation>();
		Action = DescribedWord.NewDescribedVerb("");
		Subject = DescribedWord.NewDescribedNoun("");
	}
	
	/**
	 * A base-case interpretation that is initialized from a rule and a phrase. 
	 * Will initialize wither the wordbag, unknownItem, or Action of the clause using the words in the phrase
	 * @param p The phrase to start with.
	 * @param rule The rule to use.
	 * @throws JWNLException 
	 */
	public void initialize(Phrase p) throws JWNLException {
		//reset the interpretation so we don't double-up what we already have:
		wordbag = new ArrayList<Word>();
		UnknownItem = DescribedWord.NewDescribedNoun("");
		Objects = new ArrayList<ObjectInterpretation>();
		Action = DescribedWord.NewDescribedVerb("");
		Subject = DescribedWord.NewDescribedNoun("");
		//re-initialize the interpretation:
		switch(p.interpretationRule) {
		case Adjectives:
			UnknownItem.AppendDescriptors(p);
			break;
		case Adverbs:
			Action.AppendDescriptors(p);
			break;
		case SingleAction:
			Action.MainWord = p.get(0);
			break;
		case SingleObject:
			UnknownItem.MainWord = p.get(0);
			break;
		case Wordbag:
		default:
			wordbag.addAll(p.words);
			break;
		}
	}
	
	/**
	 * This is the core functionality of the class, and the key to making the whole thing work.
	 * This allows us to combine two clause phrases, and if implemented correctly should allow 
	 * pattern matchers to build interpretations of increasing complexity starting from simple parts.
	 * @param c the clause interpretation to combine this one with.
	 * @param rule the rule that will govern the combination
	 * @throws JWNLException 
	 */
	public ClauseInterpretation Combine(ClauseInterpretation c, InterpretationCombinationRule rule) throws JWNLException {
		ClauseInterpretation result = new ClauseInterpretation();
		switch(rule) {
		
		//The simplest possible option, just combining wordbags.
		case Wordbag:
			result.wordbag.addAll(wordbag);
			result.wordbag.addAll(c.wordbag);
			return result;
			
		//For a set of adjectives, combine them within the unknown item.
		case Adjectives:
		case SingleObject:
			result.UnknownItem = UnknownItem.combine(c.UnknownItem);
			result.UnknownItem.AppendDescriptors(wordbag);
			result.UnknownItem.AppendDescriptors(c.wordbag);
			return result;
			
		//For a set of adverbs, combine them within the action.
		case Adverbs:
		case SingleAction: 
			result.Action = Action.combine(c.Action);
			result.Action.AppendDescriptors(wordbag);
			result.Action.AppendDescriptors(c.wordbag);
			return result;
			
		//Getting unknown object sorted into object/subject
		case Object:
			result.Action = Action.combine(c.Action);
			result.Objects.add(new ObjectInterpretation(UnknownItem));
			result.Objects.add(new ObjectInterpretation(c.UnknownItem));
			result.Objects.addAll(Objects);
			result.Objects.addAll(c.Objects);
		case Subject:
			result.Subject = Subject.combine(c.Subject).combine(UnknownItem).combine(c.UnknownItem);
			result.Subject.AppendDescriptors(wordbag);
			result.Subject.AppendDescriptors(c.wordbag);
			
		//For the full clause combination, combine actions and subjects but make a list of objects:
		case FullClause:
			result.Action = Action.combine(c.Action);
			result.Subject = Subject.combine(c.Subject);
			result.Objects.addAll(Objects);
			result.Objects.addAll(c.Objects);
			result.UnknownItem = UnknownItem.combine(c.UnknownItem);
			result.wordbag.addAll(wordbag);
			result.wordbag.addAll(c.wordbag);
			return result;
		default:
			return null;
		}	
	}
	
	@Override
	public String toString() {
		String result = "";
		if(!Subject.isEmpty()) {
			result += "------------------------------------------------" + System.lineSeparator()
				+ "Subject: " + System.lineSeparator() + Subject;
		}
		if(!Action.isEmpty()) {
			result += "------------------------------------------------" + System.lineSeparator()
			+ "Action: " + System.lineSeparator() + Action;
		}
		if(!Objects.isEmpty()) {
			result += "------------------------------------------------" + System.lineSeparator()
			+ "Objects: " + System.lineSeparator();
			for(ObjectInterpretation o : Objects) {
				result += o + System.lineSeparator() + System.lineSeparator();
			}			
		}
		if(!UnknownItem.isEmpty()) {
			result += "------------------------------------------------" + System.lineSeparator()
			+ "Unknown Item: " + System.lineSeparator() + UnknownItem;
		}
		if(!wordbag.isEmpty()) {
			result += "------------------------------------------------" + System.lineSeparator()
			+ "Wordbag: ";
			for(Word w : wordbag) {
				result += w + " ";
			}
		}
		return result;
	}
}
