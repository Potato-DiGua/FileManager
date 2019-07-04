import java.io.UnsupportedEncodingException;

public class FCB {
    String filepath;  //文件路径
    String filename;  //文件名
    //String neirong;	  //文件内容
    String shuxing;      //文件属性'w'只写,'r'只读
    int length=0;       //文件长度
    //int address;   //文件首地址
    Block block;
    void write(String content) throws UnsupportedEncodingException
    {
    	int len=content.getBytes("utf-8").length;  
    	System.out.println(len);
    	length=len;
    	if(len<=512) {//占用一个区块
    		  int i=Disk.disk_createfile(1);//申请一个区块
    		  if(i!=-1) {
    			  block=new Block(i,1);
        		  Disk.disk[i]=content.getBytes();
    		  }		  
    	}else
    	{
    		int num=len/512;//占用多个区块，向下取整
    		int t=Disk.disk_createfile(num+1);//需要num+1个区块存储，如果t=-1，没有空间，否则在第t个区块开始存储
    		if(t!=-1) {
        		block=new Block(t,num+1);
    		}else
    			return;
    		byte temp[]=content.getBytes();//将字符串转换成byte数组
    		for(int x=0;x<num;x++) {//512一组开始存储		
    			System.arraycopy(temp,x*512,Disk.disk[t+x],0,512);
    		}
    		System.arraycopy(temp, num*512, Disk.disk[t+num], 0, len-512*num);//最后多出来的进行存储
    	}    	
    }
    String read() throws UnsupportedEncodingException
    {
    	String readstr="";
    	for(int i=block.start;i<block.start+block.length;i++) {//一次读
    		String temp=new String(Disk.disk[i],"utf-8");
    		readstr=readstr+temp;
    	}
    	return readstr;
    }
    
}
