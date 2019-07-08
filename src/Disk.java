public class Disk {
	public static Block firstblock=new Block(0,1024);
	public static byte[][] disk=new byte[1024][512];
	public static void disk_deletefile(Block i) {
		if(i.length==0){
            return;
        }

		/*for(int q=i.block.start;q<i.block.start+i.block.length;q++) {//清空所使用的disk
			disk[q]=null;
		}*/
		if((i.start+i.length)==firstblock.start) {//删除的文件正好可以和firstblock合并

		    firstblock.length=firstblock.length+i.length;
			firstblock.start=i.start;
			firstblock.length+=i.length;
		}else
		{
			Block temp=new Block(i.start,i.length);
			firstblock.lastBlock=temp;			
			temp.nextBlock=firstblock;
		}
	}
    public static int disk_createfile(int len) {
        Block useblock=firstblock;
        while(useblock!=null&&useblock.length<len) {//找到第一个能放下len长度大小的块
            useblock=useblock.nextBlock;
        }
        if(useblock==null){
            return -1;
        }

        if(useblock.length==0) {//没有空闲区块
            return -1;
        }
        useblock.length-=len;
        int i=useblock.start;//i是可以分配的空闲区块
        useblock.start+=len;
        if(useblock.length==0) {//使用的节点变成空的
            if(useblock.nextBlock!=null) {//下一节点有节点
                if(useblock.lastBlock!=null) {//上一节点有节点
                    useblock.lastBlock.nextBlock=useblock.nextBlock;//上节点的下一个节点等于使用节点的下一个节点
                    useblock.nextBlock.lastBlock=useblock.lastBlock;//下一节点的上一个节点是使用节点的上一个节点
                }
                else
                if(useblock!=firstblock) {//使用的节点是firstblock
                    firstblock=firstblock.nextBlock;//firstblock往后移
                }
            }else
            {//下一节点是空的
                useblock.lastBlock.nextBlock=null;//上一节点的下一节点是空节点
            }
        }
        return i;//返回开始位置
    }
}
