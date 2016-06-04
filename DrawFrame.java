import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DrawFrame extends JFrame {
    DrawFrame thisFrame = this;
    private JTextField search; // 검색창으로 사용할 TextField 선언
    private JLabel info; // 검색정보를 알려줄 Label 선언
    private JPanel searchPane; // 검색도구를 배치할 Panel 선언
    private JPanel fileList;
    boolean isIconView;
    private FileTable fileTable;
    private FileGrid fileGrid;
	private JSplitPane filePane;
	private DataField dataField;
    private String currDir = ExplorerMain.class.getResource(".").getPath();

	public DrawFrame() throws IOException {
		setTitle("Explorer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		initFrame();
		
		setSize(700,700);
		setVisible(true);
	}
	
	private void initFrame() throws IOException {
        isIconView = false;
		JSplitPane folderPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // Center
		FolderTree folderTree = new FolderTree(this); // left comp
		JScrollPane folderScroll = new JScrollPane(folderTree);
		folderScroll.setMinimumSize(new Dimension(150,200));        // FolderTree의 최소 크기 지정

        search = new JTextField(10); // 검색창으로 사용할 TextField 초기화
        info = new JLabel(); // 검색정보를 알려줄 Label 초기화
        filePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // Right
        fileList = new JPanel(new BorderLayout());

        fileTable = new FileTable(); // right top comp
        fileGrid = new FileGrid();  // right top comp(reserved)
        fileList.add(fileTable, BorderLayout.CENTER);

        JPanel fileListMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        fileList.add(fileListMenu, BorderLayout.NORTH);
        searchPane = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 검색도구를 추가할 Panel 배치
        searchPane.add(info); // 검색정보 추가
        searchPane.add(search); // 검색창 추가
        search.addActionListener(new SearchFileName()); // 검색창 이용을 위한 ActionListener 추가
        fileListMenu.add(searchPane, BorderLayout.WEST); // 검색 Panel 추가

        JToggleButton toGridButton
                = new JToggleButton(
                resizeImage(currDir + "icon/switch_off.png", 32, 14), false);
        setButton(toGridButton);
        fileListMenu.add(toGridButton);

		filePane.setTopComponent(fileList);

		dataField = new DataField(this); // right bottom comp
		filePane.setBottomComponent(dataField);
		
		folderPane.setLeftComponent(folderScroll);
		folderPane.setRightComponent(filePane);
		
		add(folderPane, "Center");
		filePane.setResizeWeight(0.5); // 상하 diver 위치 조절
		folderPane.setResizeWeight(0.2); // 좌우 diver 위치 조절
	}

    class SearchFileName implements ActionListener {
        java.util.List<File> fileArray = new ArrayList<File>();
        File[] fileList;
        FileTable t;
        FileGrid g;
        String realPath;
        public void actionPerformed(ActionEvent e) {
            String input = search.getText();
            int count = 0;
            for(int i = 0; i < fileTable.rowData.length; i++) {
                String fileName = fileTable.rowData[i][0].toString();
                if(fileName.contains(input)) {
                    realPath = fileTable.filePath + fileName;
                    fileArray.add(new File(realPath));
                    count++;
                }
            }

            fileList = fileArray.toArray(new File[fileArray.size()]);
            t = new FileTable(fileList, fileTable.filePath, thisFrame);
            g = new FileGrid(fileList, fileTable.filePath, thisFrame);

            if(thisFrame.isIconView) {
                thisFrame.setFileTable(t);
                thisFrame.setFileGrid(g);
            } else {
                thisFrame.setFileGrid(g);
                thisFrame.setFileTable(t);
            }
            fileArray.clear();
        }
    }
	
	public void setFileTable(FileTable table) {
        if(isIconView) fileList.remove(fileGrid);
        else fileList.remove(fileTable);

        fileTable = table;
        fileList.add(fileTable);
		filePane.setTopComponent(fileList);
	}

    public void setFileGrid(FileGrid grid) {
        if(isIconView) fileList.remove(fileGrid);
        else fileList.remove(fileTable);

        fileGrid = grid;
        fileList.add(fileGrid);
        filePane.setTopComponent(fileList);
    }
	
	public void setDataField(DataField field) throws IOException{
		dataField = field;
		filePane.setBottomComponent(dataField);
	}

    public void setButton(JToggleButton button) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        ImageIcon onButtonIcon = resizeImage(currDir + "icon/switch_on.png", 32, 14);
        ImageIcon offButtonIcon = resizeImage(currDir + "icon/switch_off.png", 32, 14);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JToggleButton b = (JToggleButton)e.getSource();
                if(b.isSelected()) {
                    isIconView = true;
                    b.setIcon(onButtonIcon);
                    fileList.remove(fileTable);
                    fileList.add(fileGrid, BorderLayout.CENTER);
                } else {
                    isIconView = false;
                    b.setIcon(offButtonIcon);
                    fileList.remove(fileGrid);
                    fileList.add(fileTable, BorderLayout.CENTER);
                }
                filePane.setTopComponent(fileList);
            }
        });
    }

    public ImageIcon resizeImage(String imagePath, int x, int y) {
        ImageIcon icon = new ImageIcon(imagePath);

        Image originalImage = icon.getImage();
        Image resizedImage =
                originalImage.getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH);
        icon.setImage(resizedImage);

        return icon;
    }
}
