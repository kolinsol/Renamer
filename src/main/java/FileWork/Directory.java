package FileWork;

import java.io.File;

/**
 * Created by kolinsol on 2/26/17.
 */
class Directory
{
    private final static String ROOT_DIR = "/Users/kolinsol/Desktop/_temp_downloads";
    private static Directory directoryInstance = new Directory(ROOT_DIR);

    private File dirFile;
    private String dirName;

    private Directory(String path) {
        dirFile = new File(path);
        dirName = path;
    }

    static Directory getDirectoryInstance() {
        return directoryInstance;
    }

    File getDirFile() {
        return dirFile;
    }
}