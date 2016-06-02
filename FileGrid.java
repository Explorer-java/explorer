
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by soeun on 2016. 6. 1..
 */
public class FileGrid extends FileTable {
    private DrawFrame f;
    private String data[][];
    private String filePath;
    private String currDir = ExplorerMain.class.getResource(".").getPath();
    private String iconPath;

    public FileGrid() {
        JPanel jPanel = new JPanel();
    }

    public FileGrid(File[] fileList, String path, DrawFrame f) {
        data = new String[fileList.length][2];
        this.f = f;
        this.filePath = path;

        if(fileList == null || fileList.length == 0){
            setGrid();
            return;
        }

        int i = 0;
        for (File tempFile : fileList) {    // 배열의 저장된 파일을 차례로 tempFile에 저장
            String name;
            String extension;

            if(tempFile.isDirectory()) {    // tempFile이 폴더일 경우
                name = getFileName(tempFile);
                extension = "폴더";
            } else {                        // tempFile이 파일일 경우
                name = getFileName(tempFile);
                extension = getExtension(name);
            }

            data[i][0] = name;
            data[i][1] = extension;

            i++;
        }
        Arrays.sort(data, new Comparator<String[]>(){
            @Override
            public int compare(final String[] first, final String[] second){
                final String extension1 = first[1];
                final String extension2 = second[1];

                return extension2.compareTo(extension1);
            }
        });

        setGrid();
    }

    private void setGrid() {
        JPanel jPanel = new JPanel(new GridLayout(0,4,20,20));
        for(String[] tempFile : data) {
            JPanel element = newElement(tempFile[0], tempFile[1]);
            element.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JPanel panel = (JPanel)e.getSource();
                    Component[] components = panel.getComponents();
                    String realPath = filePath + ((JLabel)components[1]).getText();

                    if(getExtension(((JLabel)components[1]).getText()).equals("txt")) {
                        panel.setBackground(Color.GRAY);
                        try {
                            f.setDataField(new DataField(realPath, f));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    JPanel panel = (JPanel)e.getSource();
                    panel.setBackground(null);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    JPanel panel = (JPanel)e.getSource();
                    panel.setBackground(Color.white);
                }

            });
            jPanel.add(element);
        }

        setViewportView(jPanel);
    }

    private JPanel newElement(String fileName, String extension) {
        JPanel element = new JPanel(new BorderLayout());
        ImageIcon icon;
        {
            if (extension.equals("txt")) {
                iconPath = currDir + "icon/file.png";
            } else if (extension.equals("폴더"))
                iconPath = currDir + "icon/folder.png";
            else iconPath = currDir + "icon/file_x.png";
        }
        icon = DrawFrame.resizeImage(iconPath, 50, 50);
        JLabel image = new JLabel("", icon, JLabel.CENTER);
        element.add(image, BorderLayout.CENTER);
        JLabel name = new JLabel(fileName, JLabel.CENTER);
        element.add(name, BorderLayout.SOUTH);

        return element;
    }

}
