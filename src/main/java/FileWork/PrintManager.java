package FileWork;

import java.io.File;

/**
 * Created by kolinsol on 3/22/17.
 */
class PrintManager {

    private Directory dir = Directory.getDirectoryInstance();

    void recursivePrintFileNames() {
        File[] files = dir.getDirFile().listFiles();
        if (files != null) {
            for (File file: files) {
                printFileName(file);
            }
        }
    }

    private void printFileName(File file) {
        if (file.isDirectory()) {
            System.out.println("Directory: "+file.getAbsolutePath());
            File[] files = file.listFiles();
            for (File childFile: files) {
                printFileName(childFile);
            }
        } else
            System.out.println(file.getName());
    }
}