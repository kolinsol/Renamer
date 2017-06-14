package pojo;

import java.io.File;

public class Directory
{
    private final static String ROOT_DIR = "/Users/kolinsol/Desktop/_temp_downloads";
    private static Directory instance = new Directory(ROOT_DIR);

    private File dirFile;
    private String dirName;

    private Directory(String path) {
        dirFile = new File(path);
        dirName = path;
    }

    public static Directory getInstance() {
        if (instance == null) {
            instance = new Directory(ROOT_DIR);
        }
        return instance;
    }

    public File getDirFile() {
        return dirFile;
    }
}