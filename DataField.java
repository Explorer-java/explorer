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

    private String currDir;
    private String filePath;
    private String historyPath;    //TODO: make history.txt file

    public DataField(){
    	
    }
    
	public DataField(String path) throws IOException { //TODO: 인자 추가하기, this.filepath = filepath;
        this.currDir = ExplorerMain.class.getResource(".").getPath();
        this.historyPath = currDir + "history.txt";
        this.filePath = path;
        setFileHistory();

        pullContent(textField);
        drawFrame();
        addHistory(filePath);
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
        
        JScrollPane scroll = new JScrollPane(textField);

        jPanel.add(upperPane, BorderLayout.NORTH);
        jPanel.add(scroll, BorderLayout.CENTER);
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
        FileReader fileReader;
        try {
            fileReader = new FileReader(historyPath);

        } catch (IOException e) {
            PrintWriter printWriter = new PrintWriter(historyPath);
            printWriter.close();
            fileReader = new FileReader(historyPath);
        }
        BufferedReader reader = new BufferedReader(fileReader);
        System.out.println("historyIndex[" + historyIndex + "] @setFileHistory()-1");
        for(historyIndex=0; historyIndex<MAX_HISTORY; historyIndex++) {
            String line = reader.readLine();
            if(line==null) break;
            fileHistory[historyIndex] = line;
        }
        System.out.println("historyIndex[" + historyIndex + "] @setFileHistory()-2");
        reader.close();
        fileReader.close();
    }

    private void saveHistory() throws IOException {     // historyPath 파일 <- fileHistory[]저장.
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
    }

    private void addHistory(String filePath) throws IOException {
        System.out.println("historyIndex[" + historyIndex + "] @addFileHistory()");
        if(fileHistory[0]!=null)
            for(int i=0; i<MAX_HISTORY; i++)                    // 이전에
                if(fileHistory[i]!=null
                        && fileHistory[i].equals(filePath)) {   // 동일 history 존재할 때
                    deleteHistory(i);                           // 중복되는 기존 history 지움
                    historyIndex--;
                }
        if(historyIndex>=MAX_HISTORY) { // history 꽉 찼을 때
            deleteHistory(0);           // 오래된 순으로 지움
            historyIndex--;
        }
        fileHistory[historyIndex] = filePath;
        for(int i=0; i<MAX_HISTORY; i++)
            if(fileHistory[i]!=null)
                historyLabel[i].setText(fileHistory[i]);
        saveHistory();
    }

    private void deleteHistory(int goalIndex) {
        for(int i=goalIndex; i+1<MAX_HISTORY; i++)
            if(fileHistory[i+1]!=null)
                fileHistory[i] = fileHistory[i+1];
            else fileHistory[i] = null;
    }
}
