import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

/**
 * UI
 * encoding:UTF-8
 */


public class GUI {


    private JPanel rootpane;
    private JTable table1;
    private JScrollPane jsp;
    private JButton reButton;
    private JPanel topPanel;
    private JPanel listjp;
    private JTree tree1;
    private JScrollPane jsp2;
    private JTextField pathinput;
    private JButton diaodubtn;
    private Vector<String> columnNname = new Vector<>();
    private Vector<Vector> data = new Vector<>();
    private JPopupMenu popupMenu = new JPopupMenu();
    private DefaultTableModel model;
    public static Color focuscolor = new Color(0xC6E2FF);
    public static Color selectcolor=new Color(12, 175, 170);
    private JTextField jtf = new JTextField();
    private DefaultMutableTreeNode root;//文件树的根目录
    private DefaultTreeModel treemodel;
    private FileJTableRenderer ftr;//自定义Jtable渲染器

    public GUI() {



        //设置当前路径为根目录
        MFD.nowPath.add(MFD.root);

        columnNname.add("名称");
        columnNname.add("类型");

        diaodubtn.addActionListener(e -> new DiaoDU());

        model = new DefaultTableModel(data, columnNname) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return column == 0;
            }
        };

        table1.setModel(model);
        //当输入框获取焦点时全选
        jtf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                jtf.selectAll();
            }
        });
        //table1.getColumn("")
        //设置table1的编辑器为输入框
        DefaultCellEditor dfc = new DefaultCellEditor(jtf);

        //设置table1单击开始编辑
        dfc.setClickCountToStart(1);
        table1.setDefaultEditor(table1.getColumnClass(0), dfc);
        ftr=new FileJTableRenderer(-1);

        table1.setDefaultRenderer(table1.getClass(),ftr);

        //失去焦点时保存正在编辑的内容
        table1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        //右键菜单新建文件夹
        JMenuItem createDir = new JMenuItem("新建文件夹");
        popupMenu.add(createDir);
        createDir.addActionListener(e -> createDir());

        //右键菜单刷新
        JMenuItem refreshmenu = new JMenuItem("刷新");
        refreshmenu.addActionListener(e ->
                refresh()
        );
        //右键菜单重命名
        JMenuItem rename = new JMenuItem("重命名");
        rename.addActionListener(e ->
            table1.editCellAt(table1.getSelectedRow(), 0)
        );
        //右键菜单删除
        JMenuItem delete = new JMenuItem("删除");
        delete.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                String name = table1.getValueAt(row, 0).toString();
                String type=table1.getValueAt(row, 1).toString();
                MFD.deletefile(name,type);
                refresh();
            }
        });
        //右键新建文件
        JMenuItem createFile = new JMenuItem("新建文件");
        createFile.addActionListener(e ->
                create_File()
        );
        //右键打开文件
        JMenuItem openFile = new JMenuItem("打开");
        openFile.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                String name = table1.getValueAt(row, 0).toString();
                String type=table1.getValueAt(row, 1).toString();
                if (type.equals("文件夹"))//双击打开文件夹
                {
                    System.out.println(name);
                    open(name);
                } else if(type.equals("文件"))//双击打开文件
                {
                    openFile(name);
                }
            }

        });
        //右键打开文件属性
        JMenuItem fileproperty = new JMenuItem("属性");
        fileproperty.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row != -1) {
                String name = table1.getValueAt(row, 0).toString();
                String type=table1.getValueAt(row, 1).toString();
                if (type.equals("文件夹")) {
                    Dir u = MFD.findDirByName(MFD.getNowDir(),name);
                    new FileProperty(this,rootpane, name, u, null, MFD.getPath(), u.property);
                } else {
                    Dir u = MFD.getNowDir();
                    FCB fcb = null;
                    for (FCB f : u.filelist)
                        if (f.filename.equals(name)) {
                            fcb = f;
                            break;
                        }
                    new FileProperty(this,rootpane, name, u, fcb, MFD.getPath(), fcb.shuxing);
                }


            }

        });


        table1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            //修改焦点所在行颜色
            public void mouseMoved(MouseEvent e) {
                if (table1.isEditing())
                    return;
                super.mouseMoved(e);
                //System.out.println(e.getPoint());
                int focusedRowIndex = table1.rowAtPoint(e.getPoint());
                //鼠标是否超出Jtable边界
                //System.out.println(focusedRowIndex);
                if (!table1.contains(e.getPoint()))
                {
                    setRowBackgroundColor(-1);
                }
                else
                    setRowBackgroundColor(focusedRowIndex);



            }
        });


        //重命名
        //将表格的修改内容更新到文件系统
        model.addTableModelListener(e -> {
            int type = e.getType();
            int row = e.getFirstRow();
            int col = e.getColumn();

            if (type == TableModelEvent.UPDATE) {
                //System.out.println("行"+row+"列："+col);
                //System.out.println(table1.getValueAt(row,col));
                String newname=model.getValueAt(row,0).toString();
                String Filetype=model.getValueAt(row,1).toString();
                String oldname;
                Dir d=MFD.getNowDir();

                if (Filetype.equals("文件夹"))
                {
                    oldname=d.childDirlist.get(row).Dirname;
                }
                else {
                    oldname = d.filelist.get(row - d.childDirlist.size()).filename;
                }

                if (!MFD.rename(Filetype,oldname,newname)) {
                    JOptionPane.showMessageDialog(null, "存在相同名字的文件,重命名失败", "提示", JOptionPane.ERROR_MESSAGE);

                }

                refresh();
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //点击其他位置停止编辑状态
                if(table1.isEditing())
                    table1.getCellEditor().stopCellEditing();
                super.mousePressed(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);


                int buttonid = e.getButton();
                int clickcount = e.getClickCount();
                Point p = e.getPoint();
                int focusedRowIndex = table1.rowAtPoint(p);
                int focusedColumnIndex = table1.columnAtPoint(p);

                if (focusedRowIndex == -1)//鼠标未选中
                {
                    table1.clearSelection();
                }
                //右键菜单显示
                if (buttonid == MouseEvent.BUTTON3) {
                    popupMenu.removeAll();
                    if (focusedRowIndex != -1) {//选中文件或文件夹
                        table1.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                        popupMenu.add(openFile);
                        popupMenu.add(rename);
                        popupMenu.add(delete);
                        popupMenu.add(fileproperty);

                    } else {//未选中文件或文件夹
                        popupMenu.add(createDir);
                        popupMenu.add(createFile);
                        popupMenu.add(refreshmenu);
                    }
                    popupMenu.show(table1, e.getX(), e.getY());
                }
                else if (buttonid == MouseEvent.BUTTON1 && clickcount == 2) {
                    if (focusedRowIndex != -1) {
                        String name = table1.getValueAt(focusedRowIndex, 0).toString();
                        String Filetype = table1.getValueAt(focusedRowIndex, 1).toString();
                        if (Filetype.equals("文件夹"))//双击打开文件夹
                        {
                            //System.out.println(name);
                            open(name);
                        } else {//双击打开文件
                            openFile(name);
                        }

                    }
                }
            }
        });
        tree1.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node=(DefaultMutableTreeNode)tree1.getLastSelectedPathComponent();
            if(node!=null)
            {
                    TreeNode[] path=node.getPath();
                    StringBuilder pathtotal=new StringBuilder();
                    for(TreeNode treeNode:path)
                    {
                        pathtotal.append("/");
                        pathtotal.append(treeNode.toString());
                    }
                    //System.out.println(pathstr);

                    if(MFD.findDirByPath(pathtotal.toString())!=null)
                    {
                        refreshList();
                        refreshpath();
                    }
            }
            else
                tree1.clearSelection();
        });

        table1.setRowSorter(new TableRowSorter<>(model));
        //返回按钮
        reButton.addActionListener(e -> {
            MFD.rePath();
            refreshList();
            refreshpath();
            if(MFD.nowPath.size()==1)
                reButton.setEnabled(false);
        });
        pathinput.addActionListener(e -> {
            String path=pathinput.getText();
            if(MFD.findDirByPath(path)!=null)
            {
                refreshList();

            }
            else
            {
                refreshpath();
                JOptionPane.showMessageDialog(rootpane,"该路径不存在","提示",JOptionPane.ERROR_MESSAGE);
            }

        });
        initJTree();
        reButton.setEnabled(false);
        refreshpath();
    }
    private void initJTree()
    {
        root=new DefaultMutableTreeNode(MFD.root);

        treemodel=new DefaultTreeModel(root);

        tree1.setModel(treemodel);
        tree1.setCellRenderer(new FileJTreeRenderer());
    }
    private void reBuildJtree(DefaultMutableTreeNode root,Dir droot) {
/*添加文件
        for(FCB f:droot.filelist)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(f);
            node.setAllowsChildren(false);
            root.add(node);
        }*/
        for(Dir d:droot.childDirlist)
        {

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(d);
            node.setAllowsChildren(true);
            root.add(node);
            reBuildJtree(node,d);
        }

    }

    //刷新列表背景色
    public void setRowBackgroundColor(int focusOnRow)
    {
        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumn(table1.getColumnName(i)).setCellRenderer(new FileJTableRenderer(focusOnRow));
        }
        table1.updateUI();
    }

    private void open(String name) {
        MFD.openPath(name);


        refreshList();
        refreshpath();
        reButton.setEnabled(true);
    }

    private void openFile(String name) {
        String content = "";

        Dir nowDir=MFD.getNowDir();

        try {
            content=nowDir.openFile(name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        fileEditWindow(name, content, nowDir);
    }

    private void fileEditWindow(String name, String content, Dir dir) {
        if (dir == null)
            return;
        JFrame frame = new JFrame(name);
        CreatFile cf = new CreatFile(content);
        frame.setContentPane(cf.mpanel);
        //窗口关闭事件
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                String text = cf.getContent();
                System.out.println(text);

                int options = JOptionPane.showConfirmDialog(frame, "是否在关闭之前保存文件", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
                if (options == JOptionPane.YES_OPTION) {
                    try {
                        dir.xiugaiFilewords(name, text);
                    } catch (UnsupportedEncodingException ue) {
                        ue.printStackTrace();
                    }

                    frame.dispose();
                } else if (options == JOptionPane.NO_OPTION) {
                    frame.dispose();
                }
            }
        });

        frame.pack();

        frame.setLocationRelativeTo(rootpane);

        frame.setVisible(true);
    }

    private void refreshtree() {

        root.removeAllChildren();

        reBuildJtree(root,MFD.root);

        tree1.updateUI();
    }
    public void refresh()
    {
        refreshList();
        refreshpath();
        refreshtree();
    }
    private void refreshpath() {
        pathinput.setText(MFD.getPath());
    }

    private void refreshList() {
        int rowcount = model.getRowCount();

        for (int i = rowcount - 1; i >= 0; i--)
            model.removeRow(i);
        table1.updateUI();

        String[][] filelist=MFD.getFilelist(MFD.getNowDir());

        for(String[] row:filelist)
        {
            Vector<String> Row = new Vector<>();
            Row.add(row[0]);
            Row.add(row[1]);
            model.addRow(Row);
        }
        table1.setDefaultRenderer(table1.getClass(),ftr);
        table1.updateUI();

    }

    private void createDir() {
        String name;
        Dir nowDir=MFD.getNowDir();
        if(nowDir.property.equals("r"))
        {
            JOptionPane.showMessageDialog(null,"该文件夹为只读文件夹,禁止创建或删除");
            return;
        }
        for (int i = 0; ; i++) {
            name = "新建文件夹";
            if (i != 0)
                name += "(" + i + ")";
            if (MFD.findDirByName(nowDir,name)==null)
                break;
        }
        nowDir.childDirlist.add(new Dir(name));
        refresh();
    }

    private void create_File() {
        Dir nowDir=MFD.getNowDir();
        if(nowDir.property.equals("r"))
        {
            JOptionPane.showMessageDialog(null,"该文件夹为只读文件夹,禁止创建或删除");
            return;
        }
        nowDir.createFile(MFD.getPath());
        refresh();
        table1.editCellAt(table1.getRowCount() - 1, 0);
    }

    public static void main(String[] args) {
        //设置UI风格为当前系统风格
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JPanel rootPanel=new GUI().rootpane;


        JFrame frame = new JFrame("文件管理");
        frame.setContentPane(rootPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
