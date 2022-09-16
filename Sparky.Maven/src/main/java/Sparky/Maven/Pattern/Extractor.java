/**
 * 
 */
package Sparky.Maven.Pattern;

import java.util.ArrayList;
import java.util.List;

import net.didion.jwnl.JWNLException;

/**
 * @author Bernadette Jzexoia Tix
 * 
 * Class to extract words and sentences from a string of text.
 */
public class Extractor {
	/**
	 * Gets a list of strings representing each sentence in a story.
	 * Multi-quote dialog is kept together with the sentence where the quote started.
	 * @param story The source to extract from.
	 * @return A list of strings. Each string is a sentence.
	 */
	public static List<String> extractSentenceStrings(String story){
		//Start by switching quote ends and periods to keep quote ends with the previous sentence.
		story = story.replace(".’", "’.");
		story = story.replace(".'", "'.");
		story = story.replace(".\"", "\".");
		
		//Replace single end quotes with proper apostrophes when they are used as apostrophes:
		int totalLength = story.length();
		for(int i = 0; i < totalLength - 1; i++)
		{
			char c = story.charAt(i);
			if (c == '’') {
				char c1 = story.charAt(i + 1);
				switch (c1) {
					case ' ':
					case ',':
					case ';':
					case '.':
					case '\n':
					case '\r':
						break;
					default:
						story = story.substring(0,i) + '\'' + story.substring(i+1);
				}
			}
		}
		/*
		story = story.replace("’t ", "'t ");
		story = story.replace("’m ", "'m ");
		story = story.replace("’ll ", "'ll ");
		*/
		int index = 0;
		int sentenceIndex = 0;
		List<String> results = new ArrayList<String>();
		boolean inQuote = false;
		
		//there will always be at least one sentence in the list:
		results.add("");
		//Scan the story for sentences:
		while(index < totalLength) {
			char c = story.charAt(index);
			//If we found a period, create a new sentence:
			//Note: multi-sentence quotes are kept with the sentence that they are started in.
			if(!inQuote && '.' == c) {
				sentenceIndex++;
				index ++;
				if(index < totalLength) {
					results.add("");				
				}
			}
			//otherwise add the new character to the current sentence
			else {
				results.set(sentenceIndex, results.get(sentenceIndex) + c);
				//check for the start of a quote:
				if(!inQuote && (c == '"' || c == '‘')) {
					inQuote = true;
				}
				//check for the end of a quote:
				if(inQuote && (c == '"' || c == '’')) {
					inQuote = false;
				}
				index ++;
			}
		}
		//remove the last sentence if blank:
		if(results.get(sentenceIndex) == "") results.remove(sentenceIndex);
		//remove leading spaces:
		for (int i = 0; i < results.size(); i++) {
			if(results.get(i).charAt(0) == ' ') {
				results.set(i, results.get(i).substring(1));
			}
		}
		return results;
	}
	
	/**
	 * Converts a sentence string into a list of words. 
	 * @param sentence the source sentence.
	 * @return a list of words.
	 * @throws JWNLException 
	 */
	public static Phrase extractWords(String sentence) throws JWNLException
	{
		int index = 0;
		int totalLength = sentence.length();
		Phrase results = new Phrase();
		boolean inQuote = false;
		
		String w = ""; //the word in progress.
		//Scan the sentence for words:
		while(index < totalLength) {
			char c = sentence.charAt(index);
			// There are two cases, quotes and non-quotes. This is because quotes are collapsed into a single word.
			//In a quote
			if(c == '"' || c == '‘') inQuote = true;
			if(inQuote) {
				w = w + c;
				//Close the quote and add it:
				if(c == '"' || c == '’') { 
					inQuote = false;
					results.add(new Quote(w));
					w = "";
				}
				index++;
			}
			//Not in a quote:
			else {
				switch (c){
					//Skip spaces and punctuation and newlines, and create a new word if needed.
					case ' ':
					case ',':
					case ';':
					case ':':
					case '\n':
					case '\r':
						if(w != "") {
							results.add(new Word(w));
							w = "";
						}
						break;
					default:
						w = w + c;
				}
				index++;
			}
		}
		//add the last word if it is in progress:
		if(w != "")	results.add(new Word(w));
		return results;
	}
}
