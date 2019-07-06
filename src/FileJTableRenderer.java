import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;

public class FileJTableRenderer extends DefaultTableCellRenderer {
    public static ImageIcon fileicon = new ImageIcon("src/Icon/FileIcon.png");
    public static ImageIcon diricon = new ImageIcon("src/Icon/DirectoryIcon.png");
    public int focusOnRow;

    public FileJTableRenderer(int focusOnRow) {
        this.focusOnRow = focusOnRow;

        /*
        fileicon=;
        diricon=new ImageIcon("src/Icon/DirectoryIcon.png");
        File f=new File("src/Icon/DirectoryIcon.png");
        File f2=new File("src/Icon/FileIcon.png");
        if(f.exists()&&f2.exists())
            System.out.println("读取到图片");
        else
            System.out.println("读取图片失败");

         */
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        boolean selected = (table.getSelectedRow() == row);
        if (column != 0) {
            if (row != focusOnRow && !selected) {
                setBackground(Color.white);
            } else if(row==focusOnRow&&!selected){
                setBackground(GUI.focuscolor);
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        }
        else {

            JLabel label = new JLabel();
            label.setOpaque(true);
            if (!selected&&row!=focusOnRow) {
                label.setBackground(Color.white);
            }else if(row==focusOnRow&&!selected)
            {
                label.setBackground(GUI.focuscolor);
            }
            else
            {
                label.setBackground(GUI.selectcolor);
            }


            label.setText(value.toString());

            if (table.getValueAt(row, 1).equals("文件")) {
                label.setIcon(fileicon);
            } else {
                label.setIcon(diricon);
            }
            return label;
        }

    }
}
