import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FolderTree extends JTree implements TreeWillExpandListener, TreeSelectionListener{
	private FileSystemView filesystem;
	private DefaultMutableTreeNode root;
	
	public FolderTree(){
		initTree();
		initFileSystem();
        /*
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
 
        vegetableNode.add(new DefaultMutableTreeNode("Capsicum"));
        vegetableNode.add(new DefaultMutableTreeNode("Carrot"));
        vegetableNode.add(new DefaultMutableTreeNode("Tomato"));
        vegetableNode.add(new DefaultMutableTreeNode("Potato"));
        
        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);
        */
        DefaultTreeModel model = new DefaultTreeModel(root);
        setModel(model);
        
	}
	
	private void initTree(){
		addTreeWillExpandListener(this);

		root = new DefaultMutableTreeNode();
	}
	
	private void initFileSystem(){
		File[] dir = File.listRoots();
		for(int i=0; i<dir.length; i++){
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(dir[i].toString());
			root.add(node);
		}
		
		/*
		File[] f = filesystem.getRoots();
		root.setUserObject(f[0]);
		
		for(int i=0; i<f.length; i++)
			System.out.println(filesystem.getSystemDisplayName(f[i]));
		
		
		
		File[] list = getChildFolder(f[0]);
		for(int i=0; i<list.length; i++){
			DefaultMutableTreeNode test = new DefaultMutableTreeNode();
			test.setUserObject(list[i]);
			root.add(test);
			//System.out.println(filesystem.getSystemDisplayName(list[i]));
		}
		*/
	}
	/*
	private File[] getChildFolder(File f){
		File[] files = f.listFiles();
		ArrayList<File> fileArray = new ArrayList<File>();
		for(int i=0; i<files.length; i++){
			if(files[i].isDirectory())
				fileArray.add(files[i]);
		}
		
		File[] fileList = new File[fileArray.size()];
		fileArray.toArray(fileList);
		
		return fileList;
	}
	 */
	
	private String getPos(TreePath tp){
		StringTokenizer tk = new StringTokenizer(tp.toString(), "[,]");
		String str = "";
		tk.nextToken();
		while(tk.hasMoreTokens())
			str += tk.nextToken().trim() + File.separator;
		
		return str;
	}
	
	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		// TODO Auto-generated method stub
		System.out.println("getPath: "+event.getPath().toString());
		if(event.getPath().toString().equals("[]"))
			return;
		String parentPath = getPos(event.getPath());
		System.out.println("parent: "+parentPath);
		if(parentPath == null || parentPath.trim().length() == 0)
			return;
		
		File parentFile = new File(parentPath);
		File[] childrenFiles = parentFile.listFiles();
		
		DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
		lastNode.removeAllChildren();
		for(int i=0; i<childrenFiles.length; i++){
			if(childrenFiles[i].isDirectory())
				lastNode.add(new DefaultMutableTreeNode(childrenFiles[i].getName()));
		}
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
