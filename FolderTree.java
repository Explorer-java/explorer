import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class FolderTree extends JTree{
	
	public FolderTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
 
        vegetableNode.add(new DefaultMutableTreeNode("Capsicum"));
        vegetableNode.add(new DefaultMutableTreeNode("Carrot"));
        vegetableNode.add(new DefaultMutableTreeNode("Tomato"));
        vegetableNode.add(new DefaultMutableTreeNode("Potato"));
        
        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);
        
        DefaultTreeModel model = new DefaultTreeModel(root);
        setModel(model);
	}
}
