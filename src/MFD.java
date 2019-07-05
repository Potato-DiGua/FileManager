import java.util.ArrayList;
import java.util.Arrays;

public class MFD {
    public static Dir root=new Dir("root");
    public static ArrayList<Dir> nowPath=new ArrayList<>();

    public static String getPath() {
        String pathstr = "";
        for (Dir d : nowPath)
            pathstr += "/" + d.Dirname;
        return pathstr;
    }
    public static Dir getNowDir()
    {
        return nowPath.get(nowPath.size()-1);
    }
    public static Dir findDirByPath(String path)
    {
        ArrayList<String> names= new ArrayList<>(Arrays.asList(path.split("/")));
        return find(root,names);

    }
    public static Dir findDirByName(Dir parent,String name)
    {
        for(Dir d:parent.childDirlist)
        {
            if(d.Dirname.equals(name))
                return d;
        }
        return null;
    }

    private static Dir find(Dir Root,ArrayList<String> names)
    {
        for(Dir d:Root.childDirlist)
        {
            if(d.Dirname.equals(names.get(0)))
            {
                names.remove(0);
                if(names.size()==1)
                    return d;
                else
                    return find(d,names);
            }

        }

        return null;
    }
    public static void openPath(String name) {

        Dir dir=nowPath.get(nowPath.size()-1);
        for(Dir d:dir.childDirlist)
        {
            if(d.Dirname.equals(name))
            {
                nowPath.add(d);
            }

        }
    }

    public static String[][] getFilelist(Dir dir){
        int filenum=dir.childDirlist.size()+dir.filelist.size();
        String[][] filenames=new String[filenum][2];
        int i=0;
        for(Dir temp:dir.childDirlist)
        {
            filenames[i][0]=temp.Dirname;
            filenames[i][1]="文件夹";
            i++;
        }

        for(FCB f:dir.filelist)
        {
            filenames[i][0]=f.filename;
            filenames[i][1]="文件";
            i++;
        }
        return filenames;
    }

    public static void rePath() {

        nowPath.remove(nowPath.size() - 1);
    }
    public static void deletefile(String name,String Filetype)//
    {
        Dir t=nowPath.get(nowPath.size()-1);
        if(Filetype.equals("文件"))
        {
            t.deleteFile(name);
        }
        else if(Filetype.equals("文件夹"))
        {
            Dir d=findDirByName(t,name);
            if(d!=null)
                t.childDirlist.remove(d);
        }
    }
    public static boolean rename(String Filetype,String oldname, String newname)
    {
        if(oldname.equals(newname))
            return true;
        Dir t=nowPath.get(nowPath.size()-1);
        if(Filetype.equals("文件"))
        {
            return t.renameFile(oldname,newname);
        }
        else if(Filetype.equals("文件夹"))
        {
            Dir d=findDirByName(t,newname);
            if(d!=null)
                return false;
            else
            {
                d=findDirByName(t,oldname);
                d.Dirname=newname;
            }
        }

        return true;
    }
}
