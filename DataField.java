import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class DataField extends JTabbedPane{
    JPanel jPanel = new JPanel(new BorderLayout());
    JTextArea textField = new JTextArea();
    JLabel[] historyLabel = new JLabel[3];

    String[] fileHistory = new String[3];
    int historyIndex;

    String filePath = "//Users//soeun//Desktop//file.txt";
    /**
     * @param String filePath
     * */
	public DataField() throws IOException { //TODO: add parameter, this.filepath = filepath;
        pullContent(textField);
        setFileHistory();
        drawFrame();
        add(jPanel);
	}

    private void drawFrame() {
        JPanel dataFieldPane = new JPanel();
        dataFieldPane.setLayout(new GridLayout(1,2));

        JPanel historyPane = new JPanel();  // dataField's historyList
        historyPane.setLayout(new FlowLayout(FlowLayout.LEFT));

        for(int i=0; i<3; i++) {
            historyLabel[i] = new JLabel(fileHistory[i]);
            dataFieldPane.add(historyLabel[i]);
        }

        JPanel menuPane = new JPanel(); // dataField's menuBar
        menuPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(new PressingListener());

        menuPane.add(saveButton);

        dataFieldPane.add(menuPane);

        jPanel.add(dataFieldPane, BorderLayout.NORTH);
        jPanel.add(textField, BorderLayout.CENTER);
    }

    private class PressingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                saveContent();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    void setFileHistory() throws IOException {
        String historyPath = "//Users//soeun//Desktop//history.txt";    //TODO: change to floating route
        FileReader fileReader = new FileReader(historyPath);
        BufferedReader reader = new BufferedReader(fileReader);

        for(historyIndex=0; historyIndex<3; historyIndex++) {
            String line = reader.readLine();
            if(line==null) break;
            fileHistory[historyIndex] = line;
        }
        reader.close();
        fileReader.close();
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
        System.out.println(historyIndex);
        if(historyIndex==2 && fileHistory[historyIndex]!=null) {
            for (int i = 0; i < historyIndex; i++)
                fileHistory[i] = fileHistory[i + 1];
//       else if(fileHistory[0].isEmpty())
        } else if(fileHistory[historyIndex]!=null) {
            historyIndex++;
        }

        fileHistory[historyIndex] = filePath;
        historyLabel[historyIndex].setText(filePath);
    }
}
