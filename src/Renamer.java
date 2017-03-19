import java.io.File;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.setOut;

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
        deleteInvisibleFiles();
        OUT: while(true) {
            System.out.println("1. Show list of files.\n" +
                    "2. Rename files.\n" +
                    "3. Exit.");
            index = Integer.parseInt(input.next());
            switch(index) {
                case 1:
                    printFileNames();
                    break;
                case 2:
                    renameFiles();
                    break;
                case 3:
                    break OUT;
            }
        }
    }

    private void deleteInvisibleFiles() {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().charAt(0)=='.'){
                if (!file.delete())
                    System.out.println("Error deleting file");
            }
        }
    }

    private void printFileNames() {
        File[] child_files = null;
        try {
            child_files = folder.listFiles();
        }
        catch (NullPointerException e) {
            System.out.println("Empty directory"+e);
        }
        for (File child: child_files)
            System.out.println(child.getName());
    }

    int folderSize () {
        return folder.listFiles().length;
    }

    private String[] formatFileNamesByTemplate() {
        File[] files = folder.listFiles();
        String[] file_names = new String[files.length];
        for (int i=0; i<file_names.length;i++)
        {
            file_names[i] = files[i].getName();
            file_names[i] = file_names[i].replace(".mp3","");
            file_names[i] = file_names[i].replace("[www.MP3Fiber.com]","");
            file_names[i] = file_names[i].replace("[MP3Fiber.com]","");
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
            if(file_names[i].contains("(p"))
                file_names[i]=file_names[i]+")";
            if((file_names[i].contains("(w")) && !(file_names[i].contains("(p")))
                file_names[i]=file_names[i]+")";
            if((file_names[i].contains("(w")) && (file_names[i].contains("(p")))
                file_names[i] = file_names[i].replace(" (p", ") (p");
            file_names[i] = file_names[i]+".mp3";
        }
        return(file_names);
    }

    private void renameFiles() {
        int renameCounter = 0;
        File[] files = folder.listFiles();
        String[] file_names = formatFileNamesByTemplate();
        if(files.length==file_names.length) {
            for(int i=0; i<files.length;i++)
                if(files[i].renameTo(new File(folder_name+"/"+file_names[i]))) {
                    renameCounter++;
                }
                else
                    System.out.println("file ["+(i+1)+"] not renamed.");
        }
        else
            System.out.println("wrong number of files.");
        if (renameCounter==folderSize())
            System.out.println("All files renamed successfully");
    }
}