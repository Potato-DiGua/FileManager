import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

public class FileProperty {
    public JPanel rootpane;
    private JTextField namejtf;
    private JLabel typeLabel;
    private JLabel locationlabel;
    private JComboBox comboBox1;
    private JLabel sizelabel;
    private String filename;
    private static long kb=1024;
    private static long mb=1048576;
    public FileProperty(GUI mainWindow,Component parent, String name, Dir u, FCB f, String path, String property)
    {
        if(u==null)
            return;
        filename=name;
        namejtf.setText(name);

        if(f!=null)
        {
            typeLabel.setText("文件");
            sizelabel.setText(getSizeStr(f.length));

        }
        else {

            sizelabel.setText(getSizeStr(MFD.getDirSize(u)));
            typeLabel.setText("文件夹");

        }
        locationlabel.setText(path);
        comboBox1.addItem("只读");
        comboBox1.addItem("读取/写入");

        if(property.equals("a"))
            comboBox1.setSelectedIndex(1);
        else
            comboBox1.setSelectedIndex(0);

        comboBox1.addItemListener(e -> {
            int Index=comboBox1.getSelectedIndex();
            String newproperty;
            if(Index==0)
                newproperty="r";
            else
                newproperty="a";

            if(f==null)
            {
                u.property=newproperty;
            }
            else
                f.shuxing=newproperty;

        });
        JFrame frame = new JFrame(name);
        frame.setContentPane(rootpane);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String newname=namejtf.getText();
                if(!newname.equals(filename))
                {
                    if(f==null)//文件夹
                    {
                        if(!MFD.rename("文件夹",filename,newname))
                            JOptionPane.showMessageDialog(parent, "存在相同名字的文件夹,重命名失败", "提示", JOptionPane.ERROR_MESSAGE);

                    }
                    else//文件
                    {
                        if (!MFD.rename("文件",filename,newname)) {
                            JOptionPane.showMessageDialog(parent, "存在相同名字的文件,重命名失败", "提示", JOptionPane.ERROR_MESSAGE);

                        }
                    }

                }
                frame.dispose();
                mainWindow.refresh();
            }
        });
        frame.pack();
        frame.setLocationRelativeTo(parent);
        frame.setVisible(true);
    }
    private String getSizeStr(long size)
    {
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        if(size<kb)
        {
            return size+"B";
        }
        else if(size<mb)
        {
            return decimalFormat.format(size/kb)+"KB";
        }
        else
        {
            return decimalFormat.format(size/mb)+"MB";
        }
    }
}
