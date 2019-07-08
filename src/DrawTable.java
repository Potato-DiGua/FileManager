import javax.swing.*;

public class DrawTable extends JFrame
{
    DiaoDU father;
    Control DiaoDUClass;
    JTable[] jt=null;
    JScrollPane[] jsp;
    String[][] fcfs,sstf,scan,cscan;
    int DiaoDUNum;
    String[] head={"被访问的下一磁道","移动距离"};
    public DrawTable(DiaoDU father,Control DiaoDUClass)
    {
        this.father=father;
        this.DiaoDUClass=DiaoDUClass;
        jt=new JTable[4];
        jsp=new JScrollPane[4];

        DiaoDUNum=father.DiaoDUNum;
        fcfs=toString(DiaoDUClass.fcfs);
        sstf=toString(DiaoDUClass.sstf);
        scan=toString(DiaoDUClass.scan);
        cscan=toString(DiaoDUClass.cscan);
    }
    public void draw()
    {
        int i;
        int height;
        father.drew=true;
        jt[0]=new JTable(fcfs,head);
        jt[1]=new JTable(sstf,head);
        jt[2]=new JTable(scan,head);
        jt[3]=new JTable(cscan,head);
        height=(jt[0].getRowCount()+1)*jt[0].getRowHeight()+5;
        for(i=0;i<jt.length;i++)
        {
            jsp[i]=new JScrollPane(jt[i]);
            jt[i].setEnabled(false);
            jt[i].getTableHeader().setReorderingAllowed(false);
            jsp[i].setBounds(20+185*i,170,140,360);
            father.add(jsp[i]);
        }
    }
    public String[][] toString(int DiaoDU[][])
    {
        int i;
        float sum=0;
        String[][] temp=new String[DiaoDUNum+1][2];
        for(i=0;i<DiaoDUNum;i++)
        {
            sum+=DiaoDU[i][1];
            temp[i][0]=String.valueOf(DiaoDU[i][0]);
            temp[i][1]=String.valueOf(DiaoDU[i][1]);
        }
        temp[DiaoDUNum][0]="平均";
        temp[DiaoDUNum][1]=String.valueOf((Math.round(sum/DiaoDUNum*10))/10.0);
        return temp;
    }
}
