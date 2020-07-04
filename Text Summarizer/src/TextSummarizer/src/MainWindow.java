import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import PreProcessing.Pre;
import PreProcessing.Sentence;
import PreProcessing.Word;

import java.awt.SystemColor;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComboBox;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.knowledgebooks.nlp.KeyPhraseExtractionAndSummary;

import java.awt.Component;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

public class MainWindow {

	private JFrame frmTextSummarizer;
	private JTextArea textAreaQuestion;
	private JScrollPane scrollPaneQuestion;
	private JScrollPane scrollPaneAnswer;
	private JTabbedPane tabbedPaneSummary;
	private JTextArea textAreaAnswer;
	private JLabel lblQuestion;
	private JLabel lblAnswer;
	private JLabel lblAuthor;
	private JLabel lblTopic;
	private JPanel basePanel;
	private JPanel selectionPanel;
	private JLabel lblSummary;
	private JButton btnPreprocess;
	private JPanel summaryMethodPanel;
	private JScrollPane scrollPanePreprocessWords;
	private JTextArea textAreaPreprocessWords;
	private JLabel lblSelectQuestion;
	private JComboBox<String> comboBoxQuestionSelect;
	private JButton btnSelect;
	private JLabel lblSelectSummarizationMethod;

	// >> MY VARIABLES
	private TextReader textReader;
	private ArrayList<Question> questions;
	private ArrayList<String> summaries;
	private int[] questionIds;
	private JScrollPane scrollPaneSummary;
	private JTextArea textAreaSummary;
	private JLabel lblSentencePos;
	private JLabel lblSummaryMode;
	private JLabel lblFrequency;
	private JComboBox<String> comboBoxSummaryMode;
	private JComboBox<Double> comboBoxFrequency;
	private JComboBox<Double> comboBoxTitleSimilarity;
	private JComboBox<Double> comboBoxSentencePos;
	private JLabel lblTitleSimilarity;
	private JScrollPane scrollPaneHandmadeSummary;
	private JTextArea textAreaHandmadeSummary;
	private JComboBox<String> comboBoxMethod;
	private JRadioButton rdbtnWeightAuthorWords;
	private JRadioButton rdbtnQuestionWords;
	// << MY VARIABLES

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					window.frmTextSummarizer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		String[] serverInfo = new String[] { "jdbc:postgresql://localhost:5432/TextDB", // Address
				"postgres", // Username
				"1234" // Password
		};
		textReader = new TextReader(serverInfo);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmTextSummarizer = new JFrame();
		frmTextSummarizer.setResizable(false);
		frmTextSummarizer.getContentPane().setBackground(new Color(51, 51, 51));
		frmTextSummarizer.setTitle("Text Summarizer");
		frmTextSummarizer.setBounds(100, 100, 1204, 730);
		frmTextSummarizer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTextSummarizer.getContentPane().setLayout(null);

		// This is the panel that we select questions from
		selectionPanel = new JPanel();
		selectionPanel.setBounds(12, 13, 183, 75);
		selectionPanel.setBackground(new Color(102, 102, 102));
		selectionPanel.setBorder(new LineBorder(new Color(255, 153, 0)));
		frmTextSummarizer.getContentPane().add(selectionPanel);
		selectionPanel.setLayout(null);

		lblSelectQuestion = new JLabel("Select Question:");
		lblSelectQuestion.setForeground(new Color(204, 204, 204));
		lblSelectQuestion.setBackground(new Color(204, 204, 204));
		lblSelectQuestion.setBounds(12, 10, 148, 16);
		lblSelectQuestion.setFont(new Font("Tahoma", Font.BOLD, 14));
		selectionPanel.add(lblSelectQuestion);

		comboBoxQuestionSelect = new JComboBox<String>();
		comboBoxQuestionSelect.setForeground(new Color(255, 153, 0));
		comboBoxQuestionSelect.setBackground(new Color(51, 51, 51));
		comboBoxQuestionSelect.setBounds(12, 39, 43, 25);
		selectionPanel.add(comboBoxQuestionSelect);

		JPanel parameterPanel = new JPanel();
		parameterPanel.setBorder(new LineBorder(new Color(255, 153, 0)));
		parameterPanel.setBackground(new Color(102, 102, 102));
		parameterPanel.setBounds(438, 13, 750, 162);
		frmTextSummarizer.getContentPane().add(parameterPanel);
		parameterPanel.setLayout(null);

