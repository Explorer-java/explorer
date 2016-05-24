import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

public class DrawFrame extends JFrame {
    DataField dataField = new DataField();  // @dataField(@right bottom comp) main content

	public DrawFrame() throws IOException {
		setTitle("Explorer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initFrame();
		
		setSize(800,800);
		setVisible(true);
	}
	
	private void initFrame() throws IOException {
		JSplitPane folderPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // Center
		FolderTree folderTree = new FolderTree(); // left comp
		
		JSplitPane filePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // Right
		FileTable fileTable = new FileTable(); // right top comp
        filePane.setTopComponent(fileTable);
        JPanel bottomPane = new JPanel(); // right bottom comp
        filePane.setBottomComponent(bottomPane);
        bottomPane.setLayout(new BorderLayout());

        JPanel menuPane = new JPanel(); // dataField's menuBar
        menuPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("저장");
        saveButton.addActionListener(new PressingListener());
        menuPane.add(saveButton);
        bottomPane.add(menuPane, BorderLayout.NORTH);

        bottomPane.add(dataField, BorderLayout.CENTER);
		
		folderPane.setLeftComponent(folderTree);
		folderPane.setRightComponent(filePane);
		
		add(folderPane, "Center");
		filePane.setResizeWeight(0.5); // 상하 diver 위치 조절
		folderPane.setResizeWeight(0.2); // 좌우 diver 위치 조절
	}

    private class PressingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                dataField.saveContent();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
