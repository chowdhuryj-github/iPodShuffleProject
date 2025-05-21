package iPodShuffleJava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import iPodShuffleJava.iPodShuffle;

public class Shuffler {

    private String path;
    private boolean trackVoiceOver;
    private boolean playlistVoiceOver;
    private boolean rename;
    private int trackGain;
    private int autoDirectoryPlaylists;

    private List<String> tracks;
    private List<String> albums;
    private List<String> artists;
    private List<String> lists;
    private Object tunesSD;



    /**
     * the constructor
     * @param path root path to iPod storage device
     * @param trackVoiceOver generate voiceover WAV files for each track
     * @param playlistVoiceOver generate voiceover files for playlists
     * @param rename rename files that have non-latin characters
     * @param trackGain optional volume boost to all tracks
     * @param autoDirectoryPlaylists generate playlists for subfolders under iPod_Control/Music/
     */
    public Shuffler(String path, boolean trackVoiceOver, boolean playlistVoiceOver, boolean rename, int trackGain,
        int autoDirectoryPlaylists) {

            this.path = Paths.get(path).toAbsolutePath().toString();
            this.trackVoiceOver = trackVoiceOver;
            this.playlistVoiceOver = playlistVoiceOver;
            this.rename = rename;
            this.trackGain = trackGain;
            this.tracks = new ArrayList<>();
            this.albums = new ArrayList<>();
            this.artists = new ArrayList<>();
            this.lists = new ArrayList<>();
            this.tunesSD = null;
    }

    /**
     * overloaded constructor
     * @param path root path to iPod storage device
     */
    public Shuffler(String path) {
        this(path, false, false, false, 0, -1);
    }

    private void initialize() {

        String[] filePathsOne = {"iPod_Control/Speakable/Playlists", "iPod_Control/Speakable/Tracks" };
        for(int i = 0; i < filePathsOne.length; i++) {
            Path directoryPathOne = Paths.get(this.path + filePathsOne[i]);
            try {
                Files.delete(directoryPathOne);
                System.out.println("Directory Deleted: " + directoryPathOne);
            } catch(IOException exception) {
                System.out.println("Directory Deletion Failed");
                exception.printStackTrace();
            }
        } 

        String[] filePathsTwo = {"iPod_Control/iTunes", "iPod_Control/Music", "iPod_Control/Speakable/Playlists",
            "iPod_Control/Speakable/Tracks"};
        for(int i = 0; i < filePathsTwo.length; i++) {
            Path directoryPathTwo = Paths.get(this.path + filePathsTwo[i]);
            iPodShuffle.createDirectory(directoryPathTwo.toString());
        }
    }








    
}
