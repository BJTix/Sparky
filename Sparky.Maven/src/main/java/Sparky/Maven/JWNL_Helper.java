package Sparky.Maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.dictionary.Dictionary;

public class JWNL_Helper {
	private static Dictionary _myDictionary;
	//private static JWNL_Helper _instance;
	
	public static void initialize() {
		String propertiesFileName = "JWNL-properties.xml";
		File propertiesFile = new File(propertiesFileName);
		String absolutePath = propertiesFile.getAbsolutePath();
		FileInputStream fis;
		try {
			fis = new FileInputStream(absolutePath);
			JWNL.initialize(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		_myDictionary = Dictionary.getInstance();
	}
	
	public static Dictionary getDictionary() {
		if(null == _myDictionary) initialize();
		return _myDictionary;
	}
	
	public static IndexWord[] getAllWords(String wrd) throws JWNLException {
		IndexWordSet wordset = getDictionary().lookupAllIndexWords(wrd);
		IndexWord[] wordArray = wordset.getIndexWordArray();	
		return wordArray;
	}
	
}
