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
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by kolinsol on 3/24/17.
 */
class AlbumManager {

    private void setAlbumTrackMetaData(File file, String artistTag, String albumTag, String trackTag, String totalTrackTag) {
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

    private int getTotalCoverNumber(File file) {
        File[] files = file.listFiles();
        int jpgCounter = 0;
        if (files != null) {
            for (File childFile : files) {
                if (childFile.getName().contains(".jpg")) {
                    jpgCounter++;
                }
            }
        }
        return jpgCounter;
    }

    private File getAlbumArtwork(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File childFile : files) {
                if (childFile.getName().contains(".jpg")) {
                    return childFile;
                }
            }
        }
        return null;
    }

    private void setAlbumArtwork(File file, File artwork) {
        try {
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            Artwork a = ArtworkFactory.createArtworkFromFile(artwork);
            tag.deleteArtworkField();
            tag.setField(a);
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

    void setAlbumMetaData(File file) {
        if (getTotalCoverNumber(file) == 0) {
            System.out.println("No cover found in album directory."+getTotalCoverNumber(file));
            return;
        } else if (getTotalCoverNumber(file) > 1) {
            System.out.println("More than one covers found. Please leave one."+getTotalCoverNumber(file));
            return;
        }
        String fileName = file.getName().substring(0,file.getName().length()-6);
            String[] audioTags = fileName.split(" - ");
            String artistTag = audioTags[0];
            String albumTag = audioTags[1];
            File[] files = file.listFiles();
            if (files != null) {
                File albumArtwork = getAlbumArtwork(file);
                int totalTrackTag = getTotalTrackNumber(file);
                int trackTag = 1;
                for (File childFile: files) {
                    if (childFile.getName().contains(".mp3")) {
                        setAlbumTrackMetaData(childFile, artistTag, albumTag, String.valueOf(trackTag), String.valueOf(totalTrackTag));
                        setAlbumArtwork(childFile, albumArtwork);
                        trackTag++;
                    }
                }
                deleteCoverFiles(file);
        }
    }

    private void deleteCoverFiles(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File childFile : files) {
                if (childFile.getName().contains(".jpg")) {
                    if (!childFile.delete()) {
                        System.out.println("Error deleting file "+childFile.getAbsolutePath());
                    }
                }
            }
        }
    }
}
