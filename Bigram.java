//Michael Dobrzanski
import java.util.Map;
import java.util.Scanner;
import java.util.*;

public class Bigram {
	private Map<String, Map<String, Integer>> classMap = new TreeMap(); {} //using TreeMap to sort entries

	public Bigram(String text) {  //Create a Bigram object that uses a given text as its "universe" of acceptable text.
		Scanner textScanner = new Scanner(text);
		String keyword1, keyword2;
		Map<String, Map<String, Integer>> outerMap = new TreeMap();
		keyword1 = textScanner.next();
		while (textScanner.hasNext()) {
			keyword2 = textScanner.next();
			if (outerMap.containsKey(keyword1) == false) { //have we not seen a map for keyword1?
				outerMap.put(keyword1, new TreeMap<String, Integer>()); //make a new map for keyword1 
			}
			if (outerMap.containsKey(keyword1) && outerMap.get(keyword1).containsKey(keyword2) == false) { //have we not seen keyword1 with keyword2?
				outerMap.get(keyword1).put(keyword2, 1); //make a new map for keyword1, keyword2 = 1
			}
			else {  //we already have a map for keyword1, keyword2
				outerMap.get(keyword1).put(keyword2, outerMap.get(keyword1).get(keyword2) + 1); //keyword1, keyword2++
			}
			if (textScanner.hasNext()) {  //if there is another word after keyword2
				keyword1 = keyword2; //make keyword2 the new keyword1, next word is now keyword2, repeat
			}
		}		
		textScanner.close();
		classMap = outerMap; 
	}

	public boolean check(String sentence) {
		Scanner bigramChecker = new Scanner(sentence); //scan the incoming sentence
		String keyword1, keyword2;
		keyword1 = bigramChecker.next();
		while (bigramChecker.hasNext()) { //while there is a word following keyword1
			keyword2 = bigramChecker.next();
			if ((classMap.containsKey(keyword1) && classMap.get(keyword1).containsKey(keyword2))) { //look at overlapping bigrams
				keyword1 = keyword2; //make keyword2 the new keyword1, next word is now keyword2, repeat
			}
			else {
				bigramChecker.close();
				return false;
			}
		}
		bigramChecker.close();
		return true;
	}

	public String[] generate(String word, int count) {
		String[] generatedSentence = new String[count];  //use an array of length count to construct a sentence for reasons unknown
		String topWord = word;
		generatedSentence[0] = word;  // seed array with "word"
		for (int i = 1; i < count; i++) {
			if (classMap.containsKey(topWord) == false) { // if there is no map for "word"
				return Arrays.copyOfRange(generatedSentence, 0, i); //return that word; copyOfRange will cut off the array if 
			}
			int topWordValue = 0;
			classMap.get(word); 
			for (Map.Entry<String,Integer> nextWord : classMap.get(topWord).entrySet())  {  //for each word, get the  highest ranked nextWord
				if (nextWord.getValue() > topWordValue) {  //compare word occurrence values
					topWord = nextWord.getKey(); // becomes the next key
					topWordValue = nextWord.getValue();  //use top weighted value as key for nextWord
					generatedSentence[i] = topWord;  //put that word into the array
				}
			}
		}
		return generatedSentence;
	}
}

//	value = key, look up value, value = next word
//	 - if inner map (key’s values) is sorted, we can return the key’s first value
//	if key or value = null, stop search, return abbreviated sentence/array
//	speedup: if the inner map is sorted, we don’t need to retain the rest of the values.  Dump all but the top value. 
// - use treemap instead of hashmap
// foreach loop to set up the following key
// cut array short if it hits a null value - array.copyOfRange(name of array, first index, last index without a null value
