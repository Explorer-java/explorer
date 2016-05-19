import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class DataField extends JTabbedPane{
	public DataField(){
		JTextArea textField = new JTextArea();
		textField.setText("hello world");
		add(textField);
	}
}
