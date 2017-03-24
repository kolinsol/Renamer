package FileWork;

import Exception.EmptyFolderExceprtion;
import Exception.NotADirectoryException;

/**
 * Created by kolinsol on 3/22/17.
 */
class StateManager {

    private Directory dir = Directory.getDirectoryInstance();

    void checkDirectory() throws EmptyFolderExceprtion, NotADirectoryException {

        if (!dir.getDirFile().isDirectory()) {
            throw new NotADirectoryException();
        }
        if (dir.getDirFile().listFiles().length == 0) {
            throw new EmptyFolderExceprtion();
        }
    }
}