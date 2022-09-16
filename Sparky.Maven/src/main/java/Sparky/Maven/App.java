package Sparky.Maven;

import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.data.POS;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ie.util.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import java.util.*;

import Sparky.Maven.Pattern.*;
import Sparky.Maven.Pattern.Word;
import Sparky.Maven.Pattern.Interpretation.InterpretationCombinationRule;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {		
		//File loader testing:
		String HnG = FileReader.FileToString("C:\\Users\\zyfro\\OneDrive\\Documents\\School\\UHM Dissertation\\Source Text\\Grimm\\HANSEL AND GRETEL.txt", " ");
		System.out.print(HnG);	
		PatternTests();
    }
    private static void PatternTests() throws Exception {
    	//create some word nodes:
    	Word run = new Word("ran");
    	Word tree = new Word("Tree");
    	
    	Word[] pattern = new Word[2];
    	pattern[0] = run;
    	pattern[1] = tree;
    	
    	try {
			System.out.println(run.isNoun());
	    	System.out.println(run.isVerb());

			System.out.println(tree.isNoun());
	    	System.out.println(tree.isVerb());
	    	
	    	//System.out.println(tree.matchFound(pattern, 0));
	    	//System.out.println(tree.matchFound(pattern, 1));
	    	
	    	//System.out.println(run.matchFound(pattern, 0));
	    	//System.out.println(run.matchFound(pattern, 1));

	    	System.out.println("Prepositions:");
	    	System.out.println(WordHelper.isPreposition("of"));
	    	System.out.println(WordHelper.isPreposition("to"));
	    	System.out.println(WordHelper.isPreposition("tree"));
	    	System.out.println(WordHelper.isPrepositionPhrase("instead","of"));
	    	System.out.println(WordHelper.isPrepositionPhrase("instead","with"));
	    	


	    	System.out.println("Sentence and word extraction:");
			String HnG = FileReader.FileToString("C:\\Users\\zyfro\\OneDrive\\Documents\\School\\UHM Dissertation\\Source Text\\Grimm\\HANSEL AND GRETEL.txt", " ");
			
			List<String> sentences = Extractor.extractSentenceStrings(HnG);
			for(String s : sentences) System.out.println(s);
			
			Phrase sent0 = Extractor.extractWords(sentences.get(0));
			Phrase sent1 = Extractor.extractWords(sentences.get(1));
			Phrase sent2 = Extractor.extractWords(sentences.get(2));
			Phrase sent3 = Extractor.extractWords(sentences.get(3));
			Phrase sent4 = Extractor.extractWords(sentences.get(4));
			

	    	System.out.println("Sentence 0: " + sentences.get(0));
	    	System.out.println(sent0);
	    	System.out.println("Sentence 1: " + sentences.get(1));
	    	System.out.println(sent1);
	    	System.out.println("Sentence 2: " + sentences.get(2));
	    	System.out.println(sent2);
	    	System.out.println("Sentence 3: " + sentences.get(3));
	    	System.out.println(sent3);
	    	System.out.println("Sentence 4: " + sentences.get(4));
	    	System.out.println(sent4);

	    	System.out.println("Static Pattern Matching:");
	    	StaticPhraseMatcher pm1 = new StaticPhraseMatcher("SPM1","SPM1").Verb().Verb().Noun();
	    	StaticPhraseMatcher pm2 = new StaticPhraseMatcher("SPM2","SPM2").Adverb().Preposition().Determiner().Adjective().Noun();
	    	StaticPhraseMatcher pm3 = new StaticPhraseMatcher("SPM3","SPM3").Preposition().Determiner().Adjective().Noun();
	    	
	    	System.out.println(pm1.findAllMatches(sent0, 0));
	    	System.out.println(pm2.findAllMatches(sent0, 0));
	    	System.out.println(pm3.findAllMatches(sent0, 0));
	    	System.out.println(pm3.findAllMatches(sent0, 1));
	    	System.out.println(pm3.findAllMatches(sent0, 1).get(0).FlatString(false));
	    	System.out.println(pm3.findAllMatches(sent0, 1).get(0).FlatString(true));
	    	System.out.println(pm3.findAllMatches("Into the tall grass"));
	    	System.out.println(pm3.findAllMatches("The frog jumped into the tall grass", 3));
	    	System.out.println(pm3.findAllMatches("Fell into the tall grass"));
	    	
	    	SingleWordMatcher swm = SingleWordMatcher.get(ExtendedPOS.Adjective);
	    	System.out.println(swm.findAllMatches("giraffes"));
	    	System.out.println(swm.findAllMatches("tall giraffes"));
	    	System.out.println(swm.findAllMatches("very tall giraffes"));
	    	
	    	
	    	///RECURSION BITCHES!!!!
	    	//Professional tone ^^ I am writing this late at night let's have some FUN.
	    	System.out.println("Dynamic Pattern Matching");
	    	DynamicPhraseMatcher AdjectiveList = new DynamicPhraseMatcher("AdjLi", "Adjective List", 0, InterpretationCombinationRule.Adjectives);
	    	AdjectiveList.createSequence().add(ExtendedPOS.Adjective);
	    	AdjectiveList.createSequence().add(ExtendedPOS.Adjective).addSelf();
	    	AdjectiveList.createSequence().add(ExtendedPOS.Adjective).add(ExtendedPOS.Conjunction).addSelf();
	    	
	    	System.out.println(AdjectiveList.findAllMatches("bright, red, bouncy"));
	    	System.out.println(AdjectiveList.findAllMatches("bright and bouncy"));
	    	System.out.println(AdjectiveList.findAllMatches("bright and bouncy and very, very tall"));
	    	System.out.println(AdjectiveList.findAllMatches("blue Giraffe"));
	    	

	    	DynamicPhraseMatcher DescribedNoun = new DynamicPhraseMatcher("DescNn", "Described Noun", 1, InterpretationCombinationRule.SingleObject);
	    	
	    	DescribedNoun.createSequence().add(ExtendedPOS.Noun);
	    	DescribedNoun.createSequence().add(ExtendedPOS.Determiner).add(ExtendedPOS.Noun);
	    	DescribedNoun.createSequence().add(AdjectiveList).add(ExtendedPOS.Noun);
	    	DescribedNoun.createSequence().add(ExtendedPOS.Determiner).add(AdjectiveList).add(ExtendedPOS.Noun);
	    	
	    	System.out.println(DescribedNoun.findAllMatches("bright and bouncy"));
	    	//A bright and bouncy ball DEBUG!!!
	    	ArrayList<Phrase> matches1 = DescribedNoun.findAllMatches("A bright and bouncy ball");	    	
	    	System.out.println(matches1.get(1).DetailedReadout());
	    	
	    	ArrayList<Phrase> matches2 = DescribedNoun.findAllMatches("A very very tall and handsome man");
	    	for(Phrase p : matches2) {
		    	System.out.println(p.DetailedReadout());
	    	}
	    	System.out.println(DescribedNoun.sequenceReadout());
	    	
	    	//Testing optional match sequences:
	    	DynamicPhraseMatcher DescribedNounWithOptional = new DynamicPhraseMatcher("DescNnOpt", "Described Noun", 1, InterpretationCombinationRule.SingleObject);
	    	DescribedNounWithOptional.addOptional(ExtendedPOS.Determiner).addOptional("AdjLi").add(ExtendedPOS.Noun);

	    	ArrayList<Phrase> matches3 = DescribedNounWithOptional.findAllMatches("A very very tall and handsome man");
	    	for(Phrase p : matches3) {
		    	System.out.println(p.DetailedReadout());
	    	}
	    	ArrayList<Phrase> matches4 = DescribedNounWithOptional.findAllMatches("tall and handsome men");
	    	for(Phrase p : matches4) {
		    	System.out.println(p.DetailedReadout());
	    	}
	    	matches4 = DescribedNoun.findAllMatches("tall and handsome men");
	    	for(Phrase p : matches4) {
		    	System.out.println(p.DetailedReadout());
	    	}
	    	System.out.println(DescribedNounWithOptional.sequenceReadout());

	    	System.out.println(DescribedNoun.findAllMatches("A girl who lived in a beautiful house"));
	    	//System.out.println(WordHelper.isNoun("A"));
	    	//WordHelper.printAllSenses("A");
	    	//WordHelper.printAllSenses("very");
	    	
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static void WordNetTests() {
    	try {
		JWNL_Helper.initialize();
		
		IndexWordSet ColorSet = JWNL_Helper.getDictionary().lookupAllIndexWords("Color");
		IndexWord[] ColorWords = ColorSet.getIndexWordArray();
		
		int len = ColorWords.length;
		for(int i = 0; i < len; i++) {
			System.out.println(ColorWords[i].toString());
		}
		System.out.println(WordHelper.isAdjective("Color"));
		System.out.println(WordHelper.isAdjective("Hike"));
		System.out.println(WordHelper.isAdjective("Tall"));
		WordHelper.printAllSenses("Fly");
		WordHelper.printAllSenses("Color");
		WordHelper.printAllSenses("Paint");
		WordHelper.printAllSenses("Harry");
		System.out.println(WordHelper.Synonyms("color", "Tinge", POS.VERB));
		System.out.println(WordHelper.Synonyms("Paint", "Color", POS.VERB));
		System.out.println(WordHelper.Synonyms("color", "Tinge", POS.ADJECTIVE));
		System.out.println(WordHelper.Synonyms("color", "Road", POS.VERB));
		System.out.println(WordHelper.Synonyms("color", "George", POS.NOUN));
		System.out.println("End");
		
    	} 
    	catch (JWNLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    private static void StanfordCoreNLPTests() {

		//File loader testing:
		String HnG = FileReader.FileToString("C:\\Users\\zyfro\\OneDrive\\Documents\\School\\UHM Dissertation\\Source Text\\Grimm\\HANSEL AND GRETEL.txt", " ");
		System.out.print(HnG);
		//Stanford NLP Sample:
		// set up pipeline properties
	    System.out.println("1");

	    Properties props = new Properties();
	    // set the list of annotators to run
	    System.out.println("2");
	    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
	    // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
	    System.out.println("3");
	    props.setProperty("coref.algorithm", "neural");
	    // build pipeline
	    System.out.println("4");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    // create a document object
	    System.out.println("5");
	    CoreDocument document = new CoreDocument(HnG);
	    // annnotate the document
	    System.out.println("6");
	    pipeline.annotate(document);
	    // examples

	    // 10th token of the document
	    CoreLabel token = document.tokens().get(10);
	    System.out.println("Example: token");
	    System.out.println(token);
	    System.out.println();
	    // text of the first sentence
	    String sentenceText = document.sentences().get(0).text();
	    System.out.println("Example: sentence");
	    System.out.println(sentenceText);
	    System.out.println();
	    CoreSentence firstSentence = document.sentences().get(0);
	    
	    
	    
	    // second sentence
	    CoreSentence sentence = document.sentences().get(1);
	    System.out.println(sentence.text());

	    System.out.println("7");

	    // list of the part-of-speech tags for the second sentence
	    List<String> posTags = sentence.posTags();
	    System.out.println("Example: pos tags");
	    System.out.println(posTags);
	    System.out.println();

	    // list of the ner tags for the second sentence
	    List<String> nerTags = sentence.nerTags();
	    System.out.println("Example: ner tags");
	    System.out.println(nerTags);
	    System.out.println();

	    // constituency parse for the second sentence
	    Tree constituencyParse1 = firstSentence.constituencyParse();
	    Tree constituencyParse2 = sentence.constituencyParse();
	    System.out.println("Example: constituency parse");
	    System.out.println(constituencyParse1);
	    System.out.println(constituencyParse2);
	    System.out.println();

	    // dependency parse for the second sentence
	    SemanticGraph dependencyParse1 = sentence.dependencyParse();
	    SemanticGraph dependencyParse2 = firstSentence.dependencyParse();
	    System.out.println("Example: dependency parse");
	    System.out.println(dependencyParse1);
	    System.out.println(dependencyParse2);
	    System.out.println();
	    
	    
	    //Get all the noun phrases in Sentence 1:
	    System.out.println(dependencyParse1.getFirstRoot());
	    
	    System.out.println(sentence.nounPhrases());
	    System.out.println(sentence.nounPhraseTrees());

	    
	    //Get all the Verb Phrases in Sentence 1:
	    System.out.println(sentence.verbPhrases());
	    System.out.println(sentence.verbPhraseTrees());
	    
	    //Get all the Subjects in Sentence 1:
	    List<String> NounPhrases = sentence.nounPhrases();
	    System.out.println(NounPhrases);
	    
	    // kbp relations found in fifth sentence
	    List<RelationTriple> relations =
	        document.sentences().get(0).relations();
	    System.out.println("Example: relation");
	    System.out.println(relations.get(0));
	    System.out.println();

	    // entity mentions in the second sentence
	    List<CoreEntityMention> entityMentions = sentence.entityMentions();
	    System.out.println("Example: entity mentions");
	    System.out.println(entityMentions);
	    System.out.println();

	    // coreference between entity mentions
	    CoreEntityMention originalEntityMention = document.sentences().get(3).entityMentions().get(1);
	    System.out.println("Example: original entity mention");
	    System.out.println(originalEntityMention);
	    System.out.println("Example: canonical entity mention");
	    System.out.println(originalEntityMention.canonicalEntityMention().get());
	    System.out.println();

	    // get document wide coref info
	    Map<Integer, CorefChain> corefChains = document.corefChains();
	    System.out.println("Example: coref chains for document");
	    System.out.println(corefChains);
	    System.out.println();

	    // get quotes in document
	    List<CoreQuote> quotes = document.quotes();
	    CoreQuote quote = quotes.get(0);
	    System.out.println("Example: quote");
	    System.out.println(quote);
	    System.out.println();

	    // original speaker of quote
	    // note that quote.speaker() returns an Optional
	    System.out.println("Example: original speaker of quote");
	    System.out.println(quote.speaker().get());
	    System.out.println();

	    // canonical speaker of quote
	    System.out.println("Example: canonical speaker of quote");
	    System.out.println(quote.canonicalSpeaker().get());
	    System.out.println();
    }
}
