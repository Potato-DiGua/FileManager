public class Control
{
    int[][] fcfs,sstf,scan,cscan;
    int init,diskNum;
    int[] disk;

    public Control(int disk[],int initDisk)
    {
        this.disk=disk;
        this.init=initDisk;
        diskNum=disk.length;
        fcfs=new int[diskNum][2];
        sstf=new int[diskNum][2];
        scan=new int[diskNum][2];
        cscan=new int[diskNum][2];
        Fcfs();
        Sstf();
        Scan();
    }
    public void Fcfs()
    {
        for(int i=0;i<diskNum;i++)
        {
            fcfs[i][0]=disk[i];
        }
        fcfs[0][1]=Math.abs(init-fcfs[0][0]);
        for(int i=1;i<diskNum;i++)
        {
            fcfs[i][1]=Math.abs(fcfs[i][0]-fcfs[i-1][0]);
        }
    }

    public void Sstf()
    {
        int[] diskTemp=new int[diskNum];
        int miniDiff=9999;
        int diff,nextDisk=0;
        int curr=init;
        int i,j;


        for(i=0;i<diskNum;i++)
        {
            diskTemp[i]=disk[i];
        }
        for(i=0;i<diskNum;i++)
        {
            for(j=0;j<diskNum;j++)
            {
                diff=Math.abs(curr-diskTemp[j]);
                if(diff<miniDiff)
                {
                    miniDiff=diff;
                    nextDisk=j;
                }
            }
            sstf[i][0]=diskTemp[nextDisk];
            sstf[i][1]=miniDiff;
            curr=sstf[i][0];
            diskTemp[nextDisk]=-1000;
            miniDiff=9999;
        }

    }

    public void Scan()
    {
        int[] diskTemp=new int[diskNum];
        int i,j,temp,point=0;

        for(i=0;i<diskNum;i++)
        {
            diskTemp[i]=disk[i];
        }
        for(i=0;i<diskNum;i++)
        {
            for(j=i;j<diskNum;j++)
            {
                if(diskTemp[i]>diskTemp[j])
                {
                    temp=diskTemp[j];
                    diskTemp[j]=diskTemp[i];
                    diskTemp[i]=temp;
                }
            }
        }

        for(i=0;i<diskNum;i++)
        {
            if(diskTemp[i]>=init)
            {
                scan[point][0]=diskTemp[i];
                cscan[point][0]=diskTemp[i];
                point++;
            }
        }
        j=point;
        for(i=0;i<diskNum-j;i++)
        {
            scan[point][0]=diskTemp[diskNum-j-i-1];
            cscan[point][0]=diskTemp[i];
            point++;
        }
        scan[0][1]=Math.abs(init-scan[0][0]);
        cscan[0][1]=Math.abs(init-cscan[0][0]);
        for(i=1;i<diskNum;i++)
        {
            scan[i][1]=Math.abs(scan[i-1][0]-scan[i][0]);
            cscan[i][1]=Math.abs(cscan[i-1][0]-cscan[i][0]);
        }
    }
}
