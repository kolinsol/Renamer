package FileWork;

import FileWork.Directory;
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
 * Created by kolinsol on 3/22/17.
 */
class MetaDataManager {

    private Directory dir = Directory.getDirectoryInstance();

    private void getMetaData(File file) {
        if ((!file.isDirectory()) && (file.getName().contains(".mp3"))) {
            try {
                AudioFile f = AudioFileIO.read(file);
                Tag tag = f.getTag();
                System.out.println("ARTIST: "+tag.getFirst(FieldKey.ARTIST)+
                        "\nSONG: "+tag.getFirst(FieldKey.TITLE)+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            } catch (CannotReadException e) {
                e.printStackTrace();
            }
        }
    }

    private void setMetaData(File file) {
        if ((!file.isDirectory()) && (file.getName().contains(".mp3"))) {
            try {
                String filename = file.getName();
                filename = filename.replace(".mp3","");
                String[] tags = filename.split(" - ");
                AudioFile f = AudioFileIO.read(file);
                Tag tag = f.getTag();
                tag.setField(FieldKey.ARTIST, tags[0]);
                tag.setField(FieldKey.TITLE, tags[1]);
                f.commit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TagException e) {
                e.printStackTrace();
            } catch (ReadOnlyFileException e) {
                e.printStackTrace();
            } catch (InvalidAudioFrameException e) {
                e.printStackTrace();
            } catch (CannotWriteException e) {
                e.printStackTrace();
            } catch (CannotReadException e) {
                e.printStackTrace();
            }
        }
    }

    private void recursiveSetMetaData() {
        File[] files = dir.getDirFile().listFiles();
        for (File file: files) {
            setMetaData(file);
        }
    }
}