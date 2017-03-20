/**
 * Created by kolinsol on 2/26/17.
 */
public class Main {
    private final static String root_dir = "/Users/kolinsol/Desktop/_temp_downloads";
    public static void main(String[] args) {
        Renamer root = new Renamer(root_dir);
        root.run();
    }
}
