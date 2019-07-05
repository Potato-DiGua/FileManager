import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class FileJTreeRenderer extends DefaultTreeCellRenderer {

    public static ImageIcon fileicon=new ImageIcon("src/Icon/FileIconsmall.png");
    public static ImageIcon diricon=new ImageIcon("src/Icon/DirectoryIconsmall.png");

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        String classname=node.getUserObject().getClass().getName();
        if(classname.equals(Dir.class.getName()))
        {
            this.setIcon(diricon);
        }
        else if(classname.equals(FCB.class.getName()))
        {
            this.setIcon(fileicon);
        }
        return this;
    }
}
