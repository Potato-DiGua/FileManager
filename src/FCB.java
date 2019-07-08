import java.awt.*;
import java.io.UnsupportedEncodingException;
import javax.swing.*;
public class FCB {
    String filepath;  //文件路径
    String filename;  //文件名
    String shuxing="a";      //文件属性'w'只写,'r'只读,'a'读写
    int length=0;       //文件长度
    Block block=new Block();
    public String toString()
	{
		return filename;
	}

    public boolean write(String content) throws UnsupportedEncodingException
    {
    	int len=content.getBytes("utf-8").length;  
    	System.out.println(len);

    	if(len<=512) {//占用一个区块
    		  int i=Disk.disk_createfile(1);//申请一个区块
    		  if(i!=-1) {
				  length=len;
    			  block=new Block(i,1);
        		  Disk.disk[i]=content.getBytes("utf-8");
        		  return true;
    		  }else
    		  	return false;

    	}else
    	{
    		int num=len/512;//占用多个区块，向下取整
    		int t=Disk.disk_createfile(num+1);//需要num+1个区块存储，如果t=-1，没有空间，否则在第t个区块开始存储
    		if(t!=-1) {
				length=len;
        		block=new Block(t,num+1);
    		}else
			{
				JOptionPane.showMessageDialog(
						new Frame(), "空间不足，保存失败 ");
				return false;
			}

    		byte temp[]=content.getBytes("utf-8");//将字符串转换成byte数组
    		for(int x=0;x<num;x++) {//512一组开始存储		
    			System.arraycopy(temp,x*512,Disk.disk[t+x],0,512);
    		}
    		System.arraycopy(temp, num*512, Disk.disk[t+num], 0, len-512*num);//最后多出来的进行存储
    		return true;
    	}

    }
    String read() throws UnsupportedEncodingException
    {
    	if(length==0||block.start==-1) {
    		return "";
    	}
    	byte temp[]=new byte[length];
    	int num=length/512;
    	for(int x=0;x<num;x++) {//512一组开始读取		
			System.arraycopy(Disk.disk[block.start+x],0,temp,x*512,512);
		}
    	System.arraycopy(Disk.disk[block.start+num], 0,temp , num*512, length-512*num);//最后多出来的进行读取
    	String temp2=new String(temp,"utf-8");
    	return temp2;
    }
    
}
