package PreProcessing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Pre {

	/*
	 * We will do 4 things - Sentence Segmentation & Lower Case - Tokenization -
	 * Stop Word Removal - Stemming
	 */
	private ArrayList<String> stopwords;

	public Pre() {
		// Get the stopwords list
		stopwords = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\erhan\\workspace\\TextSummarizer\\src\\PreProcessing\\stopwords.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				stopwords.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Returns the sentences with their words all preprocessed
	 * 
	 * @param text
	 * @return
	 */
	public ArrayList<Sentence> PreProcess(String text) {
		text += " "; // needed for later operations on word and sentence tokenization
		ArrayList<Sentence> sentences = new ArrayList<>();
		ArrayList<Word> words = new ArrayList<>();
		// Step 1 : Sentence Segmentation
		sentences = this.SentenceSegmentation(text); // get the list of sentences
		for (int j = 0; j < sentences.size(); j++) {
			Sentence s = sentences.get(j); // get the  sentence
			// Step 2 : Word Tokenization
			words = this.WordTokenization(s); // get the list of words
			// Step 2.5 : Remove words with characters that are not letters
			for (int i = words.size() - 1; i >= 0; i--) {
				boolean allLetters = true;
				char[] tmpChar = words.get(i).word.toCharArray();
				if (tmpChar.length == 0) {
					allLetters = false;
				} else {
					for (char c : tmpChar) {
						if (!Character.isLetter(c)) {
							allLetters = false;
						}
					}
				}
				if (!allLetters) {
					words.remove(i);
				}
			}
			// Step 3 : Removing Stop Words
			words = this.RemoveStopWords(words); // remove stop words from the list of words													
			// Step 4 : Stemming
			for (int i = 0; i < words.size(); i++) { // Stem each word				
				words.set(i, this.StemWord(words.get(i)));
			}
			sentences.get(j).words = words; // update the list of words for the sentence
		}
		return sentences;
	}

	/**
	 * Returns the list of sentences all lowercase
	 * 
	 * @param text
	 * @return sentenceList
	 */
	public ArrayList<Sentence> SentenceSegmentation(String text) {
		int id = 0;
		char[] textChar = text.toCharArray();
		ArrayList<Sentence> ans = new ArrayList<>();
		String s = "";
		for (int i = 0; i<textChar.length; i++) {
			char c = textChar[i];
			if ((c == '?' || c == '!' || c == '.') && s.length() > 0) { // condition
				try {
					if ( textChar[i+1]==' ') {
						ans.add(new Sentence(id++, s, c, null));
						s = "";
					}
				}
				catch (IndexOutOfBoundsException excep) {
					// this is to detect things such as 2.5 
				}
				
			} else {
				s += Character.toLowerCase(c);
			}
		}
		return ans;
	}

	/**
	 * Returns the list of words in a sentence
	 * 
	 * @param sentence
	 * @return wordList
	 */
	public ArrayList<Word> WordTokenization(Sentence s) {
		char[] sentenceChar = s.content.toCharArray();
		ArrayList<Word> ans = new ArrayList<>();
		String str = "";
		for (int i = 0; i<sentenceChar.length; i++) {
			char c = sentenceChar[i];
			if ((c == ' ' || c == ',' || c == '?' || c == '!' || (c == '.' && sentenceChar[i+1]==' ') ) && str.length()>0 ) { // condition
				try {
						ans.add(new Word(s.sentenceId, str, c));
						str = "";
				}
				catch (IndexOutOfBoundsException excep) {
					// this is to detect things such as 2.5 
				}				
			} else {
				str += c;
			}
		}
		return ans;
	}

	/**
	 * Removes stop words from a list of words.
	 * 
	 * @param words
	 * @return
	 */
	public ArrayList<Word> RemoveStopWords(ArrayList<Word> words) {
		for (int i = words.size() - 1; i >= 0; i--) {
			if (stopwords.contains(words.get(i).word)) {
				words.remove(i);
			}
		}
		return words;
	}
	
	/**
	 *  Stems a given word.
	 * 
	 * @param w
	 * @return
	 */
	public Word StemWord(Word w) {
		Stemmer stemmer = new Stemmer(); // initialize stemmer
		stemmer.add(w.word.toCharArray(), w.word.length()); // add word to stemmer
		stemmer.stem(); // stem
		w.word = stemmer.toString(); // set word to result
		return w;
	}
}
