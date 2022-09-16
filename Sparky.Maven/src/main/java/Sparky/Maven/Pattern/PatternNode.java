package Sparky.Maven.Pattern;

import java.util.List;

import net.didion.jwnl.JWNLException;

/**
 * 
 * @author Bernadette Jzexoia Tix
 * Created: 2022-08-17
 * Description: Interface for a single node in a pattern, which is comprised of a string of nodes.
 * A pattern node must accept an array of other nodes and an index and evaluate whether it matches the pattern starting at that index.
 * 
 */
public abstract class PatternNode {
	public abstract List<PatternNode> findMatches(Phrase source, int startPos) throws JWNLException;
	public abstract boolean SingleWordNode();
	
}
