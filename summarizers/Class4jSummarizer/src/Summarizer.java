/**
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.classifier4J.Utilities;
import net.sf.classifier4J.summariser.ISummariser;

/**
 * @author mbrouns
 *
 */
public class Summarizer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String input = "MEDICINE Testing, Testing Unusual proteins could improve cancer diagnosis and reduce deaths The key to treating cancer is to catch it early. But identifying the subtle changes in cells that betray their turncoat tendencies requires skill-and good luck-on the part of pathologists. Many cancers are not spotted until too late, when the rebel colonies are well enough established to put up a fight and found new mutinous outposts. Matritech, a Massachusetts-based start-up company, has developed a diagnostic technique that detects bladder cancer more easily-and possibly more effectively-than existing methods can. Matritech's test, which the company expects the Food and Drug Administration to approve by this summer, measures the amount of a particular type of protein in urine. Bladder cancer patients excrete this substance, called a nuclear matrix protein, in greater amounts than healthy subjects do. All cell nuclei contain matrix proteins, constituents that give the nucleus its shape and organize the chromosomes. Researchers have known of their existence since the 1970s. Their possible value has become apparent just in the past few years, however, since investigators at the Massachusetts Institute of Technology showed that some nuclear matrix proteins in cancer cells are different from those in normal cells. Others are present in elevated amounts. The unusual proteins seem to explain why the nuclei of cancer cells are often oddly shaped. The proteins escape into body fluids, where they can be identified using antibodies. Thus, the way is opened to tests for abnormal matrix proteins or, as in the case of Matritech's bladder cancer test, a normal one in unusual amounts. \"There's been all this hoopla about genetic screening, but nuclear matrix protein testing could have the biggest impact of all,\" says Lance Willsey of Harvard Medical School. Stephen D. Chubb, Matritech's chief executive, says his company's test, called NMP22, detected all cases of invasive disease in a trial with 1,000 subjects who had previously been treated for bladder cancer and were being monitored for recurrences-which are very common. Furthermore, it found about 70 percent of cases of bladder cancer that was still localized and in less need of urgent treatment. A negative result meant patients had a 90 percent chance of cancer not developing in the next three to six months-a useful predictive ability, because that is the usual interval between follow-up visits for bladder cancer patients. Those figures, Chubb notes, indicate that NMP22 could be used instead of current techniques, which involve examining cells from the bladder shed in urine or viewing the inside of the bladder with a fiber-optic device (cystoscopy). Moreover, Matritech's test is one sixth the price of cystoscopy, which is typically billed at $300, and obviates any risk of infection. Matritech is initially seeking approval for NMP22 solely to check for recurrences of bladder cancer. But Chubb is not averse to the idea that NMP22 could be used more widely to screen for the disease in people who have not previously been diagnosed. NMP22 might be the first of a series of matrix protein-based tests. Although the matrix protein that NMP22 detects is found in low levels in nuclei throughout the body, other nuclear matrix proteins are more specific. In April, Robert H. Getzenberg of the University of Pittsburgh Cancer Institute and his colleagues reported their discovery of five matrix proteins (not yet employed in any test) that occur exclusively in bladder cancer cells-thus suggesting the possibility of even more accurate diagnosis. Chubb states that Matritech has strong patent protection for all uses of nuclear matrix proteins as cancer diagnostics and that it is working on such tests for early detection of prostate, colon, cervical and breast cancer. Most of these will be based on proteins that occur exclusively in particular cancers. But Willsey wonders whether Matritech has sufficient resources to develop nuclear matrix protein-based tests as fast as the company, and he, would like. Nuclear matrix proteins could represent targets for therapeutic agents, too. The difficulty is that drugs have trouble penetrating cell nuclei. But Chubb says Matritech is giving the development of such therapeutics serious thought-and about 10 percent of its research budget.";
		int docID = 147;
		Connection c = null;
		try{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:../../app/webroot/crowdsum");
			c.setAutoCommit(false);
			System.out.println("Database connection established");
			
			PreparedStatement sqlCheckDocumentSummarized = c.prepareStatement("Select [document_id] FROM sentences WHERE document_id = ?;");
			sqlCheckDocumentSummarized.setInt(1, docID);
			
			ResultSet rsDocumentSummarized = sqlCheckDocumentSummarized.executeQuery();
			while (rsDocumentSummarized.next()){
				System.out.println("Document already summarized, aborting");
				System.exit(1);
			}
			
			PreparedStatement sqlSelectDocumentText = c.prepareStatement("Select [fulltext] FROM documents WHERE id = ?;");
			sqlSelectDocumentText.setInt(1, docID);
			
			ResultSet rs = sqlSelectDocumentText.executeQuery();
			while (rs.next()){
				input = rs.getString("fulltext");
			}
		
			ISummariser summariser = new CustomSummarizer();
			int noOfLines = (int) Math.floor(Utilities.getSentences(input).length * 0.1);
			String result = summariser.summarise(input, noOfLines);
			String[] resultSentences = Utilities.getSentences(result);
			System.out.println("Summary sentences found, inserting into database");
			
			for(String s: resultSentences){
				PreparedStatement sqlAddSentence = c.prepareStatement("INSERT INTO sentences (document_id, sentence) VALUES (?, ?);", Statement.RETURN_GENERATED_KEYS);
				sqlAddSentence.setInt(1, docID);
				sqlAddSentence.setString(2, s);				
				sqlAddSentence.execute();
				
				ResultSet generatedKeys = sqlAddSentence.getGeneratedKeys();
				if(generatedKeys.next()){
					try{
					PreparedStatement sqlAddUserSentence = c.prepareStatement("INSERT INTO users_sentences (user_id, sentence_id, ranking) VALUES (0, ?, 1)");
					sqlAddUserSentence.setInt(1, generatedKeys.getInt(1));
					sqlAddUserSentence.execute();
					}catch (Exception e){
						e.printStackTrace();
					}
				}else{
					throw new SQLException("Adding sentence failed");
				}
				
				
			}
			System.out.println("Database insertion complete");
		    c.commit();
		    c.close();
		    System.out.println("Database connection closed");
		}catch( Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		    System.exit(0);
		}
		
	}

}
