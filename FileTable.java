import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTable extends JScrollPane {
	Object columnNames[] = {"이름", "수정한 날짜", "유형", "크기(kb)"};
	Object rowData[][] = new Object[1000][4];
	
	public FileTable(){
		setTable();
	}

	public FileTable(File[] fileList) {
		if(fileList == null || fileList.length == 0){
			setTable();
			return;
		}
		
		int i = 0;
		for (File tempFile : fileList) { // 배열의 저장된 파일을 차례로 tempFile에 저장
			String name;
			String date;
			String extension;
			long size;

			if(tempFile.isDirectory()) { // tempFile이 폴더일 경우
				name = getFileName(tempFile);
				date = getDate(tempFile);
				extension = "폴더";
				size = getSize(tempFile);
			}
			else { // tempFile이 파일일 경우
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

		setTable();
	}
	
	private void setTable(){
		JTable table = new JTable(rowData,columnNames);
		setViewportView(table); // 테이블 생성
	}

	private String getFileName(File f) {
		String fileName = f.getName();

		return  fileName;
	} // 파일의 이름을 얻는 메소드

	private String getDate(File f) {
		Date fileDate = new Date(f.lastModified());
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd a h:mm");

		return s.format(fileDate);
	} // 파일의 수정한 날짜를 얻는 메소드

	private String getExtension(String s) {
		int pos = s.lastIndexOf(".");
		String fileExtension = s.substring(pos + 1);

		return fileExtension;
	} // 파일의 유형을 얻는 메소드

	private long getSize(File f) {
		long fileSize = f.length() / 1024;

		return fileSize;
	} // 파일의 크기를 얻는 메소드
}
