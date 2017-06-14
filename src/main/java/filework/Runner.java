package filework;

import java.util.Scanner;
import exception.EmptyFolderExceprtion;
import exception.NotADirectoryException;

import static java.lang.System.in;

public class Runner {
    public void run() {
        Scanner input = new Scanner(in);
        StateManager stater = new StateManager();
        try {
            stater.checkDirectory();
        } catch (EmptyFolderExceprtion e) {
            System.out.println("Empty directory");
            return;
        } catch (NotADirectoryException e) {
            System.out.println("Not A Directory");
            return;
        }
        DeleteManager deleter = new DeleteManager();
        deleter.recursiveDeleteInvisibleFiles();
        while(true) {
            System.out.println("1. Show list of files.\n" +
                    "2. Rename files.\n" +
                    "3. Set MetaData\n" +
                    "4. Get MetaData\n" +
                    "5. Exit.");
            int index = Integer.parseInt(input.next());
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