package iPodShuffleJava;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class Track extends Record {

    public Track(Shuffler shuffler) {

        super(shuffler);

        this.struct = new LinkedHashMap<>();
        this.struct.put("header_id", new StructField("4s", "rths".getBytes()));
        this.struct.put("header_length", new StructField("I", 0x174));
        this.struct.put("start_at_pos_ms", new StructField("I", 0));
        this.struct.put("stop_at_pos_ms", new StructField("I", 0));
        this.struct.put("volume_gain", new StructField("I", shuffler.trackGain));
        this.struct.put("filetype", new StructField("I", 1));
        this.struct.put("filename", new StructField("256s", new byte[256]));
        this.struct.put("bookmark", new StructField("I", 0));
        this.struct.put("dontskip", new StructField("B", 1));
        this.struct.put("remember", new StructField("B", 0));
        this.struct.put("unintalbum", new StructField("B", 0));
        this.struct.put("unknown", new StructField("B", 0));
        this.struct.put("pregap", new StructField("I", 0x200));
        this.struct.put("postgap", new StructField("I", 0x200));
        this.struct.put("numsamples", new StructField("I", 0));
        this.struct.put("unknown2", new StructField("I", 0));
        this.struct.put("gapless", new StructField("I", 0));
        this.struct.put("unknown3", new StructField("I", 0));
        this.struct.put("albumid", new StructField("I", 0));
        this.struct.put("track", new StructField("H", 1));
        this.struct.put("disc", new StructField("H", 0));
        this.struct.put("unknown4", new StructField("Q", 0L));
        this.struct.put("dbid", new StructField("8s", new byte[8]));
        this.struct.put("artistid", new StructField("I", 0));
        this.struct.put("unknown5", new StructField("32s", new byte[32]));

    }

    public void populate(Shuffler shuffler, String fileName) {

        String relativePath = pathToiPod(shuffler, fileName);
        struct.put("filename", new StructField("256s", relativePath.getBytes(StandardCharsets.UTF_8)));


        // detecting the file type
        String extension = fileName.toLowerCase(Locale.ROOT).substring(fileName.lastIndexOf("."));
        if(extension.equals(".m4a") || extension.equals(".m4b") || extension.equals(".m4p") || extension.equals(".aa")) {
            struct.put("filetype", new StructField("I", 2));
        }

        // preparing fall back text
        File file = new File(fileName);
        String baseName = file.getName().substring(0, file.getName().lastIndexOf("."));
        String text = baseName;

        try {

            AudioFile audio = AudioFileIO.read(file);
            Tag tag = audio.getTag();

            int lengthMs = (int)(audio.getAudioHeader().getTrackLength() * 1000);
            struct.put("stop_at_pos_ms", new StructField("I", lengthMs));

            // artist
            String artist = (tag != null) ? tag.getFirst(FieldKey.ARTIST) : "Unknown";
            List<String> artists = shuffler.getArtists();
            int artistIndex = artists.indexOf(artist);
            if(artistIndex == -1) {
                artistIndex = artists.size();
                artists.add(artist);
            }
            struct.put("artistid", new StructField("I", artistIndex));
            

            // album
            String album = (tag != null) ? tag.getFirst(FieldKey.ALBUM) : "Unknown";
            List<String> albums = shuffler.getAlbums();
            int albumIndex = albums.indexOf(album);
            if(albumIndex == -1) {
                albumIndex = albums.size();
                albums.add(album);
            }

            // title
            String title = (tag != null) ? tag.getFirst(FieldKey.TITLE) : "Unknown";
            if(!title.isEmpty() && !artist.isEmpty()) {
                text = title + "-" + artist;
            }


        } catch (Exception exception) {
            System.out.println("Error Reading Meta Data: " + exception.getMessage());
        }


        try {

            byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] hash = md5.digest(textBytes);

            byte[] dbid = new byte[8];
            System.arraycopy(hash, 0, dbid, 0, 8);
            struct.put("dbid", new StructField("8s", dbid));

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    
}
