import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class DataField extends JTabbedPane{
    private final int MAX_HISTORY = 3;
    private JPanel jPanel = new JPanel(new BorderLayout()); // 화면을 둘로 쪼갠다: upperPane + textField
    private JTextArea textField = new JTextArea();
    private JLabel[] historyLabel = new JLabel[MAX_HISTORY];

    private String[] fileHistory = new String[MAX_HISTORY];
    private int historyIndex;

    private String filePath;
    private String historyPath = "/Users/ppang/Documents/history.txt";    //TODO: make history.txt file
    /**
     * @param String filePath
     * */
    public DataField(){
    	
    }
    
	public DataField(String path) throws IOException { //TODO: 인자 추가하기, this.filepath = filepath;
        this.filePath = path;
		pullContent(textField);
        setFileHistory();
        drawFrame();
        add(jPanel);
	}

    private void drawFrame() {
        JPanel upperPane = new JPanel();    // 양쪽으로 정렬하기 위함: historyLabel[] + saveButton
        upperPane.setLayout(new GridLayout(1,2));   // 서로 다른 레이아웃매니저를 설정할 수 있음

        for(int i=0; i<MAX_HISTORY; i++) {            // init historyLable[]
            historyLabel[i] = new JLabel(fileHistory[i]);
            upperPane.add(historyLabel[i]);
        }

        JPanel menuPane = new JPanel();     // 오른쪽으로 정렬하기 위함: saveButton
        menuPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(new PressingListener());

        menuPane.add(saveButton);
        upperPane.add(menuPane);

        jPanel.add(upperPane, BorderLayout.NORTH);
        jPanel.add(textField, BorderLayout.CENTER);
    }

    private class PressingListener implements ActionListener {  // to listen: saveButton
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                saveContent();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void setFileHistory() throws IOException {  // set fileHistory[] from history.txt
        FileReader fileReader = new FileReader(historyPath);
        BufferedReader reader = new BufferedReader(fileReader);

        for(historyIndex=0; historyIndex<MAX_HISTORY; historyIndex++) {
            String line = reader.readLine();
            if(line==null) break;
            fileHistory[historyIndex] = line;
        }
        reader.close();
        fileReader.close();
    }

    private void saveHistory() throws IOException {
        new File(historyPath).delete();
        PrintWriter printWriter = new PrintWriter(historyPath);

        for(int i=0; i<MAX_HISTORY; i++)
            if(fileHistory[i]!=null)
                printWriter.println(fileHistory[i]);
            else break;
        printWriter.close();
    }

    private void pullContent(JTextArea textField) throws IOException {  // filePath 파일의 내용 -> textField에 불러옴
        FileReader fileReader = new FileReader(filePath);
        BufferedReader reader = new BufferedReader(fileReader);
        textField.setText("");      // make empty textField

        while(true) {
            String line = reader.readLine();
            if(line==null) break;
            textField.append(line+"\n");
        }
        reader.close();
        fileReader.close();
    }

    private void saveContent() throws IOException { // textField의 내용 -> filePath 파일에 저장
        new File(filePath).delete();    // 덮어쓰기위해 기존 파일을 지움
        FileWriter fileWriter = new FileWriter(filePath, true);
        textField.write(fileWriter);
        fileWriter.close();

        if(historyIndex==MAX_HISTORY-1 && fileHistory[historyIndex]!=null) {
            for (int i = 0; i < historyIndex; i++)
                fileHistory[i] = fileHistory[i + 1];

        } else if(fileHistory[historyIndex]!=null) {
            historyIndex++;
        }

        if(fileHistory[0]==null
                || !fileHistory[historyIndex-1].equals(filePath)) {
            fileHistory[historyIndex] = filePath;
            historyLabel[historyIndex].setText(filePath);
        }
        saveHistory();
    }
}
