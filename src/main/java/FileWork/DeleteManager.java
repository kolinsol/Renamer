package FileWork;

import FileWork.Directory;

import java.io.File;

/**
 * Created by kolinsol on 3/22/17.
 */
class DeleteManager {

    private Directory dir = Directory.getDirectoryInstance();

    private void deleteInvisibleFile(File file) {
        if (file.getName().charAt(0)=='.') {
            if (!file.delete())
                System.out.println("Error deleting invisible file"+file.getAbsolutePath());
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childFile: files)
                deleteInvisibleFile(childFile);
        }
    }

    void recursiveDeleteInvisibleFiles() {
        File[] files = dir.getDirFile().listFiles();
        for (File file: files)
            deleteInvisibleFile(file);
    }
}
