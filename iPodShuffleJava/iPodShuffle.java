
package iPodShuffleJava;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * the main class for the iPod Shuffle
 */
public class iPodShuffle {


    /**
     * method for creating a directory!
     * @param pathToDirectory the path to where you'd like to make your directory
     */
    public static void createDirectory(String pathToDirectory) {

        File directory = new File(pathToDirectory); // handles all paths
        if(directory.exists() && directory.isDirectory()) {
            System.out.println("Directory Exists!" + directory); 
        } else {
            boolean createdDirectory = directory.mkdirs(); // creates all non-existing directories
            if(createdDirectory) {
                System.out.println("Directory Made!");
            } else {
                System.out.println("Directory Not Made!");
            }
        }

    }


    /**
     * method for checking if the file name can be encoded
     * @param fileName name of the .mp3 to be encoded
     */
    public static boolean encodingCheck(String fileName) {

        CharsetEncoder encoder = Charset.forName("ISO-8859-1").newEncoder();
        if(encoder.canEncode(fileName)) {
            return true;
        } else {
            return false;
        }

    }


    /**
     * command to check if a command can run or not
     * @param command the command being entered
     * @return if the command works or not
     */
    public static boolean execExistsInPath(String command) {

        boolean commandWorks = true;
        ProcessBuilder builder = new ProcessBuilder(); // object used for running a command

        builder.command(command.split(" ")); // splitting the command into words
        builder.redirectErrorStream(true); // combines all error messages into one

        try {

            Process process = builder.start(); // runs the command
            int exitCode = process.waitFor(); // code after running the command
            if(exitCode == 0) {
                commandWorks = true;
                return commandWorks;
            }
        } catch (IOException | InterruptedException exception ) {
            commandWorks = false;
            return commandWorks;
        }

        return commandWorks;

    }


    /**
     * splitting a path into parts
     * @param path the file path
     * @return a list of parts of the file path
     */
    public static List<String> splitPath(String path) {

        List<String> pathParts = Arrays.asList(path.split("/"));
        return pathParts;

    }

    /**
     * method to retrieve the relative path of two paths
     * @param path the path
     * @param basePath the root path
     * @return the relative path
     */
    public static String getRelativePath(String path, String basePath) {

        List<String> pathToParts = splitPath(path);
        List<String> basePathToParts = splitPath(basePath);

        int minLength = Math.min(pathToParts.size(), basePathToParts.size());
        List<String> commonPaths = new ArrayList<>();

        for(int i = 0; i < minLength; i++) {
            if(pathToParts.get(i).equals(basePathToParts.get(i))) {
                commonPaths.add(pathToParts.get(i));
            } else {
                break;
            }
        }

        String finalRelativePath = String.join("/", commonPaths);
        return finalRelativePath;
        
    }


    /**
     * method to check if a directory is in another directory
     * @param prefix the prefix path
     * @param path the path we have
     * @return confirms if directory is inside 
     */
    public static boolean isPathPrefix(String path, String prefix) {

        Path paths = Paths.get(path).toAbsolutePath().normalize();    
        Path prefixPath = Paths.get(prefix).toAbsolutePath().normalize();  

        return paths.startsWith(prefixPath);

    }










}