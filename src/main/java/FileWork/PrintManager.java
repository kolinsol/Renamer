package FileWork;

import FileWork.Directory;

import java.io.File;

/**
 * Created by kolinsol on 3/22/17.
 */
class PrintManager {

    private Directory dir = Directory.getDirectoryInstance();

    void recursivePrintFileNames() {
        File[] files = dir.getDirFile().listFiles();
        for (File file: files)
            printFileName(file);
    }

    private void printFileName(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile: files) {
                System.out.println("Directory: "+childFile.getAbsolutePath());
                printFileName(childFile);
            }
        } else
            System.out.println(file.getName());
    }
}