		lblSentencePos = new JLabel("Sentence Position:");
		lblSentencePos.setForeground(new Color(204, 204, 204));
		lblSentencePos.setBackground(new Color(204, 204, 204));
		lblSentencePos.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSentencePos.setBounds(12, 10, 144, 16);
		parameterPanel.add(lblSentencePos);

		lblTitleSimilarity = new JLabel("Title Similarity:");
		lblTitleSimilarity.setForeground(new Color(204, 204, 204));
		lblTitleSimilarity.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitleSimilarity.setBounds(12, 39, 144, 16);
		parameterPanel.add(lblTitleSimilarity);

		comboBoxSentencePos = new JComboBox<Double>();
		comboBoxSentencePos.setForeground(new Color(255, 153, 0));
		comboBoxSentencePos.setBackground(new Color(51, 51, 51));
		comboBoxSentencePos.setBounds(168, 6, 240, 25);
		parameterPanel.add(comboBoxSentencePos);

		comboBoxTitleSimilarity = new JComboBox<Double>();
		comboBoxTitleSimilarity.setForeground(new Color(255, 153, 0));
		comboBoxTitleSimilarity.setBackground(new Color(51, 51, 51));
		comboBoxTitleSimilarity.setBounds(168, 35, 240, 25);
		parameterPanel.add(comboBoxTitleSimilarity);

		comboBoxFrequency = new JComboBox<Double>();
		comboBoxFrequency.setBackground(new Color(51, 51, 51));
		comboBoxFrequency.setForeground(new Color(255, 153, 0));
		comboBoxFrequency.setBounds(168, 65, 240, 25);
		parameterPanel.add(comboBoxFrequency);

		comboBoxSummaryMode = new JComboBox<String>();
		comboBoxSummaryMode.setForeground(new Color(255, 153, 0));
		comboBoxSummaryMode.setBackground(new Color(51, 51, 51));
		comboBoxSummaryMode.setBounds(168, 94, 240, 25);
		parameterPanel.add(comboBoxSummaryMode);

		lblFrequency = new JLabel("Frequency:");
		lblFrequency.setForeground(new Color(204, 204, 204));
		lblFrequency.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFrequency.setBounds(12, 68, 128, 16);
		parameterPanel.add(lblFrequency);

		lblSummaryMode = new JLabel("Summary Mode:");
		lblSummaryMode.setForeground(new Color(204, 204, 204));
		lblSummaryMode.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSummaryMode.setBounds(12, 97, 128, 16);
		parameterPanel.add(lblSummaryMode);

		rdbtnQuestionWords = new JRadioButton("Weight Question Words");
		rdbtnQuestionWords.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnQuestionWords.setForeground(new Color(204, 204, 204));
		rdbtnQuestionWords.setBackground(new Color(102, 102, 102));
		rdbtnQuestionWords.setBounds(12, 128, 213, 25);
		parameterPanel.add(rdbtnQuestionWords);

		rdbtnWeightAuthorWords = new JRadioButton("Weight Author Words");
		rdbtnWeightAuthorWords.setForeground(new Color(204, 204, 204));
		rdbtnWeightAuthorWords.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnWeightAuthorWords.setBackground(new Color(102, 102, 102));
		rdbtnWeightAuthorWords.setBounds(229, 128, 179, 25);
		parameterPanel.add(rdbtnWeightAuthorWords);

		JTextArea textAreaParameterIntro = new JTextArea();
		textAreaParameterIntro.setWrapStyleWord(true);
		textAreaParameterIntro.setText(
				"These parameters affect how the sentence scoring works in \"My Algorithm\".\r\nSet Sentence Position to 0.0 to disable it.\r\n\r\nQuestion Words are words such as \"Yes\", \"No\", \"So\"\r\nAuthor Words are words such as \"User\", \"Author\", \"Question\"");
		textAreaParameterIntro.setFont(new Font("Cambria Math", Font.BOLD, 14));
		textAreaParameterIntro.setLineWrap(true);
		textAreaParameterIntro.setBackground(new Color(102, 102, 102));
		textAreaParameterIntro.setForeground(new Color(204, 204, 204));
		textAreaParameterIntro.setBounds(420, 8, 318, 153);
		parameterPanel.add(textAreaParameterIntro);

