package filework;

import exception.EmptyFolderExceprtion;
import exception.NotADirectoryException;
import pojo.Directory;

class StateManager {

    private Directory dir = Directory.getInstance();

    void checkDirectory() throws EmptyFolderExceprtion, NotADirectoryException {

        if (!dir.getDirFile().isDirectory()) {
            throw new NotADirectoryException();
        }
        if (dir.getDirFile().listFiles().length == 0) {
            throw new EmptyFolderExceprtion();
        }
    }
}