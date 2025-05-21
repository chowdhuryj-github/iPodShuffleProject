package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import iPodShuffleJava.Shuffler;


/**
 * the class for shuffler testing
 */
public class ShufflerTest {

    private Shuffler shuffler;
    private String testRootPath = "C:/testiPodControl/";

    /**
     * ensuring the directories are all set up for deletion
     * @throws IOException in case the file creation process fails
     */
    @BeforeEach
    public void setUp() throws IOException {

        shuffler = new Shuffler(testRootPath, false, false, false, 0, -1);
        Files.createDirectories(Paths.get(testRootPath + "iPod_Control/Speakable/Playlists"));
        Files.createDirectories(Paths.get(testRootPath + "iPod_Control/Speakable/Tracks"));
        
    }

    /**
     * test method for testing out the initialize() method from Shuffler.java
     * @throws IOException in case the test fails
     */
    @Test 
    public void testInitialize() throws IOException {

        Path dummyFilePath = Paths.get(testRootPath + "iPod_Control/Speakable/Playlists/deleteme.txt");
        Files.deleteIfExists(dummyFilePath);
        Files.createFile(dummyFilePath);
        assertTrue(Files.exists(dummyFilePath));

        shuffler.initialize();
        assertFalse(Files.exists(dummyFilePath), "Directory Wasn't Deleted");

        assertTrue(Files.exists(Paths.get(testRootPath + "iPod_Control/Speakable/Playlists")));
        assertTrue(Files.exists(Paths.get(testRootPath + "iPod_Control/Speakable/Tracks")));
        assertTrue(Files.exists(Paths.get(testRootPath + "iPod_Control/iTunes")));
        assertTrue(Files.exists(Paths.get(testRootPath + "iPod_Control/Music")));

    }

    
}
