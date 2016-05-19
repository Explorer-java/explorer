import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FileTable extends JScrollPane{
	static String columnNames[] = { "상품번호", "상품이름", "상품가격", "상품설명" };

	static Object rowData[][] ={
		{ 1, "맛동산", 100, "오리온" },
		{ 2, "아폴로", 200, "불량식품" },
		{ 3, "칸쵸코", 300, "과자계의 레전드" } };
		
	public FileTable(){
		JTable table = new JTable(rowData,columnNames);
		setViewportView(table);
	}
}
