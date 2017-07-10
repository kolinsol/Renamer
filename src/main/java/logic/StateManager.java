package logic;

import exceptions.EmptyFolderException;
import exceptions.NotADirectoryException;
import pojo.Directory;

class StateManager {

    private Directory dir = Directory.getInstance();

    void checkDirectory() throws EmptyFolderException, NotADirectoryException {

        if (!dir.getDirFile().isDirectory()) {
            throw new NotADirectoryException();
        }
        if (dir.getDirFile().listFiles().length == 0) {
            throw new EmptyFolderException();
        }
    }
}