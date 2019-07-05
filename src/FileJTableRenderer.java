import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;

public class FileJTableRenderer extends DefaultTableCellRenderer {
    public  ImageIcon fileicon;
    public  ImageIcon diricon;

    public FileJTableRenderer()
    {
        fileicon=new ImageIcon("src/Icon/FileIcon.png");
        diricon=new ImageIcon("src/Icon/DirectoryIcon.png");
        File f=new File("src/Icon/DirectoryIcon.png");
        File f2=new File("src/Icon/FileIcon.png");
        if(f.exists()&&f2.exists())
            System.out.println("读取到图片");
        else
            System.out.println("读取图片失败");
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(column!=0)
            return super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);

        JLabel label=new JLabel();
        label.setText(value.toString());
        if(table.getValueAt(row,1).equals("文件"))
        {
            label.setIcon(fileicon);
        }
        else
        {
            label.setIcon(diricon);
        }
        return label;
    }
}
