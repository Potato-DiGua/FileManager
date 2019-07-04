import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class UFD {
    public String username;
    public ArrayList<FCB> filelist=new ArrayList<>();

    public UFD(String name)
    {
        username=name;
    }

    public int existFile(String filepath,String filename) {//文件是否存在
    	for(FCB i:filelist) {
            if(i.filepath.equals(filepath)&&i.filename.equals(filename)) {
                return 1;
            }
        }
        return 0;
    }
    public FCB createFile(String filepath){//创建文件
        for(int i=1;;i++) {
            if(existFile(filepath,"新建文件("+i+")")==0) {
                FCB file=new FCB();
                file.filename="新建文件("+i+")";
                file.shuxing="a";

                //在文件列表中创建新FCB
                return file;
            }
            return null;
        }


    }
    public void deleteFile(String filepath,String filename) {//删除文件

        for(int i=0;i<filelist.size();i++) {
            if(filelist.get(i).filepath.equals(filepath)&&filelist.get(i).filename.equals(filename)) {
            	filelist.remove(i);
            	i--;
                //删除
            }
        }
        //文件列表删除FCB
    }
    public int xiugaiFilewords(String filepath,String filename,String words) throws UnsupportedEncodingException {//words为修改后的内容
        for(FCB i:filelist) {
            if(i.filepath.equals(filepath)&&i.filename.equals(filename)) {
                if(i.shuxing.equals("r")) {//只读状况下不允许写入
                    return -1;
                }else
                    i.length=words.length();
                	i.write(words);;
                return 1;
            }
        }
        return -1;
    }
    public int xiugaiFiletype(String filepath,String filename,String type) {//修改文件类型，只读r，只写w，都行a
    	for(FCB i:filelist) {
            if(i.filepath.equals(filepath)&&i.filename.equals(filename)) {
               i.shuxing=type;
            }
        }
        return -1;
    }
    public String openFile(String filepath,String filename) throws UnsupportedEncodingException {//打开文件
        for(FCB i:filelist) {
            if(i.filepath.equals(filepath)&&i.filename.equals(filename)) {
            	if(i.shuxing.equals("a")||i.shuxing.equals("r")) {
            		return i.read();
            	}
            }
        }
        return "";
    }
    public int  renameFile(String filepath,String filename,String rename) {//重命名文件
        for(FCB i:filelist) {
            if(i.filepath.equals(filepath)&&i.filename.equals(rename)) {
                return 0;//有重名，重命名失败
            }
        }
        for(FCB i:filelist) {
            if(i.filepath.equals(filepath)&&i.filename.equals(filename)) {
                i.filename=rename;//可以重命名，执行操作
                return 1;//成功返回1
            }
        }
        return -1;
    }

}
