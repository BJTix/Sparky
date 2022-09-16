package Sparky.Maven.Pattern;

import java.util.List;

import net.didion.jwnl.JWNLException;

public class POSNode extends PatternNode {

	private ExtendedPOS myPOS;
	
	public boolean matchFound(PatternNode[] source, int startPos) throws JWNLException {

		 //if it's not a single word node we don't even need to check it:
		if(source[startPos].SingleWordNode())
		{
			Word myWord = new Word(source[startPos].toString());
			//if it is a single-node word, we can get its string and then check if it matches the right part of speech.
			switch(myPOS) {
			case Noun: return myWord.isNoun();
			case Verb: return myWord.isVerb();
			case Adjective: return myWord.isAdjective();
			case Adverb: return myWord.isAdverb();
			case Preposition: return myWord.isPreposition();
			default: return false;
			}
		}
		return false;
	}

	@Override
	public boolean SingleWordNode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<PatternNode> findMatches(Phrase source, int startPos) throws JWNLException {
		// TODO Auto-generated method stub
		return null;
	}

}
