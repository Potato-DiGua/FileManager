public class Block {
    public int start;
    public int length;
    public Block nextBlock=null;
    public Block lastBlock=null;
    public Block(int a,int b) {
    	start=a;
    	length=b;
    }
}
