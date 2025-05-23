package iPodShuffleJava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * class for Shuffler
 */
public class Shuffler {

    protected String path;
    protected boolean trackVoiceOver;
    protected boolean playlistVoiceOver;
    protected boolean rename;
    protected int trackGain;
    protected int autoDirectoryPlaylists;

    private List<String> tracks;
    private List<String> albums;
    private List<String> artists;
    private List<String> lists;
    private TunesSD tunesSD;



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

    public void initialize() throws IOException {

        String[] filePathsOne = {"iPod_Control/Speakable/Playlists", "iPod_Control/Speakable/Tracks" };
        for(int i = 0; i < filePathsOne.length; i++) {
            Path directoryPathOne = Paths.get(this.path, filePathsOne[i]);
            deleteDirectoryRecursively(directoryPathOne);
        } 

        String[] filePathsTwo = {"iPod_Control/iTunes", "iPod_Control/Music", "iPod_Control/Speakable/Playlists",
            "iPod_Control/Speakable/Tracks"};
        for(int i = 0; i < filePathsTwo.length; i++) {
            Path directoryPathTwo = Paths.get(this.path, filePathsTwo[i]);
            iPodShuffle.createDirectory(directoryPathTwo.toString());
        }
    }


    /**
     * method for DumpState
     * @return a string
     */
    public String DumpState() {

        StringBuilder shuffleState = new StringBuilder();

        shuffleState.append("Shuffle DB State").append(System.lineSeparator());
        shuffleState.append("Tracks: " + this.tracks).append(System.lineSeparator());
        shuffleState.append("Albums: " + this.albums).append(System.lineSeparator());
        shuffleState.append("Artists: " + this.artists).append(System.lineSeparator());
        shuffleState.append("Playlists: " + this.lists).append(System.lineSeparator());

        return shuffleState.toString();

    }

    
    /**
     * method for deleting the contents of a directory
     * @param directoryPath the path of the directory to delete
     * @throws IOException in case the deletion process fails
     */
    public void deleteDirectoryRecursively(Path directoryPath) throws IOException {
        if(Files.exists(directoryPath)) {
            Files.walk(directoryPath)
                .sorted(Comparator.reverseOrder()) // delete child folder before the parent folder
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        System.out.println("Deleted: "+ path);
                    } catch(IOException exception) {
                        System.out.println("Failed to Delete: " + path);
                        exception.printStackTrace();
                    }
                });
        }
    }


    /**
     * getter method to return the track list
     * @return a list of tracks
     */
    public List<String> getTracks() {
        return this.tracks;
    }


    /**
     * getter method to return the album list
     * @return a list of albums
     */
    public List<String> getAlbums() {
        return this.albums;
    }


    /**
     * getter method to return a list of artists
     * @return a list of artists
     */
    public List<String> getArtists() {
        return this.artists;
    }

    
    /**
     * getter method to return a list of lists?
     * @return a list of lists
     */
    public List<String> getLists() {
        return this.lists;
    }








    
}
