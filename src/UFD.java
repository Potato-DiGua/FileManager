import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class UFD {
    public String username;
    public ArrayList<FCB> filelist=new ArrayList<>();
    public String type="文件夹";
    public UFD(String name)
    {
        username=name;
    }
    public int existFile(String filename) {//文件是否存在
    	for(FCB i:filelist) {
            if(i.filename.equals(filename)) {
                return 1;
            }
        }
        return 0;
    }
    public FCB createFile(String filepath){//创建文件
        for(int i=1;;i++) {
            if(existFile("新建文件("+i+")")==0) {
                FCB file=new FCB();
                file.filename="新建文件("+i+")";
                file.shuxing="a";

                file.filepath=filepath;
                //在文件列表中创建新FCB
                return file;
            }
            return null;
        }


    }
    public void deleteFile(String filename) {//删除文件

        for(int i=0;i<filelist.size();i++) {
            if(filelist.get(i).filename.equals(filename)) {
            	Disk.disk_deletefile(filelist.get(i));
            	filelist.remove(i);
            	i--;           	
            	break;
                //删除
            }
        }
        //文件列表删除FCB
    }
    public int xiugaiFilewords(String filename,String words) throws UnsupportedEncodingException {//words为修改后的内容
        for(FCB i:filelist) {
            if(i.filename.equals(filename)) {
                if(i.shuxing.equals("r")) {//只读状况下不允许写入
                    return -1;
                }else
                	Disk.disk_deletefile(i);
                    i.write(words);
                    return 1;
            }
        }
        return -1;
    }
    public int xiugaiFiletype(String filename,String type) {//修改文件类型，只读r，只写w，都行a
    	for(FCB i:filelist) {
            if(i.filename.equals(filename)) {
               i.shuxing=type;
            }
        }
        return -1;
    }
    public String openFile(String filename) throws UnsupportedEncodingException {//打开文件
        for(FCB i:filelist) {
            if(i.filename.equals(filename)) {
            	if(i.shuxing.equals("a")||i.shuxing.equals("r")) {
            		return i.read();
            	}
            }
        }
        return "";
    }
    public int  renameFile(String filename,String rename) {//重命名文件
        for(FCB i:filelist) {
            if(i.filename.equals(rename)) {
                return 0;//有重名，重命名失败
            }
        }
        for(FCB i:filelist) {
            if(i.filename.equals(filename)) {
                i.filename=rename;//可以重命名，执行操作
                return 1;//成功返回1
            }
        }
        return -1;
    }

}
