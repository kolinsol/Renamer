package FileWork;

import Exception.EmptyFolderExceprtion;
import Exception.NotADirectoryException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

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

    public void setDirFile(File dirFile) {
        this.dirFile = dirFile;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }
}