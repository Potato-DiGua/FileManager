import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class DiaoDU extends JFrame implements ActionListener
{
    JRadioButton jrbRandom=new JRadioButton("随机生成磁道");
    JRadioButton jrbDecide=new JRadioButton("指定磁道");
    ButtonGroup bg=new ButtonGroup();
    JLabel jlDiaoDUNum=new JLabel("磁道数量(5~20)");
    JButton jbCreate=new JButton("生成");
    JButton jbEnter=new JButton("确定");
    JButton jbEnter2=new JButton("确定");
    JTextField jtfDiaoDUNum=new JTextField("10");
    JTextField jtfDiaoDUList=new JTextField("55,58,39,18,90,160,150,38,184");
    JTextField jtfInitDiaoDU=new JTextField("100");
    JLabel jlRandom=new JLabel("随机生成磁道");
    JLabel jlText=new JLabel("注意：请用逗号隔开。");
    JLabel jlInitDiaoDU=new JLabel("磁道初始位置");
    JPanel jpRandom=new JPanel();
    JPanel jpDecide=new JPanel();

    JLabel[] jl=
            {
                    new JLabel("FCFS"),
                    new JLabel("SSTF"),
                    new JLabel("SCAN"),
                    new JLabel("CSCAN"),
            };

    int[] DiaoDU;
    int DiaoDUNum=0;
    int initDiaoDU=0;
    boolean drew=false;
    Control DiaoDUClass;
    DrawTable dt;

    public DiaoDU()
    {
        this.setTitle("磁盘寻道计算器");
        this.setLayout(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        jpRandom.setLayout(null);
        jpRandom.setBounds(10,10,235,70);
        jpRandom.setBorder(new TitledBorder(""));
        jrbRandom.setBounds(10,10,150,20);
        jrbRandom.setSelected(true);
        setState(true);
        jrbRandom.addActionListener(this);
        jpRandom.add(jrbRandom);
        jlDiaoDUNum.setBounds(10,40,95,20);
        jpRandom.add(jlDiaoDUNum);
        jtfDiaoDUNum.setBounds(105,40,40,20);
        jpRandom.add(jtfDiaoDUNum);
        jbCreate.setBounds(155,40,60,20);
        jbCreate.addActionListener(this);
        jpRandom.add(jbCreate);

        jpDecide.setLayout(null);
        jpDecide.setBounds(255,10,335,70);
        jpDecide.setBorder(new TitledBorder(""));
        jrbDecide.setBounds(10,10,80,20);
        jrbDecide.addActionListener(this);
        jpDecide.add(jrbDecide);
        jlText.setBounds(135, 10, 200, 20);
        jpDecide.add(jlText);
        jtfDiaoDUList.setBounds(10,40,250,20);
        jpDecide.add(jtfDiaoDUList);
        jbEnter.setBounds(265, 40, 60, 20);
        jbEnter.addActionListener(this);
        jpDecide.add(jbEnter);

        for(int i=0;i<jl.length;i++)
        {
            jl[i].setBounds(20+185*i,145,140,20);
            this.add(jl[i]);
        }

        bg.add(jrbRandom);
        bg.add(jrbDecide);

        jlInitDiaoDU.setBounds(10,95,80,20);
        this.add(jlInitDiaoDU);
        jtfInitDiaoDU.setBounds(95,95,50,20);
        this.add(jtfInitDiaoDU);
        jbEnter2.setBounds(155,95,60,20);
        jbEnter2.addActionListener(this);
        this.add(jbEnter2);
        setInitButtonState(false);

        this.add(jpDecide);
        this.add(jpRandom);
        this.setBounds(120,20,770,620);
        this.setVisible(true);
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==jrbRandom)
        {
            setState(true);
            setInitButtonState(false);
        }
        else if(e.getSource()==jrbDecide)
        {
            setState(false);
            setInitButtonState(false);
        }
        else if(e.getSource()==jbCreate)
        {
            try{
                DiaoDUNum=Integer.parseInt(this.jtfDiaoDUNum.getText().trim());
            }catch(Exception ex)
            {
                JOptionPane.showMessageDialog(this, "请输入正确的数字。","输入错误",JOptionPane.ERROR_MESSAGE);
            }
            if(DiaoDUNum<5||DiaoDUNum>20)
            {
                JOptionPane.showMessageDialog(this, "请输入5~20的数字。","输入错误",JOptionPane.ERROR_MESSAGE);
            }
            random(DiaoDUNum);
            setInitButtonState(true);
        }
        else if(e.getSource()==jbEnter)
        {
            String DiaoDUList=this.jtfDiaoDUList.getText().trim();
            String[] DiaoDUStr=DiaoDUList.split(",");
            DiaoDUNum=DiaoDUStr.length;
            if(DiaoDUNum<5||DiaoDUNum>20)
            {
                JOptionPane.showMessageDialog(this, "磁道的数量应为5~20个。","输入错误",JOptionPane.ERROR_MESSAGE);
                return;
            }
            DiaoDU=new int[DiaoDUNum];
            try
            {
                for(int i=0;i<DiaoDUNum;i++)
                {
                    DiaoDU[i]=Integer.parseInt(DiaoDUStr[i]);
                }
            }catch(Exception ex)
            {
                JOptionPane.showMessageDialog(this, "请输入正确的磁道数值。","输入错误",JOptionPane.ERROR_MESSAGE);
                return;
            }
            setInitButtonState(true);
        }
        else if(e.getSource()==jbEnter2)
        {
            try{
                initDiaoDU=Integer.parseInt(this.jtfInitDiaoDU.getText().trim());
            }catch(Exception ex)
            {
                JOptionPane.showMessageDialog(this, "请输入正确的初始磁道位置(0~200)。","输入错误",JOptionPane.ERROR_MESSAGE);
            }
            if(initDiaoDU<0||initDiaoDU>200)
            {
                JOptionPane.showMessageDialog(this, "请输入正确的初始磁道位置(0~200)。","输入错误",JOptionPane.ERROR_MESSAGE);
            }
            DiaoDUClass=new Control(DiaoDU,initDiaoDU);
            if(drew)
            {
                for(int i=0;i<4;i++)
                {
                    this.remove(dt.jsp[i]);
                }
            }
            dt=new DrawTable(this,DiaoDUClass);
            dt.draw();
        }
    }

    public void setState(boolean flag)
    {
        jtfDiaoDUNum.setEnabled(flag);
        jbCreate.setEnabled(flag);
        jtfDiaoDUList.setEnabled(!flag);
        jbEnter.setEnabled(!flag);
    }
    public void setInitButtonState(boolean flag)
    {
        jbEnter2.setEnabled(flag);
        jtfInitDiaoDU.setEnabled(flag);
    }


    public void random(int num)
    {
        DiaoDU=new int[num];
        Random ran=new Random();
        int i;
        for(i=0;i<num;i++)
        {
            DiaoDU[i]=(int)(ran.nextFloat()*200);
        }
    }

    /*public static void main(String[] args)
    {
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new DiaoDU();
    }*/

}