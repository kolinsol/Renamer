package filework;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import pojo.Directory;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MetaDataManager {

    private Directory dir = Directory.getInstance();

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

    //method for tagging regular files like "artistName - songName.mp3"
    private void setMetaData(File file) {
        if (file.isDirectory()) {
            if (isAlbum(file)) {
                new AlbumManager().setAlbumMetaData(file);
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File childFile : files) {
                        setMetaData(childFile);
                    }
                }
            }
        } else if (file.getName().contains(".mp3")) {
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

    void recursiveSetMetaData() {
        File[] files = dir.getDirFile().listFiles();
        if (files != null) {
            for (File file: files) {
                setMetaData(file);
            }
        }
    }

    void recursiveGetMetaData() {
        File[] files = dir.getDirFile().listFiles();
        if (files != null) {
            for (File file: files) {
                getMetaData(file);
            }
        }
    }

    private boolean isAlbum(File file) {
        Pattern pattern = Pattern.compile("\\(\\d{4}\\)$");
        Matcher matcher = pattern.matcher(file.getName());
        return matcher.find();
    }
}