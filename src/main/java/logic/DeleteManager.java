package logic;

import pojo.Directory;

import java.io.File;
import java.util.stream.Stream;

class DeleteManager {

    private Directory dir = Directory.getInstance();

    private void deleteInvisibleFile(File file) {
        if (file.getName().charAt(0)=='.') {
            if (!file.delete())
                System.out.println("Error deleting invisible file"+file.getAbsolutePath());
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    deleteInvisibleFile(childFile);
                }
            }
        }
    }

    void recursiveDeleteInvisibleFiles() {
        File[] files = dir.getDirFile().listFiles();
        if (files != null) {
            for (File file: files) {
                deleteInvisibleFile(file);
            }
        }
    }
}
