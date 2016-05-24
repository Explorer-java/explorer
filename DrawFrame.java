import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

public class DrawFrame extends JFrame {

	public DrawFrame() {
		setTitle("Explorer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initFrame();
		
		setSize(800,800);
		setVisible(true);
	}
	
	private void initFrame(){ 
		JSplitPane folderPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // Center
		FolderTree folderTree = new FolderTree(); // left comp
		JScrollPane folderScroll = new JScrollPane(folderTree);
		
		JSplitPane filePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // Right
		FileTable fileTable = new FileTable(); // right top comp
		DataField dataField = new DataField(); // right bottom comp
		filePane.setTopComponent(fileTable);
		filePane.setBottomComponent(dataField);
		
		folderPane.setLeftComponent(folderScroll);
		folderPane.setRightComponent(filePane);
		
		add(folderPane, "Center");
		filePane.setResizeWeight(0.5); // 상하 diver 위치 조절
		folderPane.setResizeWeight(0.2); // 좌우 diver 위치 조절
	}
}
