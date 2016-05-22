import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTable extends JScrollPane {
	Object columnNames[] = {"이름", "수정한 날짜", "유형", "크기(kb)"};
	Object rowData[][] = new Object[1000][4];

	public FileTable() {
		File file = new File("C:/Users/jay/Desktop/Project");
		File[] fileList = file.listFiles();
		int i = 0;

		for (File tempFile : fileList) {
			String name;
			String date;
			String extension;
			long size;

			if(tempFile.isDirectory()) {
				name = getFileName(tempFile);
				date = getDate(tempFile);
				extension = "폴더";
				size = getSize(tempFile);
			}
			else {
				name = getFileName(tempFile);
				date = getDate(tempFile);
				extension = getExtension(name);
				size = getSize(tempFile);
			}

			rowData[i][0] = name;
			rowData[i][1] = date;
			rowData[i][2] = extension;
			rowData[i][3] = size;

			i++;
		}

		JTable table = new JTable(rowData,columnNames);
		setViewportView(table);
	}

	String getFileName(File f) {
		String fileName = f.getName();

		return  fileName;
	}

	String getDate(File f) {
		Date fileDate = new Date(f.lastModified());
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd a h:mm");

		return s.format(fileDate);
	}

	String getExtension(String s) {
		int pos = s.lastIndexOf(".");
		String fileExtension = s.substring(pos + 1);

		return fileExtension;
	}

	long getSize(File f) {
		long fileSize = f.length() / 1024;

		return fileSize;
	}
}
