package PreProcessing;

import java.util.ArrayList;

public class Sentence {

	public int sentenceId;
	public String content;
	public ArrayList<Word> words;
	public char endsWith;
	
	public Sentence(int sid, String content, char endsWith, ArrayList<Word> words) {
		this.sentenceId = sid;
		this.content = content;
		this.words = words;
		this.endsWith = endsWith;
	}
}
