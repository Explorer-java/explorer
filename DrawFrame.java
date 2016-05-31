
import java.awt.*;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class DrawFrame extends JFrame {
	private FileTable fileTable;
	private JSplitPane filePane;
	private DataField dataField;

	public DrawFrame() throws IOException {
		setTitle("Explorer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initFrame();
		
		setSize(800,800);
		setVisible(true);
	}
	
	private void initFrame() throws IOException {
		JSplitPane folderPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // Center
		FolderTree folderTree = new FolderTree(this); // left comp
		JScrollPane folderScroll = new JScrollPane(folderTree);
		folderScroll.setMinimumSize(new Dimension(150,200));
		
		filePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // Right
		fileTable = new FileTable(); // right top comp
		filePane.setTopComponent(fileTable);
		dataField = new DataField(this); // right bottom comp
		filePane.setBottomComponent(dataField);
		
		folderPane.setLeftComponent(folderScroll);
		folderPane.setRightComponent(filePane);
		
		add(folderPane, "Center");
		filePane.setResizeWeight(0.5); // 상하 diver 위치 조절
		folderPane.setResizeWeight(0.2); // 좌우 diver 위치 조절
	}
	
	public void setFileTable(FileTable table){
		fileTable = table;
		filePane.setTopComponent(fileTable);
	}
	
	public void setDataField(DataField field) throws IOException{
		dataField = field;
		filePane.setBottomComponent(dataField);
	}
}
