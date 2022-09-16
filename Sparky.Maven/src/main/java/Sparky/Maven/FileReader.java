package Sparky.Maven;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class FileReader {
	
	public static String FileToString(String filename) {
		String result = "";
		try {
		      File myObj = new File(filename);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        result += data + " ";
		      }
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return result;
	}
	public static String FileToString(String filename, String newlineReplacement) {
		String Result = FileToString(filename);
		Result = Result.replaceAll(System.lineSeparator(), newlineReplacement);
		Result = Result.replaceAll("\n\r", newlineReplacement);
		Result = Result.replaceAll("\r\n", newlineReplacement);
		Result = Result.replaceAll("\n", newlineReplacement);
		return Result;
	}
}