import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.io.*;

public class DataField extends JTabbedPane{
    BufferedReader reader;
    /**
     * @param String filePath
     * */
	public DataField() throws IOException { //TODO: add parameter
        reader = new BufferedReader(new FileReader("//Users//soeun//Desktop//file.txt"));   //TODO: FileReader(filePath)
		JTextArea textField = new JTextArea();
        pullContent(textField);
		add(textField);
        reader.close();
	}

    private void pullContent(JTextArea textField) throws IOException {
        textField.setText("");      // init
        while(true) {
            String line = reader.readLine();
            if(line==null) break;
            textField.append(line+"\n");
        }
    }
}