		basePanel = new JPanel();
		basePanel.setBackground(new Color(102, 102, 102));
		basePanel.setBorder(new LineBorder(new Color(255, 153, 51)));
		basePanel.setBounds(12, 188, 1176, 494);
		frmTextSummarizer.getContentPane().add(basePanel);
		basePanel.setLayout(null);

		lblQuestion = new JLabel("Question:");
		lblQuestion.setForeground(new Color(204, 204, 204));
		lblQuestion.setFont(new Font("Cambria Math", Font.BOLD, 14));
		lblQuestion.setBounds(12, 13, 76, 16);
		basePanel.add(lblQuestion);

		lblAnswer = new JLabel("Answer:");
		lblAnswer.setForeground(new Color(204, 204, 204));
		lblAnswer.setFont(new Font("Cambria Math", Font.BOLD, 14));
		lblAnswer.setBounds(12, 101, 76, 16);
		basePanel.add(lblAnswer);

		scrollPaneQuestion = new JScrollPane();
		scrollPaneQuestion.setViewportBorder(new LineBorder(new Color(255, 153, 0), 1, true));
		scrollPaneQuestion.setBounds(12, 42, 877, 46);
		basePanel.add(scrollPaneQuestion);

		textAreaQuestion = new JTextArea();
		scrollPaneQuestion.setViewportView(textAreaQuestion);
		textAreaQuestion.setForeground(new Color(204, 204, 204));
		textAreaQuestion.setBackground(new Color(102, 102, 102));
		textAreaQuestion.setFont(new Font("Cambria Math", Font.BOLD, 14));
		textAreaQuestion.setWrapStyleWord(true);
		textAreaQuestion.setLineWrap(true);
		textAreaQuestion.setEditable(false);

		scrollPaneAnswer = new JScrollPane();
		scrollPaneAnswer.setViewportBorder(new LineBorder(new Color(255, 153, 0), 1, true));
		scrollPaneAnswer.setBounds(12, 130, 561, 348);
		basePanel.add(scrollPaneAnswer);

		textAreaAnswer = new JTextArea();
		scrollPaneAnswer.setViewportView(textAreaAnswer);
		textAreaAnswer.setForeground(new Color(204, 204, 204));
		textAreaAnswer.setBackground(new Color(102, 102, 102));
		textAreaAnswer.setFont(new Font("Cambria Math", Font.BOLD, 14));
		textAreaAnswer.setLineWrap(true);
		textAreaAnswer.setWrapStyleWord(true);
		textAreaAnswer.setEditable(false);

		tabbedPaneSummary = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneSummary.setBounds(585, 128, 579, 348);
		basePanel.add(tabbedPaneSummary);

		scrollPaneSummary = new JScrollPane();
		scrollPaneSummary.setViewportBorder(new LineBorder(new Color(255, 204, 0)));
		tabbedPaneSummary.addTab("Summary by Algorithm", null, scrollPaneSummary, null);

		textAreaSummary = new JTextArea();
		textAreaSummary.setForeground(new Color(204, 204, 204));
		textAreaSummary.setBackground(new Color(102, 102, 102));
		textAreaSummary.setFont(new Font("Cambria Math", Font.BOLD, 14));
		textAreaSummary.setLineWrap(true);
		textAreaSummary.setWrapStyleWord(true);
		textAreaSummary.setEditable(false);
		scrollPaneSummary.setViewportView(textAreaSummary);

		scrollPaneHandmadeSummary = new JScrollPane();
		scrollPaneHandmadeSummary.setViewportBorder(new LineBorder(new Color(255, 153, 0)));
		tabbedPaneSummary.addTab("Summary by Human", null, scrollPaneHandmadeSummary, null);

		textAreaHandmadeSummary = new JTextArea();
		textAreaHandmadeSummary.setForeground(new Color(204, 204, 204));
		textAreaHandmadeSummary.setBackground(new Color(102, 102, 102));
		textAreaHandmadeSummary.setFont(new Font("Cambria Math", Font.BOLD, 14));
		textAreaHandmadeSummary.setEditable(false);
		textAreaHandmadeSummary.setLineWrap(true);
		textAreaHandmadeSummary.setWrapStyleWord(true);
		scrollPaneHandmadeSummary.setViewportView(textAreaHandmadeSummary);

