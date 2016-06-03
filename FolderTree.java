import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import java.io.File;

public class FolderTree extends JTree implements TreeWillExpandListener{
	private final String EMPTYTAG = "NO MORE DIRECTORY";
	private DefaultMutableTreeNode root;
	private DrawFrame f;
    public String realPath;

	public FolderTree(DrawFrame f) {
		this.f = f;
		
		initTree();
		initFileSystem();
		
		DefaultTreeModel model = new DefaultTreeModel(root);
		setModel(model);
	}

	private void initTree() {
		/**
		 * 트리와 루트노드의 초기세팅 담당. 
		 */
		addTreeWillExpandListener(this);

		root = new DefaultMutableTreeNode("root");
	}

	private void initFileSystem() {
		/**
		 * 루트노드의 자식노드 초기화. 즉 루트디렉토리 세팅.
		 */
		File[] dir = File.listRoots();
		for (int i = 0; i < dir.length; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(dir[i].toString());
			node.add(new DefaultMutableTreeNode(EMPTYTAG));
			root.add(node);
		}
	}

	public String getPath(TreePath tp) {
		/**
		 * TreePath로 부터 String형의 파일경로를 만들어 반환한다.
		 * @param (TreePath) [root, /, .fseventsd] 같은 형태로 들어오면 
		 * @return (String) /.fseventsd/ 이렇게 바꿔줌 
		 */
		
		String realPath = "";
		String treePath = tp.toString();
		String path = treePath.substring(1, treePath.length() - 1); // [ ] 없애기

		String[] pathToken = path.split(", "); // , 단위로 분리 
		for (int i = 1; i < pathToken.length; i++) { // 분리한 토큰을 separator '/'를 포함한 파일경로로 변환. 
			realPath += pathToken[i];
			if (i != 1)
				realPath += File.separator;
		}

		return realPath;
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		/**
		 * Tree가 확장될 때 불리는 리스너로 파일경로로부터 자식 디렉토리를 노드에 추가한다.
		 * @param (event) event.getPath()로부터 파일경로를 추출하여 노드 확장에 사용.
		 */
		setSelectionPath(event.getPath()); // 화살표 버튼으로 디렉토리 확장 시 생길 수 있는 오류를 잡아준다고 함.
		String path = getPath(event.getPath());
        realPath = path;

		if(path.trim().length() == 0)
			return;
		
		File parentFile = new File(path); 
		File[] childrenFiles = parentFile.listFiles();

		DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
		lastNode.removeAllChildren();
		int i = 0;
		if (childrenFiles != null && childrenFiles.length != 0) { // 부모노드에 자식노드(디렉토리) 추가 
			int dirCount = 0;
			for (i = 0; i < childrenFiles.length; i++)
				if (childrenFiles[i].isDirectory()) {
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(childrenFiles[i].getName());
					childNode.add(new DefaultMutableTreeNode(EMPTYTAG));
					lastNode.add(childNode);
					dirCount++;
				}
			
			if(dirCount == 0) // 폴더에 파일만 존재 할 때 
				lastNode.add(new DefaultMutableTreeNode(EMPTYTAG));
			
		}else{ // 빈 폴더 일 때 
			lastNode.add(new DefaultMutableTreeNode(EMPTYTAG));
		}

		// 파일 테이블 재생성
		if(f.isIconView) {
            f.setFileTable(new FileTable(childrenFiles, path, f));
            f.setFileGrid(new FileGrid(childrenFiles, path, f));
        } else {
            f.setFileGrid(new FileGrid(childrenFiles, path, f));
            f.setFileTable(new FileTable(childrenFiles, path, f));
        }
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		// TODO Auto-generated method stub
		String path = getPath(event.getPath());
		if(path.trim().length() == 0)
			return;
		
		File parentFile = new File(path); 
		File[] childrenFiles = parentFile.listFiles();

		// 파일 테이블 재생성
        if(f.isIconView) {
            f.setFileTable(new FileTable(childrenFiles, path, f));
            f.setFileGrid(new FileGrid(childrenFiles, path, f));
        } else {
            f.setFileGrid(new FileGrid(childrenFiles, path, f));
            f.setFileTable(new FileTable(childrenFiles, path, f));
        }

	}
}
