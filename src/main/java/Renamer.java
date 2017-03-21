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
import java.util.Scanner;

import static java.lang.System.in;

/**
 * Created by kolinsol on 2/26/17.
 */
class Renamer
{
    private File folder;
    private String folder_name;

    Renamer(String path) {
        folder = new File(path);
        folder_name = path;
    }

    void run() {
        Scanner input = new Scanner(in);
        int index = 0;
        try {
            deleteInvisibleFiles();
        } catch (EmptyFolderExceprtion e) {
            System.out.println("Empty directory");
        }
        while(true) {
            System.out.println("1. Show list of files.\n" +
                    "2. Rename files.\n" +
                    "3. Get MetaData\n"+
                    "4. SetMetaData\n"+
                    "5. Exit.");
            index = Integer.parseInt(input.next());
            switch(index) {
                case 1:
                    try {
                        printFileNames();
                    } catch (EmptyFolderExceprtion e) {
                    System.out.println("Empty directory");
                    break;
                    }
                    break;
                case 2:
                    try {
                        createFilesList();
                    } catch (EmptyFolderExceprtion e) {
                        System.out.println("Empty directory");
                        break;
                    }
                    recursiveRenameFiles();
                    break;
                case 3:
                    getMetaData();
                    break;
                case 4:
                    recursiveSetMetaData();
                    break;
                case 5:
                    return;
            }
        }
    }

    private void deleteInvisibleFiles() throws EmptyFolderExceprtion {
        File[] files = createFilesList();
        for (File file : files) {
            if (file.getName().charAt(0)=='.'){
                if (!file.delete())
                    System.out.println("Error deleting file");
            }
        }
    }

    private void printFileNames() throws EmptyFolderExceprtion {
        File[] files = createFilesList();
        for (File file: files) {
            if (file.isDirectory()) {
                System.out.println("\nDirectory: "+file.getAbsolutePath());
                new Renamer(file.getAbsolutePath()).printFileNames();
                System.out.println("\n");
            } else
                System.out.println(file.getName());
        }
    }

    private String[] formatFileNamesByTemplate() {
        File[] files = folder.listFiles();
        String[] file_names = new String[files.length];
        for (int i=0; i<file_names.length;i++)
        {
            file_names[i] = files[i].getName();
            if ((!files[i].isDirectory()) && file_names[i].contains(".mp3")) {
                file_names[i] = file_names[i].replace(".mp3", "");
                file_names[i] = file_names[i].replace("[www.MP3Fiber.com]", "");
                file_names[i] = file_names[i].replace("[MP3Fiber.com]", "");
                file_names[i] = file_names[i].replace("_", " ");
                file_names[i] = file_names[i].toLowerCase();
                file_names[i] = file_names[i].replace("produced by", " (p ");
                file_names[i] = file_names[i].replace("production by", " (p ");
                file_names[i] = file_names[i].replace(" production ", " (p ");
                file_names[i] = file_names[i].replace("prod by", " (p ");
                file_names[i] = file_names[i].replace(" prod ", " (p ");
                file_names[i] = file_names[i].replace("p by", " (p ");
                file_names[i] = file_names[i].replace("featuring", " (w ");
                file_names[i] = file_names[i].replace(" feat ", " (w ");
                file_names[i] = file_names[i].replace(" ft ", " (w ");
                file_names[i] = file_names[i].replaceAll("^ +| +$|( )+", "$1");
                if (file_names[i].contains("(p"))
                    file_names[i] = file_names[i] + ")";
                if ((file_names[i].contains("(w")) && !(file_names[i].contains("(p")))
                    file_names[i] = file_names[i] + ")";
                if ((file_names[i].contains("(w")) && (file_names[i].contains("(p")))
                    file_names[i] = file_names[i].replace(" (p", ") (p");
                file_names[i] = file_names[i] + ".mp3";
            }
        }
        return(file_names);
    }

    private void renameFiles() {
        File[] files = folder.listFiles();
        String[] file_names = formatFileNamesByTemplate();
        for(int i=0; i<files.length;i++) {
            if(!files[i].renameTo(new File(folder_name+"/"+file_names[i])))
                System.out.println("Error renaming file "+files[i].getName());
        }
    }

    private void recursiveRenameFiles() {
        renameFiles();
        File[] files = folder.listFiles();
        for (File file: files) {
            if (file.isDirectory())
                new Renamer(file.getAbsolutePath()).recursiveRenameFiles();
        }
    }

    private File[] createFilesList() throws EmptyFolderExceprtion {
        File[] files = folder.listFiles();
        if (files.length==0)
            throw new EmptyFolderExceprtion();
        else
            return files;
    }

    private void getMetaData() {
        File[] files = folder.listFiles();
        for(File file: files) {
            if ((!file.isDirectory()) && (file.getName().contains(".mp3"))) {
                try {
                AudioFile f = AudioFileIO.read(file);
                Tag tag = f.getTag();
                System.out.println("ARTIST: "+tag.getFirst(FieldKey.ARTIST)+
                        "\nSONG: "+tag.getFirst(FieldKey.TITLE)+"\n");
                } catch (CannotReadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TagException e) {
                    e.printStackTrace();
                } catch (ReadOnlyFileException e) {
                    e.printStackTrace();
                } catch (InvalidAudioFrameException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setMetaData() {
        File[] files = folder.listFiles();
        for(File file: files) {
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
    }

    private void recursiveSetMetaData() {
        setMetaData();
        File[] files = folder.listFiles();
        for (File file: files) {
            if (file.isDirectory())
                new Renamer(file.getAbsolutePath()).recursiveSetMetaData();
        }
    }
}