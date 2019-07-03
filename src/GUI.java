import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class GUI {


    private JPanel rootpane;
    private JTable table1;
    private JScrollPane jsp;
    private JButton reButton;
    private JLabel pathLabel;
    private JPanel topPanel;
    private Vector<String> columnNname=new Vector();
    private Vector<Vector> data=new Vector();
    private JPopupMenu popupMenu=new JPopupMenu();

    public GUI()
    {
        //设置当前路径为根目录
        MFD.openPath("root");

        columnNname.add("名称");
        columnNname.add("类型");
        DefaultTableModel model=new DefaultTableModel(data,columnNname){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;

            }
        };

        //右键菜单新建文件夹
        JMenuItem createDir=new JMenuItem("新建文件夹");
        popupMenu.add(createDir);
        createDir.addActionListener(e -> {
                createDir();
        });

        //右键菜单重命名
        JMenuItem rename=new JMenuItem("重命名");
        rename.addActionListener(e -> {

        });
        //右键菜单删除
        JMenuItem delete=new JMenuItem("删除");
        delete.addActionListener(e -> {

        });
        table1.setModel(model);
        //右键菜单显示
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton()==MouseEvent.BUTTON3)
                {
                    popupMenu.removeAll();
                    int focusedRowIndex= table1.rowAtPoint(e.getPoint());
                    if(focusedRowIndex!=-1)
                    {
                        if(MFD.path.size()==1)
                            popupMenu.add(createDir);
                        popupMenu.add(rename);
                        popupMenu.add(delete);

                    }
                    else
                    {
                        if(MFD.path.size()==1)
                            popupMenu.add(createDir);
                    }
                    popupMenu.show(table1,e.getX(),e.getY());
                }
                else if(e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==2)
                {
                    int selectedRow= table1.getSelectedRow();
                    if(selectedRow!=-1)
                    {
                        String str= table1.getValueAt(selectedRow,0).toString();
                        System.out.println(str);
                        open(str);
                    }
                }
            }
        });
        //返回按钮
        reButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<String> dirname=new Vector<>();
                for(UFD u:MFD.ufdlist)
                {
                    dirname.add(u.username);
                }
                MFD.rePath();
                refreshtList(dirname,"文件夹");
                reButton.setEnabled(false);
            }
        });

        reButton.setEnabled(false);
        refreshpath();
    }
    private void open(String name)
    {
        for(UFD u:MFD.ufdlist)
        {
            MFD.openPath(u.username);
            if(u.username==name)
            {
                Vector<String> fname=new Vector<>();
                for(FCB f:u.filelist)
                    fname.add(f.filename);

                refreshtList(fname,"文件");
            }
        }
        reButton.setEnabled(true);
    }
    private void refreshpath()
    {
        String path="";
        for(String dir:MFD.path)
            path+="/"+dir;
        pathLabel.setText(path);
    }
    private void refreshtList(Vector<String> namelist,String type)
    {
        data.clear();
        for(String name:namelist)
        {
            Vector cell=new Vector();
            cell.add(name);
            cell.add(type);
            data.add(cell);
        }
        table1.updateUI();
        refreshpath();

    }
    private void createDir()
    {
        String name=JOptionPane.showInputDialog("文件夹名称");
        if(name==null||name.isEmpty())
            return;
        MFD.ufdlist.add(new UFD(name));
        Vector cell=new Vector();
        cell.add(name);
        cell.add("文件夹");
        data.add(cell);
        table1.updateUI();
    }
    public static void main(String[] args) {
        //设置UI风格为当前系统风格
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("文件管理");
        frame.setContentPane(new GUI().rootpane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

}
