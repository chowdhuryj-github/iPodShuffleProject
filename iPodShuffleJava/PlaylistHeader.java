package iPodShuffleJava;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlaylistHeader extends Record {

    int baseOffset = 0;

    /**
     * constructor for Playlist Header
     * @param shuffler the shuffler object
     */
    public PlaylistHeader(Shuffler shuffler) {

        super(shuffler);
        this.baseOffset = 0;

        this.struct = new LinkedHashMap<>();
        this.struct.put("header_id", new StructField("4s", "hphs".getBytes()));
        this.struct.put("total_length",  new StructField("I", 0));
        this.struct.put("number_of_playlists", new StructField("I", 0));
        this.struct.put("number_of_non_podcast_lists", new StructField("2s", new byte[] {(byte) 0xFF, (byte) 0xFF}));
        this.struct.put("number_of_master_lists", new StructField("2s", new byte[] {0x01, 0x00}));
        this.struct.put("number_of_non_audiobook_lists", new StructField("2s", new byte[] {(byte) 0xFF, (byte) 0xFF}));
        this.struct.put("unknown2", new StructField("2s", new byte[] {0x00, 0x00}));

    }

    public byte[] construct(List<String> tracks) {

        List<byte[]> chunks = new ArrayList<>();

        // building the master list
        
        

    }
    
}
