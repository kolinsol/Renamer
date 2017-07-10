package logic;

import java.util.Scanner;
import exceptions.EmptyFolderException;
import exceptions.NotADirectoryException;

import static java.lang.System.in;

public class Runner {
    public void run() throws RuntimeException {
        Scanner input = new Scanner(in);
        try {
            new StateManager().checkDirectory();
        } catch (NotADirectoryException e) {
            throw new RuntimeException("Not a directory" + e);
        } catch (EmptyFolderException e) {
            throw new RuntimeException("Empty directory" + e);
        }
        DeleteManager deleter = new DeleteManager();
        deleter.recursiveDeleteInvisibleFiles();
        while(true) {
            System.out.println("1. Show list of files.\n" +
                    "2. Rename files.\n" +
                    "3. Set MetaData\n" +
                    "4. Get MetaData\n" +
                    "5. Exit.");
            int index = input.nextInt();
            MetaDataManager metadater = new MetaDataManager();
            switch(index) {
                case 1:
                    PrintManager printer = new PrintManager();
                    printer.recursivePrintFileNames();
                    break;
                case 2:
                    RenameManager renamer = new RenameManager();
                    renamer.recursiveRenameFiles();
                    break;
                case 3:
                    metadater.recursiveSetMetaData();
                    break;
                case 4:
                    metadater.recursiveGetMetaData();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Wrong input. Try again.\n");
            }
        }
    }
}