package FileWork;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //method for tagging regular files like "artistName - songName.mp3"
    private void setMetaData(File file) {
        if (file.isDirectory()) {
            if (isAlbum(file)) {
                setAlbumMetaData(file);
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

    //method for tagging album files like "trackNumber. songName.mp3"
    private void setMetaData(File file, String artistTag, String albumTag, String trackTag, String totalTrackTag) {
        try {
            String filename = file.getName();
            filename = filename.replace(".mp3", "");
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            tag.setField(FieldKey.ARTIST, artistTag);
            tag.setField(FieldKey.TITLE, filename);
            tag.setField(FieldKey.ALBUM, albumTag);
            tag.setField(FieldKey.TRACK, trackTag);
            tag.setField(FieldKey.TRACK_TOTAL, totalTrackTag);
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

    private int getTotalTrackNumber(File file) {
        File[] files = file.listFiles();
        int mp3Counter = 0;
        if (files != null) {
            for (File childFile : files) {
                if (childFile.getName().contains(".mp3")) {
                    mp3Counter++;
                }
            }
        }
        return mp3Counter;
    }

    private boolean isAlbum(File file) {
        Pattern pattern = Pattern.compile("\\(\\d{4}\\)$");
        Matcher matcher = pattern.matcher(file.getName());
        return matcher.find();
    }

    private void setAlbumMetaData(File file) {
        String fileName = file.getName().substring(0,file.getName().length()-6);
        String[] audioTags = fileName.split(" - ");
        String artistTag = audioTags[0];
        String albumTag = audioTags[1];
        File[] files = file.listFiles();
        if (files != null) {
            int totalTrackTag = getTotalTrackNumber(file);
            int trackTag = 1;
            for (File childFile: files) {
                if (childFile.getName().contains(".mp3")) {
                    setMetaData(childFile, artistTag, albumTag, String.valueOf(trackTag), String.valueOf(totalTrackTag));
                    trackTag++;
                }
            }
        }
    }
}