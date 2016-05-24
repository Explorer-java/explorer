import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.io.*;

public class DataField extends JTabbedPane{
    JTextArea textField;
    String filePath = "//Users//soeun//Desktop//file.txt";
    /**
     * @param String filePath
     * */
	public DataField() throws IOException { //TODO: add parameter, this.filepath = filepath;
		textField = new JTextArea();
        pullContent(textField);
		add(textField);
	}

    private void pullContent(JTextArea textField) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fileReader);
        textField.setText("");      // init

        while(true) {
            String line = reader.readLine();
            if(line==null) break;
            textField.append(line+"\n");
        }
        reader.close();
        fileReader.close();
    }

    void saveContent() throws IOException {
        new File(filePath).delete();
        FileWriter fileWriter = new FileWriter(filePath, true);
        textField.write(fileWriter);
        fileWriter.close();
    }
}
