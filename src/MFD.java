import java.util.ArrayList;
import java.util.Vector;

public class MFD {
    public static ArrayList<UFD> ufdlist = new ArrayList<>();
    public static Vector<String> path = new Vector<>();

    public static String getPath() {
        String pathstr = "";
        for (String dir : path)
            pathstr += "/" + dir;
        return pathstr;
    }

    public static void openPath(String name) {
        path.add(name);
    }

    public static void rePath() {

        path.remove(path.size() - 1);
    }
    public static void rename(UFD u,String newname)
    {
        u.username=newname;
    }
}
