public class Disk {
	public static Block firstblock=new Block(0,1024);
	public static byte[][] disk=new byte[1024][512];
	public static void disk_deletefile(int i) {
		if(i<firstblock.start-1) {//删除的文件是first的start之前文件
			Block a=new Block(i,1);
			Block temp=firstblock;
			firstblock=a;
			temp.lastBlock=firstblock;
			firstblock.nextBlock=temp;//firstblock是第i个空闲块，指向原firstblock
		}else
		if(i==firstblock.start-1) {//删除的区块是start之前的一块，则start直接-1
			firstblock.start--;
		}
	}
	public static int disk_createfile(int len) {
		Block useblock=firstblock;
		while(!useblock.equals(null)&&useblock.length<len) {//找到第一个能放下len长度大小的块
			useblock=useblock.nextBlock;
		}
		
		if(useblock.equals(null)){
			return -1;
		}
		
		if(useblock.length==0) {//没有空闲区块
			return -1;
		}
		useblock.length-=len;
		int i=useblock.start;//i是可以分配的空闲区块
		useblock.start+=len;
		if(useblock.length==0) {//使用的节点变成空的
			if(!useblock.nextBlock.equals(null)) {//下一节点有节点
				if(!useblock.lastBlock.equals(null)) {//上一节点有节点
					useblock.lastBlock.nextBlock=useblock.nextBlock;//上节点的下一个节点等于使用节点的下一个节点
					useblock.nextBlock.lastBlock=useblock.lastBlock;//下一节点的上一个节点是使用节点的上一个节点
				}
				else
					if(useblock.equals(firstblock)) {//使用的节点是firstblock
						firstblock=firstblock.nextBlock;//firstblock往后移
					}
			}else
				if(useblock.nextBlock.equals(null)) {//下一节点是空的
					useblock.lastBlock.nextBlock=null;//上一节点的下一节点是空节点
				}
		}
		return i;//返回开始位置
	}
}