		scrollPanePreprocessWords = new JScrollPane();
		scrollPanePreprocessWords.setViewportBorder(new LineBorder(new Color(255, 153, 0)));
		tabbedPaneSummary.addTab("Words after Preprocessing", null, scrollPanePreprocessWords, null);

		textAreaPreprocessWords = new JTextArea();
		textAreaPreprocessWords.setForeground(new Color(204, 204, 204));
		textAreaPreprocessWords.setBackground(new Color(102, 102, 102));
		textAreaPreprocessWords.setFont(new Font("Cambria Math", Font.BOLD, 14));
		textAreaPreprocessWords.setEditable(false);
		textAreaPreprocessWords.setWrapStyleWord(true);
		textAreaPreprocessWords.setLineWrap(true);
		scrollPanePreprocessWords.setViewportView(textAreaPreprocessWords);

		lblSummary = new JLabel("Summary:");
		lblSummary.setForeground(new Color(204, 204, 204));
		lblSummary.setBackground(new Color(204, 204, 204));
		lblSummary.setFont(new Font("Cambria Math", Font.BOLD, 14));
		lblSummary.setBounds(585, 101, 76, 16);
		basePanel.add(lblSummary);

		btnPreprocess = new JButton("<< SUMMARIZE >>");
		btnPreprocess.setBounds(901, 42, 263, 46);
		basePanel.add(btnPreprocess);
		btnPreprocess.setForeground(new Color(255, 153, 0));
		btnPreprocess.setFont(new Font("Cambria Math", Font.BOLD, 14));
		btnPreprocess.setBackground(new Color(51, 51, 51));
		btnPreprocess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Set the summary from db
				textAreaHandmadeSummary.setText(summaries.get(comboBoxQuestionSelect.getSelectedIndex()));
				switch ((String) comboBoxMethod.getSelectedItem()) {
				case "My Algorithm":
					Process();
					break;
				case "Knowledgebooks NLP":
					KeyPhraseExtractionAndSummary kpes = new KeyPhraseExtractionAndSummary(textAreaAnswer.getText());
					textAreaPreprocessWords.setText(" ~Not avalible for this algorithm~ ");
					textAreaSummary.setText(kpes.getSummary());
					break;
				}

			}
		});
		btnPreprocess.setVisible(false);

		btnSelect = new JButton("Select");
		btnSelect.setFont(new Font("Segoe UI", Font.BOLD, 15));
		btnSelect.setBackground(new Color(51, 51, 51));
		btnSelect.setForeground(new Color(255, 153, 0));
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPreprocess.setVisible(true);
				Question q = GetQuestionByID(questionIds[comboBoxQuestionSelect.getSelectedIndex()]);
				lblTopic.setText("Topic: " + q.getTopic());
				lblAuthor.setText("Author: " + q.getAuthor());
				textAreaQuestion.setText(q.getQuestion());
				textAreaAnswer.setText(q.getAnswer());
			}
		});
		btnSelect.setBounds(67, 39, 97, 25);
		selectionPanel.add(btnSelect);

		summaryMethodPanel = new JPanel();
		summaryMethodPanel.setBorder(new LineBorder(new Color(255, 153, 0)));
		summaryMethodPanel.setBackground(new Color(102, 102, 102));
		summaryMethodPanel.setBounds(207, 13, 219, 75);
		frmTextSummarizer.getContentPane().add(summaryMethodPanel);
		summaryMethodPanel.setLayout(null);

		lblSelectSummarizationMethod = new JLabel("Summarization Algorithm:");
		lblSelectSummarizationMethod.setBounds(12, 10, 206, 16);
		summaryMethodPanel.add(lblSelectSummarizationMethod);
		lblSelectSummarizationMethod.setForeground(new Color(204, 204, 204));
		lblSelectSummarizationMethod.setFont(new Font("Tahoma", Font.BOLD, 14));

		comboBoxMethod = new JComboBox<String>();
		comboBoxMethod.setForeground(new Color(255, 153, 0));
		comboBoxMethod.setBackground(new Color(51, 51, 51));
		comboBoxMethod.setBounds(12, 39, 195, 25);
		summaryMethodPanel.add(comboBoxMethod);
		summaryMethodPanel
				.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { lblSelectSummarizationMethod }));

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new LineBorder(new Color(255, 153, 0)));
		panel.setBackground(new Color(102, 102, 102));
		panel.setBounds(12, 100, 414, 75);
		frmTextSummarizer.getContentPane().add(panel);

		lblTopic = new JLabel("Topic:");
		lblTopic.setBounds(12, 13, 390, 16);
		panel.add(lblTopic);
		lblTopic.setForeground(new Color(204, 204, 204));
		lblTopic.setFont(new Font("Cambria Math", Font.BOLD, 14));

		lblAuthor = new JLabel("Author:");
		lblAuthor.setBounds(12, 42, 390, 16);
		panel.add(lblAuthor);
		lblAuthor.setForeground(new Color(204, 204, 204));
		lblAuthor.setFont(new Font("Cambria Math", Font.BOLD, 14));

		InitComboBox();
	}

	// Returns the question with given id
	public Question GetQuestionByID(int qid) {
		for (Question q : questions) {
			if (q.getId() == qid) {
				return q;
			}
		}
		return null;
	}

	// Initializes the contents of comboBox for question selection
	private void InitComboBox() {
		// Read questions
		questions = textReader.GetQuestions();
		// Read Summaries
		summaries = textReader.GetSummaries();
		// Populate ComboBox
		questionIds = new int[questions.size()];
		int q_i = 0;
		for (Question q : questions) {
			questionIds[q_i++] = q.getId();
			comboBoxQuestionSelect.addItem(q.getId() + "");
		}

		// Populate Parameter ComboBoxes
		for (int i = 0; i < 7; i++) {
			comboBoxFrequency.addItem(0.5 + i * 0.5);
			comboBoxSentencePos.addItem(0.25 + i * 0.25);
			comboBoxTitleSimilarity.addItem(1.5 + i * 0.5);
		}

		comboBoxSummaryMode.addItem("Mean Filter Unordered");
		comboBoxSummaryMode.addItem("Mean Filter Ordered");
		comboBoxSummaryMode.addItem("Sentence Ratio");

		// Summarization Algorithms ComboBox
		comboBoxMethod.addItem("My Algorithm");
		comboBoxMethod.addItem("Knowledgebooks NLP");
	}

	// This is the method for "My Algorithm"
	public void Process() {
		// 0 - Get Parameters
		double frequencyMultiplier = (Double) comboBoxFrequency.getSelectedItem();
		double titleSimilarityMultiplier = (Double) comboBoxTitleSimilarity.getSelectedItem();
		double sentencePositionMultiplier = (Double) comboBoxSentencePos.getSelectedItem();
		String summaryMode = (String) comboBoxSummaryMode.getSelectedItem();

		// 1 - Preprocessors
		Pre answerPreprocessor = new Pre();
		ArrayList<Sentence> answerSentencesPreprocessed = answerPreprocessor.PreProcess(textAreaAnswer.getText());
		ArrayList<Word> answerWords = GetWordsFromSentences(answerSentencesPreprocessed);
		ArrayList<Word> answerWordsDistinct = RemoveRequrrences(answerWords);
		int answerDistinctWordCount = answerWordsDistinct.size();
		int answerSentenceCount = answerSentencesPreprocessed.size();

		double[] wordFrequencyScores = new double[answerDistinctWordCount];
		double[] wordSentencePositionScores = new double[answerDistinctWordCount];
		double[] wordTitleRelationScores = new double[answerDistinctWordCount];
		for (int i = 0; i < answerWordsDistinct.size(); i++) {
			wordFrequencyScores[i] = 0;
			wordSentencePositionScores[i] = 0;
			wordTitleRelationScores[i] = 0;
		}

		// 2.1 - Frequency
		/* If a word has high frequency in a text, it has high importance */
		for (int i = 0; i < answerDistinctWordCount; i++) {
			Word wd = answerWordsDistinct.get(i);
			for (int j = 0; j < answerWords.size(); j++) {
				Word w = answerWords.get(j);
				if (wd.word.compareTo(w.word) == 0) {
					wordFrequencyScores[i]++;
				}
			}
		}

		// 2.2 - Sentence Position
		/*
		 * Highest Value -> First Sentence Lowest Value -> Last Sentence
		 */
		for (int i = 0; i < answerDistinctWordCount; i++) {
			Word wd = answerWordsDistinct.get(i);
			double score = (((double) answerSentenceCount - 1) - (double) wd.sentenceId)
					/ ((double) answerSentenceCount - 1);
			score = (score + 0.5) / 2; // smooth the score multiplier
			wordSentencePositionScores[i] = score;
		}

		// 2.3 - Title Similarity
		/* Words that are present in title have more weight in scoring */
		Pre questionPreprocessor = new Pre();
		ArrayList<Sentence> titleSentencesPreprocessed = questionPreprocessor.PreProcess(textAreaQuestion.getText()); // words
																														// in
																														// question
		ArrayList<Word> titleWords = GetWordsFromSentences(titleSentencesPreprocessed);
		for (int i = 0; i < answerDistinctWordCount; i++) {
			Word wd = answerWordsDistinct.get(i);
			for (int j = 0; j < titleWords.size(); j++) {
				Word w = titleWords.get(j);
				if (wd.word.compareTo(w.word) == 0) {
					wordTitleRelationScores[i]++;
				}
			}
		}

		// 2.4 (Optional) Question Words add points to the score of the sentence
		int[] wordQuestionScore = new int[answerDistinctWordCount];
		for ( int i = 0; i<answerDistinctWordCount; i++ ) { wordQuestionScore[i] = 0; }
		ArrayList<String> questionWords = new ArrayList<>();
		questionWords.add("yes");
		questionWords.add("no");
		questionWords.add("so");
		if (rdbtnQuestionWords.isSelected()) {
			for (int i = 0; i < answerDistinctWordCount; i++) {
				Word wd = answerWordsDistinct.get(i);
				if (questionWords.contains(wd.word)) {
					wordQuestionScore[i] = 1;
				}
			}
		}

		// 2.5 (Optional) Author Words add points to the score of the sentence
		int[] wordAuthorScore = new int[answerDistinctWordCount];
		for ( int i = 0; i<answerDistinctWordCount; i++ ) { wordAuthorScore[i] = 0; }
		ArrayList<String> authorWords = new ArrayList<>();
		authorWords.add("user");
		authorWords.add("author");
		if (rdbtnWeightAuthorWords.isSelected()) {
			for (int i = 0; i < answerDistinctWordCount; i++) {
				Word wd = answerWordsDistinct.get(i);
				if (authorWords.contains(wd.word)) {
					wordAuthorScore[i] = 1;
				}
			}
		}

		// 3 - Sentence Scoring
		/*
		 * Related to scoring, we have: wordFreq (int[]) -> Index j -> frequency
		 * of word wordsAnswerDistinct.get(j) sentencePosWeight(double[]) ->
		 * Index j -> sentence position score of sentence of word (j)
		 * wordTitleFreq (int[]) -> Index j -> frequency of word j in title
		 */
		double[] sentenceScores = new double[answerSentenceCount];
		for (int i = 0; i < answerSentenceCount; i++) {
			sentenceScores[i] = 0;
		}
		for (int i = 0; i < answerDistinctWordCount; i++) {
			int sentenceIndex = answerWordsDistinct.get(i).sentenceId;
			double wordFreqScore = wordFrequencyScores[i] * frequencyMultiplier;
			double titleSimilarityScore = wordTitleRelationScores[i] * titleSimilarityMultiplier;
			double sentencePositionScore = wordSentencePositionScores[i] * sentencePositionMultiplier;
			double questionScore = wordQuestionScore[i] * 2;
			double authorScore = wordAuthorScore[i] * 1.25;
			sentenceScores[sentenceIndex] += (titleSimilarityScore + wordFreqScore + questionScore + authorScore)*(1+sentencePositionScore);

		}

		// 4 - Sentence Ranking
		// Sort the sentence scores
		// PrintArray("Sentence Scores", sentenceScores);
		// PrintArray("Sentence Position Scores", wordSentencePositionScores);
		// PrintArray("Title Freq Scores", wordTitleRelationScores);
		// PrintArray("Frequency Scores", wordFrequencyScores);

		// Store default indexes
		int[] sentenceIndex = new int[answerSentenceCount];
		for (int i = 0; i < answerSentenceCount; i++) {
			sentenceIndex[i] = i;
		}
		// Sort the score array while also changing the index array accordingly
		if (summaryMode.compareTo("Mean Filter Ordered") != 0) {
			for (int i = 0; i < answerSentenceCount - 1; i++) {
				double maxScore = sentenceScores[i];
				int maxIndex = i;
				for (int j = i + 1; j < answerSentenceCount; j++) {
					if (sentenceScores[j] > maxScore) {
						maxScore = sentenceScores[j];
						maxIndex = j;
					}
				}
				double d_tmp;
				d_tmp = sentenceScores[i];
				sentenceScores[i] = sentenceScores[maxIndex];
				sentenceScores[maxIndex] = d_tmp;
				int i_tmp;
				i_tmp = sentenceIndex[i];
				sentenceIndex[i] = sentenceIndex[maxIndex];
				sentenceIndex[maxIndex] = i_tmp;
			}
		}
		// PrintArray("Sorted Sentence Scores", sentenceScores);
		// PrintArray("Sorted Sentence Indexes", sentenceIndex);

		// 5 - Constructing The New Text
		String newText = "";
		int newSentenceCount = 0;
		double reduction = 0;
		switch (summaryMode) {
		case "Mean Filter Unordered":
			// Calculate the mean and get the sentence only above the mean value
			double meanScore1 = 0;
			for (int i = 0; i < answerSentenceCount; i++) {
				meanScore1 += sentenceScores[i];
			}
			meanScore1 /= answerSentenceCount;
			// Construct the new text
			for (int i = 0; i < answerSentenceCount; i++) {
				if (sentenceScores[i] >= meanScore1) {
					Sentence s = answerSentencesPreprocessed.get(sentenceIndex[i]);
					newText += s.content + s.endsWith;
					newSentenceCount++;
				} else {
					break;
				}
			}
			break;
		case "Mean Filter Ordered":
			// Calculate the mean and get the sentence only above the mean value
			double meanScore2 = 0;
			for (int i = 0; i < answerSentenceCount; i++) {
				meanScore2 += sentenceScores[i];
			}
			meanScore2 /= answerSentenceCount;
			// Construct the new text
			for (int i = 0; i < answerSentenceCount; i++) {
				if (sentenceScores[i] >= meanScore2) {
					Sentence s = answerSentencesPreprocessed.get(sentenceIndex[i]);
					newText += s.content + s.endsWith;
					newSentenceCount++;
				} else {
					// do nothing
				}
			}
			break;
		case "Sentence Ratio":
			// Summary has %50 of the original sentence count;
			double sentencesLeft = answerSentenceCount * 0.5;
			sentencesLeft = Math.floor(sentencesLeft);
			for (int i = 0; i < answerSentenceCount; i++) {
				if (sentencesLeft > 0.0) {
					Sentence s = answerSentencesPreprocessed.get(sentenceIndex[i]);
					newText += s.content + s.endsWith;
					sentencesLeft--;
					newSentenceCount++;
				} else {
					break;
				}
			}
			break;
		}
		reduction = ((answerSentenceCount - newSentenceCount) / (double) (answerSentenceCount)) * 100;
		newText += "\n\nOriginal Sentence Count = " + answerSentenceCount + "\nSummary Sentence Count = "
				+ newSentenceCount + "\nReduction = " + (new DecimalFormat("##.##").format(reduction)) + "%";
		textAreaSummary.setText(newText);

		// Extra - Show the preprocess result
		String preprocessWords = "";
		for (Word w : answerWordsDistinct) {
			preprocessWords += w.word + "\n";
		}
		textAreaPreprocessWords.setText(preprocessWords);

		// Extra - Show hand written answer

	}

	@SuppressWarnings("unused")
	private void PrintArray(String title, double[] arr) {
		System.out.println(title);
		for (double i : arr) {
			System.out.print(i + " ");
		}
		System.out.println("");
	}

	@SuppressWarnings("unused")
	private void PrintArray(String title, int[] arr) {
		System.out.println(title);
		for (int i : arr) {
			System.out.print(i + " ");
		}
		System.out.println("");
	}

	private ArrayList<Word> GetWordsFromSentences(ArrayList<Sentence> sentences) {
		ArrayList<Word> words = new ArrayList<>();
		for (Sentence s : sentences) {
			words.addAll(s.words);
		}
		return words;
	}

	// Returns a distinct list of words
	private ArrayList<Word> RemoveRequrrences(ArrayList<Word> words) {
		ArrayList<String> addedWords = new ArrayList<>();
		ArrayList<Word> distinctWords = new ArrayList<>();
		for (Word w : words) {
			if (!addedWords.contains(w.word)) {
				addedWords.add(w.word);
				distinctWords.add(w);
			}
		}
		return distinctWords;
	}
}
