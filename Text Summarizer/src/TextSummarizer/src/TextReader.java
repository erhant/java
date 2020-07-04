import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * @author erhan
 *
 * This class reads the text from PostGRE Database
 */
public class TextReader {
	
	private Connection conn;
	
	public TextReader(String[] serverInfo) {
		try {
			this.conn = DriverManager.getConnection(serverInfo[0], serverInfo[1], serverInfo[2]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println("SQL CONNECTION ERROR");
			e.printStackTrace();
		}				
	}
	
	public ArrayList<Question> GetQuestions() {
		ArrayList<Question> ans = new ArrayList<Question>();
		String selectionQuery = "SELECT qid, author, topic, question, answer FROM questions ORDER BY qid";
		try {
			PreparedStatement p;
			p = conn.prepareStatement(selectionQuery);
			ResultSet result = p.executeQuery();
			while ( result.next() ) {
				ans.add(new Question(
						result.getInt(1),
						result.getString(2),
						result.getString(3),
						result.getString(4),
						result.getString(5)
						));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
	
	public ArrayList<String> GetSummaries() {
		ArrayList<String> ans = new ArrayList<String>();
		String selectionQuery = "SELECT summary FROM summaries ORDER BY qid";
		try {
			PreparedStatement p;
			p = conn.prepareStatement(selectionQuery);
			ResultSet result = p.executeQuery();
			while ( result.next() ) {
				ans.add(result.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ans;
	}
}
