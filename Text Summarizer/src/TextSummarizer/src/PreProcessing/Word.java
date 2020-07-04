package PreProcessing;

public class Word {

	public int sentenceId;
	public String word;
	public char endsWith;
	
	public Word(int sid, String word, char endsWith) {
		this.sentenceId = sid;
		this.word = word;
		this.endsWith = endsWith;
	}
}
