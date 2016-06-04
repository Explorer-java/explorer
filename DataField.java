import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class DataField extends JTabbedPane{

    private DrawFrame f;
    private final int MAX_HISTORY = 3;
    private JPanel jPanel = new JPanel(new BorderLayout()); // 화면을 둘로 쪼갠다: upperPane + textField
    private JTextArea textField = new JTextArea();

    private JLabel[] historyLabel = new JLabel[MAX_HISTORY];
    private String[] fileHistory = new String[MAX_HISTORY];
    private int historyIndex;

    private String currDir;
    private String filePath;
    private String historyPath;

    public DataField(DrawFrame f)throws IOException {
        this(null, f);
        drawFrame();
    }
    
	public DataField(String path, DrawFrame f) throws IOException {
        this.currDir = ExplorerMain.class.getResource(".").getPath();
        this.historyPath = currDir + "history.txt";
        this.filePath = path;
        this.f = f;

        setFileHistory();
        if (filePath != null) {
            pullContent(textField);
            drawFrame();
            {
                int pos = filePath.lastIndexOf(".");
                String fileExtension = filePath.substring(pos + 1);
                if (fileExtension.equals("txt")) {
                    addHistory(filePath);
                }
                add(jPanel);
            }
        }

	}

    private void drawFrame() {
        JPanel upperPane = new JPanel();    // 양쪽으로 정렬하기 위함: historyLabel[] + saveButton
        upperPane.setLayout(new GridLayout(1,2));   // 서로 다른 레이아웃매니저를 설정할 수 있음

        for(int i=0; i<MAX_HISTORY; i++) {            // init historyLable[]
            historyLabel[i] = new JLabel(fileHistory[i]);
            historyLabel[i].addMouseListener(new HistoryListener());
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

    private class HistoryListener extends MouseAdapter {   // to listen: historyLabel[i]
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel selectedLabel = (JLabel)e.getSource();
            try {
                f.setDataField(new DataField(selectedLabel.getText(), f));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
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

    private String getExtension(String s) {
        int pos = s.lastIndexOf(".");
        String fileExtension = s.substring(pos + 1);

        return fileExtension;
    } // 파일의 유형을 얻는 메소드

    private void saveContent() throws IOException { // textField의 내용 -> filePath 파일에 저장
        String fileExtension = getExtension(filePath);

        if(fileExtension.equals("txt")) { // 파일 유형이 txt일 때만 내용 수정 후 저장 가능
            new File(filePath).delete();    // 덮어쓰기위해 기존 파일을 지움
            FileWriter fileWriter = new FileWriter(filePath, true);
            textField.write(fileWriter);
            fileWriter.close();
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
        for(historyIndex=0; historyIndex<MAX_HISTORY; historyIndex++) {
            String line = reader.readLine();
            if(line==null) break;
            fileHistory[historyIndex] = line;
        }
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
        String fileExtension = getExtension(filePath);

        if(fileExtension.equals("txt")) { // 파일 유형이 txt일 때만 내용 열기
            FileReader fileReader = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(fileReader);
            textField.setText("");      // make empty textField

            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                textField.append(line + "\n");
            }
            reader.close();
            fileReader.close();
        }
    }

    private void addHistory(String filePath) throws IOException {
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