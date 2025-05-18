
package com.ipodshuffle;
import java.io.File;


// class for ipod shuffle
public class iPodShuffle {


    // method for iterating through all the files
    public static void fileIterator(File[] folder) {

        for(int i = 0; i < folder.length; i++) {

            // checking if directory or not
            if(folder[i].isDirectory()) {

                // recusrively call the file iterator
                fileIterator(folder[i].listFiles());
            }

                // checks to see if it is a file
                else if(folder[i].isFile()) {

                    // extract filename
                    String fileName = folder[i].getName().toLowerCase();

                    // extract the file type
                    String fileType = fileName.substring(fileName.length() - 3);
                    if(fileType.equals("mp3")) {
                        System.out.println(folder[i].getAbsolutePath());
                    }

                }

        }
    }


    public static void main(String[] args) {

        File[] files = new File("D:\\iPod_Control\\Music\\").listFiles();
        fileIterator(files);


    }


}