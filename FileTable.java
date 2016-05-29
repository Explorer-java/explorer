import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class FileTable extends JScrollPane{
	private DrawFrame f;
	private Object columnNames[] = {"이름", "수정한 날짜", "유형", "크기(kb)"};
	private Object rowData[][];
	private String filePath;
	
	public FileTable(){
		rowData = new Object[1][columnNames.length];
		setTable();
	}

	public FileTable(File[] fileList, String path, DrawFrame f) {
		rowData = new Object[fileList.length][columnNames.length];
		this.f = f;
		this.filePath = path;
		
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
		
		// table sort by header
		table.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> tableSorter = new TableRowSorter<TableModel>(table.getModel());
		
		tableSorter.setComparator(3, new Comparator<Long>() {
			  @Override
			  public int compare(Long num1, Long num2) {
				  if(num1 > num2)
					  return 1;
				  else if(num1 == num2)
					  return 0;
				  else
					  return -1;
			  }
			});
			
		table.setRowSorter(tableSorter);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {		
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				String realPath = filePath+table.getValueAt(table.getSelectedRow(), 0).toString();
				try {
					f.setDataField(new DataField(realPath, f));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
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
