import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Vector;

public class GUI {


    private JPanel rootpane;
    private JTable table1;
    private JScrollPane jsp;
    private JButton reButton;
    private JLabel pathLabel;
    private JPanel topPanel;
    private Vector<String> columnNname = new Vector();
    private Vector<Vector> data = new Vector();
    private JPopupMenu popupMenu = new JPopupMenu();

    public GUI() {
        //设置当前路径为根目录
        MFD.openPath("root");

        columnNname.add("名称");
        columnNname.add("类型");
        DefaultTableModel model = new DefaultTableModel(data, columnNname) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;

            }
        };

        //右键菜单新建文件夹
        JMenuItem createDir = new JMenuItem("新建文件夹");
        popupMenu.add(createDir);
        createDir.addActionListener(e -> {
            createDir();
        });

        //右键菜单重命名
        JMenuItem rename = new JMenuItem("重命名");
        rename.addActionListener(e -> {

        });
        //右键菜单删除
        JMenuItem delete = new JMenuItem("删除");
        delete.addActionListener(e -> {

        });
        //右键新建文件
        JMenuItem createFile = new JMenuItem("新建文件");
        createFile.addActionListener(e -> {
            create_File();
        });
        table1.setModel(model);
        //右键菜单显示
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.removeAll();
                    int focusedRowIndex = table1.rowAtPoint(e.getPoint());
                    if (focusedRowIndex != -1) {//是否选中文件或文件夹
                        if (MFD.path.size() == 1)//只在根目录显示
                            popupMenu.add(createDir);
                        popupMenu.add(rename);
                        popupMenu.add(delete);

                    } else {
                        if (MFD.path.size() == 1)//只在根目录显示
                            popupMenu.add(createDir);
                        else//在二级目录显示
                            popupMenu.add(createFile);
                    }
                    popupMenu.show(table1, e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    int selectedRow = table1.getSelectedRow();
                    if (selectedRow != -1) {
                        String name = table1.getValueAt(selectedRow, 0).toString();
                        if (MFD.path.size() == 1)//双击打开文件夹
                        {
                            open(name);
                        } else {//双击打开文件
                            openFile(name);
                        }

                    }
                }
            }
        });
        //返回按钮
        reButton.addActionListener(e -> {
            MFD.rePath();
            refreshList();
            reButton.setEnabled(false);
        });

        reButton.setEnabled(false);
        refreshpath();
    }

    private void open(String name) {
        MFD.openPath(name);

        refreshList();

        reButton.setEnabled(true);
    }

    private void openFile(String name) {
        String content="";
        UFD ufd=null;
        for (UFD u : MFD.ufdlist) {
            if (u.username.equals(MFD.path.get(1))) {
                content=u.openFile(name);
                ufd=u;
            }
            break;
        }

        fileEditWindow(name,content,ufd);
    }
    private void fileEditWindow(String name,String content,UFD ufd)
    {
        if (ufd==null)
            return;
        JFrame frame = new JFrame(name);
        CreatFile cf = new CreatFile(content);
        frame.setContentPane(cf.mpanel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                String text = cf.getContent();
                System.out.println(text);
                ufd.xiugaiFilewords(name,text);
            }
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private void refreshpath() {
        pathLabel.setText(MFD.getPath());
    }

    private void refreshList() {
        data.clear();
        if (MFD.path.size() == 1) {
            for (UFD u : MFD.ufdlist) {
                Vector cell = new Vector();
                cell.add(u.username);
                cell.add("文件夹");
                data.add(cell);
            }
        } else {
            for (UFD u : MFD.ufdlist) {
                if (u.username.equals(MFD.path.get(1))) {
                    for (FCB f : u.filelist) {
                        Vector cell = new Vector();
                        cell.add(f.filename);
                        cell.add("文件");
                        data.add(cell);
                    }
                    break;
                }
            }
        }
        table1.updateUI();
        refreshpath();

    }

    private void createDir() {
        String name = JOptionPane.showInputDialog("文件夹名称");
        if (name == null || name.isEmpty())
            return;
        MFD.ufdlist.add(new UFD(name));
        Vector cell = new Vector();
        cell.add(name);
        cell.add("文件夹");
        data.add(cell);
        table1.updateUI();
    }

    private void create_File() {
        for (UFD u : MFD.ufdlist) {
            if (u.username.equals(MFD.path.get(1))) {
                u.filelist.add(u.createFile(MFD.getPath()));
                break;
            }
        }
        refreshList();
        /*
        JFrame frame = new JFrame(name);
        CreatFile cf = new CreatFile();
        frame.setContentPane(cf.mpanel);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                String text = cf.getContent();
                System.out.println(text);
            }
        });
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

         */
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
