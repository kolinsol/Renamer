package FileWork;

import java.util.Scanner;
import Exception.EmptyFolderExceprtion;
import Exception.NotADirectoryException;

import static java.lang.System.in;

/**
 * Created by kolinsol on 3/22/17.
 */
public class Runner {
    public void run() {
        Scanner input = new Scanner(in);
        int index = 0;
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
                    "3. Get MetaData\n"+
                    "4. SetMetaData\n"+
                    "5. Exit.");
            index = Integer.parseInt(input.next());
            switch(index) {
                case 1:
                    PrintManager printer = new PrintManager();
                    printer.recursivePrintFileNames();
                    break;
                case 2:
                    RenameManager renamer = new RenameManager();
                    renamer.recursiveRenameFiles();
                    break;
//                case 3:
//                    getMetaData();
//                    break;
//                case 4:
//                    recursiveSetMetaData();
//                    break;
                case 5:
                    return;
            }
        }
    }
}